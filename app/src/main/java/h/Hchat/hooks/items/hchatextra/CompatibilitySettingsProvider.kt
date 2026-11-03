package h.Hchat.hooks.items.hchatextra

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class CompatibilitySettingsProvider : SimpleFeatureSettingsProvider(
    FEATURE_ID,
    "适配微信列表",
    "8.0.49 – 8.0.80，按逆向记录与验证程度标注",
    FeatureSettingsProvider.CATEGORY_PRACTICAL
) {
    companion object {
        const val FEATURE_ID = "hchat_wechat_compatibility"
    }
}
