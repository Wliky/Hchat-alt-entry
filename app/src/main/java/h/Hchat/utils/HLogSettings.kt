package h.Hchat.utils

import android.content.Context
import h.Hchat.preferences.HchatStorage

/**
 * 日志级别的持久化与生效入口。
 *
 * 级别存在模块自己的 FastKV 里，设置页改动后立即写内存，模块重启后由 [apply] 恢复。
 */
object HLogSettings {
    const val PREFS_NAME = "Hchat_log"
    const val KEY_LEVEL = "level"

    /** 关闭 / 错误 / 警告 / 信息 / 调试。 */
    val choices: List<Pair<Int, String>> = listOf(
        HLog.LEVEL_NONE to "关闭",
        HLog.LEVEL_ERROR to "错误",
        HLog.LEVEL_WARN to "警告",
        HLog.LEVEL_INFO to "信息",
        HLog.LEVEL_DEBUG to "调试"
    )

    fun level(context: Context): Int = runCatching {
        HchatStorage.preferences(context, PREFS_NAME).getInt(KEY_LEVEL, HLog.DEFAULT_LEVEL)
    }.getOrDefault(HLog.DEFAULT_LEVEL)

    fun labelOf(level: Int): String =
        choices.firstOrNull { it.first == level }?.second ?: choices.first().second

    fun setLevel(context: Context, level: Int) {
        HLog.setLevel(level)
        runCatching {
            HchatStorage.preferences(context, PREFS_NAME).edit().putInt(KEY_LEVEL, level).apply()
        }
    }

    /** 模块进程启动时调用一次，把持久化级别灌进 [HLog]。 */
    @JvmStatic
    fun apply(context: Context) {
        HLog.setLevel(level(context))
    }
}
