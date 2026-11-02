package h.Hchat.compat.xposed;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

import h.Hchat.compat.xposed.callbacks.XCallback;

/**
 * 旧版 Xposed 回调模型到 libxposed API 102 的兼容层。
 *
 * 仅保留 Hchat 自身及脚本 API 正在使用的语义，不模拟已废弃的框架入口。
 */
public abstract class XC_MethodHook extends XCallback {
    public XC_MethodHook() {
        super();
    }

    public XC_MethodHook(int priority) {
        super(priority);
    }

    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    }

    public interface Unhook {
        void unhook();

        boolean isHooked();
    }

    public static final class MethodHookParam {
        public Member method;
        public Object thisObject;
        public Object[] args;

        Object rawResult;
        Throwable rawThrowable;
        boolean returnEarly;
        private Map<String, Object> extras;

        public Object getResult() {
            return rawResult;
        }

        public void setResult(Object result) {
            rawResult = result;
            rawThrowable = null;
            returnEarly = true;
        }

        public Throwable getThrowable() {
            return rawThrowable;
        }

        public boolean hasThrowable() {
            return rawThrowable != null;
        }

        public void setThrowable(Throwable throwable) {
            rawResult = null;
            rawThrowable = throwable;
            returnEarly = true;
        }

        public Object getObjectExtra(String key) {
            return extras != null ? extras.get(key) : null;
        }

        public void setObjectExtra(String key, Object value) {
            if (value == null) {
                if (extras != null) extras.remove(key);
                return;
            }
            if (extras == null) extras = new HashMap<>();
            extras.put(key, value);
        }
    }
}
