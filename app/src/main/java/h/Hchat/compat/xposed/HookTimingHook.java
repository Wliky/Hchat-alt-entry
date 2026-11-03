package h.Hchat.compat.xposed;

/**
 * 给功能级 hook 做低成本采样计时，供「模块排查」排序热点。
 *
 * <p>每次回调只做一次原子自增，每 64 次（以及最开始的 4 次）才取一次
 * {@link System#nanoTime()}，避免计时本身污染被测链路。计时只覆盖模块自己的
 * before / after 回调，不含被 hook 方法的执行时间——要回答的是"模块自己占了多少"。</p>
 *
 * <p>局限：{@code HookRegistry.add(unhook)} 拿到的是已经装好的句柄，无法再包一层，
 * 因此 findAndHookMethod 路径的 hook 只会计数、不会计时。</p>
 */
public final class HookTimingHook extends XC_MethodHook {
    private static final long SAMPLE_MASK = 63L;
    private static final long EARLY_SAMPLES = 4L;

    /** 由 HookRegistry 注入，避免 compat 包反向依赖 hooks.core。 */
    public interface Sink {
        long recordCall();

        boolean sampleAfter();

        void recordSample(long nanos);
    }

    private final XC_MethodHook delegate;
    private final Sink sink;

    public HookTimingHook(XC_MethodHook delegate, Sink sink) {
        super(delegate.getPriority());
        this.delegate = delegate;
        this.sink = sink;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        long count = sink.recordCall();
        boolean sampled = count <= EARLY_SAMPLES || (count & SAMPLE_MASK) == 0L;
        if (!sampled) {
            delegate.beforeHookedMethod(param);
            return;
        }
        long start = System.nanoTime();
        try {
            delegate.beforeHookedMethod(param);
        } finally {
            sink.recordSample(System.nanoTime() - start);
        }
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        if (!sink.sampleAfter()) {
            delegate.afterHookedMethod(param);
            return;
        }
        long start = System.nanoTime();
        try {
            delegate.afterHookedMethod(param);
        } finally {
            sink.recordSample(System.nanoTime() - start);
        }
    }
}
