package h.Hchat.utils;

import h.Hchat.compat.xposed.XposedBridge;

/**
 * 模块统一日志入口。
 *
 * <p>LSPosed 会把带 Throwable 的日志当成 error 处理并抓完整堆栈，单条动辄十几行；
 * 高频调用会持续占用 Binder 事务缓冲，日志里出现过
 * {@code FAILED BINDER TRANSACTION / Binder buffer full}，表现为微信卡顿、消息延迟。
 * 因此默认只走字符串通道，堆栈仅在显式传入 Throwable 时拼接。
 *
 * <p>级别：{@link #LEVEL_NONE} 关闭 / {@link #LEVEL_ERROR} 错误 /
 * {@link #LEVEL_WARN} 警告 / {@link #LEVEL_INFO} 信息 / {@link #LEVEL_DEBUG} 调试。
 * 默认错误级，行为与旧版一致但不再产生堆栈行。
 */
public final class HLog {

    public static final int LEVEL_NONE = 0;
    public static final int LEVEL_ERROR = 1;
    public static final int LEVEL_WARN = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_DEBUG = 4;

    public static final int DEFAULT_LEVEL = LEVEL_ERROR;

    private static final String TAG = "Hchat";

    private static volatile int level = DEFAULT_LEVEL;

    private HLog() {
    }

    /** 供设置页与模块入口在启动时同步级别。 */
    public static void setLevel(int value) {
        if (value < LEVEL_NONE) {
            level = LEVEL_NONE;
        } else if (value > LEVEL_DEBUG) {
            level = LEVEL_DEBUG;
        } else {
            level = value;
        }
    }

    public static int level() {
        return level;
    }

    public static boolean enabled(int target) {
        return level >= target;
    }

    public static boolean isDebug() {
        return level >= LEVEL_DEBUG;
    }

    public static boolean isInfo() {
        return level >= LEVEL_INFO;
    }

    public static void e(String message) {
        if (level < LEVEL_ERROR) return;
        emit("E", message, null);
    }

    public static void e(String message, Throwable throwable) {
        if (level < LEVEL_ERROR) return;
        emit("E", message, throwable);
    }

    public static void w(String message) {
        if (level < LEVEL_WARN) return;
        emit("W", message, null);
    }

    public static void i(String message) {
        if (level < LEVEL_INFO) return;
        emit("I", message, null);
    }

    public static void d(String message) {
        if (level < LEVEL_DEBUG) return;
        emit("D", message, null);
    }

    /**
     * 惰性日志：只有调试级打开时才拼接字符串，热路径优先用它，
     * 避免"日志关着但字符串照拼"。
     */
    public static void d(LazyMessage message) {
        if (level < LEVEL_DEBUG || message == null) return;
        emit("D", message.get(), null);
    }

    public static void i(LazyMessage message) {
        if (level < LEVEL_INFO || message == null) return;
        emit("I", message.get(), null);
    }

    private static void emit(String priority, String message, Throwable throwable) {
        StringBuilder sb = new StringBuilder(message == null ? 8 : message.length() + 12);
        sb.append(priority).append('/').append(TAG).append(": ").append(message);
        if (throwable != null) {
            sb.append('\n').append(android.util.Log.getStackTraceString(throwable));
        }
        try {
            XposedBridge.log(sb.toString());
        } catch (Throwable ignored) {
            // 日志本身不能影响主流程。
        }
    }

    /** 避免在热路径捕获 lambda 时依赖 java.util.function。 */
    public interface LazyMessage {
        String get();
    }
}
