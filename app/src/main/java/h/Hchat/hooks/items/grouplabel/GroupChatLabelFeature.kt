package h.Hchat.hooks.items.grouplabel

import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext

class GroupChatLabelFeature : BaseFeature() {
    override fun featureId(): String = ID

    override fun name(): String = "群聊标签"

    /** 纯设置页功能：标签数据由设置页维护，没有需要 hook 的运行时路径。 */
    override fun requiresRuntimeBinding(): Boolean = false

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(GroupChatLabelSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) = Unit

    companion object {
        const val ID = "group_chat_label"
    }
}
