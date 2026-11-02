package h.Hchat.hooks.items.hchatextra

import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.SimpleFeatureSettingsProvider

class BackupSettingsProvider : SimpleFeatureSettingsProvider(
    FEATURE_ID,
    "备份",
    "导出或导入 Hchat 的全部功能配置",
    FeatureSettingsProvider.CATEGORY_PRACTICAL
) {
    companion object {
        const val FEATURE_ID = "hchat_config_backup"
    }
}
