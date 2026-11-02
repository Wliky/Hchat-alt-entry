package h.Hchat.compat.xposed;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.libxposed.api.XposedInterface;

/** 将旧版 XposedBridge 调用转发到 libxposed API 102。 */
public final class XposedBridge {
    private static final String TAG = "Hchat";

    private XposedBridge() {
    }

    public static XC_MethodHook.Unhook hookMethod(Member method, XC_MethodHook callback) {
        if (!(method instanceof Executable)) {
            throw new IllegalArgumentException("Not an executable member: " + method);
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback == null");
        }
        XposedInterface.HookHandle handle = XposedCompat.module()
                .hook((Executable) method)
                .setPriority(callback.getPriority())
                .setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE)
                .intercept(new CompatHooker(callback));
        return new HookHandleUnhook(handle);
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> clazz,
                                                           String methodName,
                                                           XC_MethodHook callback) {
        Set<XC_MethodHook.Unhook> hooks = new LinkedHashSet<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) continue;
            if (Modifier.isAbstract(method.getModifiers())) continue;
            hooks.add(hookMethod(method, callback));
        }
        return hooks;
    }

    public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> clazz,
                                                                XC_MethodHook callback) {
        Set<XC_MethodHook.Unhook> hooks = new LinkedHashSet<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            hooks.add(hookMethod(constructor, callback));
        }
        return hooks;
    }

    public static void log(String text) {
        XposedCompat.log(Log.INFO, TAG, String.valueOf(text), null);
    }

    public static void log(Throwable throwable) {
        String message = throwable != null ? throwable.getMessage() : null;
        XposedCompat.log(Log.ERROR, TAG,
                message != null ? message : String.valueOf(throwable),
                throwable);
    }

    private static final class HookHandleUnhook implements XC_MethodHook.Unhook {
        private final XposedInterface.HookHandle handle;
        private final AtomicBoolean active = new AtomicBoolean(true);

        private HookHandleUnhook(XposedInterface.HookHandle handle) {
            this.handle = handle;
        }

        @Override
        public void unhook() {
            if (active.compareAndSet(true, false)) {
                handle.unhook();
            }
        }

        @Override
        public boolean isHooked() {
            return active.get();
        }
    }

    private static final class CompatHooker implements XposedInterface.Hooker {
        private final XC_MethodHook callback;

        private CompatHooker(XC_MethodHook callback) {
            this.callback = callback;
        }

        @Override
        public Object intercept(XposedInterface.Chain chain) throws Throwable {
            Executable executable = chain.getExecutable();
            boolean constructor = executable instanceof Constructor;

            XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();
            param.method = executable;
            param.thisObject = chain.getThisObject();
            param.args = chain.getArgs().toArray();

            try {
                callback.beforeHookedMethod(param);
            } catch (Throwable throwable) {
                log(throwable);
            }

            boolean skippedOriginal = !constructor && param.returnEarly;
            if (!skippedOriginal) {
                try {
                    param.rawResult = chain.proceed(param.args != null ? param.args : new Object[0]);
                    param.rawThrowable = null;
                } catch (Throwable throwable) {
                    param.rawResult = null;
                    param.rawThrowable = throwable;
                }
                param.returnEarly = false;
                param.thisObject = chain.getThisObject();
            }

            try {
                callback.afterHookedMethod(param);
            } catch (Throwable throwable) {
                log(throwable);
            }

            if (!constructor) {
                if (param.returnEarly) return param.rawResult;
                if (param.rawThrowable != null) throw param.rawThrowable;
                return param.rawResult;
            }

            if (param.rawThrowable != null) throw param.rawThrowable;
            return null;
        }
    }
}
