package h.Hchat.ui.miuix

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.ui.FeatureSettingsProvider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

private data class CompatibilityEntry(
    val version: String,
    val status: String,
    val detail: String,
    val statusColor: Color
)

private val CompatibilityHistory = Color(0xFF3FA45B)
private val CompatibilityPartial = Color(0xFFE8A33D)

private val CompatibilityEntries = listOf(
    CompatibilityEntry(
        version = "8.0.80",
        status = "静态适配",
        detail = "versionCode 3220 · 0x28005032 · arm64。已完成文本/XML、本地消息与撤回、接龙、朋友圈视频、静默视频、原生已读等入口适配；旧红包构造器和旧 CDN getter 作为可选分支处理。静态核验通过，仍需真机复测。",
        statusColor = CompatibilityPartial
    ),
    CompatibilityEntry(
        version = "8.0.78",
        status = "部分逆向记录",
        detail = "已有语音、红包等 API 的对应版本记录，未做全量功能复验。",
        statusColor = CompatibilityPartial
    ),
    CompatibilityEntry(
        version = "8.0.77",
        status = "部分逆向记录",
        detail = "已有语音、红包等 API 的对应版本记录，未做全量功能复验。",
        statusColor = CompatibilityPartial
    ),
    CompatibilityEntry(
        version = "8.0.76",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.74",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.72",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.68",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.66",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.58",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    ),
    CompatibilityEntry(
        version = "8.0.49",
        status = "历史适配记录",
        detail = "公开微信 API 文档包含该版本的横向逆向证据，本轮未重新复验。",
        statusColor = CompatibilityHistory
    )
)

@Composable
internal fun CompatibilityMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val currentVersion = remember {
        runCatching { WeChatApis.version()?.versionName() }.getOrNull().orEmpty()
    }

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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "当前环境") }
            item {
                SettingsCard {
                    Text(
                        text = if (currentVersion.isBlank()) "微信版本未知" else "微信 $currentVersion",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    InsetDivider()
                    Text(
                        text = "运行时的定位缺失、功能安装与实际触发状态，请以“模块诊断”为准。",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 13.sp
                    )
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "版本记录") }
            items(CompatibilityEntries) { entry ->
                SettingsCard(modifier = Modifier.padding(bottom = 8.dp)) {
                    CompatibilityRow(entry, currentVersion == entry.version)
                }
            }

            item {
                Text(
                    text = "说明：历史记录仅表示对应版本已有部分 API 逆向证据，不代表当前构建的全部功能均已复验。未列出的版本尚未验证。",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun CompatibilityRow(entry: CompatibilityEntry, isCurrent: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entry.version + if (isCurrent) "（当前）" else "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = entry.status,
                color = entry.statusColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = entry.detail,
            modifier = Modifier.padding(top = 6.dp),
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = 12.sp
        )
    }
}
