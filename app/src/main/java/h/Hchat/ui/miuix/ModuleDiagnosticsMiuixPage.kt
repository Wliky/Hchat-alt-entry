package h.Hchat.ui.miuix

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import h.Hchat.utils.HLogSettings
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

/** 卡片内一次最多铺多少行，超过就换一张卡，避免单个 item 过高影响滑动。 */
private const val DIAGNOSTICS_CHUNK = 14

private enum class DiagFilter(val label: String) {
    ALL("全部"),
    PROBLEM("异常"),
    PENDING("未完成"),
    DISABLED("已关闭")
}

private data class DiagRow(
    val id: String,
    val title: String,
    val status: String,
    val detail: String,
    val tint: Color
)

@Composable
internal fun ModuleDiagnosticsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var refreshTick by remember { mutableStateOf(0) }
    var filter by remember { mutableStateOf(DiagFilter.ALL) }
    val features = remember(refreshTick) { ModuleStatusStore.features() }
    val installs = remember(refreshTick) { ModuleStatusStore.installs() }
    val versionName = remember(refreshTick) {
        runCatching { WeChatApis.version()?.versionName() }.getOrNull().orEmpty()
    }
    val problems = features.filter { it.error.isNotEmpty() || (it.enabled && !it.installed) }
    val disabled = features.filter { !it.enabled }
    val healthy = (features.size - problems.size - disabled.size).coerceAtLeast(0)
    val stalledInstalls = installs.filter { !it.installed }

    val rows = remember(refreshTick, filter) {
        val featureRows = features.map { item ->
            DiagRow(
                id = item.featureId,
                title = item.name,
                status = when {
                    item.error.isNotEmpty() -> "异常"
                    !item.enabled -> "已关闭"
                    item.installed -> "正常"
                    else -> "未安装"
                },
                detail = item.error,
                tint = when {
                    item.error.isNotEmpty() -> DiagnosticsDanger
                    !item.enabled -> DiagnosticsWarn
                    item.installed -> DiagnosticsOk
                    else -> DiagnosticsWarn
                }
            )
        }
        val queueRows = stalledInstalls.map { item ->
            DiagRow(
                id = "queue:${item.key}",
                title = item.label,
                status = "待安装",
                detail = item.error.ifBlank { "已重试 ${item.retryIndex} 次" },
                tint = DiagnosticsWarn
            )
        }
        when (filter) {
            DiagFilter.ALL -> featureRows + queueRows
            DiagFilter.PROBLEM -> featureRows.filter { it.status == "异常" }
            DiagFilter.PENDING -> featureRows.filter { it.status == "未安装" } + queueRows
            DiagFilter.DISABLED -> featureRows.filter { it.status == "已关闭" }
        }
    }

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
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "概览") }
            item {
                SettingsCard {
                    if (features.isEmpty()) {
                        EmptyText("尚未采集到功能状态，请重启微信后再进入")
                    } else {
                        SummaryRow("微信版本", versionName.ifBlank { "未知" })
                        InsetDivider()
                        SummaryRow("正常 / 异常 / 关闭", "$healthy · ${problems.size} · ${disabled.size}")
                        InsetDivider()
                        SummaryRow(
                            "安装队列",
                            if (stalledInstalls.isEmpty()) "全部完成 (${installs.size})"
                            else "未完成 ${stalledInstalls.size} / ${installs.size}"
                        )
                        InsetDivider()
                        SummaryRow("快照时间", formatDiagnosticsTime(ModuleStatusStore.updatedAtMillis()))
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "日志") }
            item {
                SettingsCard {
                    var logLevel by remember { mutableStateOf(HLogSettings.level(context)) }
                    PopupOptionRow(
                        title = "日志级别",
                        summary = "当前 " + HLogSettings.labelOf(logLevel),
                        options = HLogSettings.choices.map { OptionItem(it.second, it.first) },
                        currentValue = logLevel,
                        onValueChanged = { level ->
                            logLevel = level
                            HLogSettings.setLevel(context, level)
                        }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DiagFilter.values().forEach { item ->
                        FilterPill(
                            text = when (item) {
                                DiagFilter.ALL -> "${item.label} ${features.size + stalledInstalls.size}"
                                DiagFilter.PROBLEM -> "${item.label} ${problems.size}"
                                DiagFilter.PENDING -> "${item.label} ${stalledInstalls.size}"
                                DiagFilter.DISABLED -> "${item.label} ${disabled.size}"
                            },
                            selected = filter == item,
                            onClick = { filter = item }
                        )
                    }
                }
            }

            if (rows.isEmpty()) {
                item {
                    SettingsCard {
                        EmptyText(
                            if (features.isEmpty()) "暂无数据" else when (filter) {
                                DiagFilter.ALL -> "暂无条目"
                                DiagFilter.PROBLEM -> "未发现异常功能"
                                DiagFilter.PENDING -> "安装队列已全部完成"
                                DiagFilter.DISABLED -> "没有已关闭的功能"
                            }
                        )
                    }
                }
            } else {
                items(rows.chunked(DIAGNOSTICS_CHUNK)) { chunk ->
                    SettingsCard {
                        chunk.forEachIndexed { index, row ->
                            StatusRow(
                                title = row.title,
                                status = row.status,
                                detail = row.detail,
                                tint = row.tint
                            )
                            if (index < chunk.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPill(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) {
        MiuixTheme.colorScheme.onSurface.copy(alpha = 0.10f)
    } else {
        Color.Transparent
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            color = if (selected) {
                MiuixTheme.colorScheme.onSurface
            } else {
                MiuixTheme.colorScheme.onSurfaceVariantSummary
            },
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = label, color = MiuixTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Text(text = value, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 13.sp)
    }
}

/** 正常项只占一行，只有异常/待安装才展开第二行原因，避免整页被文字挤满。 */
@Composable
private fun StatusRow(title: String, status: String, detail: String, tint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(tint))
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
            Text(
                text = title,
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (detail.isNotBlank()) {
                Text(
                    text = detail,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(text = status, color = tint, fontSize = 12.sp, maxLines = 1)
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
