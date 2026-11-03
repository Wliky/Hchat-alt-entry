package h.Hchat.hooks.core

/**
 * 所有功能模块的统一接口。
 */
interface Feature {
    fun featureId(): String

    fun name(): String = featureId()

    fun isEnabled(context: FeatureContext): Boolean {
        return context.configStore().getGlobalBoolean(featureId() + "_enabled", true)
    }

    @Throws(Throwable::class)
    fun onInit(context: FeatureContext) {
    }

    @Throws(Throwable::class)
    fun install(context: FeatureContext)

    /**
     * 可用性自检：返回 null 或空串表示依赖齐全，返回原因表示当前微信版本下这个功能
     * 实际跑不起来。和 [install] 是否抛异常是两件事——定位缺失时大多数功能会静默跳过，
     * 安装流程照样"成功"。
     */
    fun healthCheck(context: FeatureContext): String? = null

    /**
     * 运行时证据：关键 hook 是否真的命中过。"安装没抛异常"不等于功能跑起来了，
     * 排查页据此区分"正常"和"待验证"。默认读取运行时命中记录，
     * 由 [HookRegistry] 的 hook 包装和 [ModuleStatusStore.markTriggered] 写入。
     */
    fun runtimeHit(): Boolean = ModuleStatusStore.isTriggered(featureId())

    /**
     * 安装期注册的 hook 第一次被调用时，是否自动算作"已命中"。
     * 通用回调（例如 LauncherUI 的 dispatchTouchEvent、消息列表 bind）会在功能真正生效前
     * 就被高频触发，这类功能返回 false，改为在自己确认成功的分支调用 markHit()。
     */
    fun autoMarkOnHook(): Boolean = true

    /**
     * 本功能是否必须产生运行时绑定（hook 或事件订阅）才算真的挂上。
     * 纯页面、纯配置项注入、定时执行，以及只借用公共 API 层 hook 的功能返回 false，
     * 排查页才不会把"没注册 hook"误报成不可用。
     */
    fun requiresRuntimeBinding(): Boolean = true

    /**
     * 安装期是否真的注册了 hook 或事件订阅。默认 true：非 [BaseFeature] 的实现
     * 不做这项自检，避免误报。
     */
    fun hasRuntimeBinding(): Boolean = true

    fun onConfigChanged(context: FeatureContext, key: String?) {
    }

    fun onDestroy(context: FeatureContext) {
    }
}
