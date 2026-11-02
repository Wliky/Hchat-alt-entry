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
        val error: String
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

    fun installs(): List<InstallStatus> = DexInstallScheduler.snapshot()

    fun updatedAtMillis(): Long = updatedAtMillis
}
