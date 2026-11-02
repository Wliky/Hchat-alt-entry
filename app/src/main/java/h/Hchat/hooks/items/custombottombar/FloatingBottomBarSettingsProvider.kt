package h.Hchat.hooks.items.custombottombar

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class FloatingBottomBarSettingsProvider : SimpleFeatureSettingsProvider(
    FloatingBottomBarSettings.FEATURE_ID,
    "悬浮底栏",
    "将微信首页底栏显示为悬浮样式",
    FeatureSettingsProvider.CATEGORY_PRACTICAL
)
