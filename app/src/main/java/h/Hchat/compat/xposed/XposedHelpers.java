package h.Hchat.compat.xposed;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/** 将旧版 XposedHelpers 的常用反射能力转发到 libxposed Hook 兼容层。 */
public final class XposedHelpers {
    private static final Map<String, Class<?>> PRIMITIVE_TYPES = new HashMap<>();

    static {
        PRIMITIVE_TYPES.put("boolean", boolean.class);
        PRIMITIVE_TYPES.put("byte", byte.class);
        PRIMITIVE_TYPES.put("char", char.class);
        PRIMITIVE_TYPES.put("short", short.class);
        PRIMITIVE_TYPES.put("int", int.class);
        PRIMITIVE_TYPES.put("long", long.class);
        PRIMITIVE_TYPES.put("float", float.class);
        PRIMITIVE_TYPES.put("double", double.class);
        PRIMITIVE_TYPES.put("void", void.class);
    }

    private XposedHelpers() {
    }

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class not found: " + className, e);
        }
    }

    public static Method findMethodExact(Class<?> clazz, String methodName,
                                         Class<?>... parameterTypes) {
        for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
            try {
                Method method = current.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        throw new IllegalStateException("Method not found: " + clazz.getName() + '#' + methodName);
    }

    public static Constructor<?> findConstructorExact(Class<?> clazz,
                                                      Class<?>... parameterTypes) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Constructor not found: " + clazz.getName(), e);
        }
    }

    public static XC_MethodHook.Unhook findAndHookMethod(Class<?> clazz,
                                                         String methodName,
                                                         Object... parameterTypesAndCallback) {
        HookArgs hookArgs = splitHookArgs(parameterTypesAndCallback);
        Class<?>[] parameterTypes = resolveParameterTypes(clazz, hookArgs.parameterTypes);
        return XposedBridge.hookMethod(findMethodExact(clazz, methodName, parameterTypes),
                hookArgs.callback);
    }

    public static XC_MethodHook.Unhook findAndHookMethod(String className,
                                                         ClassLoader classLoader,
                                                         String methodName,
                                                         Object... parameterTypesAndCallback) {
        return findAndHookMethod(findClass(className, classLoader), methodName,
                parameterTypesAndCallback);
    }

    public static XC_MethodHook.Unhook findAndHookConstructor(Class<?> clazz,
                                                              Object... parameterTypesAndCallback) {
        HookArgs hookArgs = splitHookArgs(parameterTypesAndCallback);
        Class<?>[] parameterTypes = resolveParameterTypes(clazz, hookArgs.parameterTypes);
        return XposedBridge.hookMethod(findConstructorExact(clazz, parameterTypes),
                hookArgs.callback);
    }

    public static Object callMethod(Object instance, String methodName, Object... args) {
        Class<?> clazz = instance instanceof Class<?> ? (Class<?>) instance : instance.getClass();
        Method method = findMethodBestMatch(clazz, methodName, args);
        return invoke(method, instance instanceof Class<?> ? null : instance, args);
    }

    public static Object callMethod(Object instance, String methodName,
                                    Class<?>[] parameterTypes, Object... args) {
        Class<?> clazz = instance instanceof Class<?> ? (Class<?>) instance : instance.getClass();
        Method method = findMethodExact(clazz, methodName, parameterTypes);
        return invoke(method, instance instanceof Class<?> ? null : instance, args);
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        Method method = findMethodBestMatch(clazz, methodName, args);
        return invoke(method, null, args);
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName,
                                          Class<?>[] parameterTypes, Object... args) {
        return invoke(findMethodExact(clazz, methodName, parameterTypes), null, args);
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Object... args) {
        Method best = null;
        int bestScore = -1;
        for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(methodName)) continue;
                if (method.getParameterCount() != args.length) continue;
                int score = score(method.getParameterTypes(), args);
                if (score > bestScore || (score == bestScore && best != null
                        && best.getDeclaringClass().isAssignableFrom(current))) {
                    best = method;
                    bestScore = score;
                }
            }
        }
        if (best == null || bestScore < 0) {
            throw new IllegalStateException("Method not found: " + clazz.getName() + '#' + methodName);
        }
        best.setAccessible(true);
        return best;
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Object... args) {
        Constructor<?> best = null;
        int bestScore = -1;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() != args.length) continue;
            int score = score(constructor.getParameterTypes(), args);
            if (score > bestScore) {
                best = constructor;
                bestScore = score;
            }
        }
        if (best == null || bestScore < 0) {
            throw new IllegalStateException("Constructor not found: " + clazz.getName());
        }
        best.setAccessible(true);
        return best;
    }

    public static Object newInstance(Class<?> clazz, Object... args) {
        try {
            return findConstructorBestMatch(clazz, args).newInstance(args);
        } catch (Throwable throwable) {
            throw propagate(throwable);
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }
        throw new IllegalStateException("Field not found: " + clazz.getName() + '#' + fieldName);
    }

    public static Object getObjectField(Object instance, String fieldName) {
        return getField(instance, fieldName);
    }

    public static void setObjectField(Object instance, String fieldName, Object value) {
        setField(instance, fieldName, value);
    }

    public static boolean getBooleanField(Object instance, String fieldName) {
        return (Boolean) getField(instance, fieldName);
    }

    public static void setBooleanField(Object instance, String fieldName, boolean value) {
        setField(instance, fieldName, value);
    }

    public static int getIntField(Object instance, String fieldName) {
        return (Integer) getField(instance, fieldName);
    }

    public static void setIntField(Object instance, String fieldName, int value) {
        setField(instance, fieldName, value);
    }

    public static long getLongField(Object instance, String fieldName) {
        return (Long) getField(instance, fieldName);
    }

    public static void setLongField(Object instance, String fieldName, long value) {
        setField(instance, fieldName, value);
    }

    public static Object getStaticObjectField(Class<?> clazz, String fieldName) {
        return getField(clazz, fieldName);
    }

    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {
        setField(clazz, fieldName, value);
    }

    public static int getStaticIntField(Class<?> clazz, String fieldName) {
        return (Integer) getField(clazz, fieldName);
    }

    public static void setStaticIntField(Class<?> clazz, String fieldName, int value) {
        setField(clazz, fieldName, value);
    }

    private static Object getField(Object instance, String fieldName) {
        Class<?> clazz = instance instanceof Class<?> ? (Class<?>) instance : instance.getClass();
        try {
            Field field = findField(clazz, fieldName);
            return field.get(Modifier.isStatic(field.getModifiers()) ? null : instance);
        } catch (Throwable throwable) {
            throw propagate(throwable);
        }
    }

    private static void setField(Object instance, String fieldName, Object value) {
        Class<?> clazz = instance instanceof Class<?> ? (Class<?>) instance : instance.getClass();
        try {
            Field field = findField(clazz, fieldName);
            field.set(Modifier.isStatic(field.getModifiers()) ? null : instance, value);
        } catch (Throwable throwable) {
            throw propagate(throwable);
        }
    }

    private static Object invoke(Method method, Object receiver, Object[] args) {
        try {
            return method.invoke(receiver, args);
        } catch (Throwable throwable) {
            throw propagate(throwable);
        }
    }

    private static RuntimeException propagate(Throwable throwable) {
        if (throwable instanceof InvocationTargetException) {
            Throwable cause = ((InvocationTargetException) throwable).getCause();
            if (cause instanceof RuntimeException) return (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            return new RuntimeException(cause);
        }
        if (throwable instanceof RuntimeException) return (RuntimeException) throwable;
        if (throwable instanceof Error) throw (Error) throwable;
        return new RuntimeException(throwable);
    }

    private static int score(Class<?>[] parameterTypes, Object[] args) {
        int score = 0;
        for (int i = 0; i < parameterTypes.length; i++) {
            int itemScore = score(parameterTypes[i], args[i]);
            if (itemScore < 0) return -1;
            score += itemScore;
        }
        return score;
    }

    private static int score(Class<?> parameterType, Object argument) {
        if (argument == null) return parameterType.isPrimitive() ? -1 : 1;
        Class<?> argumentType = argument.getClass();
        if (parameterType == argumentType) return 4;
        Class<?> boxed = box(parameterType);
        if (boxed == argumentType) return 3;
        if (boxed.isAssignableFrom(argumentType)) return 2;
        if (parameterType.isPrimitive() && isNumeric(boxed) && isNumeric(argumentType)) return 1;
        return -1;
    }

    private static Class<?> box(Class<?> type) {
        if (!type.isPrimitive()) return type;
        if (type == boolean.class) return Boolean.class;
        if (type == byte.class) return Byte.class;
        if (type == char.class) return Character.class;
        if (type == short.class) return Short.class;
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == float.class) return Float.class;
        if (type == double.class) return Double.class;
        return Void.class;
    }

    private static boolean isNumeric(Class<?> type) {
        return type == Byte.class || type == Short.class || type == Integer.class
                || type == Long.class || type == Float.class || type == Double.class
                || type == Character.class;
    }

    private static HookArgs splitHookArgs(Object[] parameterTypesAndCallback) {
        if (parameterTypesAndCallback.length == 0
                || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1]
                instanceof XC_MethodHook)) {
            throw new IllegalArgumentException("Last argument must be an XC_MethodHook");
        }
        Object[] types = new Object[parameterTypesAndCallback.length - 1];
        System.arraycopy(parameterTypesAndCallback, 0, types, 0, types.length);
        XC_MethodHook callback =
                (XC_MethodHook) parameterTypesAndCallback[parameterTypesAndCallback.length - 1];
        return new HookArgs(types, callback);
    }

    private static Class<?>[] resolveParameterTypes(Class<?> clazz, Object[] values) {
        Class<?>[] result = new Class<?>[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value instanceof Class<?>) {
                result[i] = (Class<?>) value;
            } else if (value instanceof String) {
                String name = (String) value;
                Class<?> primitive = PRIMITIVE_TYPES.get(name);
                if (primitive != null) {
                    result[i] = primitive;
                } else {
                    result[i] = findClass(name, clazz.getClassLoader());
                }
            } else {
                throw new IllegalArgumentException("Invalid parameter type at index " + i + ": " + value);
            }
        }
        return result;
    }

    private static final class HookArgs {
        private final Object[] parameterTypes;
        private final XC_MethodHook callback;

        private HookArgs(Object[] parameterTypes, XC_MethodHook callback) {
            this.parameterTypes = parameterTypes;
            this.callback = callback;
        }
    }
}
