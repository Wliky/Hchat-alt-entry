package h.Hchat.hooks.core

import h.Hchat.event.Events
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
        val initializedFeatures = ArrayList<Feature>()
        val enabledStates = HashMap<Feature, Boolean>()
        val failures = HashMap<Feature, String>()
        for (feature in features) {
            val displayName = safeName(feature)
            try {
                feature.onInit(context)
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
                feature.install(context)
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
        publishStatus(enabledStates, failures)
        DexInstallScheduler.markDexBridgeReady()
    }

    /** 把本次安装结果写入快照，「模块排查」页面直接读取，避免再跑一遍定位。 */
    private fun publishStatus(enabledStates: Map<Feature, Boolean>, failures: Map<Feature, String>) {
        val statuses = features.map { feature ->
            ModuleStatusStore.FeatureStatus(
                featureId = try {
                    feature.featureId()
                } catch (_: Throwable) {
                    ""
                },
                name = safeName(feature),
                enabled = enabledStates[feature] ?: false,
                installed = installedFeatures.contains(feature),
                error = failures[feature].orEmpty()
            )
        }
        ModuleStatusStore.updateFeatures(statuses)
    }

    private fun describe(error: Throwable): String {
        val message = error.message.orEmpty().trim()
        return if (message.isEmpty()) error.javaClass.simpleName else message
    }

    fun notifyConfigChanged(context: FeatureContext, featureId: String?, key: String?) {
        for (feature in installedFeatures) {
            if (featureId != null && featureId != feature.featureId()) continue
            try {
                feature.onConfigChanged(context, key)
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
    }
}
