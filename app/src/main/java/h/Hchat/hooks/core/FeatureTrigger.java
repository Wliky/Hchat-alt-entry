package h.Hchat.hooks.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 运行时命中探针：功能的关键回调真正被调用时打一次点。
 *
 * <p>Java 侧 API（各 WeChatXxxApi 的 subscribe）用它把「当前正在安装的功能 id」
 * 绑到监听器上，监听器第一次收到事件时把该功能从「待验证」提升为「正常」。</p>
 */
public final class FeatureTrigger {
    private FeatureTrigger() {
    }

    public static void mark(String featureId) {
        if (featureId == null || featureId.isEmpty()) return;
        ModuleStatusStore.INSTANCE.markTriggered(featureId);
    }

    /**
     * 给功能安装期间注册的监听器套一层代理，第一次回调时打点。
     * featureId 为空（非功能安装期注册）时原样返回，不产生额外开销。
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(final Class<T> type, final T listener, final String featureId) {
        if (listener == null || featureId == null || featureId.isEmpty()) return listener;
        if (type == null || !type.isInterface()) return listener;
        if (Proxy.isProxyClass(listener.getClass())) return listener;
        ClassLoader loader = type.getClassLoader();
        if (loader == null) loader = listener.getClass().getClassLoader();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    String name = method.getName();
                    if ("equals".equals(name)) return proxy == args[0];
                    if ("hashCode".equals(name)) return System.identityHashCode(proxy);
                    if ("toString".equals(name)) return "HchatTrigger(" + listener + ")";
                }
                mark(featureId);
                try {
                    return method.invoke(listener, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        };
        return (T) Proxy.newProxyInstance(loader, new Class<?>[]{type}, handler);
    }
}
