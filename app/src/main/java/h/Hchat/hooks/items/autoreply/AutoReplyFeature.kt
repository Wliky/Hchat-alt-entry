package h.Hchat.hooks.items.autoreply

import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext
import h.Hchat.utils.HLog

class AutoReplyFeature : BaseFeature() {
    override fun featureId(): String = ID

    override fun name(): String = "自动回复"

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(AutoReplySettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
        subscribe(Events.DexReady::class.java) {
            val observe = WeChatApis.message().observe()
            if (observe == null) {
                HLog.e("$TAG 消息观察 API 未就绪")
            } else {
                trackSubscription(observe.subscribe { message ->
                    AutoReplyRuntime.handleMessage(context.hostContext(), message)
                })
            }
        }
    }

    companion object {
        const val ID = "auto_reply"
        private const val TAG = "[Hchat:AutoReply]"
    }
}
