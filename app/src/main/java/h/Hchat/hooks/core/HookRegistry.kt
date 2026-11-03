package h.Hchat.hooks.core

import h.Hchat.compat.xposed.TriggerMarkingHook
import h.Hchat.compat.xposed.XC_MethodHook
import h.Hchat.compat.xposed.XposedBridge
import java.lang.reflect.Member
import java.util.concurrent.CancellationException

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

    fun hook(method: Member, callback: XC_MethodHook): XC_MethodHook.Unhook = synchronized(lock) {
        if (!isInstallationGenerationActive(installationGeneration())) {
            throw CancellationException("安装代次已失效")
        }
        val unhook = XposedBridge.hookMethod(method, markRuntimeHit(callback))
        if (unhook != null) {
            unhooks.add(unhook)
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

    fun add(unhook: XC_MethodHook.Unhook?) {
        if (unhook == null) return
        synchronized(lock) {
            if (isInstallationGenerationActive(installationGeneration())) {
                unhooks.add(unhook)
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
        synchronized(lock) { unhooks.remove(unhook) }
    }

    fun unhookAll() {
        val previous = synchronized(lock) {
            generation++
            unhooks.toList().also { unhooks.clear() }
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
