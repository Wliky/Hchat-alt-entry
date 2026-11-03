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

    fun onConfigChanged(context: FeatureContext, key: String?) {
    }

    fun onDestroy(context: FeatureContext) {
    }
}
