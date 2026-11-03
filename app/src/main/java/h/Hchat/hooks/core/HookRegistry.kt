package h.Hchat.hooks.core

import h.Hchat.compat.xposed.HookTimingHook
import h.Hchat.compat.xposed.TriggerMarkingHook
import h.Hchat.compat.xposed.XC_MethodHook
import h.Hchat.compat.xposed.XposedBridge
import java.lang.reflect.Member
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * 统一保存 Xposed hook 句柄，便于防御性卸载。
 */
class HookRegistry private constructor() {
    private val lock = Any()
    private val unhooks = ArrayList<XC_MethodHook.Unhook>()
    private val installationLease = ThreadLocal<Long>()
    private val featureLease = ThreadLocal<String>()
    private val autoMarkLease = ThreadLocal<Boolean>()
    @Volatile private var generation = 0L

    /** 按功能统计实际注册成功的 hook 数，供「模块排查」判断功能是否真的挂上了。 */
    private val featureHookCounts = ConcurrentHashMap<String, AtomicInteger>()
    private val hookOwners = HashMap<XC_MethodHook.Unhook, String>()

    fun installationGeneration(): Long = installationLease.get() ?: generation

    fun isInstallationGenerationActive(generation: Long): Boolean = this.generation == generation

    fun <T> withInstallationGeneration(generation: Long, action: () -> T): T {
        val previous = installationLease.get()
        if (!isInstallationGenerationActive(generation) || previous != null && previous != generation) {
            throw CancellationException("安装代次已失效")
        }
        installationLease.set(generation)
        try { return action() }
        finally {
            if (previous == null) installationLease.remove() else installationLease.set(previous)
        }
    }

    /** 当前线程正在安装的功能 id，hook 与监听器注册时用它绑定运行时打点。 */
    fun currentFeatureTag(): String? = featureLease.get()

    /** 当前功能是否允许"hook 一被调用就算命中"，见 [Feature.autoMarkOnHook]。 */
    fun currentAutoMarkOnHook(): Boolean = autoMarkLease.get() ?: true

    fun <T> withFeatureTag(featureId: String?, autoMarkOnHook: Boolean = true, action: () -> T): T {
        val previous = featureLease.get()
        val previousAutoMark = autoMarkLease.get()
        featureLease.set(featureId)
        autoMarkLease.set(autoMarkOnHook)
        try {
            return action()
        } finally {
            if (previous == null) featureLease.remove() else featureLease.set(previous)
            if (previousAutoMark == null) autoMarkLease.remove() else autoMarkLease.set(previousAutoMark)
        }
    }

    /** 该功能当前还挂着多少个 hook。 */
    fun hookCountFor(featureId: String): Int {
        if (featureId.isEmpty()) return 0
        return featureHookCounts[featureId]?.get() ?: 0
    }

    fun hook(method: Member, callback: XC_MethodHook): XC_MethodHook.Unhook = synchronized(lock) {
        if (!isInstallationGenerationActive(installationGeneration())) {
            throw CancellationException("安装代次已失效")
        }
        val unhook = XposedBridge.hookMethod(method, instrument(markRuntimeHit(callback)))
        if (unhook != null) {
            unhooks.add(unhook)
            countHook(unhook)
        }
        unhook
    }

    /**
     * 功能安装期注册的 hook 套一层一次性打点：微信真正调用到它，才认为该功能跑起来了。
     * 非功能安装期注册的 hook（API 层公共 hook）原样返回，避免无意义的开销。
     */
    private fun markRuntimeHit(callback: XC_MethodHook): XC_MethodHook {
        val featureId = featureLease.get()?.takeIf { it.isNotEmpty() } ?: return callback
        if (!currentAutoMarkOnHook()) return callback
        return TriggerMarkingHook(callback, Runnable { ModuleStatusStore.markTriggered(featureId) })
    }

    /**
     * 给功能级 hook 套一层采样计时，排查页据此找热点。
     * 公共 API 层（featureTag 为空）的 hook 不计量，避免把微信自身的调用算到模块头上。
     * 与 [markRuntimeHit] 不同，这个包装不受 [Feature.autoMarkOnHook] 影响：
     * 即使功能选了手动打点，它的 hook 开销依然要统计。
     */
    private fun instrument(callback: XC_MethodHook): XC_MethodHook {
        val featureId = featureLease.get()?.takeIf { it.isNotEmpty() } ?: return callback
        val stat = ModuleStatusStore.hookStatFor(featureId)
        return HookTimingHook(callback, object : HookTimingHook.Sink {
            override fun recordCall(): Long = stat.recordCall()
            override fun sampleAfter(): Boolean = stat.sampleAfter()
            override fun recordSample(nanos: Long) = stat.recordSample(nanos)
        })
    }

    /** 把 hook 记到当前功能名下；只在 [lock] 内调用。 */
    private fun countHook(unhook: XC_MethodHook.Unhook?) {
        if (unhook == null) return
        val featureId = featureLease.get()?.takeIf { it.isNotEmpty() } ?: return
        hookOwners[unhook] = featureId
        featureHookCounts.getOrPut(featureId) { AtomicInteger(0) }.incrementAndGet()
    }

    /** 只在 [lock] 内调用。功能级 hook（含共享 key 抢装）卸载后计数要能回落。 */
    private fun releaseHook(unhook: XC_MethodHook.Unhook) {
        val featureId = hookOwners.remove(unhook) ?: return
        val counter = featureHookCounts[featureId] ?: return
        if (counter.decrementAndGet() <= 0) featureHookCounts.remove(featureId, counter)
    }

    fun add(unhook: XC_MethodHook.Unhook?) {
        if (unhook == null) return
        synchronized(lock) {
            if (isInstallationGenerationActive(installationGeneration())) {
                unhooks.add(unhook)
                // findAndHookMethod 走的是这条路径，同样是功能自己的 hook，必须计数。
                countHook(unhook)
                return
            }
        }
        unhook.unhook()
        throw CancellationException("安装代次已失效")
    }

    fun size(): Int = synchronized(lock) { unhooks.size }

    fun unhook(unhook: XC_MethodHook.Unhook?) {
        if (unhook == null) return
        unhook.unhook()
        // Xposed 解除回调后，还必须释放模块注册表持有的句柄。
        synchronized(lock) {
            unhooks.remove(unhook)
            releaseHook(unhook)
        }
    }

    fun unhookAll() {
        val previous = synchronized(lock) {
            generation++
            val pending = unhooks.toList()
            unhooks.clear()
            hookOwners.clear()
            featureHookCounts.clear()
            pending
        }
        for (unhook in previous) {
            try {
                unhook.unhook()
            } catch (_: Throwable) {
            }
        }
    }

    companion object {
        private val INSTANCE = HookRegistry()

        @JvmStatic
        fun get(): HookRegistry = INSTANCE
    }
}
