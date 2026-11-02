package h.Hchat.ui.miuix

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import h.Hchat.hooks.items.custombottombar.CustomBottomBarIconPickResult
import h.Hchat.hooks.items.custombottombar.CustomBottomBarIconPicker
import h.Hchat.hooks.items.custombottombar.CustomBottomBarIconStore
import h.Hchat.hooks.items.custombottombar.CustomBottomBarSettings
import h.Hchat.hooks.items.custombottombar.FloatingBottomBarSettings
import h.Hchat.preferences.HchatStorage
import h.Hchat.ui.FeatureSettingsProvider
import java.util.concurrent.atomic.AtomicBoolean
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

private val BOTTOM_BAR_TAB_LABELS = listOf("微信", "通讯", "发现", "我的")

@Composable
internal fun FloatingBottomBarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FloatingBottomBarSettings.PREFS_NAME) }
    val customSp = remember { HchatStorage.preferences(context, CustomBottomBarSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(FloatingBottomBarSettings.KEY_ENABLE, FloatingBottomBarSettings.DEFAULT_ENABLE) &&
                sp.getString(FloatingBottomBarSettings.KEY_STYLE, null) ==
                FloatingBottomBarSettings.LEGACY_STYLE_FLOATING
        )
    }
    var glassEnabled by remember {
        mutableStateOf(sp.getBoolean(FloatingBottomBarSettings.KEY_GLASS, FloatingBottomBarSettings.DEFAULT_GLASS))
    }
    var blurRadius by remember {
        mutableStateOf(
            FloatingBottomBarSettings.normalizeBlurRadius(
                sp.getInt(FloatingBottomBarSettings.KEY_BLUR_RADIUS, FloatingBottomBarSettings.DEFAULT_BLUR_RADIUS)
            )
        )
    }
    var hideLabels by remember {
        mutableStateOf(sp.getBoolean(FloatingBottomBarSettings.KEY_HIDE_LABELS, FloatingBottomBarSettings.DEFAULT_HIDE_LABELS))
    }
    var showBadges by remember {
        mutableStateOf(
            sp.getBoolean(
                FloatingBottomBarSettings.KEY_SHOW_BADGES,
                FloatingBottomBarSettings.DEFAULT_SHOW_BADGES
            )
        )
    }
    var vibrationEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                FloatingBottomBarSettings.KEY_VIBRATION_ENABLED,
                FloatingBottomBarSettings.DEFAULT_VIBRATION_ENABLED
            )
        )
    }
    var vibrationStrength by remember {
        mutableStateOf(
            FloatingBottomBarSettings.normalizeVibrationStrength(
                sp.getInt(
                    FloatingBottomBarSettings.KEY_VIBRATION_STRENGTH,
                    FloatingBottomBarSettings.DEFAULT_VIBRATION_STRENGTH
                )
            )
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(text = "悬浮样式") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用悬浮底栏",
                        summary = "将微信首页底栏悬浮显示在页面底部",
                        onCheckedChange = {
                            enabled = it
                            if (it) {
                                customSp.edit().putBoolean(CustomBottomBarSettings.KEY_ENABLE, false).commit()
                            }
                            sp.edit()
                                .putBoolean(FloatingBottomBarSettings.KEY_ENABLE, it)
                                .putString(
                                    FloatingBottomBarSettings.KEY_STYLE,
                                    FloatingBottomBarSettings.LEGACY_STYLE_FLOATING
                                )
                                .apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        SwitchRow(
                            checked = glassEnabled,
                            title = "液态玻璃",
                            summary = "启用玻璃模糊效果",
                            onCheckedChange = {
                                glassEnabled = it
                                sp.edit().putBoolean(FloatingBottomBarSettings.KEY_GLASS, it).apply()
                            }
                        )
                        if (glassEnabled) {
                            InsetDivider()
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = "模糊半径 ${blurRadius}dp",
                                    color = MiuixTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                                Slider(
                                    value = blurRadius.toFloat(),
                                    onValueChange = {
                                        blurRadius = FloatingBottomBarSettings.normalizeBlurRadius(it.toInt())
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                    valueRange = FloatingBottomBarSettings.MIN_BLUR_RADIUS.toFloat()..
                                        FloatingBottomBarSettings.MAX_BLUR_RADIUS.toFloat(),
                                    steps = FloatingBottomBarSettings.MAX_BLUR_RADIUS -
                                        FloatingBottomBarSettings.MIN_BLUR_RADIUS - 1,
                                    onValueChangeFinished = {
                                        sp.edit().putInt(FloatingBottomBarSettings.KEY_BLUR_RADIUS, blurRadius).apply()
                                    },
                                    showKeyPoints = true,
                                    keyPoints = listOf(0f, 8f, 16f, 24f, 32f, 40f)
                                )
                            }
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = hideLabels,
                            title = "隐藏标签文字",
                            summary = "只显示微信、通讯、发现和我的图标",
                            onCheckedChange = {
                                hideLabels = it
                                sp.edit().putBoolean(FloatingBottomBarSettings.KEY_HIDE_LABELS, it).apply()
                            }
                        )
                        InsetDivider()
                        SwitchRow(
                            checked = showBadges,
                            title = "显示角标",
                            summary = "显示微信、通讯录、发现和我的未读数字或红点",
                            onCheckedChange = {
                                showBadges = it
                                sp.edit().putBoolean(FloatingBottomBarSettings.KEY_SHOW_BADGES, it).apply()
                            }
                        )
                        InsetDivider()
                        SwitchRow(
                            checked = vibrationEnabled,
                            title = "振动反馈",
                            summary = "点击或切换底栏标签时振动",
                            onCheckedChange = {
                                vibrationEnabled = it
                                sp.edit()
                                    .putBoolean(FloatingBottomBarSettings.KEY_VIBRATION_ENABLED, it)
                                    .apply()
                            }
                        )
                        if (vibrationEnabled) {
                            InsetDivider()
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = "振动强度 ${vibrationStrength}%",
                                    color = MiuixTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                                Slider(
                                    value = vibrationStrength.toFloat(),
                                    onValueChange = {
                                        vibrationStrength = FloatingBottomBarSettings
                                            .normalizeVibrationStrength(it.toInt())
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                    valueRange = FloatingBottomBarSettings.MIN_VIBRATION_STRENGTH.toFloat()..
                                        FloatingBottomBarSettings.MAX_VIBRATION_STRENGTH.toFloat(),
                                    steps = FloatingBottomBarSettings.MAX_VIBRATION_STRENGTH -
                                        FloatingBottomBarSettings.MIN_VIBRATION_STRENGTH - 1,
                                    onValueChangeFinished = {
                                        sp.edit()
                                            .putInt(
                                                FloatingBottomBarSettings.KEY_VIBRATION_STRENGTH,
                                                vibrationStrength
                                            )
                                            .apply()
                                    },
                                    showKeyPoints = true,
                                    keyPoints = listOf(1f, 25f, 50f, 75f, 100f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
