package h.Hchat.hooks.core

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
}
