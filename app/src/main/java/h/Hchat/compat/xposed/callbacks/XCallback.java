package h.Hchat.compat.xposed.callbacks;

/** 旧版回调优先级常量。 */
public class XCallback {
    public static final int PRIORITY_DEFAULT = 50;
    public static final int PRIORITY_LOWEST = -10000;
    public static final int PRIORITY_LOW = -5000;
    public static final int PRIORITY_HIGH = 5000;
    public static final int PRIORITY_HIGHEST = 10000;

    private final int priority;

    public XCallback() {
        this(PRIORITY_DEFAULT);
    }

    public XCallback(int priority) {
        this.priority = priority;
    }

    public final int getPriority() {
        return priority;
    }
}
