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
import androidx.compose.runtime.LaunchedEffect
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
import h.Hchat.hooks.items.diagnostics.DiagnosticsFeature
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

/** 没有 hook 可观测的功能：不算正常，也不算未验证，用中性色区分。 */
private val DiagnosticsIdle = Color(0xFF8A8A8E)

/** 卡片内一次最多铺多少行，超过就换一张卡，避免单个 item 过高影响滑动。 */
private const val DIAGNOSTICS_CHUNK = 14

private enum class DiagFilter(val label: String) {
    ALL("全部"),
    PROBLEM("异常"),
    UNVERIFIED("待验证"),
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
    // 排查页本体就是这个功能的产出：能打开并渲染，才算它可用。
    LaunchedEffect(Unit) { ModuleStatusStore.markTriggered(DiagnosticsFeature.ID) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var refreshTick by remember { mutableStateOf(0) }
    var filter by remember { mutableStateOf(DiagFilter.ALL) }
    val features = remember(refreshTick) { ModuleStatusStore.features() }
    val installs = remember(refreshTick) { ModuleStatusStore.installs() }
    // 子项（id#名称）单独归并，不占父功能的展示名额。
    val hotspots = remember(refreshTick) { ModuleStatusStore.hookHotspots(Int.MAX_VALUE) }
    val hookCalls = remember(refreshTick) { ModuleStatusStore.hookCallTotal() }
    // hook 命中不一定都走 markTriggered（通用回调、早期入口），用累计调用数兜底，
    // 避免"实际用过了却一直显示待验证"。
    val liveHitIds = remember(refreshTick) {
        features.asSequence()
            .filter { it.enabled && it.installed }
            .map { it.featureId }
            .filter { ModuleStatusStore.hookCallsFor(it) > 0L }
            .toSet()
    }
    val featureNames = remember(refreshTick) {
        features.associate { it.featureId to it.name }
    }
    // 父功能取前 12 条，分段计时归到各自父项下面。
    val parentHotspots = remember(hotspots) {
        hotspots.filter { !it.featureId.contains('#') }.take(12)
    }
    val childHotspotsByParent = remember(hotspots) {
        hotspots.filter { it.featureId.contains('#') }
            .groupBy { it.featureId.substringBefore('#') }
    }
    val versionName = remember(refreshTick) {
        runCatching { WeChatApis.version()?.versionName() }.getOrNull().orEmpty()
    }
    val locate = remember(refreshTick) { ModuleStatusStore.locate() }
    // "问题"包含自检失败：定位缺失的功能安装流程照样不报错，但实际跑不起来。
    val problems = features.filter {
        it.error.isNotEmpty() || it.health.isNotEmpty() || (it.enabled && !it.installed)
    }
    val disabled = features.filter { !it.enabled }
    val hit = { item: ModuleStatusStore.FeatureStatus ->
        item.triggered || liveHitIds.contains(item.featureId)
    }
    // 安装成功、自检也过了，但关键 hook 还没真正命中过——只能算"待验证"，不能算正常。
    val unverified = features.filter {
        it.enabled && it.installed && it.error.isEmpty() && it.health.isEmpty() &&
            it.runtimeTracked && !hit(it)
    }
    // 没有 hook 可观测的功能（纯页面/配置项）不能算待验证，单列"无信号"。
    val untracked = features.filter {
        it.enabled && it.installed && it.error.isEmpty() && it.health.isEmpty() &&
            !it.runtimeTracked && !hit(it)
    }
    val healthy = features.filter {
        it.enabled && it.installed && it.error.isEmpty() && it.health.isEmpty() && hit(it)
    }
    val stalledInstalls = installs.filter { !it.installed }

    val rows = remember(refreshTick, filter) {
        val featureRows = features.map { item ->
            DiagRow(
                id = item.featureId,
                title = item.name,
                status = when {
                    item.error.isNotEmpty() -> "异常"
                    !item.enabled -> "已关闭"
                    !item.installed -> "未安装"
                    item.health.isNotEmpty() -> "不可用"
                    !item.runtimeTracked && !hit(item) -> "无信号"
                    !hit(item) -> "待验证"
                    else -> "正常"
                },
                detail = item.error.ifBlank {
                    item.health.ifBlank {
                        when {
                            item.enabled && item.installed && !item.runtimeTracked && !hit(item) ->
                                "没有 hook 可观测，也没有主动上报运行证据"
                            item.enabled && item.installed && !hit(item) ->
                                "已加载，等待实际使用一次"
                            else -> ""
                        }
                    }
                },
                tint = when {
                    item.error.isNotEmpty() -> DiagnosticsDanger
                    !item.enabled -> DiagnosticsWarn
                    !item.installed -> DiagnosticsWarn
                    item.health.isNotEmpty() -> DiagnosticsDanger
                    !item.runtimeTracked && !hit(item) -> DiagnosticsIdle
                    !hit(item) -> DiagnosticsWarn
                    else -> DiagnosticsOk
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
            DiagFilter.PROBLEM -> featureRows.filter { it.status == "异常" || it.status == "不可用" }
            DiagFilter.UNVERIFIED -> featureRows.filter { it.status == "待验证" }
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
                        buildDiagnosticsReport(
                            versionName, features, locate, healthy, unverified, untracked,
                            problems, disabled, stalledInstalls, hotspots
                        )
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
                        SummaryRow(
                            "定位就位",
                            if (locate == null) "未采集"
                            else "${locate.located} / ${locate.total}，缺 ${locate.missing.size} 项"
                        )
                        InsetDivider()
                        SummaryRow(
                            "正常 / 待验证 / 不可用·异常 / 关闭",
                            "${healthy.size} · ${unverified.size} · ${problems.size} · ${disabled.size}"
                        )
                        if (untracked.isNotEmpty()) {
                            InsetDivider()
                            SummaryRow("无运行时信号", "${untracked.size} 项（没有 hook 可观测）")
                        }
                        InsetDivider()
                        SummaryRow(
                            "安装队列",
                            if (stalledInstalls.isEmpty()) "全部完成 (${installs.size})"
                            else "未完成 ${stalledInstalls.size} / ${installs.size}"
                        )
                        InsetDivider()
                        SummaryRow("Hook 回调总数", "$hookCalls 次")
                        InsetDivider()
                        SummaryRow("快照时间", formatDiagnosticsTime(ModuleStatusStore.updatedAtMillis()))
                        InsetDivider()
                        SummaryRow("命中记录", "按微信版本保存，升级微信后重置")
                    }
                }
            }

            locate?.missing?.takeIf { it.isNotEmpty() }?.let { missing ->
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "定位缺失（必需）") }
                item {
                    SettingsCard {
                        EmptyText("以下 ${missing.size} 项 API 在当前微信版本没找到，依赖它们的功能会不可用：\n" +
                            missing.joinToString("、"))
                    }
                }
            }

            locate?.optionalMissing?.takeIf { it.isNotEmpty() }?.let { optionalMissing ->
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "定位缺失（可选）") }
                item {
                    SettingsCard {
                        EmptyText("以下 ${optionalMissing.size} 项没找到，但有回退路径，不影响功能：\n" +
                            optionalMissing.joinToString("、"))
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "Hook 热点") }
            item {
                SettingsCard {
                    if (parentHotspots.isEmpty()) {
                        EmptyText("还没有 hook 被触发过")
                    } else {
                        var first = true
                        parentHotspots.forEach { item ->
                            if (!first) InsetDivider()
                            first = false
                            SummaryRow(
                                featureNames[item.featureId] ?: item.featureId,
                                hotspotValue(item)
                            )
                            // 分段计时挂在父功能下面，显示子项名，不单独占热点名额。
                            childHotspotsByParent[item.featureId]?.forEach { child ->
                                SummaryRow(
                                    "  └ ${child.featureId.substringAfter('#')}",
                                    hotspotValue(child)
                                )
                            }
                        }
                    }
                }
            }
            item {
                SettingsCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                ModuleStatusStore.resetHookStats()
                                refreshTick++
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "重置热点统计",
                            color = MiuixTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "清零后重新观察",
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = 13.sp
                        )
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
                                // 未安装的走"未完成"页，这里只数真正的异常与不可用。
                                DiagFilter.PROBLEM -> "${item.label} " +
                                    problems.count { it.error.isNotEmpty() || it.health.isNotEmpty() }
                                DiagFilter.UNVERIFIED -> "${item.label} ${unverified.size}"
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
                                DiagFilter.UNVERIFIED -> "所有功能都已实际触发过"
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
    // 原因常常超过两行（缺字段清单），点一下展开，排查时才能看全。
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = detail.isNotBlank()) { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 9.dp),
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
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
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
    locate: ModuleStatusStore.LocateStatus?,
    healthy: List<ModuleStatusStore.FeatureStatus>,
    unverified: List<ModuleStatusStore.FeatureStatus>,
    untracked: List<ModuleStatusStore.FeatureStatus>,
    problems: List<ModuleStatusStore.FeatureStatus>,
    disabled: List<ModuleStatusStore.FeatureStatus>,
    pendingInstalls: List<ModuleStatusStore.InstallStatus>,
    hotspots: List<ModuleStatusStore.HookHotspot>
): String {
    val builder = StringBuilder()
    builder.appendLine("Hchat 模块排查报告")
    builder.appendLine("微信版本: ${versionName.ifBlank { "未知" }}")
    builder.appendLine("采集时间: ${formatDiagnosticsTime(ModuleStatusStore.updatedAtMillis())}")
    builder.appendLine(
        "功能点: 共 ${features.size} 项，正常 ${healthy.size} 项，" +
            "待验证 ${unverified.size} 项，无信号 ${untracked.size} 项，" +
            "异常 ${problems.size} 项，已关闭 ${disabled.size} 项"
    )
    builder.appendLine(
        "定位就位: " + if (locate == null) {
            "未采集"
        } else {
            "${locate.located} / ${locate.total}，必需缺失 ${locate.missing.size} 项，" +
                "可选缺失 ${locate.optionalMissing.size} 项"
        }
    )
    builder.appendLine()
    // 对齐微信版本时最先要看的就是这两段：缺了哪些 API，以及是否必需。
    builder.appendLine("[定位缺失（必需）]（依赖它们的功能会不可用）")
    when {
        locate == null -> builder.appendLine("未采集")
        locate.missing.isEmpty() -> builder.appendLine("无")
        else -> builder.appendLine(locate.missing.joinToString("、"))
    }
    builder.appendLine()
    builder.appendLine("[定位缺失（可选）]（有回退路径，不影响功能）")
    when {
        locate == null -> builder.appendLine("未采集")
        locate.optionalMissing.isEmpty() -> builder.appendLine("无")
        else -> builder.appendLine(locate.optionalMissing.joinToString("、"))
    }
    builder.appendLine()
    builder.appendLine("[待验证功能]（已加载，但还没有任何运行证据；命中记录按微信版本保存）")
    if (unverified.isEmpty()) {
        builder.appendLine("无")
    } else {
        unverified.forEach { builder.appendLine("${it.name} (${it.featureId})") }
    }
    builder.appendLine()
    builder.appendLine("[无运行时信号]（没有 hook 可观测，也没有主动上报；不代表不可用）")
    if (untracked.isEmpty()) {
        builder.appendLine("无")
    } else {
        untracked.forEach { builder.appendLine("${it.name} (${it.featureId})") }
    }
    builder.appendLine()
    builder.appendLine("[异常功能]")
    if (problems.isEmpty()) {
        builder.appendLine("无")
    } else {
        problems.forEach {
            // health 非空是"当前版本跑不起来"（多数是定位缺失），不能写成安装未完成。
            val reason = it.error.ifBlank {
                it.health.ifBlank { "已启用但安装未完成" }
            }
            builder.appendLine("${it.name} (${it.featureId}): $reason")
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
        builder.appendLine()
    builder.appendLine("[Hook 热点]（按慢调用与调用次数排序，耗时只含模块自己的回调）")
    val parentHotspots = hotspots.filter { !it.featureId.contains('#') }.take(12)
    if (parentHotspots.isEmpty()) {
        builder.appendLine("暂无（hook 还没被触发过）")
    } else {
        parentHotspots.forEach { item ->
            builder.appendLine(
                buildString {
                    append("${item.featureId}: ${item.calls} 次，均 ${formatHookCost(item.averageNanos)}")
                    append("，峰 ${formatHookCost(item.maxNanos)}，采样 ${item.samples} 次")
                    if (item.slowCalls > 0L) append("，慢 ${item.slowCalls} 次")
                }
            )
            // 分段计时跟在父功能后面，读报告时能直接看出慢在哪个阶段。
            hotspots.asSequence()
                .filter { it.featureId.startsWith("${item.featureId}#") }
                .forEach { child ->
                    builder.appendLine(
                        "  └ ${child.featureId.substringAfter('#')}: ${child.calls} 次，" +
                            "均 ${formatHookCost(child.averageNanos)}"
                    )
                }
        }
    }
    return builder.toString()
}

private fun copyDiagnosticsReport(context: Context, report: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    clipboard.setPrimaryClip(ClipData.newPlainText("Hchat 模块排查", report))
    Toast.makeText(context, "报告已复制", Toast.LENGTH_SHORT).show()
}

private fun hotspotValue(item: ModuleStatusStore.HookHotspot): String = buildString {
    append("${item.calls} 次 · 均 ${formatHookCost(item.averageNanos)}")
    if (item.slowCalls > 0L) append(" · 慢 ${item.slowCalls} 次")
}

/** 采样耗时是"模块自己这段回调跑了多久"，量级通常在纳秒到微秒，不能按毫秒一刀切。 */
private fun formatHookCost(nanos: Long): String = when {
    nanos <= 0L -> "采样中"
    nanos >= 1_000_000L -> String.format(Locale.US, "%.1f ms", nanos / 1_000_000.0)
    nanos >= 1_000L -> "${nanos / 1_000L} µs"
    else -> "$nanos ns"
}

private fun formatDiagnosticsTime(millis: Long): String {
    if (millis <= 0L) return "未知"
    return SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()).format(Date(millis))
}
