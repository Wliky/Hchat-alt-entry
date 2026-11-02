package h.Hchat.ui.miuix

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.core.ModuleStatusStore
import h.Hchat.ui.FeatureSettingsProvider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DiagnosticsDanger = Color(0xFFE5484D)
private val DiagnosticsWarn = Color(0xFFE8A33D)
private val DiagnosticsOk = Color(0xFF3FA45B)

@Composable
internal fun ModuleDiagnosticsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var refreshTick by remember { mutableStateOf(0) }
    val features = remember(refreshTick) { ModuleStatusStore.features() }
    val installs = remember(refreshTick) { ModuleStatusStore.installs() }
    val versionName = remember(refreshTick) {
        runCatching { WeChatApis.version()?.versionName() }.getOrNull().orEmpty()
    }
    val problems = features.filter { it.error.isNotEmpty() || (it.enabled && !it.installed) }
    val disabled = features.filter { !it.enabled }
    val healthy = (features.size - problems.size - disabled.size).coerceAtLeast(0)
    val stalledInstalls = installs.filter { !it.installed }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "返回",
                onPrimaryClick = onBack,
                secondaryText = "复制报告",
                onSecondaryClick = {
                    copyDiagnosticsReport(
                        context,
                        buildDiagnosticsReport(versionName, features, problems, disabled, stalledInstalls)
                    )
                },
                middleText = "刷新",
                onMiddleClick = { refreshTick++ }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 96.dp
            )
        ) {
            item { SmallTitle(text = "概览") }
            item {
                SettingsCard {
                    if (features.isEmpty()) {
                        EmptyText("尚未采集到功能状态，请重启微信后再进入")
                    } else {
                        SummaryRow("微信版本", versionName.ifBlank { "未知" })
                        InsetDivider()
                        SummaryRow("功能点总数", "${features.size} 项")
                        InsetDivider()
                        SummaryRow("正常 / 异常 / 已关闭", "$healthy / ${problems.size} / ${disabled.size}")
                        InsetDivider()
                        SummaryRow("安装队列未完成", "${stalledInstalls.size} 项")
                        InsetDivider()
                        SummaryRow("快照时间", formatDiagnosticsTime(ModuleStatusStore.updatedAtMillis()))
                    }
                }
            }

            item { SmallTitle(text = "异常功能 (${problems.size})") }
            item {
                SettingsCard {
                    if (problems.isEmpty()) {
                        EmptyText("未发现异常功能")
                    } else {
                        problems.forEachIndexed { index, item ->
                            StatusRow(
                                title = item.name,
                                status = "异常",
                                detail = item.error.ifBlank { "已启用但安装未完成" },
                                tint = DiagnosticsDanger
                            )
                            if (index < problems.lastIndex) InsetDivider()
                        }
                    }
                }
            }

            item { SmallTitle(text = "DexKit 安装队列 (${stalledInstalls.size})") }
            item {
                SettingsCard {
                    if (stalledInstalls.isEmpty()) {
                        EmptyText("安装队列已全部完成")
                    } else {
                        stalledInstalls.forEachIndexed { index, item ->
                            StatusRow(
                                title = item.label,
                                status = "待安装",
                                detail = item.error.ifBlank { "重试 ${item.retryIndex} 次" },
                                tint = DiagnosticsWarn
                            )
                            if (index < stalledInstalls.lastIndex) InsetDivider()
                        }
                    }
                }
            }

            item { SmallTitle(text = "全部功能 (${features.size})") }
            if (features.isEmpty()) {
                item { SettingsCard { EmptyText("暂无数据") } }
            } else {
                items(features) { item ->
                    SettingsCard {
                        StatusRow(
                            title = item.name,
                            status = when {
                                item.error.isNotEmpty() -> "异常"
                                !item.enabled -> "已关闭"
                                item.installed -> "正常"
                                else -> "未安装"
                            },
                            detail = item.featureId,
                            tint = when {
                                item.error.isNotEmpty() -> DiagnosticsDanger
                                !item.enabled -> DiagnosticsWarn
                                item.installed -> DiagnosticsOk
                                else -> DiagnosticsWarn
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = label, color = MiuixTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Text(text = value, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 13.sp)
    }
}

@Composable
private fun StatusRow(title: String, status: String, detail: String, tint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(tint))
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
            Text(
                text = title,
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (detail.isNotBlank()) {
                Text(
                    text = detail,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(text = status, color = tint, fontSize = 12.sp)
    }
}

private fun buildDiagnosticsReport(
    versionName: String,
    features: List<ModuleStatusStore.FeatureStatus>,
    problems: List<ModuleStatusStore.FeatureStatus>,
    disabled: List<ModuleStatusStore.FeatureStatus>,
    pendingInstalls: List<ModuleStatusStore.InstallStatus>
): String {
    val builder = StringBuilder()
    builder.appendLine("Hchat 模块排查报告")
    builder.appendLine("微信版本: ${versionName.ifBlank { "未知" }}")
    builder.appendLine("采集时间: ${formatDiagnosticsTime(ModuleStatusStore.updatedAtMillis())}")
    builder.appendLine("功能点: 共 ${features.size} 项，异常 ${problems.size} 项，已关闭 ${disabled.size} 项")
    builder.appendLine()
    builder.appendLine("[异常功能]")
    if (problems.isEmpty()) {
        builder.appendLine("无")
    } else {
        problems.forEach {
            builder.appendLine("${it.name} (${it.featureId}): ${it.error.ifBlank { "已启用但安装未完成" }}")
        }
    }
    builder.appendLine()
    builder.appendLine("[DexKit 未完成]")
    if (pendingInstalls.isEmpty()) {
        builder.appendLine("无")
    } else {
        pendingInstalls.forEach {
            builder.appendLine("${it.label}: ${it.error.ifBlank { "等待安装" }}（重试 ${it.retryIndex} 次）")
        }
    }
    return builder.toString()
}

private fun copyDiagnosticsReport(context: Context, report: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    clipboard.setPrimaryClip(ClipData.newPlainText("Hchat 模块排查", report))
    Toast.makeText(context, "报告已复制", Toast.LENGTH_SHORT).show()
}

private fun formatDiagnosticsTime(millis: Long): String {
    if (millis <= 0L) return "未知"
    return SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()).format(Date(millis))
}
