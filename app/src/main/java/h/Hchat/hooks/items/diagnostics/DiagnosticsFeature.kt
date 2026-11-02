package h.Hchat.hooks.items.diagnostics

import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext

/** 只提供「模块排查」页面，不挂钩微信内部方法。 */
class DiagnosticsFeature : BaseFeature() {
    override fun featureId(): String = ID

    override fun name(): String = "模块排查"

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(ModuleDiagnosticsSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
        // 诊断页只读取内存快照，无需安装 hook。
    }

    companion object {
        const val ID = "module_diagnostics"
    }
}
