package h.Hchat.hooks.core

import h.Hchat.utils.HLog
import java.util.concurrent.atomic.AtomicLong

/**
 * 功能点安装状态快照，供「杂项 → 模块排查」页面读取。
 * 写入与读取都在宿主进程内，不需要跨进程通信。
 */
object ModuleStatusStore {
    data class FeatureStatus(
        val featureId: String,
        val name: String,
        val enabled: Boolean,
        val installed: Boolean,
        val error: String,
        /** 空串表示依赖齐全；非空是当前版本下不可用的原因（多数是定位缺失）。 */
        val health: String = "",
        /** 关键 hook 是否真的命中过；未命中只说明还没被触发，不能算正常。 */
        val triggered: Boolean = false
    )

    /** 全局定位快照：就位数 / 总数 / 缺失字段名。 */
    data class LocateStatus(
        val located: Int,
        val total: Int,
        val missing: List<String>
    )

    data class InstallStatus(
        val key: String,
        val label: String,
        val installed: Boolean,
        val retryIndex: Int,
        val error: String
    )

    @Volatile
    private var featureStatuses: List<FeatureStatus> = emptyList()

    @Volatile
    private var updatedAtMillis: Long = 0L

    /**
     * 已命中过的功能 id。命中可能发生在安装快照发布之前（hook 立即回调），
     * 所以命中记录独立于 [featureStatuses] 保存，发布快照时再合并。
     */
    private val triggeredIds = java.util.concurrent.ConcurrentHashMap.newKeySet<String>()

    fun updateFeatures(statuses: List<FeatureStatus>) {
        featureStatuses = statuses
        updatedAtMillis = System.currentTimeMillis()
    }

    fun features(): List<FeatureStatus> = featureStatuses

    fun isTriggered(featureId: String): Boolean =
        featureId.isNotEmpty() && triggeredIds.contains(featureId)

    /** 功能的关键 hook 命中时调用，把"待验证"提升为"正常"，不重跑整轮安装。 */
    fun markTriggered(featureId: String) {
        if (featureId.isEmpty()) return
        // hook 回调会高频进入这里，已命中过的功能直接返回，避免每次都复制整张快照。
        if (!triggeredIds.add(featureId)) return
        val snapshot = featureStatuses
        if (snapshot.isEmpty()) return
        var changed = false
        val updated = snapshot.map {
            if (it.featureId == featureId && !it.triggered) {
                changed = true
                it.copy(triggered = true)
            } else {
                it
            }
        }
        if (!changed) return
        featureStatuses = updated
        updatedAtMillis = System.currentTimeMillis()
    }

    @Volatile
    private var locateStatus: LocateStatus? = null

    fun updateLocate(status: LocateStatus) {
        locateStatus = status
    }

    fun locate(): LocateStatus? = locateStatus

    fun installs(): List<InstallStatus> = DexInstallScheduler.snapshot()

    fun updatedAtMillis(): Long = updatedAtMillis

    // ============ hook 热点统计 ============

    /**
     * 单个功能的 hook 调用累计值。全部走原子累加，回调线程不加锁；
     * 引用由 [hookStatFor] 保证唯一，包装器可以在构造时缓存下来，回调里不再查表。
     */
    class HookStat internal constructor(private val featureId: String) {
        private val calls = AtomicLong()
        private val samples = AtomicLong()
        private val totalNanos = AtomicLong()
        private val maxNanos = AtomicLong()
        private val slowCalls = AtomicLong()
        private val afterTicks = AtomicLong()
        private val lastWarnNanos = AtomicLong()

        fun recordCall(): Long = calls.incrementAndGet()

        /** after 回调独立计数，避免和 before 的调用次数互相干扰采样节奏。 */
        fun sampleAfter(): Boolean = (afterTicks.incrementAndGet() and SAMPLE_MASK) == 0L

        fun recordSample(nanos: Long) {
            samples.incrementAndGet()
            totalNanos.addAndGet(nanos)
            var current = maxNanos.get()
            while (nanos > current && !maxNanos.compareAndSet(current, nanos)) {
                current = maxNanos.get()
            }
            if (nanos >= SLOW_HOOK_NANOS) reportSlow(nanos)
        }

        /**
         * 超阈值只计数，不每次都写日志：卡顿正在发生时日志本身也要走 Binder，
         * 每条都打会让"慢"变成"更慢"。同一功能 5 秒内最多一条。
         */
        private fun reportSlow(nanos: Long) {
            val total = slowCalls.incrementAndGet()
            val now = System.nanoTime()
            val previous = lastWarnNanos.get()
            if (previous != 0L && now - previous < SLOW_WARN_INTERVAL_NANOS) return
            if (!lastWarnNanos.compareAndSet(previous, now)) return
            HLog.w(
                "$TAG 慢 hook feature=$featureId cost=${nanos / 1_000_000}ms" +
                    " calls=${callCount} slow=$total"
            )
        }

        /**
         * 就地清零，不换对象：HookTimingHook 在构造时缓存了本实例的引用，
         * 换成新对象会让旧引用继续写进孤儿统计，回调里也不会再多一次查表。
         */
        fun reset() {
            calls.set(0)
            samples.set(0)
            totalNanos.set(0)
            maxNanos.set(0)
            slowCalls.set(0)
            afterTicks.set(0)
            lastWarnNanos.set(0)
        }

        val callCount: Long get() = calls.get()
        val sampleCount: Long get() = samples.get()
        val totalCostNanos: Long get() = totalNanos.get()
        val maxCostNanos: Long get() = maxNanos.get()
        val slowCallCount: Long get() = slowCalls.get()
    }

    /** 排查页展示用的一条热点。耗时只统计模块自己的回调，不含微信原方法。 */
    data class HookHotspot(
        val featureId: String,
        val calls: Long,
        val samples: Long,
        val averageNanos: Long,
        val maxNanos: Long,
        val slowCalls: Long
    )

    private val hookStats = java.util.concurrent.ConcurrentHashMap<String, HookStat>()

    /** 同一个 featureId 始终拿到同一个统计对象。 */
    fun hookStatFor(featureId: String): HookStat {
        hookStats[featureId]?.let { return it }
        val created = HookStat(featureId)
        return hookStats.putIfAbsent(featureId, created) ?: created
    }

    /** 有慢回调的排前面，其余按调用次数降序；一次都没触发过的 hook 不计入。 */
    fun hookHotspots(limit: Int = 8): List<HookHotspot> {
        return hookStats.mapNotNull { (featureId, stat) ->
            val calls = stat.callCount
            if (calls <= 0L) return@mapNotNull null
            val samples = stat.sampleCount
            HookHotspot(
                featureId = featureId,
                calls = calls,
                samples = samples,
                averageNanos = if (samples > 0L) stat.totalCostNanos / samples else 0L,
                maxNanos = stat.maxCostNanos,
                slowCalls = stat.slowCallCount
            )
        }.sortedWith(
            compareByDescending<HookHotspot> { it.slowCalls }
                .thenByDescending { it.calls }
        ).take(limit)
    }

    /** 全部功能的 hook 调用总数，概览里一眼看出模块被调了多少次。 */
    fun hookCallTotal(): Long = hookStats.values.sumOf { it.callCount }

    /** 排查页的「重置热点统计」：清空累计值后重新观察，已安装的 hook 不受影响。 */
    fun resetHookStats() {
        hookStats.values.forEach { it.reset() }
    }
}

private const val TAG = "[Hchat:Perf]"

/** 采样间隔：64 次里的 1 次。 */
private const val SAMPLE_MASK = 63L

/** 单次模块回调超过 2ms 视为慢 hook：主线程上这个量级已经能被感知。 */
private const val SLOW_HOOK_NANOS = 2_000_000L

/** 同一功能 5 秒内最多打一条慢 hook 日志。 */
private const val SLOW_WARN_INTERVAL_NANOS = 5_000_000_000L
