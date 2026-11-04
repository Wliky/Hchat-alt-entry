package h.Hchat.hooks.core

import h.Hchat.event.Events
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.items.settings.SettingsFeature
import h.Hchat.hooks.items.conversationgroup.ConversationGroupHomeProjection
import h.Hchat.utils.HLog
import java.util.Collections

/**
 * 功能模块调度器。
 */
class FeatureManager {
    private val features = ArrayList<Feature>()
    private val installedFeatures = ArrayList<Feature>()

    fun register(feature: Feature?): FeatureManager {
        if (feature != null) {
            features.add(feature)
        }
        return this
    }

    fun features(): List<Feature> = Collections.unmodifiableList(features)

    fun installedFeatures(): List<Feature> = Collections.unmodifiableList(installedFeatures)

    fun installAll(context: FeatureContext) {
        // 命中记录按微信版本落盘，必须早于功能安装回灌：
        // 否则安装期间立即触发的 hook 会把历史记录覆盖成"只有本次进程"。
        ModuleStatusStore.attach(context.hostContext(), currentWeChatVersion())
        val initializedFeatures = ArrayList<Feature>()
        val enabledStates = HashMap<Feature, Boolean>()
        val failures = HashMap<Feature, String>()
        for (feature in features) {
            val displayName = safeName(feature)
            try {
                // init 阶段的订阅同样归属这个功能，事件派发时才能反过来打点。
                HookRegistry.get().withFeatureTag(safeFeatureId(feature), feature.autoMarkOnHook()) {
                    feature.onInit(context)
                }
                initializedFeatures.add(feature)
            } catch (e: Throwable) {
                failures[feature] = describe(e)
                HLog.e("$TAG onInit 失败: $displayName, error=$e", e)
            }
        }

        for (feature in initializedFeatures) {
            val displayName = safeName(feature)
            val enabled = try {
                feature.isEnabled(context)
            } catch (e: Throwable) {
                HLog.e("$TAG isEnabled 检查异常，默认启用: $displayName", e)
                true
            }
            enabledStates[feature] = enabled
            if (!enabled) {
                continue
            }

            try {
                // 安装期间注册的 hook 和监听器都绑定这个功能 id，命中时才能反过来打点。
                HookRegistry.get().withFeatureTag(safeFeatureId(feature), feature.autoMarkOnHook()) {
                    feature.install(context)
                }
                installedFeatures.add(feature)
                try {
                    context.eventBus().post(Events.FeatureInstalled(displayName))
                } catch (_: Throwable) {
                }
            } catch (e: Throwable) {
                failures[feature] = describe(e)
                HLog.e("$TAG 功能安装失败: $displayName, error=$e", e)
            }
        }
        publishStatus(context, enabledStates, failures)
        DexInstallScheduler.markDexBridgeReady()
    }

    /** 版本号只用于隔离命中记录；取不到时归入 unknown，不影响功能本身。 */
    private fun currentWeChatVersion(): String = try {
        WeChatApis.version()?.versionName().orEmpty()
    } catch (_: Throwable) {
        ""
    }

    /** 把本次安装结果写入快照，「模块排查」页面直接读取，避免再跑一遍定位。 */
    private fun publishStatus(
        context: FeatureContext,
        enabledStates: Map<Feature, Boolean>,
        failures: Map<Feature, String>
    ) {
        publishLocateStatus(context)
        val statuses = features.map { feature ->
            val enabled = enabledStates[feature] ?: false
            val installed = installedFeatures.contains(feature)
            ModuleStatusStore.FeatureStatus(
                featureId = try {
                    feature.featureId()
                } catch (_: Throwable) {
                    ""
                },
                name = safeName(feature),
                enabled = enabled,
                installed = installed,
                error = failures[feature].orEmpty(),
                // 只对真正跑起来的功能做自检；没启用或安装失败的状态已经能说明问题。
                health = if (enabled && installed) {
                    try {
                        // healthCheck 没意见时补一条通用自检：声明要靠 hook 生效却一个都没挂上。
                        feature.healthCheck(context).orEmpty().ifEmpty { describeBindingGap(feature) }
                    } catch (e: Throwable) {
                        describe(e)
                    }
                } else {
                    ""
                },
                triggered = enabled && installed && try {
                    feature.runtimeHit()
                } catch (_: Throwable) {
                    false
                },
                // 没注册 hook 的功能没有可观测信号，排查页不能把它算成"待验证"。
                runtimeTracked = try {
                    HookRegistry.get().hookCountFor(safeFeatureId(feature)) > 0
                } catch (_: Throwable) {
                    false
                }
            )
        }
        ModuleStatusStore.updateFeatures(statuses)
    }

    /**
     * 通用兜底自检：功能声明自己必须靠 hook 生效，安装流程也没抛异常，
     * 却一个 hook / 事件订阅都没注册——这种情况通常是定位失败后静默 return，
     * 只靠 install 是否抛异常看不出来。
     *
     * 两个不判定的场景：功能显式声明不依赖 hook；DexKit 队列里还有它没跑完的安装项
     * （BRIDGE/WARMUP 未就绪），此时"还没装"不能当成"装不上"。
     */
    private fun describeBindingGap(feature: Feature): String {
        return try {
            if (!feature.requiresRuntimeBinding()) return ""
            if (feature.hasRuntimeBinding()) return ""
            if (DexInstallScheduler.hasPendingBinding(safeFeatureId(feature))) return ""
            BINDING_GAP
        } catch (_: Throwable) {
            ""
        }
    }

    /** 记录定位就位率：排查页用它回答"这个版本到底有多少 API 没找到"。 */
    private fun publishLocateStatus(context: FeatureContext) {
        try {
            val stats = context.dexFinder().locateStatus()
            if (stats == null || stats.size < 2) return
            val missing = context.dexFinder().missingLocateFields() ?: emptyList()
            val optionalMissing = context.dexFinder().optionalMissingLocateFields() ?: emptyList()
            ModuleStatusStore.updateLocate(
                ModuleStatusStore.LocateStatus(
                    located = stats[0],
                    total = stats[1],
                    missing = missing,
                    optionalMissing = optionalMissing
                )
            )
        } catch (e: Throwable) {
            HLog.e("$TAG 读取定位状态失败: $e", e)
        }
    }

    private fun describe(error: Throwable): String {
        val message = error.message.orEmpty().trim()
        return if (message.isEmpty()) error.javaClass.simpleName else message
    }

    fun notifyConfigChanged(context: FeatureContext, featureId: String?, key: String?) {
        for (feature in installedFeatures) {
            if (featureId != null && featureId != feature.featureId()) continue
            try {
                HookRegistry.get().withFeatureTag(safeFeatureId(feature), feature.autoMarkOnHook()) {
                    feature.onConfigChanged(context, key)
                }
            } catch (e: Throwable) {
                HLog.e("$TAG onConfigChanged 异常: ${safeName(feature)}, error=$e", e)
            }
        }
    }

    fun destroyAll(context: FeatureContext) {
        DexInstallScheduler.cancelAll()
        // 先使旧安装租约失效，不等待后台定位，也不允许销毁后重新挂钩。
        HookRegistry.get().unhookAll()
        for (i in installedFeatures.size - 1 downTo 0) {
            val feature = installedFeatures[i]
            try {
                feature.onDestroy(context)
            } catch (e: Throwable) {
                HLog.e("$TAG onDestroy 异常: ${safeName(feature)}, error=$e", e)
            }
        }
        installedFeatures.clear()
        // 首页投影由聊天分组与标签栏共享，只在全量卸载后复位。
        ConversationGroupHomeProjection.resetAfterUnhookAll()
    }

    private fun safeFeatureId(feature: Feature): String {
        return try {
            feature.featureId()
        } catch (_: Throwable) {
            ""
        }
    }

    private fun safeName(feature: Feature): String {
        return try {
            val name = feature.name()
            if (name.isEmpty()) feature.javaClass.name else name
        } catch (_: Throwable) {
            feature.javaClass.name
        }
    }

    companion object {
        private const val TAG = "[Hchat:FeatureManager]"

        /** 排查页按"health 非空"判定不可用，这段文字会原样展示给用户。 */
        const val BINDING_GAP = "未注册任何 hook"
    }
}
