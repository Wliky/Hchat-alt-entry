package h.Hchat.compat.xposed;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 给已有 Xposed 回调再包一层：第一次真正被微信调用时执行一次 marker。
 *
 * <p>只做打点，不改参数、不拦截返回值，优先级沿用原回调，因此对功能行为透明。
 * 供「模块排查」判断某个功能的关键 hook 是否真的命中过。</p>
 */
public final class TriggerMarkingHook extends XC_MethodHook {
    private final XC_MethodHook delegate;
    private final Runnable marker;
    private final AtomicBoolean marked = new AtomicBoolean(false);

    public TriggerMarkingHook(XC_MethodHook delegate, Runnable marker) {
        super(delegate.getPriority());
        this.delegate = delegate;
        this.marker = marker;
    }

    private void mark() {
        if (marker == null || !marked.compareAndSet(false, true)) return;
        try {
            marker.run();
        } catch (Throwable ignored) {
            // 打点失败不能影响原 hook 的行为。
        }
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        mark();
        delegate.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        delegate.afterHookedMethod(param);
    }
}
