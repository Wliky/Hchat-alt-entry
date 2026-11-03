package h.Hchat.ui.miuix

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Handler
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.os.SystemClock
import android.provider.OpenableColumns
import android.provider.MediaStore
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TimePicker
import android.widget.Toast
import android.widget.VideoView
import h.Hchat.BuildConfig
import h.Hchat.R
import h.Hchat.hooks.api.ui.HchatAgentIconDrawable
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutFeature
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyph
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyphDrawable
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutGlyphs
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconPickResult
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconPicker
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutIconStore
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutItem
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutRuntime
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutSettings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import h.Hchat.hooks.api.core.WeChatApis
import h.Hchat.hooks.api.media.WeChatFavoriteItem
import h.Hchat.hooks.api.message.WeChatMessageObserveApi
import h.Hchat.hooks.api.model.ContactLabelBean
import h.Hchat.hooks.api.model.WeChatContact
import h.Hchat.hooks.items.audiotransform.AudioTransformFeature
import h.Hchat.hooks.items.audiotransform.AudioTransformCancelledException
import h.Hchat.hooks.items.audiotransform.AudioTransformSegmenter
import h.Hchat.hooks.items.audiotransform.AudioTransformSettings
import h.Hchat.hooks.items.antirecall.AntiRecallFeature
import h.Hchat.hooks.items.antirecall.AntiRecallSettings
import h.Hchat.hooks.items.autoreply.AutoReplyFeature
import h.Hchat.hooks.items.autoreply.AutoReplyRule
import h.Hchat.hooks.items.autoreply.AutoReplyRuntime
import h.Hchat.hooks.items.autoreply.AutoReplySettings
import h.Hchat.hooks.items.autoreply.AutoReplyStep
import h.Hchat.hooks.items.autoreply.XiaozhiMcpStatus
import h.Hchat.hooks.items.autoreply.AutoReplyXiaozhiConfig
import h.Hchat.hooks.items.autoreply.AutoReplyZhiliaConfig
import h.Hchat.hooks.items.autoreply.XiaozhiAgentOption
import h.Hchat.hooks.items.autoreply.XiaozhiConsoleApi
import h.Hchat.hooks.items.autoreply.XiaozhiModelOption
import h.Hchat.hooks.items.autoreply.XiaozhiVoiceOption
import h.Hchat.hooks.items.autooriginal.AutoOriginalImageFeature
import h.Hchat.hooks.items.autooriginal.AutoOriginalImageSettings
import h.Hchat.hooks.items.autovieworiginal.AutoViewOriginalFeature
import h.Hchat.hooks.items.autovieworiginal.AutoViewOriginalSettings
import h.Hchat.hooks.items.atallnotify.AtAllNotificationBlockFeature
import h.Hchat.hooks.items.atallnotify.AtAllNotificationBlockSettings
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitFeature
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitSettings
import h.Hchat.hooks.items.conversationgroup.ConversationGroup
import h.Hchat.hooks.items.conversationgroup.ConversationGroupFeature
import h.Hchat.hooks.items.conversationgroup.ConversationGroupPickerSupport
import h.Hchat.hooks.items.conversationgroup.ConversationGroupRuntime
import h.Hchat.hooks.items.conversationgroup.ConversationGroupStore
import h.Hchat.hooks.items.custombottombar.CustomBottomBarFeature
import h.Hchat.hooks.items.custombottombar.FloatingBottomBarSettings
import h.Hchat.hooks.items.customnotify.CustomNotificationFeature
import h.Hchat.hooks.items.customnotify.CustomNotificationRule
import h.Hchat.hooks.items.customnotify.CustomNotificationRuntime
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarFeature
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarPicker
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarSettings
import h.Hchat.hooks.items.customfriendavatar.CustomFriendAvatarStore
import h.Hchat.hooks.items.customnotify.CustomNotificationSettings
import h.Hchat.hooks.items.editmsg.EditMessageFeature
import h.Hchat.hooks.items.editmsg.EditMessageSettings
import h.Hchat.hooks.items.emojisave.EmojiSaveFeature
import h.Hchat.hooks.items.emojisave.EmojiSaveSettings
import h.Hchat.hooks.items.fakelocation.FakeLocationFeature
import h.Hchat.hooks.items.fakelocation.FakeLocationSettings
import h.Hchat.hooks.items.fakescancamera.FakeScanCameraFeature
import h.Hchat.hooks.items.fakescancamera.FakeScanCameraSettings
import h.Hchat.hooks.items.fakevoiceduration.FakeVoiceDurationFeature
import h.Hchat.hooks.items.fakevoiceduration.FakeVoiceDurationSettings
import h.Hchat.hooks.items.forwardlimit.RemoveForwardLimitFeature
import h.Hchat.hooks.items.forwardlimit.RemoveForwardLimitSettings
import h.Hchat.hooks.items.gameemoji.GameEmojiFeature
import h.Hchat.hooks.items.gameemoji.GameEmojiSettings
import h.Hchat.hooks.items.groupleave.GroupLeaveMonitorFeature
import h.Hchat.hooks.items.groupleave.GroupLeaveReplyTemplate
import h.Hchat.hooks.items.groupleave.GroupLeaveReplyTemplateBinding
import h.Hchat.hooks.items.groupleave.GroupLeaveMonitorSettings
import h.Hchat.hooks.items.grouplabel.GroupChatLabel
import h.Hchat.hooks.items.grouplabel.GroupChatLabelFeature
import h.Hchat.hooks.items.grouplabel.GroupChatLabelStore
import h.Hchat.hooks.items.groupnicknamecolor.GroupNicknameColorSettings
import h.Hchat.hooks.items.groupnicknamecolor.GroupNicknameColorStore
import h.Hchat.hooks.items.grouprename.GroupRenameMonitorFeature
import h.Hchat.hooks.items.grouprename.GroupRenameReplyTemplate
import h.Hchat.hooks.items.grouprename.GroupRenameMonitorSettings
import h.Hchat.hooks.items.grouprename.GroupRenameTemplateBinding
import h.Hchat.hooks.items.hideavatar.HideChatAvatarFeature
import h.Hchat.hooks.items.hideavatar.HideChatAvatarSettings
import h.Hchat.hooks.items.hidemenu.HideChatMenuFeature
import h.Hchat.hooks.items.hidemenu.HideChatMenuSettings
import h.Hchat.hooks.items.hotupdate.DisableHotUpdateFeature
import h.Hchat.hooks.items.hotupdate.DisableHotUpdateSettings
import h.Hchat.hooks.items.inputhint.InputHintFeature
import h.Hchat.hooks.items.inputhint.InputHintSettings
import h.Hchat.hooks.items.inputhint.InputHintStats
import h.Hchat.hooks.items.inputhint.OutgoingMessageStatsRepository
import h.Hchat.hooks.items.zombiecheck.ZombieCheckController
import h.Hchat.hooks.items.zombiecheck.ZombieCheckActionResult
import h.Hchat.hooks.items.zombiecheck.ZombieCheckFeature
import h.Hchat.hooks.items.zombiecheck.ZombieCheckResultType
import h.Hchat.hooks.items.zombiecheck.ZombieCheckSettings
import h.Hchat.hooks.items.membertitle.MemberTitleFeature
import h.Hchat.hooks.items.membertitle.MemberTitleSettings
import h.Hchat.hooks.items.membertitle.MemberTitleStore
import h.Hchat.hooks.items.messageaffix.MessageAffixFeature
import h.Hchat.hooks.items.messageaffix.MessageAffixSettings
import h.Hchat.hooks.items.messageblock.MessageBlockBinding
import h.Hchat.hooks.items.messageblock.MessageBlockDefaultRule
import h.Hchat.hooks.items.messageblock.MessageBlockFeature
import h.Hchat.hooks.items.messageblock.MessageBlockSettings
import h.Hchat.hooks.items.messageblock.MessageBlockTemplate
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsFeature
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsSettings
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsFeature
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsSettings
import h.Hchat.hooks.items.moments.OriginalMomentsUploadFeature
import h.Hchat.hooks.items.moments.OriginalMomentsUploadSettings
import h.Hchat.hooks.items.moments.MomentsBottomDetailFeature
import h.Hchat.hooks.items.moments.MomentsBottomDetailSettings
import h.Hchat.hooks.items.moments.MomentsPostNotificationFeature
import h.Hchat.hooks.items.moments.MomentsPostNotificationSettings
import h.Hchat.hooks.items.moments.MomentsUploadTailFeature
import h.Hchat.hooks.items.moments.MomentsUploadTailPresets
import h.Hchat.hooks.items.moments.MomentsUploadTailSettings
import h.Hchat.hooks.items.moments.RemoveMomentsAdsFeature
import h.Hchat.hooks.items.moments.RemoveMomentsAdsSettings
import h.Hchat.hooks.items.moments.SnsAntiRecallFeature
import h.Hchat.hooks.items.moments.SnsAntiRecallSettings
import h.Hchat.hooks.items.momentsfake.MomentsFakeCommentSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeForwardRuntimeRegistry
import h.Hchat.hooks.items.momentsfake.MomentsFakeForwardSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeLikeSettingsProvider
import h.Hchat.hooks.items.momentsfake.MomentsFakeLikeCandidateRepository
import h.Hchat.hooks.items.momentsfake.MomentsFakeInteractionRuntimeRegistry
import h.Hchat.hooks.items.momentsfake.MomentsFakeInteractionSettings
import h.Hchat.hooks.items.payment.core.AutoRedPacketFeature
import h.Hchat.hooks.items.payment.core.PaymentTemplateTimeFormatter
import h.Hchat.hooks.items.payment.core.RedPacketReplyStep
import h.Hchat.hooks.items.payment.core.RedPacketRuleBinding
import h.Hchat.hooks.items.payment.core.RedPacketRuleConfig
import h.Hchat.hooks.items.payment.core.RedPacketRuleTemplate
import h.Hchat.hooks.items.payment.core.RedPacketSettings
import h.Hchat.hooks.items.payment.fakebalance.FakeWalletBalanceFeature
import h.Hchat.hooks.items.payment.fakebalance.FakeWalletBalanceSettings
import h.Hchat.hooks.items.payment.gift.AutoGiftFeature
import h.Hchat.hooks.items.payment.gift.GiftSettings
import h.Hchat.hooks.items.payment.transfer.AutoTransferFeature
import h.Hchat.hooks.items.payment.transfer.AutoTransferSettings
import h.Hchat.hooks.items.payment.transfer.TransferReceiveAccountStore
import h.Hchat.hooks.items.payment.transfer.TransferRuleBinding
import h.Hchat.hooks.items.payment.transfer.TransferRuleConfig
import h.Hchat.hooks.items.payment.transfer.TransferRuleTemplate
import h.Hchat.hooks.items.patblock.PatBlockFeature
import h.Hchat.hooks.items.patblock.PatBlockSettings
import h.Hchat.hooks.items.profileid.ProfileIdFeature
import h.Hchat.hooks.items.profileid.ProfileIdSettings
import h.Hchat.hooks.items.quoteclear.QuoteDeleteClearFeature
import h.Hchat.hooks.items.quoteclear.QuoteDeleteClearSettings
import h.Hchat.hooks.items.quickcontactedit.QuickContactEditFeature
import h.Hchat.hooks.items.quickcontactedit.QuickContactEditSettings
import h.Hchat.hooks.items.quickmoments.QuickMomentsFeature
import h.Hchat.hooks.items.quickmoments.QuickMomentsSettings
import h.Hchat.hooks.items.quickread.QuickMarkReadFeature
import h.Hchat.hooks.items.quickread.QuickMarkReadSettings
import h.Hchat.hooks.items.quickterminate.QuickTerminateFeature
import h.Hchat.hooks.items.quickterminate.QuickTerminateSettings
import h.Hchat.hooks.items.realtail.RealNameTailFeature
import h.Hchat.hooks.items.realtail.RealNameTailSettings
import h.Hchat.hooks.items.realtail.RealNameTailStore
import h.Hchat.hooks.items.roundavatar.RoundAvatarFeature
import h.Hchat.hooks.items.roundavatar.RoundAvatarSettings
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskContentItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskFeature
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskRuntimeCoordinator
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskSettings
import h.Hchat.hooks.items.selectedmessages.SelectedMessageSnapshot
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesFeature
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesRuntimeCoordinator
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesSettings
import h.Hchat.hooks.items.settings.SettingsEntrySettings
import h.Hchat.hooks.items.settings.SettingsFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadSettings
import h.Hchat.hooks.items.swipequote.SwipeQuoteFeature
import h.Hchat.hooks.items.swipequote.SwipeQuoteSettings
import h.Hchat.hooks.items.tablet.WeChatTabletFeature
import h.Hchat.hooks.items.tablet.WeChatTabletSettings
import h.Hchat.hooks.items.textspeech.TextSpeechFeature
import h.Hchat.hooks.items.textspeech.TextSpeechEngineCatalog
import h.Hchat.hooks.items.textspeech.TextSpeechSettings
import h.Hchat.hooks.items.textspeech.TextSpeechVoiceCatalog
import h.Hchat.hooks.items.textvoice.TextVoiceFeature
import h.Hchat.hooks.items.textvoice.TextVoiceSettings
import h.Hchat.hooks.items.typingreport.TypingReportBlockFeature
import h.Hchat.hooks.items.typingreport.TypingReportBlockSettings
import h.Hchat.hooks.items.voiceforward.VoiceForwardFeature
import h.Hchat.hooks.items.voiceforward.VoiceForwardSettings
import h.Hchat.hooks.items.hchatextra.GroupMemberHistorySettingsProvider
import h.Hchat.hooks.items.hchatextra.MessageDetailsSettingsProvider
import h.Hchat.hooks.items.hchatextra.RedPacketDetailsSettingsProvider
import h.Hchat.hooks.items.hchatextra.SkipWebRiskSettingsProvider
import h.Hchat.hooks.items.hchatextra.HchatExtraSettings
import h.Hchat.hooks.items.voicepreview.VoicePreviewFeature
import h.Hchat.hooks.items.voicepreview.VoicePreviewSettings
import h.Hchat.media.AudioTransformBridge
import h.Hchat.preferences.HchatStorage
import h.Hchat.ui.FeatureSettingsProvider
import h.Hchat.ui.UIRegistry
import h.Hchat.utils.KavaReflector
import h.Hchat.compat.xposed.XC_MethodHook
import h.Hchat.compat.xposed.XposedBridge
import org.json.JSONArray
import org.json.JSONObject
import top.yukonga.miuix.kmp.basic.BreadcrumbBar
import top.yukonga.miuix.kmp.basic.BreadcrumbItem
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PullToRefresh
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.DropdownEntry
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.menu.WindowDropdownMenu
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.nav.core.NavDisplay
import top.yukonga.miuix.kmp.nav.core.NavKey
import top.yukonga.miuix.kmp.nav.core.navBackStackOf
import top.yukonga.miuix.kmp.nav.transition.NavTransitions
import top.yukonga.miuix.kmp.utils.springAnimateToPage
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.blur.Backdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme
import top.yukonga.miuix.kmp.window.WindowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.lang.ref.WeakReference
import java.net.URL
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Calendar
import java.util.LinkedHashSet
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.random.Random

@Composable
internal fun RedPacketTemplatePage(
    context: Context,
    request: RedPacketTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (RedPacketRuleTemplate) -> Unit,
    onDelete: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RedPacketSettings.PREFS_NAME) }
    var name by remember(request) { mutableStateOf(request.template.name) }
    var enabled by remember(request) { mutableStateOf(request.template.enabled) }
    var grabMode by remember(request) { mutableStateOf(request.template.grabMode) }
    var delayMode by remember(request) { mutableStateOf(normalizeRedPacketDelayModeForUi(request.template)) }
    var delayMs by remember(request) { mutableStateOf(request.template.delayMs.coerceAtLeast(0L).toString()) }
    var randomMinMs by remember(request) { mutableStateOf(request.template.randomMinMs.coerceAtLeast(0L).toString()) }
    var randomMaxMs by remember(request) { mutableStateOf(request.template.randomMaxMs.coerceAtLeast(0L).toString()) }
    var skipSelf by remember(request) { mutableStateOf(request.template.skipSelf) }
    var keywordMode by remember(request) { mutableStateOf(request.template.keywordMode) }
    var keywords by remember(request) { mutableStateOf(request.template.keywords) }
    var quietEnabled by remember(request) { mutableStateOf(request.template.quietEnabled) }
    var quietStart by remember(request) { mutableStateOf(formatRedPacketSecond(request.template.quietStartSecond)) }
    var quietEnd by remember(request) { mutableStateOf(formatRedPacketSecond(request.template.quietEndSecond)) }
    val initialPrivateReplySteps = remember(request) {
        normalizeRedPacketReplyStepsForUi(
            request.template.replySteps,
            request.template.replyMode,
            request.template.replyText,
            request.template.replyDelayMs,
            request.template.replyRandom
        )
    }
    var privateReplySteps by remember(request) {
        mutableStateOf(initialPrivateReplySteps)
    }
    var groupReplySteps by remember(request) {
        mutableStateOf(
            request.template.groupReplySteps
                ?.map(::normalizeRedPacketReplyStepForUi)
                ?: initialPrivateReplySteps
        )
    }
    var notifySystemEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifySystemEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SYSTEM_ENABLE, false)
            }
        )
    }
    var notifyToastEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyToastEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_TOAST_ENABLE, false)
            }
        )
    }
    var notifySoundEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifySoundEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SOUND_ENABLE, false)
            }
        )
    }
    var notifySoundMode by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifySoundMode
            } else {
                sp.getInt(RedPacketSettings.KEY_NOTIFY_SOUND_MODE, RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM)
            }
        )
    }
    var notifyVibrateEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyVibrateEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_VIBRATE_ENABLE, false)
            }
        )
    }
    var notifySoundUri by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifySoundUri
            } else {
                sp.getString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, "") ?: ""
            }
        )
    }
    var notifyText by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyText
            } else {
                sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
            }
        )
    }
    var notifyToastText by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyToastText
            } else {
                sp.getString(
                    RedPacketSettings.KEY_NOTIFY_TOAST_TEXT,
                    sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
                ) ?: "抢到红包 {amount} 元"
            }
        )
    }
    var notifyFailedSystemEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyFailedSystemEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_SYSTEM_ENABLE, false)
            }
        )
    }
    var notifyFailedToastEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyFailedToastEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_ENABLE, false)
            }
        )
    }
    var notifyFailedText by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyFailedText
            } else {
                sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "未抢到红包"
            }
        )
    }
    var notifyFailedToastText by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.notifyFailedToastText
            } else {
                sp.getString(
                    RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_TEXT,
                    sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "未抢到红包"
                ) ?: "未抢到红包"
            }
        )
    }
    var announceEnabled by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.announceEnabled
            } else {
                sp.getBoolean(RedPacketSettings.KEY_ANNOUNCE_ENABLE, false)
            }
        )
    }
    var announceText by remember(request) {
        mutableStateOf(
            if (request.template.notificationConfigured) {
                request.template.announceText
            } else {
                sp.getString(RedPacketSettings.KEY_ANNOUNCE_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
            }
        )
    }
    var editingReplyStepsForGroup by remember(request) { mutableStateOf<Boolean?>(null) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    editingReplyStepsForGroup?.let { group ->
        RedPacketReplyStepsPage(
            context = context,
            title = if (group) "群红包回复" else "私聊红包回复",
            initialSteps = if (group) groupReplySteps else privateReplySteps,
            onBack = { editingReplyStepsForGroup = null },
            onSave = { steps ->
                if (group) groupReplySteps = steps else privateReplySteps = steps
                editingReplyStepsForGroup = null
                Toast.makeText(context, "回复步骤已更新", Toast.LENGTH_SHORT).show()
            }
        )
        return
    }

    PageScaffold(
        title = name.ifBlank { "红包模板" },
        largeTitle = name.ifBlank { "红包模板" },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存模板",
                onPrimaryClick = {
                    val customDelay = if (delayMode == RedPacketRuleConfig.DELAY_CUSTOM) {
                        delayMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L
                    } else {
                        0L
                    }
                    val minDelay = randomMinMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L
                    val maxDelay = randomMaxMs.toLongOrNull()?.coerceIn(minDelay, 600000L) ?: minDelay
                    val cleanPrivateReplySteps = cleanRedPacketReplySteps(privateReplySteps)
                    val cleanGroupReplySteps = cleanRedPacketReplySteps(groupReplySteps)
                    val firstReply = cleanPrivateReplySteps.firstOrNull()
                    onSave(
                        RedPacketRuleTemplate(
                            id = request.template.id,
                            name = name.trim().ifBlank { "模板 ${request.index + 1}" },
                            enabled = enabled,
                            grabMode = grabMode,
                            delayMode = delayMode,
                            delayMs = customDelay,
                            randomMinMs = minDelay,
                            randomMaxMs = maxDelay,
                            skipSelf = skipSelf,
                            listMode = request.template.listMode,
                            whitelist = request.template.whitelist,
                            blacklist = request.template.blacklist,
                            keywordMode = keywordMode,
                            keywords = if (keywordMode == 0) "" else keywords.trim(),
                            quietEnabled = quietEnabled,
                            quietStartSecond = parseRedPacketSecond(quietStart, request.template.quietStartSecond),
                            quietEndSecond = parseRedPacketSecond(quietEnd, request.template.quietEndSecond),
                            replyMode = firstReply?.mode ?: RedPacketRuleConfig.REPLY_OFF,
                            replyText = firstReply?.content.orEmpty(),
                            replyDelayMs = firstReply?.delayMs ?: 0L,
                            replyRandom = firstReply?.random ?: false,
                            replySteps = cleanPrivateReplySteps,
                            groupReplySteps = cleanGroupReplySteps,
                            notificationConfigured = true,
                            notifySystemEnabled = notifySystemEnabled,
                            notifyToastEnabled = notifyToastEnabled,
                            notifySoundEnabled = notifySoundEnabled,
                            notifySoundMode = notifySoundMode,
                            notifyVibrateEnabled = notifyVibrateEnabled,
                            notifySoundUri = notifySoundUri.trim(),
                            notifyText = notifyText,
                            notifyToastText = notifyToastText,
                            notifyFailedSystemEnabled = notifyFailedSystemEnabled,
                            notifyFailedToastEnabled = notifyFailedToastEnabled,
                            notifyFailedText = notifyFailedText,
                            notifyFailedToastText = notifyFailedToastText,
                            announceEnabled = announceEnabled,
                            announceText = announceText
                        )
                    )
                    Toast.makeText(context, "模板已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "例如：慢抢红包群、只抢口令红包", name) { name = it }
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用模板",
                        summary = "关闭后使用该模板的聊天不会自动抢",
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "抢包") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "抢包模式",
                        summary = if (grabMode == 1) "静默抢包" else "打开红包页面",
                        options = optionItems("打开红包页面" to 0, "静默抢包" to 1),
                        currentValue = grabMode,
                        onValueChanged = { grabMode = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = skipSelf,
                        title = "跳过自己的红包",
                        summary = "自己发出的红包不会抢",
                        onCheckedChange = { skipSelf = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "延迟") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "抢包延迟",
                        summary = redPacketDelayModeLabel(delayMode),
                        options = redPacketDelayModeOptions(includeRandom = true),
                        currentValue = delayMode,
                        onValueChanged = { delayMode = it }
                    )
                    if (delayMode == RedPacketRuleConfig.DELAY_CUSTOM) {
                        InsetDivider()
                        NumberInputRow("自定义延迟", "单位 ms，0 表示不延迟", delayMs) { delayMs = it }
                    } else if (delayMode == RedPacketRuleConfig.DELAY_RANDOM) {
                        InsetDivider()
                        NumberInputRow("最小延迟", "单位 ms", randomMinMs) { randomMinMs = it }
                        InsetDivider()
                        NumberInputRow("最大延迟", "单位 ms，不能小于最小延迟", randomMaxMs) { randomMaxMs = it }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "过滤") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "关键词过滤",
                        summary = when (keywordMode) {
                            1 -> "只抢含关键词"
                            2 -> "屏蔽含关键词"
                            else -> "不限关键词"
                        },
                        options = optionItems("不限关键词" to 0, "只抢含关键词" to 1, "屏蔽含关键词" to 2),
                        currentValue = keywordMode,
                        onValueChanged = {
                            if (it == 0) keywords = ""
                            keywordMode = it
                        }
                    )
                    if (keywordMode != 0) {
                        InsetDivider()
                        InputRow(
                            title = "关键词",
                            summary = "多个用 |、逗号或换行分隔",
                            value = keywords,
                            minLines = 2,
                            onValueChange = { keywords = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = quietEnabled,
                        title = "禁抢时段",
                        summary = "在指定时间段内不抢红包",
                        onCheckedChange = { quietEnabled = it }
                    )
                    if (quietEnabled) {
                        InsetDivider()
                        TimeOfDayPickerRow("开始时间", quietStart) { quietStart = it }
                        InsetDivider()
                        TimeOfDayPickerRow("结束时间", quietEnd) { quietEnd = it }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "抢后回复") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "私聊红包回复",
                        summary = describeRedPacketReplySteps(privateReplySteps),
                        onClick = { editingReplyStepsForGroup = false }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "群红包回复",
                        summary = describeRedPacketReplySteps(groupReplySteps),
                        onClick = { editingReplyStepsForGroup = true }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "通知提醒") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = notifySystemEnabled,
                        title = "通知栏提醒",
                        summary = "抢到红包时提醒",
                        onCheckedChange = { notifySystemEnabled = it }
                    )
                    if (notifySystemEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "通知栏文案",
                            summary = "支持下方变量",
                            value = notifyText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { notifyText = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = notifyToastEnabled,
                        title = "浮窗提醒",
                        summary = "抢到红包时短暂提示",
                        onCheckedChange = { notifyToastEnabled = it }
                    )
                    if (notifyToastEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "浮窗文案",
                            summary = "支持下方变量",
                            value = notifyToastText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { notifyToastText = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = notifySoundEnabled,
                        title = "通知铃声",
                        summary = "开启后播放通知铃声",
                        onCheckedChange = { notifySoundEnabled = it }
                    )
                    if (notifySoundEnabled) {
                        InsetDivider()
                        PopupOptionRow(
                            title = "铃声模式",
                            summary = if (notifySoundMode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) "从文件选择铃声" else "选择系统铃声",
                            options = optionItems(
                                "选择系统铃声" to RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM,
                                "从文件选择铃声" to RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM
                            ),
                            currentValue = notifySoundMode,
                            onValueChanged = {
                                notifySoundMode = it
                                notifySoundUri = ""
                            }
                        )
                        InsetDivider()
                        ActionRow("选择铃声", ringtoneDisplayName(context, notifySoundUri, notifySoundMode)) {
                            val activity = context as? Activity
                            if (activity == null) {
                                Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                            } else {
                                val onPicked: (String) -> Unit = { uri ->
                                    if (uri.isNotBlank()) {
                                        notifySoundUri = if (notifySoundMode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                            CustomNotificationRuntime.freezeRingtoneUri(context, uri)
                                        } else {
                                            uri
                                        }
                                    }
                                }
                                if (notifySoundMode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                    RingtonePickerBridge.launchFile(activity, onPicked)
                                } else {
                                    RingtonePickerBridge.launchSystem(activity, notifySoundUri, onPicked)
                                }
                            }
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = notifyVibrateEnabled,
                        title = "通知震动",
                        summary = "开启后触发通知震动",
                        onCheckedChange = { notifyVibrateEnabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = announceEnabled,
                        title = "抢到红包播报",
                        summary = "抢到后用系统语音播报",
                        onCheckedChange = { announceEnabled = it }
                    )
                    if (announceEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "播报文案",
                            summary = "支持下方变量",
                            value = announceText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { announceText = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = notifyFailedSystemEnabled,
                        title = "未抢到提醒",
                        summary = "未抢到红包时通知",
                        onCheckedChange = { notifyFailedSystemEnabled = it }
                    )
                    if (notifyFailedSystemEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "未抢到通知栏文案",
                            summary = "支持下方变量",
                            value = notifyFailedText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { notifyFailedText = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = notifyFailedToastEnabled,
                        title = "未抢到浮窗",
                        summary = "未抢到红包时短暂提示",
                        onCheckedChange = { notifyFailedToastEnabled = it }
                    )
                    if (notifyFailedToastEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "未抢到浮窗文案",
                            summary = "支持下方变量",
                            value = notifyFailedToastText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { notifyFailedToastText = it }
                        )
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除模板", "删除后使用该模板的聊天会变成未绑定模板") {
                            onDelete()
                            Toast.makeText(context, "模板已删除", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun RedPacketBindingPage(
    context: Context,
    request: RedPacketBindingEditorRequest,
    templates: List<RedPacketRuleTemplate>,
    onBack: () -> Unit,
    onSave: (RedPacketRuleBinding) -> Unit,
    onDelete: () -> Unit
) {
    var enabled by remember(request) { mutableStateOf(request.binding.enabled) }
    var templateId by remember(request) { mutableStateOf(request.binding.templateId) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val targetTitle = request.binding.label.ifBlank { request.binding.targetId }

    PageScaffold(
        title = targetTitle,
        largeTitle = targetTitle,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存聊天",
                onPrimaryClick = {
                    onSave(
                        request.binding.copy(
                            enabled = enabled,
                            templateId = templateId,
                            customRules = false,
                            overrideRule = null
                        )
                    )
                    Toast.makeText(context, "适用聊天已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天") }
            item {
                SettingsCard {
                    InfoRow("名称", targetTitle)
                    InsetDivider()
                    InfoRow("ID", request.binding.targetId)
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用",
                        summary = "关闭后这个聊天不会按模板自动抢红包",
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    OptionChoiceRow(
                        item = OptionItem("跟随默认规则", -1, "启用后按默认规则或旧版全局设置抢红包"),
                        selected = templateId.isBlank(),
                        onClick = { templateId = "" }
                    )
                    if (templates.isNotEmpty()) InsetDivider()
                    templates.forEachIndexed { index, template ->
                        OptionChoiceRow(
                            item = OptionItem(
                                label = template.name.ifBlank { "模板 ${index + 1}" },
                                value = index,
                                summary = describeRedPacketTemplate(template)
                            ),
                            selected = templateId == template.id,
                            onClick = { templateId = template.id }
                        )
                        if (index < templates.lastIndex) InsetDivider()
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("移除适用聊天", "移除后该聊天回到默认规则") {
                            onDelete()
                            Toast.makeText(context, "适用聊天已移除", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun AntiRecallMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, AntiRecallSettings.PREFS_NAME) }
    var noticeText by remember {
        mutableStateOf(
            normalizeAntiRecallNoticeText(sp.getString(
                AntiRecallSettings.KEY_NOTICE_TEXT,
                AntiRecallSettings.DEFAULT_NOTICE_TEXT
            ) ?: AntiRecallSettings.DEFAULT_NOTICE_TEXT)
        )
    }
    var noticeTimeFormat by remember {
        mutableStateOf(
            sp.getString(
                AntiRecallSettings.KEY_NOTICE_TIME_FORMAT,
                AntiRecallSettings.DEFAULT_NOTICE_TIME_FORMAT
            ) ?: AntiRecallSettings.DEFAULT_NOTICE_TIME_FORMAT
        )
    }
    val usesTimeVariable = noticeText.contains(AntiRecallSettings.VAR_SEND_TIME) ||
        noticeText.contains(AntiRecallSettings.VAR_RECALL_TIME)
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = save@{
                    val normalizedTimeFormat = noticeTimeFormat.trim()
                        .ifBlank { AntiRecallSettings.DEFAULT_NOTICE_TIME_FORMAT }
                    val timeFormatValid = runCatching {
                        SimpleDateFormat(normalizedTimeFormat, Locale.getDefault()).format(Date())
                    }.isSuccess
                    if (usesTimeVariable && !timeFormatValid) {
                        Toast.makeText(context, "时间格式无效", Toast.LENGTH_SHORT).show()
                        return@save
                    }
                    sp.edit()
                        .putString(
                            AntiRecallSettings.KEY_NOTICE_TEXT,
                            noticeText.ifBlank { AntiRecallSettings.DEFAULT_NOTICE_TEXT }
                        )
                        .putString(
                            AntiRecallSettings.KEY_NOTICE_TIME_FORMAT,
                            if (timeFormatValid) {
                                normalizedTimeFormat
                            } else {
                                AntiRecallSettings.DEFAULT_NOTICE_TIME_FORMAT
                            }
                        )
                        .apply()
                    Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 20.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "防撤回") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        AntiRecallSettings.KEY_ENABLE,
                        "防撤回",
                        "保留被撤回的消息，并在下方插入提示",
                        AntiRecallSettings.DEFAULT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        AntiRecallSettings.KEY_KEEP_SELF_RECALL,
                        "保留自己撤回",
                        "开启后自己撤回的消息也会保留",
                        AntiRecallSettings.DEFAULT_KEEP_SELF_RECALL
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        AntiRecallSettings.KEY_SHOW_NOTICE,
                        "显示撤回提示",
                        "在被撤回消息下方插入提示",
                        AntiRecallSettings.DEFAULT_SHOW_NOTICE
                    )
                    InsetDivider()
                    VariableInputRow(
                        title = "提示文案",
                        summary = "点击下方变量插入到光标位置",
                        value = noticeText,
                        variables = antiRecallTemplateVariables,
                        onValueChange = { noticeText = it }
                    )
                    if (usesTimeVariable) {
                        InsetDivider()
                        InputRow(
                            "时间格式",
                            "使用日期格式，例如 yyyy-MM-dd HH:mm:ss",
                            noticeTimeFormat
                        ) { noticeTimeFormat = it }
                    }
                }
            }
        }
    }
}
