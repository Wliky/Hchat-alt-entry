package h.Hchat.compat.xposed;

import android.util.Log;

import java.util.Objects;

import io.github.libxposed.api.XposedModule;

/** 当前进程中的 libxposed 模块实例。 */
public final class XposedCompat {
    private static final String TAG = "Hchat";
    private static volatile XposedModule module;

    private XposedCompat() {
    }

    public static void install(XposedModule instance) {
        Objects.requireNonNull(instance, "module");
        int apiVersion = instance.getApiVersion();
        if (apiVersion < 102) {
            throw new IllegalStateException("Hchat requires libxposed API 102+, current " + apiVersion);
        }
        module = instance;
    }

    public static boolean isInstalled() {
        return module != null;
    }

    static XposedModule module() {
        XposedModule instance = module;
        if (instance == null) {
            throw new IllegalStateException("libxposed module is not initialized");
        }
        return instance;
    }

    static void log(int priority, String tag, String message, Throwable throwable) {
        XposedModule instance = module;
        if (instance != null) {
            instance.log(priority, tag, message, throwable);
        } else if (throwable == null) {
            Log.println(priority, tag, message);
        } else {
            Log.println(priority, tag, message + '\n' + Log.getStackTraceString(throwable));
        }
    }
}
