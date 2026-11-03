package h.Hchat.hooks.items.audiotransform

import h.Hchat.hooks.core.BaseFeature
import h.Hchat.hooks.core.FeatureContext

class AudioTransformFeature : BaseFeature() {
    override fun featureId(): String = ID

    override fun name(): String = "音频转换"

    /** 纯设置页功能：真正生效点在设置页操作里，由页面调用 markHit 手动打点。 */
    override fun requiresRuntimeBinding(): Boolean = false

    override fun onFeatureInit(context: FeatureContext) {
        registerSettingsProvider(AudioTransformSettingsProvider())
    }

    override fun onFeatureInstall(context: FeatureContext) {
    }

    companion object {
        const val ID = "audio_transform"
    }
}
