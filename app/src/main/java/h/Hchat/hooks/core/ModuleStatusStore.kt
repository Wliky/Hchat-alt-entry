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
        val health: String = ""
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

    fun updateFeatures(statuses: List<FeatureStatus>) {
        featureStatuses = statuses
        updatedAtMillis = System.currentTimeMillis()
    }

    fun features(): List<FeatureStatus> = featureStatuses

    @Volatile
    private var locateStatus: LocateStatus? = null

    fun updateLocate(status: LocateStatus) {
        locateStatus = status
    }

    fun locate(): LocateStatus? = locateStatus

    fun installs(): List<InstallStatus> = DexInstallScheduler.snapshot()

    fun updatedAtMillis(): Long = updatedAtMillis
}
