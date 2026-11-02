package h.Hchat.hooks.items.diagnostics

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class ModuleDiagnosticsSettingsProvider : SimpleFeatureSettingsProvider(
    DiagnosticsFeature.ID,
    "模块排查",
    "查看全部功能点的安装与运行状态",
    FeatureSettingsProvider.CATEGORY_PRACTICAL
)
