package h.Hchat.hooks.core

import h.Hchat.event.EventBus
import h.Hchat.event.EventHandler
import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.utils.HLog
import h.Hchat.utils.KavaReflector

/**
 * 功能模块基类。
 */
abstract class BaseFeature : Feature {
    private val subscriptions = ArrayList<EventBus.Subscription>()
    private val trackedSubscriptions = ArrayList<Any>()
    private val registeredProviderIds = ArrayList<String>()
    private var context: FeatureContext? = null

    @Throws(Throwable::class)
    final override fun onInit(context: FeatureContext) {
        this.context = context
        onFeatureInit(context)
    }

    @Throws(Throwable::class)
    final override fun install(context: FeatureContext) {
        this.context = context
        onFeatureInstall(context)
    }

    final override fun onDestroy(context: FeatureContext) {
        try {
            onFeatureDestroy(context)
        } catch (e: Throwable) {
            logError("onDestroy异常", e)
        }
        for (subscription in subscriptions) {
            try {
                context.eventBus().unsubscribe(subscription)
            } catch (_: Throwable) {
            }
        }
        subscriptions.clear()
        for (subscription in trackedSubscriptions) {
            try {
                KavaReflector.invoke(
                    KavaReflector.findMethod(subscription.javaClass, "unsubscribe"),
                    subscription
                )
            } catch (_: Throwable) {
            }
        }
        trackedSubscriptions.clear()
        for (featureId in registeredProviderIds) {
            try {
                context.uiRegistry().unregisterProvider(featureId)
            } catch (_: Throwable) {
            }
        }
        registeredProviderIds.clear()
    }

    @Throws(Throwable::class)
    protected open fun onFeatureInit(context: FeatureContext) {
    }

    @Throws(Throwable::class)
    protected abstract fun onFeatureInstall(context: FeatureContext)

    protected open fun onFeatureDestroy(context: FeatureContext) {
    }

    protected fun context(): FeatureContext? = context

    protected fun registerSettingsProvider(provider: FeatureSettingsProvider?) {
        val currentContext = context ?: return
        if (provider == null) return
        currentContext.uiRegistry().registerProvider(provider)
        registeredProviderIds.add(provider.featureId())
    }

    protected fun <E> subscribe(
        eventType: Class<E>,
        handler: EventHandler<E>
    ): EventBus.Subscription {
        return subscribe(eventType, handler, 0)
    }

    protected fun <E> subscribe(
        eventType: Class<E>,
        handler: EventHandler<E>,
        priority: Int
    ): EventBus.Subscription {
        val currentContext = context ?: return EventBus.Subscription.empty()
        // 事件派发不在安装过程中，这里补回功能上下文：
        // 处理函数里再注册的 hook / 监听器才能绑定到本功能，命中时才能打点。
        val featureId = featureId()
        val tagged = EventHandler<E> { event ->
            HookRegistry.get().withFeatureTag(featureId) { handler.handleEvent(event) }
        }
        val subscription = currentContext.eventBus().subscribe(eventType, tagged, priority)
        subscriptions.add(subscription)
        return subscription
    }

    /**
     * 手动标记运行时命中：没有任何 hook/订阅可打点的功能（纯页面、纯配置项、
     * 定时执行类）在自己的实际生效点调用它，排查页才会从「待验证」变「正常」。
     */
    protected fun markHit() {
        ModuleStatusStore.markTriggered(featureId())
    }

    protected fun <T : Any> trackSubscription(subscription: T?): T? {
        if (subscription != null) {
            trackedSubscriptions.add(subscription)
        }
        return subscription
    }

    protected fun globalBoolean(key: String, defValue: Boolean): Boolean {
        return context?.configStore()?.getGlobalBoolean(key, defValue) ?: defValue
    }

    protected fun featureBoolean(key: String, defValue: Boolean): Boolean {
        return context?.configStore()?.getBoolean(featureId(), key, defValue) ?: defValue
    }

    protected fun featureInt(key: String, defValue: Int): Int {
        return context?.configStore()?.getInt(featureId(), key, defValue) ?: defValue
    }

    protected fun featureString(key: String, defValue: String): String {
        return context?.configStore()?.getString(featureId(), key, defValue) ?: defValue
    }

    protected fun logError(message: String, throwable: Throwable?) {
        HLog.e(
            "[Hchat:${featureId()}] $message" +
                (throwable?.let { ": ${it.message}" } ?: ""),
            throwable
        )
    }
}
