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
import h.Hchat.hooks.items.callmedialimit.CallRingtoneBlockFeature
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
import h.Hchat.hooks.items.grouplabel.QuickGroupChatLabelFeature
import h.Hchat.hooks.items.grouplabel.QuickGroupChatLabelSettings
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
import h.Hchat.hooks.items.moments.MomentsAutoCommentFeature
import h.Hchat.hooks.items.moments.MomentsAutoCommentSettings
import h.Hchat.hooks.items.moments.MomentsAutoLikeFeature
import h.Hchat.hooks.items.moments.MomentsAutoLikeSettings
import h.Hchat.hooks.items.moments.MomentsAutoForwardFeature
import h.Hchat.hooks.items.moments.MomentsAutoForwardSettings
import h.Hchat.hooks.items.moments.MomentsAutoRefreshFeature
import h.Hchat.hooks.items.moments.MomentsAutoRefreshSettings
import h.Hchat.hooks.items.moments.MomentsBottomDetailFeature
import h.Hchat.hooks.items.moments.MomentsBottomDetailSettings
import h.Hchat.hooks.items.moments.MomentsContactFilterFeature
import h.Hchat.hooks.items.moments.MomentsContactFilterSettings
import h.Hchat.hooks.items.moments.MomentsKeywordBlockFeature
import h.Hchat.hooks.items.moments.MomentsKeywordBlockSettings
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
import h.Hchat.hooks.items.multirecall.MultiRecallFeature
import h.Hchat.hooks.items.multirecall.MultiRecallSettings
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
import h.Hchat.utils.KeywordReplacementRule
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
import top.yukonga.miuix.kmp.preference.SwitchPreference
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

internal object MarkdownUi {

@Composable
fun Content(
    context: Context,
    markdown: String,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    bodyFontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    onCopyCode: ((String) -> Unit)? = null
) {
    val lines = markdown.replace("\r\n", "\n").replace('\r', '\n').lines()
    var inCodeBlock = false
    var inlineState = MarkdownInlineState()
    val codeLines = ArrayList<String>()
    Column(modifier = Modifier.fillMaxWidth().padding(contentPadding)) {
        lines.forEach { rawLine ->
            val line = rawLine.trimEnd()
            if (line.trimStart().startsWith("```")) {
                if (inCodeBlock) {
                    CodeBlock(codeLines.joinToString("\n"), onCopyCode)
                    codeLines.clear()
                    inCodeBlock = false
                } else {
                    inCodeBlock = true
                    codeLines.clear()
                }
                return@forEach
            }
            if (inCodeBlock) {
                codeLines += rawLine
                return@forEach
            }
            val nextState = MarkdownLine(context, line, inlineState, bodyFontSize)
            inlineState = nextState
        }
        if (inCodeBlock && codeLines.isNotEmpty()) {
            CodeBlock(codeLines.joinToString("\n"), onCopyCode)
        }
    }
}

@Composable
private fun MarkdownLine(
    context: Context,
    line: String,
    inlineState: MarkdownInlineState,
    bodyFontSize: androidx.compose.ui.unit.TextUnit = 13.sp
): MarkdownInlineState {
    val trimmed = line.trim()
    return when {
        trimmed.isBlank() -> {
            Box(modifier = Modifier.height(8.dp))
            inlineState
        }
        trimmed.matches(Regex("""-{3,}|_{3,}|\*{3,}""")) -> {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(1.dp)
                    .background(MiuixTheme.colorScheme.outline)
            )
            inlineState
        }
        trimmed.startsWith("#") -> {
            val level = trimmed.takeWhile { it == '#' }.length.coerceIn(1, 6)
            val text = trimmed.drop(level).trim()
            MarkdownTextResult(
                context = context,
                text = text,
                inlineState = inlineState,
                modifier = Modifier.padding(top = if (level <= 2) 10.dp else 8.dp, bottom = 4.dp),
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = when (level) {
                    1 -> 22.sp
                    2 -> 19.sp
                    3 -> 17.sp
                    else -> 15.sp
                },
                fontWeight = FontWeight.SemiBold
            )
        }
        trimmed.startsWith(">") -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier.padding(end = 8.dp)
                        .size(width = 3.dp, height = 20.dp)
                        .background(MiuixTheme.colorScheme.primary)
                )
                MarkdownTextResult(
                    context = context,
                    text = trimmed.removePrefix(">").trim(),
                    inlineState = inlineState,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = bodyFontSize,
                    modifier = Modifier.weight(1f)
                )
            }
            inlineState
        }
        trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("+ ") -> {
            MarkdownBullet(context, trimmed.drop(2).trim(), inlineState, fontSize = bodyFontSize)
        }
        Regex("""^\d+[.)]\s+.*""").matches(trimmed) -> {
            val marker = trimmed.substringBefore(' ').trim()
            MarkdownBullet(context, trimmed.removePrefix(marker).trim(), inlineState, marker, bodyFontSize)
        }
        else -> {
            MarkdownTextResult(
                context = context,
                text = line,
                inlineState = inlineState,
                fontSize = bodyFontSize,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun MarkdownBullet(
    context: Context,
    text: String,
    inlineState: MarkdownInlineState,
    marker: String = "•",
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp
): MarkdownInlineState {
    var nextState = inlineState
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = marker,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontSize = fontSize,
            modifier = Modifier.padding(end = 8.dp)
        )
        nextState = MarkdownTextResult(
            context = context,
            text = text,
            inlineState = inlineState,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
    }
    return nextState
}

@Composable
fun CodeBlock(code: String, onCopy: ((String) -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MiuixTheme.colorScheme.surfaceVariant)
    ) {
        if (onCopy != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "代码",
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    imageVector = NavIcons.Copy,
                    contentDescription = "复制代码",
                    colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurfaceVariantSummary),
                    modifier = Modifier.size(26.dp).clip(RoundedCornerShape(4.dp))
                        .clickable { onCopy(code) }
                        .padding(6.dp)
                )
            }
        }
        Text(
            text = code,
            color = MiuixTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun Text(
    context: Context,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MiuixTheme.colorScheme.onSurface,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    fontWeight: FontWeight? = null
) {
    MarkdownTextResult(context, text, MarkdownInlineState(), modifier, color, fontSize, fontWeight)
}

@Composable
private fun MarkdownTextResult(
    context: Context,
    text: String,
    inlineState: MarkdownInlineState,
    modifier: Modifier = Modifier,
    color: Color = MiuixTheme.colorScheme.onSurface,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    fontWeight: FontWeight? = null
): MarkdownInlineState {
    val primary = MiuixTheme.colorScheme.primary
    val result = remember(text, primary, inlineState.bold) {
        buildMarkdownAnnotatedString(text, primary, inlineState)
    }
    ClickableText(
        text = result.text,
        modifier = modifier,
        style = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight),
        onClick = { offset ->
            result.text.getStringAnnotations(MARKDOWN_LINK_TAG, offset, offset)
                .firstOrNull()
                ?.let { annotation -> openMarkdownLink(context, annotation.item) }
        }
    )
    return result.state
}

private data class MarkdownInlineState(val bold: Boolean = false)

private data class MarkdownInlineResult(
    val text: AnnotatedString,
    val state: MarkdownInlineState
)

private fun buildMarkdownAnnotatedString(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineResult {
    var state = initialState
    val annotated = buildAnnotatedString {
        state = appendInlineMarkdown(text, accent, state)
    }
    return MarkdownInlineResult(annotated, state)
}

private fun AnnotatedString.Builder.appendInlineMarkdown(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineState {
    var state = initialState
    var segmentStart = 0
    MARKDOWN_LINK_REGEX.findAll(text).forEach { match ->
        if (match.range.first > segmentStart) {
            state = appendInlineMarkdownSegment(
                text.substring(segmentStart, match.range.first),
                accent,
                state
            )
        }
        val label = match.groupValues[1]
        val url = match.groupValues[2].trim()
        if (label.isNotBlank() && url.isNotBlank()) {
            pushStringAnnotation(MARKDOWN_LINK_TAG, url)
            appendStyledInline(label, accent, state, link = true)
            pop()
        } else {
            append(match.value)
        }
        segmentStart = match.range.last + 1
    }
    if (segmentStart < text.length) {
        state = appendInlineMarkdownSegment(text.substring(segmentStart), accent, state)
    }
    return state
}

private fun AnnotatedString.Builder.appendInlineMarkdownSegment(
    text: String,
    accent: Color,
    initialState: MarkdownInlineState
): MarkdownInlineState {
    var state = initialState
    var index = 0
    while (index < text.length) {
        when {
            text.startsWith("**", index) -> {
                state = state.copy(bold = !state.bold)
                index += 2
            }
            text[index] == '`' -> {
                val end = text.indexOf('`', index + 1)
                if (end > index) {
                    withStyle(SpanStyle(color = accent, fontFamily = FontFamily.Monospace)) {
                        append(text.substring(index + 1, end))
                    }
                    index = end + 1
                } else {
                    append(text[index])
                    index++
                }
            }
            else -> {
                appendStyledInline(text[index].toString(), accent, state, link = false)
                index++
            }
        }
    }
    return state
}

private fun AnnotatedString.Builder.appendStyledInline(
    value: String,
    accent: Color,
    state: MarkdownInlineState,
    link: Boolean
) {
    val style = SpanStyle(
        color = if (link) accent else Color.Unspecified,
        fontWeight = when {
            link -> FontWeight.Medium
            state.bold -> FontWeight.SemiBold
            else -> null
        }
    )
    withStyle(style) {
        append(value)
    }
}

private fun openMarkdownLink(context: Context, url: String) {
    val value = url.trim()
    if (value.isBlank()) return
    runCatching {
        val normalized = if (value.contains("://")) value else "https://$value"
        val uri = Uri.parse(normalized)
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

}

@Composable
internal fun WeChatTabletMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, WeChatTabletSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "平板模式") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        WeChatTabletSettings.KEY_ENABLE,
                        "平板模式",
                        "开启平板模式，退出微信登陆生效",
                        WeChatTabletSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun AutoTransferMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, AutoTransferSettings.PREFS_NAME) }
    var templates by remember {
        mutableStateOf(TransferRuleConfig.parseTemplates(sp.getString(TransferRuleConfig.KEY_TEMPLATES, "")))
    }
    var bindings by remember {
        mutableStateOf(TransferRuleConfig.parseBindings(sp.getString(TransferRuleConfig.KEY_BINDINGS, "")))
    }
    var defaultTemplateId by remember {
        mutableStateOf(sp.getString(TransferRuleConfig.KEY_DEFAULT_TEMPLATE_ID, "") ?: "")
    }
    var whitelist by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_WHITELIST, "") ?: "") }
    var blacklist by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_BLACKLIST, "") ?: "") }
    var listMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_MODE, 0)) }
    var delayMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_MS, 0L).toString()) }
    var delayMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_DELAY_MODE, TransferRuleConfig.DELAY_CUSTOM)) }
    var randomMinMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MIN, 500L).toString()) }
    var randomMaxMs by remember { mutableStateOf(sp.getLong(AutoTransferSettings.KEY_DELAY_RANDOM_MAX, 3000L).toString()) }
    var amountEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, false)) }
    var amountCondition by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_AMOUNT_COND, 1)) }
    var amountAction by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_AMOUNT_ACTION, 0)) }
    var amountValue by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_AMOUNT_VALUE, "0") ?: "0") }
    var keywords by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_KEYWORDS, "") ?: "") }
    var keywordMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_KEYWORD_MODE, 0)) }
    var quietEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_QUIET_ENABLE, false)) }
    var quietStart by remember { mutableStateOf(formatRedPacketSecond(sp.getInt(AutoTransferSettings.KEY_QUIET_START_SECOND, 0))) }
    var quietEnd by remember { mutableStateOf(formatRedPacketSecond(sp.getInt(AutoTransferSettings.KEY_QUIET_END_SECOND, 0))) }
    var replySteps by remember { mutableStateOf(loadGlobalTransferReplySteps(sp)) }
    var groupReplySteps by remember { mutableStateOf(loadGlobalGroupTransferReplySteps(sp)) }
    var notifySystemEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SYSTEM_ENABLE, false)) }
    var notifyToastEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_TOAST_ENABLE, false)) }
    var notifySoundEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_SOUND_ENABLE, false)) }
    var notifySoundMode by remember { mutableStateOf(sp.getInt(AutoTransferSettings.KEY_NOTIFY_SOUND_MODE, AutoTransferSettings.NOTIFY_SOUND_MODE_SYSTEM)) }
    var notifyVibrateEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_NOTIFY_VIBRATE_ENABLE, false)) }
    var notifySoundUri by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_SOUND_URI, "") ?: "") }
    var notifyText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_TEXT, "已收款 {amount} 元") ?: "") }
    var notifyToastText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_NOTIFY_TOAST_TEXT, "已收款 {amount} 元") ?: "") }
    var announceEnabled by remember { mutableStateOf(sp.getBoolean(AutoTransferSettings.KEY_ANNOUNCE_ENABLE, false)) }
    var announceText by remember { mutableStateOf(sp.getString(AutoTransferSettings.KEY_ANNOUNCE_TEXT, "收到转账 {amount} 元") ?: "") }
    var timeFormat by remember {
        mutableStateOf(
            PaymentTemplateTimeFormatter.normalizePattern(
                sp.getString(AutoTransferSettings.KEY_TIME_FORMAT, AutoTransferSettings.DEFAULT_TIME_FORMAT)
            )
        )
    }
    var receiveAccount by remember {
        mutableStateOf(
            sp.getString(
                AutoTransferSettings.KEY_RECEIVE_ACCOUNT,
                TransferReceiveAccountStore.DEFAULT_KEY
            ) ?: TransferReceiveAccountStore.DEFAULT_KEY
        )
    }
    val receiveAccountOptions = remember { transferReceiveAccountOptions(context) }
    if (receiveAccountOptions.none { it.value == receiveAccount }) {
        receiveAccount = TransferReceiveAccountStore.DEFAULT_KEY
    }
    var picker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var templateEditor by remember { mutableStateOf<TransferTemplateEditorRequest?>(null) }
    var bindingEditor by remember { mutableStateOf<TransferBindingEditorRequest?>(null) }
    var showTemplates by remember { mutableStateOf(false) }
    var showBindings by remember { mutableStateOf(false) }
    var showBatchApply by remember { mutableStateOf(false) }
    var showReplySteps by remember { mutableStateOf(false) }
    var replyTarget by remember { mutableStateOf(TransferReplyTarget.PRIVATE) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun persistRules(
        nextTemplates: List<TransferRuleTemplate> = templates,
        nextBindings: List<TransferRuleBinding> = bindings,
        nextDefault: String = defaultTemplateId
    ) {
        sp.edit()
            .putString(TransferRuleConfig.KEY_TEMPLATES, TransferRuleConfig.encodeTemplates(nextTemplates))
            .putString(TransferRuleConfig.KEY_BINDINGS, TransferRuleConfig.encodeBindings(nextBindings))
            .putString(TransferRuleConfig.KEY_DEFAULT_TEMPLATE_ID, nextDefault)
            .commit()
    }

    val route: AutoTransferRoute = when {
        templateEditor != null -> AutoTransferRoute.TemplateEditor(templateEditor!!)
        bindingEditor != null -> AutoTransferRoute.BindingEditor(bindingEditor!!)
        picker != null -> AutoTransferRoute.ContactPicker(picker!!)
        showReplySteps -> AutoTransferRoute.GlobalReplySteps(replyTarget)
        showTemplates -> AutoTransferRoute.TemplateManager
        showBindings -> AutoTransferRoute.BindingManager
        showBatchApply -> AutoTransferRoute.BatchApply
        else -> AutoTransferRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "AutoTransferRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is AutoTransferRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { picker = null },
                    onConfirm = { selected ->
                        request.onValue(formatIds(selected.map { it.id }))
                        picker = null
                    }
                )
            }
            is AutoTransferRoute.TemplateEditor -> TransferTemplateEditorPage(
                context = context,
                request = currentRoute.request,
                onBack = { templateEditor = null },
                onSave = { updated ->
                    val request = currentRoute.request
                    val next = if (request.index in templates.indices) {
                        templates.toMutableList().also { it[request.index] = updated }
                    } else templates + updated
                    templates = next
                    if (defaultTemplateId.isBlank()) defaultTemplateId = updated.id
                    persistRules(nextTemplates = next, nextDefault = defaultTemplateId)
                    templateEditor = null
                },
                onDelete = {
                    val request = currentRoute.request
                    if (request.index in templates.indices) {
                        val deleted = templates[request.index].id
                        templates = templates.toMutableList().also { it.removeAt(request.index) }
                        bindings = bindings.map { if (it.templateId == deleted) it.copy(templateId = "") else it }
                        if (defaultTemplateId == deleted) defaultTemplateId = templates.firstOrNull()?.id.orEmpty()
                        persistRules()
                    }
                    templateEditor = null
                }
            )
            is AutoTransferRoute.BindingEditor -> TransferBindingEditorPage(
                context = context,
                request = currentRoute.request,
                templates = templates,
                onBack = { bindingEditor = null },
                onSave = { updated ->
                    val base = bindings.toMutableList()
                    if (currentRoute.request.index in base.indices) base.removeAt(currentRoute.request.index)
                    bindings = upsertTransferBindings(base, listOf(updated))
                    persistRules(nextBindings = bindings)
                    bindingEditor = null
                },
                onDelete = {
                    if (currentRoute.request.index in bindings.indices) {
                        bindings = bindings.toMutableList().also { it.removeAt(currentRoute.request.index) }
                        persistRules(nextBindings = bindings)
                    }
                    bindingEditor = null
                }
            )
            AutoTransferRoute.TemplateManager -> TransferTemplateListPage(
                templates = templates,
                onBack = { showTemplates = false },
                onOpen = { index, value -> templateEditor = TransferTemplateEditorRequest(index, value, true) },
                onAdd = { templateEditor = TransferTemplateEditorRequest(templates.size, newTransferTemplate(templates.size + 1, sp), false) }
            )
            AutoTransferRoute.BindingManager -> TransferBindingListPage(
                bindings = bindings,
                templates = templates,
                onBack = { showBindings = false },
                onOpen = { index, value -> bindingEditor = TransferBindingEditorRequest(index, value, true) },
                onDeleteBindings = { targets ->
                    val targetIds = targets.mapTo(HashSet()) { it.id }
                    bindings = bindings.filterNot { it.id in targetIds }
                    persistRules(nextBindings = bindings)
                    Toast.makeText(context, "已删除 ${targets.size} 个适用聊天", Toast.LENGTH_SHORT).show()
                },
                onAdd = {
                    picker = ContactPickerRequest(
                        title = "选择适用聊天",
                        mode = ContactPickerMode.BOTH,
                        multiSelect = true,
                        existingValue = "",
                        enableLabels = true,
                        onValue = { value ->
                            val additions = parseIds(value).map { id ->
                                bindings.firstOrNull { it.targetId == id }
                                    ?: TransferRuleBinding(id, id, transferContactLabel(id), false, if (templates.size == 1) templates.first().id else "")
                            }
                            if (additions.size == 1) {
                                val item = additions.first()
                                val index = bindings.indexOfFirst { it.targetId == item.targetId }
                                bindingEditor = TransferBindingEditorRequest(index.takeIf { it >= 0 } ?: bindings.size, item, index >= 0)
                            } else if (additions.isNotEmpty()) {
                                bindings = upsertTransferBindings(bindings, additions)
                                persistRules(nextBindings = bindings)
                            }
                        }
                    )
                }
            )
            AutoTransferRoute.BatchApply -> TransferBatchApplyPage(
                templates = templates,
                bindings = bindings,
                onBack = { showBatchApply = false },
                onPickChats = { templateId ->
                    picker = ContactPickerRequest(
                        title = "批量套用收款模板",
                        mode = ContactPickerMode.BOTH,
                        multiSelect = true,
                        existingValue = formatIds(bindings.filter { it.templateId == templateId }.map { it.targetId }),
                        enableLabels = true,
                        onValue = { value ->
                            val selected = parseIds(value)
                            val retained = bindings.filterNot { it.templateId == templateId && it.targetId !in selected }
                            val additions = selected.map { id ->
                                TransferRuleBinding(id, id, transferContactLabel(id), true, templateId)
                            }
                            bindings = upsertTransferBindings(retained, additions)
                            persistRules(nextBindings = bindings)
                            Toast.makeText(context, "模板已套用到 ${selected.size} 个聊天", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
            is AutoTransferRoute.GlobalReplySteps -> RedPacketReplyStepsPage(
                context = context,
                title = if (currentRoute.target == TransferReplyTarget.GROUP) "群聊收款回复" else "私聊收款回复",
                initialSteps = if (currentRoute.target == TransferReplyTarget.GROUP) groupReplySteps else replySteps,
                templateVariables = transferTemplateVariables,
                onBack = { showReplySteps = false },
                onSave = {
                    if (currentRoute.target == TransferReplyTarget.GROUP) groupReplySteps = it else replySteps = it
                    showReplySteps = false
                }
            )
            AutoTransferRoute.Main -> PageScaffold(
                title = provider.title(),
                largeTitle = provider.title(),
                scrollBehavior = scrollBehavior,
                bottomBar = {
                    BottomActionBar(
                        primaryText = "保存设置",
                        onPrimaryClick = save@{
                            if (!PaymentTemplateTimeFormatter.isValidPattern(timeFormat)) {
                                Toast.makeText(context, "时间格式无效", Toast.LENGTH_SHORT).show()
                                return@save
                            }
                            val normalizedTimeFormat = PaymentTemplateTimeFormatter.normalizePattern(timeFormat)
                            val minDelay = randomMinMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L
                            val maxDelay = randomMaxMs.toLongOrNull()?.coerceIn(minDelay, 600000L) ?: minDelay
                            val cleanSteps = cleanRedPacketReplySteps(replySteps)
                            val cleanGroupSteps = cleanRedPacketReplySteps(groupReplySteps)
                            sp.edit()
                                .putString(AutoTransferSettings.KEY_WHITELIST, whitelist)
                                .putString(AutoTransferSettings.KEY_BLACKLIST, blacklist)
                                .putInt(AutoTransferSettings.KEY_MODE, listMode)
                                .putLong(AutoTransferSettings.KEY_DELAY_MS, delayMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L)
                                .putInt(AutoTransferSettings.KEY_DELAY_MODE, delayMode)
                                .putLong(AutoTransferSettings.KEY_DELAY_RANDOM_MIN, minDelay)
                                .putLong(AutoTransferSettings.KEY_DELAY_RANDOM_MAX, maxDelay)
                                .putString(AutoTransferSettings.KEY_AMOUNT_VALUE, amountValue)
                                .putBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, amountEnabled)
                                .putInt(AutoTransferSettings.KEY_AMOUNT_COND, amountCondition)
                                .putInt(AutoTransferSettings.KEY_AMOUNT_ACTION, amountAction)
                                .putInt(AutoTransferSettings.KEY_KEYWORD_MODE, keywordMode)
                                .putString(AutoTransferSettings.KEY_KEYWORDS, if (keywordMode == 0) "" else keywords)
                                .putString(AutoTransferSettings.KEY_RECEIVE_ACCOUNT, receiveAccount)
                                .putBoolean(AutoTransferSettings.KEY_QUIET_ENABLE, quietEnabled)
                                .putInt(AutoTransferSettings.KEY_QUIET_START_SECOND, parseRedPacketSecond(quietStart, 0))
                                .putInt(AutoTransferSettings.KEY_QUIET_END_SECOND, parseRedPacketSecond(quietEnd, 0))
                                .putString(AutoTransferSettings.KEY_REPLY_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanSteps))
                                .putString(AutoTransferSettings.KEY_REPLY_GROUP_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanGroupSteps))
                                .putBoolean(AutoTransferSettings.KEY_REPLY_ENABLE, cleanSteps.isNotEmpty())
                                .putString(AutoTransferSettings.KEY_REPLY_TEXT, cleanSteps.firstOrNull()?.content.orEmpty())
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_SYSTEM_ENABLE, notifySystemEnabled)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_TOAST_ENABLE, notifyToastEnabled)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_SOUND_ENABLE, notifySoundEnabled)
                                .putInt(AutoTransferSettings.KEY_NOTIFY_SOUND_MODE, notifySoundMode)
                                .putBoolean(AutoTransferSettings.KEY_NOTIFY_VIBRATE_ENABLE, notifyVibrateEnabled)
                                .putString(AutoTransferSettings.KEY_NOTIFY_SOUND_URI, notifySoundUri)
                                .putString(AutoTransferSettings.KEY_NOTIFY_TEXT, notifyText)
                                .putString(AutoTransferSettings.KEY_NOTIFY_TOAST_TEXT, notifyToastText)
                                .putBoolean(AutoTransferSettings.KEY_ANNOUNCE_ENABLE, announceEnabled)
                                .putString(AutoTransferSettings.KEY_ANNOUNCE_TEXT, announceText)
                                .putString(AutoTransferSettings.KEY_TIME_FORMAT, normalizedTimeFormat)
                                .apply()
                            replySteps = cleanSteps
                            groupReplySteps = cleanGroupSteps
                            timeFormat = normalizedTimeFormat
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
                        bottom = padding.calculateBottomPadding() + 84.dp
                    )
                ) {
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则") }
                    item {
                        SettingsCard {
                            SwitchRow(sp, AutoTransferSettings.KEY_ENABLE, "自动收款", "自动领取待收款转账", false)
                            InsetDivider()
                            ActionRow("收款规则模板", if (templates.isEmpty()) "暂无模板" else "${templates.size} 个模板") { showTemplates = true }
                            InsetDivider()
                            PopupChoiceRow(
                                title = "默认规则",
                                summary = describeTransferDefault(defaultTemplateId, templates),
                                options = listOf(PopupChoice("旧版全局设置", "")) + templates.map { PopupChoice(it.name, it.id) },
                                currentValue = defaultTemplateId,
                                onValueChanged = {
                                    defaultTemplateId = it
                                    persistRules(nextDefault = it)
                                }
                            )
                            InsetDivider()
                            ActionRow("适用聊天", if (bindings.isEmpty()) "暂无单独配置" else "${bindings.size} 个聊天") { showBindings = true }
                            InsetDivider()
                            ActionRow("批量套用模板", "一次给多个聊天分配同一规则") { showBatchApply = true }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "全局收款") }
                    item {
                        SettingsCard {
                            PopupChoiceRow("收款位置", "使用微信当前账号提供的收款账户", receiveAccountOptions, receiveAccount, onValueChanged = { receiveAccount = it })
                            InsetDivider()
                            SwitchRow(sp, AutoTransferSettings.KEY_REFUND_REJECTED, "拒收时退回", "规则不通过时原路退回", false)
                            InsetDivider()
                            PopupOptionRow("收款延迟", transferDelayModeLabel(delayMode), redPacketDelayModeOptions(true), delayMode, onValueChanged = { delayMode = it })
                            if (delayMode == TransferRuleConfig.DELAY_CUSTOM) {
                                InsetDivider(); NumberInputRow("自定义延迟", "单位 ms", delayMs) { delayMs = it }
                            } else if (delayMode == TransferRuleConfig.DELAY_RANDOM) {
                                InsetDivider(); NumberInputRow("最小延迟", "单位 ms", randomMinMs) { randomMinMs = it }
                                InsetDivider(); NumberInputRow("最大延迟", "单位 ms", randomMaxMs) { randomMaxMs = it }
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "全局过滤") }
                    item {
                        SettingsCard {
                            PopupOptionRow("收款范围", transferListModeLabel(listMode), optionItems("全部接收" to 0, "只接收白名单" to 1, "拒收黑名单" to 2), listMode, onValueChanged = { listMode = it; sp.edit().putInt(AutoTransferSettings.KEY_MODE, it).apply() })
                            if (listMode == 1 || listMode == 2) {
                                InsetDivider()
                                ActionRow(if (listMode == 1) "白名单" else "黑名单", autoReplySelectedIdSummary(if (listMode == 1) whitelist else blacklist)) {
                                    picker = ContactPickerRequest(if (listMode == 1) "选择白名单" else "选择黑名单", ContactPickerMode.BOTH, true, if (listMode == 1) whitelist else blacklist, {
                                        if (listMode == 1) whitelist = it else blacklist = it
                                    }, true)
                                }
                            }
                            InsetDivider()
                            SwitchRow(amountEnabled, "启用金额规则", "按转账金额决定接收或拒收") { amountEnabled = it; sp.edit().putBoolean(AutoTransferSettings.KEY_AMOUNT_ENABLE, it).apply() }
                            if (amountEnabled) {
                                InsetDivider(); PopupOptionRow("金额条件", transferAmountConditionLabel(amountCondition), optionItems("大于" to 0, "小于" to 1, "等于" to 2), amountCondition, onValueChanged = { amountCondition = it; sp.edit().putInt(AutoTransferSettings.KEY_AMOUNT_COND, it).apply() })
                                InsetDivider(); InputRow("金额数值", "单位元，例如 10.5", amountValue) { amountValue = it.filter { ch -> ch.isDigit() || ch == '.' } }
                                InsetDivider(); PopupOptionRow("命中后动作", transferAmountActionLabel(amountAction), optionItems("拒收/忽略" to 0, "仅接收满足条件" to 1), amountAction, onValueChanged = { amountAction = it; sp.edit().putInt(AutoTransferSettings.KEY_AMOUNT_ACTION, it).apply() })
                            }
                            InsetDivider()
                            PopupOptionRow("关键词规则", transferKeywordModeLabel(keywordMode), optionItems("不启用" to 0, "必须包含关键词" to 1, "包含则拒收" to 2), keywordMode, onValueChanged = { keywordMode = it; sp.edit().putInt(AutoTransferSettings.KEY_KEYWORD_MODE, it).apply() })
                            if (keywordMode != 0) {
                                InsetDivider(); InputRow("关键词", "多个关键词用 |、逗号或换行分隔", keywords, minLines = 2) { keywords = it }
                            }
                            InsetDivider()
                            SwitchRow(quietEnabled, "禁收时段", "指定时段内不自动收款") { quietEnabled = it }
                            if (quietEnabled) {
                                InsetDivider(); TimeOfDayPickerRow("开始时间", quietStart) { quietStart = it }
                                InsetDivider(); TimeOfDayPickerRow("结束时间", quietEnd) { quietEnd = it }
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板变量") }
                    item {
                        SettingsCard {
                            InputRow(
                                "时间变量格式",
                                "用于 {time}，例如 yyyy-MM-dd HH:mm:ss",
                                timeFormat
                            ) { timeFormat = it }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款后回复") }
                    item {
                        SettingsCard {
                            SelectRow("私聊收款回复", describeRedPacketReplySteps(replySteps)) {
                                replyTarget = TransferReplyTarget.PRIVATE
                                showReplySteps = true
                            }
                            InsetDivider()
                            SelectRow("群聊收款回复", describeRedPacketReplySteps(groupReplySteps)) {
                                replyTarget = TransferReplyTarget.GROUP
                                showReplySteps = true
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "成功提醒") }
                    item {
                        TransferNotificationSettingsCard(
                            context, notifySystemEnabled, { notifySystemEnabled = it }, notifyToastEnabled, { notifyToastEnabled = it },
                            notifySoundEnabled, { notifySoundEnabled = it }, notifySoundMode, { notifySoundMode = it; notifySoundUri = "" },
                            notifyVibrateEnabled, { notifyVibrateEnabled = it }, notifySoundUri, { notifySoundUri = it },
                            notifyText, { notifyText = it }, notifyToastText, { notifyToastText = it },
                            announceEnabled, { announceEnabled = it }, announceText, { announceText = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferTemplateListPage(
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onOpen: (Int, TransferRuleTemplate) -> Unit,
    onAdd: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "收款规则模板",
        largeTitle = "收款规则模板",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("新增模板", onAdd, "返回", onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。新增后可设为默认规则或分配给指定聊天。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                template.name.ifBlank { "模板 ${index + 1}" },
                                describeTransferTemplate(template)
                            ) { onOpen(index, template) }
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferBindingListPage(
    bindings: List<TransferRuleBinding>,
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onOpen: (Int, TransferRuleBinding) -> Unit,
    onAdd: () -> Unit,
    onDeleteBindings: (List<TransferRuleBinding>) -> Unit
) {
    val context = LocalContext.current
    var category by remember { mutableStateOf(ConversationRuleCategory.ALL) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val lower = query.trim().lowercase(Locale.US)
    val visible = bindings.mapIndexed { index, value -> index to value }.filter { (_, value) ->
        conversationRuleCategoryMatches(value, category) && (
            lower.isBlank() || value.label.lowercase(Locale.US).contains(lower) ||
                value.targetId.lowercase(Locale.US).contains(lower) ||
                templates.firstOrNull { it.id == value.templateId }?.name?.lowercase(Locale.US)?.contains(lower) == true
            )
    }
    val visibleIds = visible.mapTo(LinkedHashSet()) { it.second.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedBindings = bindings.filter { it.id in selectedIds }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "适用聊天",
        largeTitle = "适用聊天",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedBindings.size}）",
                    onPrimaryClick = {
                        if (selectedBindings.isEmpty()) {
                            Toast.makeText(context, "请先选择适用聊天", Toast.LENGTH_SHORT).show()
                        } else {
                            showDeleteConfirm = true
                        }
                    },
                    secondaryText = "取消",
                    onSecondaryClick = {
                        batchDeleteMode = false
                        selectedIds = emptySet()
                    },
                    middleText = if (visibleIds.isEmpty()) null else if (allVisibleSelected) "取消全选" else "全选",
                    onMiddleClick = if (visibleIds.isEmpty()) null else {
                        {
                            selectedIds = if (allVisibleSelected) {
                                selectedIds - visibleIds
                            } else {
                                selectedIds + visibleIds
                            }
                        }
                    }
                )
            } else {
                BottomActionBar(
                    primaryText = "添加聊天",
                    onPrimaryClick = onAdd,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
                    middleText = if (bindings.isEmpty()) null else "批量删除",
                    onMiddleClick = if (bindings.isEmpty()) null else {
                        {
                            batchDeleteMode = true
                            selectedIds = emptySet()
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    ConversationRuleCategoryTabs(
                        selected = category,
                        onSelected = { category = it },
                        includeOfficial = false
                    )
                }
            }
            item { SettingsCard { InputRow("搜索聊天", "昵称 / ID / 模板名", query) { query = it } } }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天 · ${visible.size}/${bindings.size} 项") }
            item {
                SettingsCard {
                    if (visible.isEmpty()) {
                        EmptyText(if (bindings.isEmpty()) "暂无适用聊天。" else "没有匹配结果。")
                    } else {
                        visible.forEachIndexed { row, (index, binding) ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = binding.label.ifBlank { binding.targetId },
                                        value = index,
                                        summary = describeTransferBinding(binding, templates)
                                    ),
                                    selected = binding.id in selectedIds,
                                    onClick = {
                                        selectedIds = if (binding.id in selectedIds) {
                                            selectedIds - binding.id
                                        } else {
                                            selectedIds + binding.id
                                        }
                                    }
                                )
                            } else {
                                SelectRow(
                                    binding.label.ifBlank { binding.targetId },
                                    describeTransferBinding(binding, templates)
                                ) { onOpen(index, binding) }
                            }
                            if (row < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedBindings.size} 个适用聊天，此操作不可撤销。",
        labels = selectedBindings.map { it.label.ifBlank { it.targetId } },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedBindings
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedIds = emptySet()
            onDeleteBindings(targets)
        }
    )
}

@Composable
internal fun TransferBindingEditorPage(
    context: Context,
    request: TransferBindingEditorRequest,
    templates: List<TransferRuleTemplate>,
    onBack: () -> Unit,
    onSave: (TransferRuleBinding) -> Unit,
    onDelete: () -> Unit
) {
    var enabled by remember(request) { mutableStateOf(request.binding.enabled) }
    var templateId by remember(request) { mutableStateOf(request.binding.templateId) }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = request.binding.label.ifBlank { request.binding.targetId },
        largeTitle = request.binding.label.ifBlank { request.binding.targetId },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                "保存聊天",
                {
                    onSave(request.binding.copy(enabled = enabled, templateId = templateId))
                    Toast.makeText(context, "适用聊天已保存", Toast.LENGTH_SHORT).show()
                },
                "返回",
                onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天") }
            item {
                SettingsCard {
                    InfoRow("ID", request.binding.targetId)
                    InsetDivider()
                    SwitchRow(enabled, "启用自动收款", "关闭后该聊天不会自动收款") { enabled = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    OptionChoiceRow(
                        OptionItem("跟随默认规则", -1, "使用默认模板或全局设置"),
                        templateId.isBlank()
                    ) { templateId = "" }
                    templates.forEachIndexed { index, template ->
                        InsetDivider()
                        OptionChoiceRow(
                            OptionItem(template.name.ifBlank { "模板 ${index + 1}" }, index, describeTransferTemplate(template)),
                            templateId == template.id
                        ) { templateId = template.id }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item { SettingsCard { ActionRow("移除适用聊天", "移除后恢复默认规则", onDelete) } }
            }
        }
    }
}

@Composable
internal fun TransferBatchApplyPage(
    templates: List<TransferRuleTemplate>,
    bindings: List<TransferRuleBinding>,
    onBack: () -> Unit,
    onPickChats: (String) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "批量套用模板",
        largeTitle = "批量套用模板",
        scrollBehavior = scrollBehavior,
        bottomBar = { BottomActionBar("返回", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "选择模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("请先新增收款规则模板。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            val count = bindings.count { it.templateId == template.id }
                            SelectRow(template.name, "$count 个聊天 · ${describeTransferTemplate(template)}") {
                                onPickChats(template.id)
                            }
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransferTemplateEditorPage(
    context: Context,
    request: TransferTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (TransferRuleTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var value by remember(request) { mutableStateOf(request.template) }
    var showReplySteps by remember { mutableStateOf(false) }
    var replyTarget by remember { mutableStateOf(TransferReplyTarget.PRIVATE) }
    var groupReplySteps by remember(request) {
        mutableStateOf(request.template.groupReplySteps ?: request.template.replySteps)
    }
    var picker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    val accountOptions = remember { transferReceiveAccountOptions(context) }
    if (showReplySteps) {
        RedPacketReplyStepsPage(
            context = context,
            title = if (replyTarget == TransferReplyTarget.GROUP) "模板群聊收款回复" else "模板私聊收款回复",
            initialSteps = if (replyTarget == TransferReplyTarget.GROUP) groupReplySteps else value.replySteps,
            templateVariables = transferTemplateVariables,
            onBack = { showReplySteps = false },
            onSave = {
                if (replyTarget == TransferReplyTarget.GROUP) groupReplySteps = it else value = value.copy(replySteps = it)
                showReplySteps = false
            }
        )
        return
    }
    picker?.let { requestPicker ->
        ContactPickerPage(
            context = context,
            request = requestPicker,
            onBack = { picker = null },
            onConfirm = {
                requestPicker.onValue(formatIds(it.map { option -> option.id }))
                picker = null
            }
        )
        return
    }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = value.name.ifBlank { "收款模板" },
        largeTitle = value.name.ifBlank { "收款模板" },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                "保存模板",
                {
                    onSave(value.copy(
                        name = value.name.ifBlank { "收款模板" },
                        replySteps = cleanRedPacketReplySteps(value.replySteps),
                        groupReplySteps = cleanRedPacketReplySteps(groupReplySteps)
                    ))
                    Toast.makeText(context, "收款模板已保存", Toast.LENGTH_SHORT).show()
                },
                "返回",
                onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "用于默认规则和聊天绑定", value.name) { value = value.copy(name = it) }
                    InsetDivider()
                    SwitchRow(value.enabled, "启用模板", "关闭后使用该模板的聊天不会自动收款") { value = value.copy(enabled = it) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款") }
            item {
                SettingsCard {
                    PopupChoiceRow("收款位置", "选择转账到账账户", accountOptions, value.receiveAccount, onValueChanged = { value = value.copy(receiveAccount = it) })
                    InsetDivider()
                    SwitchRow(value.refundRejected, "拒收时退回", "规则不通过时原路退回") { value = value.copy(refundRejected = it) }
                    InsetDivider()
                    PopupOptionRow("收款延迟", transferDelayModeLabel(value.delayMode), redPacketDelayModeOptions(true), value.delayMode, onValueChanged = { value = value.copy(delayMode = it) })
                    if (value.delayMode == TransferRuleConfig.DELAY_CUSTOM) {
                        InsetDivider(); NumberInputRow("自定义延迟", "单位 ms", value.delayMs.toString()) { value = value.copy(delayMs = it.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L) }
                    } else if (value.delayMode == TransferRuleConfig.DELAY_RANDOM) {
                        InsetDivider(); NumberInputRow("最小延迟", "单位 ms", value.randomMinMs.toString()) { value = value.copy(randomMinMs = it.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L) }
                        InsetDivider(); NumberInputRow("最大延迟", "单位 ms", value.randomMaxMs.toString()) { value = value.copy(randomMaxMs = it.toLongOrNull()?.coerceIn(value.randomMinMs, 600000L) ?: value.randomMinMs) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "过滤") }
            item {
                SettingsCard {
                    PopupOptionRow("收款范围", transferListModeLabel(value.listMode), optionItems("全部接收" to 0, "只接收白名单" to 1, "拒收黑名单" to 2), value.listMode, onValueChanged = { value = value.copy(listMode = it) })
                    if (value.listMode == 1 || value.listMode == 2) {
                        InsetDivider()
                        val listValue = if (value.listMode == 1) value.whitelist else value.blacklist
                        ActionRow(if (value.listMode == 1) "白名单" else "黑名单", autoReplySelectedIdSummary(listValue)) {
                            picker = ContactPickerRequest(
                                if (value.listMode == 1) "选择白名单" else "选择黑名单",
                                ContactPickerMode.BOTH,
                                true,
                                listValue,
                                { selected -> value = if (value.listMode == 1) value.copy(whitelist = selected) else value.copy(blacklist = selected) },
                                true
                            )
                        }
                    }
                    InsetDivider()
                    SwitchRow(value.amountEnabled, "启用金额规则", "按转账金额决定接收或拒收") { value = value.copy(amountEnabled = it) }
                    if (value.amountEnabled) {
                        InsetDivider(); PopupOptionRow("金额条件", transferAmountConditionLabel(value.amountCondition), optionItems("大于" to 0, "小于" to 1, "等于" to 2), value.amountCondition, onValueChanged = { value = value.copy(amountCondition = it) })
                        InsetDivider(); InputRow("金额数值", "单位元，例如 10.5", value.amountValue) { text -> value = value.copy(amountValue = text.filter { it.isDigit() || it == '.' }) }
                        InsetDivider(); PopupOptionRow("命中后动作", transferAmountActionLabel(value.amountAction), optionItems("拒收/忽略" to 0, "仅接收满足条件" to 1), value.amountAction, onValueChanged = { value = value.copy(amountAction = it) })
                    }
                    InsetDivider()
                    PopupOptionRow("关键词规则", transferKeywordModeLabel(value.keywordMode), optionItems("不启用" to 0, "必须包含关键词" to 1, "包含则拒收" to 2), value.keywordMode, onValueChanged = { value = value.copy(keywordMode = it) })
                    if (value.keywordMode != 0) {
                        InsetDivider(); InputRow("关键词", "多个关键词用 |、逗号或换行分隔", value.keywords, minLines = 2) { value = value.copy(keywords = it) }
                    }
                    InsetDivider()
                    SwitchRow(value.quietEnabled, "禁收时段", "指定时段内不自动收款") { value = value.copy(quietEnabled = it) }
                    if (value.quietEnabled) {
                        InsetDivider(); TimeOfDayPickerRow("开始时间", formatRedPacketSecond(value.quietStartSecond)) { value = value.copy(quietStartSecond = parseRedPacketSecond(it, value.quietStartSecond)) }
                        InsetDivider(); TimeOfDayPickerRow("结束时间", formatRedPacketSecond(value.quietEndSecond)) { value = value.copy(quietEndSecond = parseRedPacketSecond(it, value.quietEndSecond)) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收款后回复") }
            item {
                SettingsCard {
                    SelectRow("私聊收款回复", describeRedPacketReplySteps(value.replySteps)) {
                        replyTarget = TransferReplyTarget.PRIVATE
                        showReplySteps = true
                    }
                    InsetDivider()
                    SelectRow("群聊收款回复", describeRedPacketReplySteps(groupReplySteps)) {
                        replyTarget = TransferReplyTarget.GROUP
                        showReplySteps = true
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "成功提醒") }
            item {
                TransferNotificationSettingsCard(
                    context,
                    value.notifySystemEnabled, { value = value.copy(notifySystemEnabled = it) },
                    value.notifyToastEnabled, { value = value.copy(notifyToastEnabled = it) },
                    value.notifySoundEnabled, { value = value.copy(notifySoundEnabled = it) },
                    value.notifySoundMode, { value = value.copy(notifySoundMode = it, notifySoundUri = "") },
                    value.notifyVibrateEnabled, { value = value.copy(notifyVibrateEnabled = it) },
                    value.notifySoundUri, { value = value.copy(notifySoundUri = it) },
                    value.notifyText, { value = value.copy(notifyText = it) },
                    value.notifyToastText, { value = value.copy(notifyToastText = it) },
                    value.announceEnabled, { value = value.copy(announceEnabled = it) },
                    value.announceText, { value = value.copy(announceText = it) }
                )
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item { SettingsCard { ActionRow("删除模板", "删除后相关聊天恢复默认规则", onDelete) } }
            }
        }
    }
}

@Composable
internal fun TransferNotificationSettingsCard(
    context: Context,
    system: Boolean,
    onSystem: (Boolean) -> Unit,
    toast: Boolean,
    onToast: (Boolean) -> Unit,
    sound: Boolean,
    onSound: (Boolean) -> Unit,
    soundMode: Int,
    onSoundMode: (Int) -> Unit,
    vibrate: Boolean,
    onVibrate: (Boolean) -> Unit,
    soundUri: String,
    onSoundUri: (String) -> Unit,
    noticeText: String,
    onNoticeText: (String) -> Unit,
    toastText: String,
    onToastText: (String) -> Unit,
    announce: Boolean,
    onAnnounce: (Boolean) -> Unit,
    announceText: String,
    onAnnounceText: (String) -> Unit
) {
    SettingsCard {
        SwitchRow(system, "通知栏提醒", "收款请求成功后显示") { onSystem(it) }
        if (system) {
            InsetDivider(); VariableInputRow("通知栏文案", "支持下方变量", noticeText, transferTemplateVariables, onValueChange = onNoticeText)
        }
        InsetDivider()
        SwitchRow(toast, "浮窗提醒", "收款请求成功后短暂提示") { onToast(it) }
        if (toast) {
            InsetDivider(); VariableInputRow("浮窗文案", "支持下方变量", toastText, transferTemplateVariables, onValueChange = onToastText)
        }
        InsetDivider()
        SwitchRow(sound, "通知铃声", "开启后播放通知铃声") { onSound(it) }
        if (sound) {
            InsetDivider()
            PopupOptionRow(
                "铃声模式",
                if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) "从文件选择铃声" else "选择系统铃声",
                optionItems("选择系统铃声" to AutoTransferSettings.NOTIFY_SOUND_MODE_SYSTEM, "从文件选择铃声" to AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM),
                soundMode,
                onSoundMode
            )
            InsetDivider()
            ActionRow("选择铃声", ringtoneDisplayName(context, soundUri, soundMode)) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                } else {
                    val picked: (String) -> Unit = { uri ->
                        if (uri.isNotBlank()) {
                            onSoundUri(
                                if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                    CustomNotificationRuntime.freezeRingtoneUri(context, uri)
                                } else uri
                            )
                        }
                    }
                    if (soundMode == AutoTransferSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                        RingtonePickerBridge.launchFile(activity, picked)
                    } else {
                        RingtonePickerBridge.launchSystem(activity, soundUri, picked)
                    }
                }
            }
        }
        InsetDivider()
        SwitchRow(vibrate, "通知震动", "开启后触发通知震动") { onVibrate(it) }
        InsetDivider()
        SwitchRow(announce, "收款语音播报", "收款请求成功后用系统语音播报") { onAnnounce(it) }
        if (announce) {
            InsetDivider(); VariableInputRow("播报文案", "支持下方变量", announceText, transferTemplateVariables, onValueChange = onAnnounceText)
        }
    }
}

@Composable
internal fun FakeWalletBalanceMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeWalletBalanceSettings.PREFS_NAME) }
    var balanceEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_BALANCE_ENABLE
            )
        )
    }
    var lqtEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_ENABLE
            )
        )
    }
    var businessEnabled by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.isAccountEnabled(
                sp,
                FakeWalletBalanceSettings.KEY_BUSINESS_ENABLE
            )
        )
    }
    val storedBalanceAmount = remember {
        sp.getString(
            FakeWalletBalanceSettings.KEY_BALANCE_AMOUNT,
            FakeWalletBalanceSettings.DEFAULT_AMOUNT
        ) ?: FakeWalletBalanceSettings.DEFAULT_AMOUNT
    }
    val storedLqtAmount = remember {
        sp.getString(
            FakeWalletBalanceSettings.KEY_LQT_AMOUNT,
            FakeWalletBalanceSettings.DEFAULT_AMOUNT
        ) ?: FakeWalletBalanceSettings.DEFAULT_AMOUNT
    }
    val storedBusinessAmount = remember {
        sp.getString(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT, null)
            ?: storedLqtAmount
    }
    var balanceAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedBalanceAmount))
    }
    var lqtAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedLqtAmount))
    }
    var businessAmount by remember {
        mutableStateOf(FakeWalletBalanceSettings.normalizeAmount(storedBusinessAmount))
    }
    var balanceMode by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_BALANCE_MODE,
                storedBalanceAmount
            )
        )
    }
    var lqtMode by remember {
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_MODE,
                storedLqtAmount
            )
        )
    }
    var businessMode by remember {
        val fallbackMode = if (!sp.contains(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT)) {
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_LQT_MODE,
                storedLqtAmount
            )
        } else {
            FakeWalletBalanceSettings.DEFAULT_MODE
        }
        mutableStateOf(
            FakeWalletBalanceSettings.amountMode(
                sp,
                FakeWalletBalanceSettings.KEY_BUSINESS_MODE,
                storedBusinessAmount,
                fallbackMode
            )
        )
    }
    val amountModeOptions = remember {
        listOf(
            PopupChoice("固定金额", FakeWalletBalanceSettings.MODE_FIXED),
            PopupChoice("增加金额", FakeWalletBalanceSettings.MODE_INCREASE),
            PopupChoice("减少金额", FakeWalletBalanceSettings.MODE_DECREASE)
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    val normalizedBalance = FakeWalletBalanceSettings.normalizeAmount(balanceAmount)
                    val normalizedLqt = FakeWalletBalanceSettings.normalizeAmount(lqtAmount)
                    val normalizedBusiness = FakeWalletBalanceSettings.normalizeAmount(businessAmount)
                    balanceAmount = normalizedBalance
                    lqtAmount = normalizedLqt
                    businessAmount = normalizedBusiness
                    sp.edit()
                        .putString(FakeWalletBalanceSettings.KEY_BALANCE_AMOUNT, normalizedBalance)
                        .putString(FakeWalletBalanceSettings.KEY_LQT_AMOUNT, normalizedLqt)
                        .putString(FakeWalletBalanceSettings.KEY_BUSINESS_AMOUNT, normalizedBusiness)
                        .putString(FakeWalletBalanceSettings.KEY_BALANCE_MODE, balanceMode)
                        .putString(FakeWalletBalanceSettings.KEY_LQT_MODE, lqtMode)
                        .putString(FakeWalletBalanceSettings.KEY_BUSINESS_MODE, businessMode)
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
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "显示") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = balanceEnabled,
                        title = "余额",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        balanceEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_BALANCE_ENABLE, enabled).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = lqtEnabled,
                        title = "零钱通",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        lqtEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_LQT_ENABLE, enabled).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = businessEnabled,
                        title = "经营账户",
                        summary = "固定显示或按真实金额动态增减"
                    ) { enabled ->
                        businessEnabled = enabled
                        sp.edit().putBoolean(FakeWalletBalanceSettings.KEY_BUSINESS_ENABLE, enabled).apply()
                    }
                }
            }
            if (balanceEnabled || lqtEnabled || businessEnabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "金额") }
                item {
                    SettingsCard {
                        if (balanceEnabled) {
                            PopupChoiceRow(
                                title = "余额显示方式",
                                summary = "选择固定显示或按真实余额计算",
                                options = amountModeOptions,
                                currentValue = balanceMode,
                                onValueChanged = { balanceMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (balanceMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "余额增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "余额减少金额"
                                    else -> "余额金额"
                                },
                                if (balanceMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实余额计算"
                                },
                                balanceAmount
                            ) { value ->
                                balanceAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                        if (balanceEnabled && (lqtEnabled || businessEnabled)) {
                            InsetDivider()
                        }
                        if (lqtEnabled) {
                            PopupChoiceRow(
                                title = "零钱通显示方式",
                                summary = "选择固定显示或按真实金额计算",
                                options = amountModeOptions,
                                currentValue = lqtMode,
                                onValueChanged = { lqtMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (lqtMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "零钱通增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "零钱通减少金额"
                                    else -> "零钱通金额"
                                },
                                if (lqtMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实金额计算"
                                },
                                lqtAmount
                            ) { value ->
                                lqtAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                        if (lqtEnabled && businessEnabled) {
                            InsetDivider()
                        }
                        if (businessEnabled) {
                            PopupChoiceRow(
                                title = "经营账户显示方式",
                                summary = "选择固定显示或按真实金额计算",
                                options = amountModeOptions,
                                currentValue = businessMode,
                                onValueChanged = { businessMode = it }
                            )
                            InsetDivider()
                            InputRow(
                                when (businessMode) {
                                    FakeWalletBalanceSettings.MODE_INCREASE -> "经营账户增加金额"
                                    FakeWalletBalanceSettings.MODE_DECREASE -> "经营账户减少金额"
                                    else -> "经营账户金额"
                                },
                                if (businessMode == FakeWalletBalanceSettings.MODE_FIXED) {
                                    "直接显示该金额"
                                } else {
                                    "基于微信真实金额计算"
                                },
                                businessAmount
                            ) { value ->
                                businessAmount = value.filter { it.isDigit() || it == '.' || it == ',' }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MemberTitleMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MemberTitleSettings.PREFS_NAME) }
    var ownerTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_TITLE, MemberTitleSettings.DEFAULT_OWNER_TITLE) ?: MemberTitleSettings.DEFAULT_OWNER_TITLE) }
    var adminTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_TITLE, MemberTitleSettings.DEFAULT_ADMIN_TITLE) ?: MemberTitleSettings.DEFAULT_ADMIN_TITLE) }
    var memberTitle by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_TITLE, MemberTitleSettings.DEFAULT_MEMBER_TITLE) ?: MemberTitleSettings.DEFAULT_MEMBER_TITLE) }
    var ownerColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_COLOR, MemberTitleSettings.DEFAULT_OWNER_COLOR) ?: MemberTitleSettings.DEFAULT_OWNER_COLOR) }
    var adminColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_COLOR, MemberTitleSettings.DEFAULT_ADMIN_COLOR) ?: MemberTitleSettings.DEFAULT_ADMIN_COLOR) }
    var memberColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_COLOR, MemberTitleSettings.DEFAULT_MEMBER_COLOR) ?: MemberTitleSettings.DEFAULT_MEMBER_COLOR) }
    var customColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_CUSTOM_COLOR, MemberTitleSettings.DEFAULT_CUSTOM_COLOR) ?: MemberTitleSettings.DEFAULT_CUSTOM_COLOR) }
    var ownerTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_OWNER_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var adminTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_ADMIN_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var memberTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_MEMBER_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var customTextColor by remember { mutableStateOf(sp.getString(MemberTitleSettings.KEY_CUSTOM_TEXT_COLOR, MemberTitleSettings.DEFAULT_TEXT_COLOR) ?: MemberTitleSettings.DEFAULT_TEXT_COLOR) }
    var showMember by remember { mutableStateOf(sp.getBoolean(MemberTitleSettings.KEY_SHOW_MEMBER, MemberTitleSettings.DEFAULT_SHOW_MEMBER)) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    sp.edit()
                        .putString(MemberTitleSettings.KEY_OWNER_TITLE, MemberTitleStore.cleanTitle(ownerTitle).ifEmpty { MemberTitleSettings.DEFAULT_OWNER_TITLE })
                        .putString(MemberTitleSettings.KEY_ADMIN_TITLE, MemberTitleStore.cleanTitle(adminTitle).ifEmpty { MemberTitleSettings.DEFAULT_ADMIN_TITLE })
                        .putString(MemberTitleSettings.KEY_MEMBER_TITLE, MemberTitleStore.cleanTitle(memberTitle).ifEmpty { MemberTitleSettings.DEFAULT_MEMBER_TITLE })
                        .putString(MemberTitleSettings.KEY_OWNER_COLOR, MemberTitleStore.cleanColorSpec(ownerColor).ifEmpty { MemberTitleSettings.DEFAULT_OWNER_COLOR })
                        .putString(MemberTitleSettings.KEY_ADMIN_COLOR, MemberTitleStore.cleanColorSpec(adminColor).ifEmpty { MemberTitleSettings.DEFAULT_ADMIN_COLOR })
                        .putString(MemberTitleSettings.KEY_MEMBER_COLOR, MemberTitleStore.cleanColorSpec(memberColor).ifEmpty { MemberTitleSettings.DEFAULT_MEMBER_COLOR })
                        .putString(MemberTitleSettings.KEY_CUSTOM_COLOR, MemberTitleStore.cleanColorSpec(customColor).ifEmpty { MemberTitleSettings.DEFAULT_CUSTOM_COLOR })
                        .putString(MemberTitleSettings.KEY_OWNER_TEXT_COLOR, MemberTitleStore.cleanColorSpec(ownerTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_ADMIN_TEXT_COLOR, MemberTitleStore.cleanColorSpec(adminTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_MEMBER_TEXT_COLOR, MemberTitleStore.cleanColorSpec(memberTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
                        .putString(MemberTitleSettings.KEY_CUSTOM_TEXT_COLOR, MemberTitleStore.cleanColorSpec(customTextColor).ifEmpty { MemberTitleSettings.DEFAULT_TEXT_COLOR })
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
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(sp, MemberTitleSettings.KEY_ENABLE, "群员头衔", "在群聊昵称左侧显示身份头衔", MemberTitleSettings.DEFAULT_ENABLE)
                    InsetDivider()
                    SwitchRow(showMember, "显示普通群员", "关闭后只显示群主、管理员和自定义头衔") {
                        showMember = it
                        sp.edit().putBoolean(MemberTitleSettings.KEY_SHOW_MEMBER, it).apply()
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "名称") }
            item {
                SettingsCard {
                    InputRow("群主名称", "最多 8 个字符", ownerTitle) { ownerTitle = it.take(8) }
                    InsetDivider()
                    InputRow("管理员名称", "最多 8 个字符", adminTitle) { adminTitle = it.take(8) }
                    if (showMember) {
                        InsetDivider()
                        InputRow("群员名称", "最多 8 个字符", memberTitle) { memberTitle = it.take(8) }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "颜色") }
            item {
                SettingsCard {
                    ColorPickerRow("群主颜色", "默认黄色，渐变用 #F59E0B,#FDE047", ownerColor, onReset = { ownerColor = MemberTitleSettings.DEFAULT_OWNER_COLOR }) { ownerColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("群主文字颜色", "默认白色，支持渐变", ownerTextColor, onReset = { ownerTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { ownerTextColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("管理员颜色", "默认绿色，渐变用 #22C55E,#14B8A6", adminColor, onReset = { adminColor = MemberTitleSettings.DEFAULT_ADMIN_COLOR }) { adminColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("管理员文字颜色", "默认白色，支持渐变", adminTextColor, onReset = { adminTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { adminTextColor = it.take(19) }
                    if (showMember) {
                        InsetDivider()
                        ColorPickerRow("群员颜色", "默认灰色，渐变用 #64748B,#94A3B8", memberColor, onReset = { memberColor = MemberTitleSettings.DEFAULT_MEMBER_COLOR }) { memberColor = it.take(19) }
                        InsetDivider()
                        ColorPickerRow("群员文字颜色", "默认白色，支持渐变", memberTextColor, onReset = { memberTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { memberTextColor = it.take(19) }
                    }
                    InsetDivider()
                    ColorPickerRow("自定义默认色", "点击聊天头衔可单独设置每个人的名称和颜色，支持渐变", customColor, onReset = { customColor = MemberTitleSettings.DEFAULT_CUSTOM_COLOR }) { customColor = it.take(19) }
                    InsetDivider()
                    ColorPickerRow("自定义文字默认色", "单人未设置文字颜色时使用，支持渐变", customTextColor, onReset = { customTextColor = MemberTitleSettings.DEFAULT_TEXT_COLOR }) { customTextColor = it.take(19) }
                }
            }
        }
    }
}

@Composable
internal fun GroupNicknameColorMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GroupNicknameColorSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(GroupNicknameColorSettings.KEY_ENABLE, GroupNicknameColorSettings.DEFAULT_ENABLE)
        )
    }
    var color by remember {
        mutableStateOf(
            sp.getString(GroupNicknameColorSettings.KEY_COLOR, GroupNicknameColorSettings.DEFAULT_COLOR)
                ?: GroupNicknameColorSettings.DEFAULT_COLOR
        )
    }
    var weight by remember {
        mutableStateOf(
            sp.getInt(GroupNicknameColorSettings.KEY_WEIGHT, GroupNicknameColorSettings.DEFAULT_WEIGHT).toString()
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    sp.edit()
                        .putString(
                            GroupNicknameColorSettings.KEY_COLOR,
                            GroupNicknameColorStore.cleanColorSpec(color)
                        )
                        .putInt(
                            GroupNicknameColorSettings.KEY_WEIGHT,
                            GroupNicknameColorStore.cleanWeight(
                                weight.toIntOrNull() ?: GroupNicknameColorSettings.DEFAULT_WEIGHT
                            )
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
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群昵称") }
            item {
                SettingsCard {
                    SwitchRow(enabled, "群昵称自定义颜色", "修改群聊成员昵称的颜色和粗细") {
                        enabled = it
                        sp.edit().putBoolean(GroupNicknameColorSettings.KEY_ENABLE, it).apply()
                    }
                    if (enabled) {
                        InsetDivider()
                        ColorPickerRow(
                            "昵称颜色",
                            "留空跟随微信，渐变用 #F59E0B,#22C55E",
                            color,
                            onReset = { color = GroupNicknameColorSettings.DEFAULT_COLOR }
                        ) { color = it.take(19) }
                        InsetDivider()
                        WeightInputRow("昵称粗细", weight) { weight = it }
                    }
                }
            }
        }
    }
}

@Composable
internal fun RealNameTailMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RealNameTailSettings.PREFS_NAME) }
    var prefix by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GLOBAL_PREFIX, "") ?: "") }
    var maleText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_MALE_TEXT, RealNameTailSettings.DEFAULT_GENDER_MALE_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_MALE_TEXT) }
    var femaleText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_FEMALE_TEXT, RealNameTailSettings.DEFAULT_GENDER_FEMALE_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_FEMALE_TEXT) }
    var unknownGenderText by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_UNKNOWN_TEXT, RealNameTailSettings.DEFAULT_GENDER_UNKNOWN_TEXT) ?: RealNameTailSettings.DEFAULT_GENDER_UNKNOWN_TEXT) }
    var tailColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_TAIL_COLOR, RealNameTailSettings.DEFAULT_TAIL_COLOR) ?: RealNameTailSettings.DEFAULT_TAIL_COLOR) }
    var bracketColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_BRACKET_COLOR, RealNameTailSettings.DEFAULT_BRACKET_COLOR) ?: RealNameTailSettings.DEFAULT_BRACKET_COLOR) }
    var genderColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_GENDER_COLOR, RealNameTailSettings.DEFAULT_GENDER_COLOR) ?: RealNameTailSettings.DEFAULT_GENDER_COLOR) }
    var regionColor by remember { mutableStateOf(sp.getString(RealNameTailSettings.KEY_REGION_COLOR, RealNameTailSettings.DEFAULT_REGION_COLOR) ?: RealNameTailSettings.DEFAULT_REGION_COLOR) }
    var tailWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_TAIL_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var bracketWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_BRACKET_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var genderWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_GENDER_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var regionWeight by remember { mutableStateOf(sp.getInt(RealNameTailSettings.KEY_REGION_WEIGHT, RealNameTailSettings.DEFAULT_TEXT_WEIGHT).toString()) }
    var prefixEnabled by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_GLOBAL_PREFIX_ENABLE, false)) }
    var showGender by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_SHOW_GENDER, RealNameTailSettings.DEFAULT_SHOW_GENDER)) }
    var showRegion by remember { mutableStateOf(sp.getBoolean(RealNameTailSettings.KEY_SHOW_REGION, RealNameTailSettings.DEFAULT_SHOW_REGION)) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = {
                    sp.edit()
                        .putString(RealNameTailSettings.KEY_GLOBAL_PREFIX, prefix.trim().take(8))
                        .putString(RealNameTailSettings.KEY_GENDER_MALE_TEXT, maleText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_GENDER_FEMALE_TEXT, femaleText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_GENDER_UNKNOWN_TEXT, unknownGenderText.trim().take(12))
                        .putString(RealNameTailSettings.KEY_TAIL_COLOR, RealNameTailStore.cleanColorSpec(tailColor))
                        .putString(RealNameTailSettings.KEY_BRACKET_COLOR, RealNameTailStore.cleanColorSpec(bracketColor))
                        .putString(RealNameTailSettings.KEY_GENDER_COLOR, RealNameTailStore.cleanColorSpec(genderColor))
                        .putString(RealNameTailSettings.KEY_REGION_COLOR, RealNameTailStore.cleanColorSpec(regionColor))
                        .putInt(RealNameTailSettings.KEY_TAIL_WEIGHT, RealNameTailStore.cleanWeight(tailWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_BRACKET_WEIGHT, RealNameTailStore.cleanWeight(bracketWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_GENDER_WEIGHT, RealNameTailStore.cleanWeight(genderWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
                        .putInt(RealNameTailSettings.KEY_REGION_WEIGHT, RealNameTailStore.cleanWeight(regionWeight.toIntOrNull() ?: RealNameTailSettings.DEFAULT_TEXT_WEIGHT))
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
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(sp, RealNameTailSettings.KEY_ENABLE, "实名尾字", "在群聊昵称后显示已查询到的实名尾字", RealNameTailSettings.DEFAULT_ENABLE)
                    InsetDivider()
                    SwitchRow(sp, RealNameTailSettings.KEY_MESSAGE_QUERY, "消息触发查询", "收到群成员消息后自动补查", RealNameTailSettings.DEFAULT_MESSAGE_QUERY)
                    InsetDivider()
                    SwitchRow(sp, RealNameTailSettings.KEY_VISIBLE_QUERY, "可见成员查询", "聊天页出现未缓存成员时自动补查", RealNameTailSettings.DEFAULT_VISIBLE_QUERY)
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "显示") }
            item {
                SettingsCard {
                    SwitchRow(prefixEnabled, "实名前缀", "用统一前缀替换实名尾字前半部分") {
                        prefixEnabled = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_GLOBAL_PREFIX_ENABLE, it).apply()
                    }
                    if (prefixEnabled) {
                        InsetDivider()
                        InputRow("前缀内容", "最多 8 个字符，例如 *", prefix) { prefix = it.take(8) }
                    }
                    InsetDivider()
                    SwitchRow(showGender, "显示性别", "在实名尾字后追加联系人性别") {
                        showGender = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_SHOW_GENDER, it).apply()
                    }
                    if (showGender) {
                        InsetDivider()
                        InputRow("男性文案", "最多 12 个字符，默认 男", maleText) { maleText = it.take(12) }
                        InsetDivider()
                        InputRow("女性文案", "最多 12 个字符，默认 女", femaleText) { femaleText = it.take(12) }
                        InsetDivider()
                        InputRow("未知文案", "最多 12 个字符，留空则不显示", unknownGenderText) { unknownGenderText = it.take(12) }
                    }
                    InsetDivider()
                    SwitchRow(showRegion, "显示地区", "在实名尾字后追加联系人地区") {
                        showRegion = it
                        sp.edit().putBoolean(RealNameTailSettings.KEY_SHOW_REGION, it).apply()
                    }
                    InsetDivider()
                    ColorPickerRow("尾字颜色", "留空跟随昵称，渐变用 #F59E0B,#22C55E", tailColor, onReset = { tailColor = RealNameTailSettings.DEFAULT_TAIL_COLOR }) { tailColor = it.take(19) }
                    InsetDivider()
                    WeightInputRow("尾字粗细", tailWeight) { tailWeight = it }
                    InsetDivider()
                    ColorPickerRow("括号颜色", "留空跟随昵称，渐变用 #F59E0B,#22C55E", bracketColor, onReset = { bracketColor = RealNameTailSettings.DEFAULT_BRACKET_COLOR }) { bracketColor = it.take(19) }
                    InsetDivider()
                    WeightInputRow("括号粗细", bracketWeight) { bracketWeight = it }
                    if (showGender) {
                        InsetDivider()
                        ColorPickerRow("性别颜色", "留空跟随昵称，支持渐变", genderColor, onReset = { genderColor = RealNameTailSettings.DEFAULT_GENDER_COLOR }) { genderColor = it.take(19) }
                        InsetDivider()
                        WeightInputRow("性别粗细", genderWeight) { genderWeight = it }
                    }
                    if (showRegion) {
                        InsetDivider()
                        ColorPickerRow("地区颜色", "留空跟随昵称，支持渐变", regionColor, onReset = { regionColor = RealNameTailSettings.DEFAULT_REGION_COLOR }) { regionColor = it.take(19) }
                        InsetDivider()
                        WeightInputRow("地区粗细", regionWeight) { regionWeight = it }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockContactPickerPage(
    context: Context,
    request: MessageBlockContactPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<MessageBlockContactOption>) -> Unit
) {
    var loading by remember(request) { mutableStateOf(true) }
    var error by remember(request) { mutableStateOf("") }
    var data by remember(request) { mutableStateOf(MessageBlockContactData()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedIds = remember(request) { parseIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    var contactFilter by remember(request) { mutableStateOf(MessageBlockContactFilter.ALL) }
    var selectedLabel by remember(request) { mutableStateOf<MessageBlockLabelOption?>(null) }
    var selectedConversationGroupId by remember(request) { mutableStateOf("") }
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadMessageBlockContacts(context) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取联系人失败"
            } else {
                data = result
            }
        }
    }
    LaunchedEffect(contactFilter) {
        if (contactFilter != MessageBlockContactFilter.LABELS) {
            selectedLabel = null
        }
        if (!request.allowOfficialAccounts && contactFilter == MessageBlockContactFilter.OFFICIALS) {
            contactFilter = MessageBlockContactFilter.ALL
        }
    }

    val currentLabel = selectedLabel
    val pageTitle = currentLabel?.let { "标签：${it.name}" } ?: request.title
    val availableContacts = data.contacts.filter {
        request.allowOfficialAccounts || it.kind != MessageBlockContactKind.OFFICIAL
    }
    val conversationGroupFilters = remember(availableContacts) {
        ConversationGroupPickerSupport.filters(context, availableContacts.map { it.contact.id })
    }
    val conversationGroupChoices = remember(conversationGroupFilters) {
        listOf(PopupChoice("全部会话", "")) + conversationGroupFilters.map {
            PopupChoice(it.name, it.id)
        }
    }
    LaunchedEffect(conversationGroupFilters) {
        if (selectedConversationGroupId.isNotBlank() &&
            conversationGroupFilters.none { it.id == selectedConversationGroupId }
        ) {
            selectedConversationGroupId = ""
        }
    }
    val selectedConversationIds = remember(conversationGroupFilters, selectedConversationGroupId) {
        conversationGroupFilters.firstOrNull { it.id == selectedConversationGroupId }?.conversationIds
    }
    val scopedContacts = remember(availableContacts, selectedConversationIds) {
        selectedConversationIds?.let { ids ->
            availableContacts.filter { it.contact.id in ids }
        } ?: availableContacts
    }
    val availableContactIds = scopedContacts.map { it.contact.id }.toSet()
    val bottomLower = query.trim().lowercase(Locale.US)
    val bottomVisibleContacts = scopedContacts.filter { row ->
        val contact = row.contact
        val typeMatched = when (contactFilter) {
            MessageBlockContactFilter.FRIENDS -> row.kind == MessageBlockContactKind.FRIEND
            MessageBlockContactFilter.GROUPS -> row.kind == MessageBlockContactKind.GROUP
            MessageBlockContactFilter.OFFICIALS -> row.kind == MessageBlockContactKind.OFFICIAL
            MessageBlockContactFilter.LABELS -> currentLabel?.contactIds?.contains(contact.id) == true
            MessageBlockContactFilter.ALL -> true
        }
        typeMatched && (
            contact.matchesSearch(bottomLower) ||
                row.labelNames.any { it.lowercase(Locale.US).contains(bottomLower) }
        )
    }
    val bottomShowingLabelList = contactFilter == MessageBlockContactFilter.LABELS && currentLabel == null
    val bottomVisibleContactIds = bottomVisibleContacts.map { it.contact.id }.toSet()
    val bottomVisibleAllSelected = bottomVisibleContactIds.isNotEmpty() && bottomVisibleContactIds.all { selectedIds.contains(it) }
    PageScaffold(
        title = pageTitle,
        largeTitle = pageTitle,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = {
                    val picked = availableContacts.filter { selectedIds.contains(it.contact.id) }
                    onConfirm(picked)
                },
                secondaryText = if (currentLabel == null) "返回" else "返回标签",
                onSecondaryClick = {
                    if (currentLabel == null) {
                        onBack()
                    } else {
                        selectedLabel = null
                        query = ""
                    }
                },
                middleText = if (!bottomShowingLabelList && bottomVisibleContactIds.isNotEmpty()) {
                    if (bottomVisibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (!bottomShowingLabelList && bottomVisibleContactIds.isNotEmpty()) {
                    {
                        selectedIds = if (bottomVisibleAllSelected) {
                            selectedIds - bottomVisibleContactIds
                        } else {
                            selectedIds + bottomVisibleContactIds
                        }
                    }
                } else {
                    null
                }
            )
        }
    ) { padding ->
        val listRoute = if (contactFilter == MessageBlockContactFilter.LABELS && currentLabel == null) {
            MessageBlockContactPickerRoute.Labels
        } else {
            MessageBlockContactPickerRoute.Contacts(contactFilter, currentLabel)
        }
        SettingsRouteTransition(
            targetState = listRoute,
            label = "MessageBlockContactPickerTransition",
            depthOf = { it.depth() },
            horizontalOnSameDepth = false
        ) { route ->
            val lower = query.trim().lowercase(Locale.US)
            val showingLabelList = route is MessageBlockContactPickerRoute.Labels
            val routeFilter = (route as? MessageBlockContactPickerRoute.Contacts)?.filter
                ?: MessageBlockContactFilter.LABELS
            val routeLabel = (route as? MessageBlockContactPickerRoute.Contacts)?.label
            val visibleLabels = data.labels.filter { label ->
                label.contactIds.any { availableContactIds.contains(it) } && (
                    lower.isEmpty() ||
                        label.name.lowercase(Locale.US).contains(lower) ||
                        label.id.lowercase(Locale.US).contains(lower)
                    )
            }.selectedFirst { label -> label.contactIds.any { initialSelectedIds.contains(it) } }
            val visibleContacts = scopedContacts.filter { row ->
                val contact = row.contact
                val typeMatched = when (routeFilter) {
                    MessageBlockContactFilter.FRIENDS -> row.kind == MessageBlockContactKind.FRIEND
                    MessageBlockContactFilter.GROUPS -> row.kind == MessageBlockContactKind.GROUP
                    MessageBlockContactFilter.OFFICIALS -> row.kind == MessageBlockContactKind.OFFICIAL
                    MessageBlockContactFilter.LABELS -> routeLabel?.contactIds?.contains(contact.id) == true
                    MessageBlockContactFilter.ALL -> true
                }
                typeMatched && (
                    contact.matchesSearch(lower) ||
                        row.labelNames.any { it.lowercase(Locale.US).contains(lower) }
                )
            }.selectedFirst { initialSelectedIds.contains(it.contact.id) }
            val visibleContactIds = visibleContacts.map { it.contact.id }.toSet()
            val sectionTitle = when {
                showingLabelList -> "标签"
                routeFilter == MessageBlockContactFilter.LABELS -> "标签成员"
                else -> routeFilter.title
            }
            val sectionCount = if (showingLabelList) visibleLabels.size else visibleContactIds.size
            val routeListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = routeListState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item {
                    SettingsCard {
                        MessageBlockContactFilterRow(
                            selected = contactFilter,
                            enableOfficials = request.allowOfficialAccounts,
                            onSelected = {
                                contactFilter = it
                                if (it == MessageBlockContactFilter.LABELS) {
                                    selectedLabel = null
                                }
                                query = ""
                            }
                        )
                    }
                }
                if (conversationGroupChoices.size > 1) {
                    item {
                        SettingsCard {
                            PopupChoiceRow(
                                title = "聊天分组",
                                summary = "只显示所选分组及其子分组中的会话",
                                options = conversationGroupChoices,
                                currentValue = selectedConversationGroupId,
                                onValueChanged = {
                                    selectedConversationGroupId = it
                                    query = ""
                                }
                            )
                        }
                    }
                }
                item {
                    SettingsCard {
                        InputRow(
                            title = "搜索",
                            summary = if (showingLabelList) "标签名称" else "昵称 / 群聊备注 / wxid",
                            value = query,
                            onValueChange = { query = it }
                        )
                    }
                }
                item {
                    PickerSectionHeader(
                        text = if (sectionCount > 0) "$sectionTitle · $sectionCount 项" else sectionTitle
                    )
                }
                when {
                    loading -> item { SettingsCard { EmptyText("正在载入列表...") } }
                    error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                    showingLabelList -> {
                        if (visibleLabels.isEmpty()) {
                            item { SettingsCard { EmptyText("没有匹配标签") } }
                        } else {
                            visibleLabels.forEach { label ->
                                item {
                                    MessageBlockLabelListCard(
                                        label = label,
                                        onClick = {
                                            selectedLabel = label
                                            query = ""
                                        }
                                    )
                                }
                            }
                        }
                    }
                    visibleContacts.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                    else -> visibleContacts.forEach { row ->
                        item {
                            val option = row.contact
                            MessageBlockContactListCard(
                                row = row,
                                selected = selectedIds.contains(option.id),
                                onClick = {
                                    selectedIds = if (selectedIds.contains(option.id)) {
                                        selectedIds - option.id
                                    } else {
                                        selectedIds + option.id
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ContactPickerPage(
    context: Context,
    request: ContactPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<ContactOption>) -> Unit
) {
    var loading by remember(request) { mutableStateOf(true) }
    var error by remember(request) { mutableStateOf("") }
    var contacts by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedIds = remember(request) { parseIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    val scopeSelectionEnabled = request.enableScopeSelection &&
        request.multiSelect &&
        request.mode == ContactPickerMode.FRIENDS
    var scopeTab by remember(request) { mutableStateOf(ContactPickerScopeTab.FRIENDS) }
    var selectedConversationGroupId by remember(request) { mutableStateOf("") }
    var contactFilter by remember(request) {
        mutableStateOf(
            if (request.mode == ContactPickerMode.FRIENDS && request.enableLabels) {
                ContactPickerFilter.FRIENDS
            } else {
                ContactPickerFilter.ALL
            }
        )
    }
    var selectedLabel by remember(request) { mutableStateOf("") }
    val groupLabels = remember(request) {
        if (request.multiSelect && request.enableGroupLabels && request.mode.supportsGroups()) {
            GroupChatLabelStore.load(context)
        } else {
            emptyList()
        }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadContacts(context, request.mode, request.enableLabels) { result, throwable ->
            loading = false
            if (throwable != null) {
                error = throwable.message ?: "读取联系人失败"
            } else {
                contacts = result
            }
        }
    }
    val conversationGroupFilters = remember(contacts) {
        ConversationGroupPickerSupport.filters(context, contacts.map { it.id })
    }
    val conversationGroupChoices = remember(conversationGroupFilters) {
        listOf(PopupChoice("全部会话", "")) + conversationGroupFilters.map {
            PopupChoice(it.name, it.id)
        }
    }
    LaunchedEffect(conversationGroupFilters) {
        if (selectedConversationGroupId.isNotBlank() &&
            conversationGroupFilters.none { it.id == selectedConversationGroupId }
        ) {
            selectedConversationGroupId = ""
        }
    }
    val selectedConversationIds = remember(conversationGroupFilters, selectedConversationGroupId) {
        conversationGroupFilters.firstOrNull { it.id == selectedConversationGroupId }?.conversationIds
    }
    val scopedContacts = remember(contacts, selectedConversationIds) {
        selectedConversationIds?.let { ids -> contacts.filter { it.id in ids } } ?: contacts
    }
    val labels = remember(scopedContacts) {
        scopedContacts.flatMap { it.labels }
            .distinct()
            .sortedWith(compareBy { it.lowercase(Locale.US) })
    }
    val contactLabelScopes = remember(contacts) {
        contacts.asSequence()
            .filter { !it.group && !it.official }
            .flatMap { contact -> contact.labels.asSequence().map { it to contact.id } }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .map { (name, ids) -> ContactPickerScope(name, ids.toSet()) }
            .sortedBy { it.name.lowercase(Locale.US) }
    }
    val availableGroupIds = remember(scopedContacts) {
        scopedContacts.filter { it.group }.map { it.id }.toSet()
    }
    val selectableGroupLabels = remember(groupLabels, availableGroupIds) {
        groupLabels.mapNotNull { label ->
            val ids = label.groupIds.intersect(availableGroupIds)
            label.copy(groupIds = ids).takeIf { ids.isNotEmpty() }
        }
    }
    val showGroupLabels = request.mode == ContactPickerMode.GROUPS ||
        contactFilter == ContactPickerFilter.GROUPS
    LaunchedEffect(contactFilter, labels) {
        if (contactFilter == ContactPickerFilter.LABELS) {
            if (selectedLabel.isBlank() || !labels.contains(selectedLabel)) {
                selectedLabel = labels.firstOrNull().orEmpty()
            }
        } else if (selectedLabel.isNotBlank()) {
            selectedLabel = ""
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val visibleContacts = remember(
        scopedContacts,
        contactFilter,
        selectedLabel,
        request.enableLabels,
        lower,
        initialSelectedIds
    ) {
        scopedContacts.asSequence().filter {
            when (contactFilter) {
                ContactPickerFilter.FRIENDS -> !it.group && !it.official
                ContactPickerFilter.GROUPS -> it.group
                ContactPickerFilter.OFFICIALS -> it.official
                ContactPickerFilter.LABELS -> request.enableLabels &&
                    selectedLabel.isNotBlank() &&
                    !it.group &&
                    !it.official &&
                    it.labels.contains(selectedLabel)
                ContactPickerFilter.ALL -> true
            }
        }.filter { it.matchesSearch(lower) }
            .toList()
            .selectedFirst { initialSelectedIds.contains(it.id) }
    }
    val visibleLabelScopes = remember(contactLabelScopes, lower, initialSelectedIds) {
        contactLabelScopes.filter {
            lower.isEmpty() || it.name.lowercase(Locale.US).contains(lower)
        }.selectedFirst { scope -> scope.memberIds.any(initialSelectedIds::contains) }
    }
    val visibleConversationGroupScopes = remember(conversationGroupFilters, lower, initialSelectedIds) {
        conversationGroupFilters.filter {
            lower.isEmpty() || it.name.lowercase(Locale.US).contains(lower)
        }.selectedFirst { scope -> scope.conversationIds.any(initialSelectedIds::contains) }
    }
    val visibleContactIds = remember(visibleContacts) {
        visibleContacts.mapTo(linkedSetOf()) { it.id }
    }
    val bottomVisibleIds = remember(
        scopeSelectionEnabled,
        scopeTab,
        visibleContactIds,
        visibleLabelScopes,
        visibleConversationGroupScopes
    ) {
        when {
            !scopeSelectionEnabled || scopeTab == ContactPickerScopeTab.FRIENDS -> visibleContactIds
            scopeTab == ContactPickerScopeTab.LABELS -> {
                visibleLabelScopes.flatMapTo(linkedSetOf()) { it.memberIds }
            }
            else -> {
                visibleConversationGroupScopes.flatMapTo(linkedSetOf()) { it.conversationIds }
            }
        }
    }
    val bottomVisibleAllSelected = bottomVisibleIds.isNotEmpty() && bottomVisibleIds.all { selectedIds.contains(it) }

    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = if (request.multiSelect) "保存选择" else request.singleConfirmText,
                onPrimaryClick = {
                    val picked = contacts.filter { selectedIds.contains(it.id) }
                    if (picked.isEmpty() && !request.multiSelect) {
                        Toast.makeText(context, "请选择联系人", Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirm(picked)
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (request.multiSelect && bottomVisibleIds.isNotEmpty()) {
                    if (bottomVisibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (request.multiSelect && bottomVisibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (bottomVisibleAllSelected) {
                            selectedIds - bottomVisibleIds
                        } else {
                            selectedIds + bottomVisibleIds
                        }
                    }
                } else {
                    null
                }
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
            if (scopeSelectionEnabled) {
                item {
                    SettingsCard {
                        ContactPickerScopeFilterRow(
                            selected = scopeTab,
                            onSelected = {
                                scopeTab = it
                                contactFilter = ContactPickerFilter.FRIENDS
                                query = ""
                            }
                        )
                    }
                }
            } else if (request.mode == ContactPickerMode.FRIENDS && request.enableLabels) {
                item {
                    SettingsCard {
                        FriendContactFilterRow(
                            selected = contactFilter,
                            onSelected = {
                                contactFilter = it
                                query = ""
                            }
                        )
                    }
                }
            } else if (request.mode == ContactPickerMode.BOTH || request.mode == ContactPickerMode.ALL_CHATS) {
                item {
                    SettingsCard {
                        ContactFilterRow(
                            selected = contactFilter,
                            enableLabels = request.enableLabels,
                            enableOfficials = request.mode == ContactPickerMode.ALL_CHATS,
                            onSelected = {
                                contactFilter = it
                                query = ""
                            }
                        )
                    }
                }
            }
            if (!scopeSelectionEnabled && request.enableLabels && contactFilter == ContactPickerFilter.LABELS) {
                item {
                    SettingsCard {
                        ContactLabelFilterRow(
                            labels = labels,
                            selected = selectedLabel,
                            onSelected = {
                                selectedLabel = it
                                query = ""
                            }
                        )
                    }
                }
            }
            if (!scopeSelectionEnabled && conversationGroupChoices.size > 1) {
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "聊天分组",
                            summary = "只显示所选分组及其子分组中的会话",
                            options = conversationGroupChoices,
                            currentValue = selectedConversationGroupId,
                            onValueChanged = {
                                selectedConversationGroupId = it
                                query = ""
                            }
                        )
                    }
                }
            }
            if (showGroupLabels && selectableGroupLabels.isNotEmpty()) {
                item { PickerSectionHeader(text = "群聊标签") }
                item {
                    SettingsCard {
                        selectableGroupLabels.forEachIndexed { index, label ->
                            val selectedCount = label.groupIds.count { selectedIds.contains(it) }
                            GroupChatLabelPickerRow(
                                label = label,
                                selectedCount = selectedCount,
                                onClick = {
                                    selectedIds = if (selectedCount == label.groupIds.size) {
                                        selectedIds - label.groupIds
                                    } else {
                                        selectedIds + label.groupIds
                                    }
                                }
                            )
                            if (index < selectableGroupLabels.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item {
                SettingsCard {
                    InputRow(
                        title = "搜索",
                        summary = when {
                            scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> "好友标签名称"
                            scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> "聊天分组名称"
                            else -> "昵称 / 群聊备注 / wxid"
                        },
                        value = query,
                        onValueChange = { query = it }
                    )
                }
            }
            item {
                PickerSectionHeader(
                    text = when {
                        scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> {
                            if (visibleLabelScopes.isEmpty()) "好友标签" else "好友标签 · ${visibleLabelScopes.size} 项"
                        }
                        scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> {
                            if (visibleConversationGroupScopes.isEmpty()) "聊天分组" else "聊天分组 · ${visibleConversationGroupScopes.size} 项"
                        }
                        visibleContacts.isNotEmpty() -> "${contactFilter.title} · ${visibleContacts.size} 项"
                        else -> contactFilter.title
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入列表...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.LABELS -> {
                    if (visibleLabelScopes.isEmpty()) {
                        item { SettingsCard { EmptyText("没有匹配的好友标签") } }
                    } else {
                        item {
                            SettingsCard {
                                visibleLabelScopes.forEachIndexed { index, scope ->
                                    val selectedCount = scope.memberIds.count(selectedIds::contains)
                                    ContactPickerScopeRow(
                                        title = scope.name,
                                        memberCount = scope.memberIds.size,
                                        selectedCount = selectedCount,
                                        memberUnit = "位好友",
                                        onClick = {
                                            selectedIds = if (selectedCount == scope.memberIds.size) {
                                                selectedIds - scope.memberIds
                                            } else {
                                                selectedIds + scope.memberIds
                                            }
                                        }
                                    )
                                    if (index < visibleLabelScopes.lastIndex) InsetDivider()
                                }
                            }
                        }
                    }
                }
                scopeSelectionEnabled && scopeTab == ContactPickerScopeTab.CONVERSATION_GROUPS -> {
                    if (visibleConversationGroupScopes.isEmpty()) {
                        item { SettingsCard { EmptyText("没有匹配的聊天分组") } }
                    } else {
                        item {
                            SettingsCard {
                                visibleConversationGroupScopes.forEachIndexed { index, scope ->
                                    val selectedCount = scope.conversationIds.count(selectedIds::contains)
                                    ContactPickerScopeRow(
                                        title = scope.name,
                                        memberCount = scope.conversationIds.size,
                                        selectedCount = selectedCount,
                                        memberUnit = "位好友",
                                        onClick = {
                                            selectedIds = if (selectedCount == scope.conversationIds.size) {
                                                selectedIds - scope.conversationIds
                                            } else {
                                                selectedIds + scope.conversationIds
                                            }
                                        }
                                    )
                                    if (index < visibleConversationGroupScopes.lastIndex) InsetDivider()
                                }
                            }
                        }
                    }
                }
                visibleContacts.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                else -> items(items = visibleContacts, key = { it.id }) { option ->
                    ContactListCard(
                        option = option,
                        selected = selectedIds.contains(option.id),
                        multiSelect = request.multiSelect,
                        onClick = {
                            selectedIds = if (request.multiSelect) {
                                if (selectedIds.contains(option.id)) {
                                    selectedIds - option.id
                                } else {
                                    selectedIds + option.id
                                }
                            } else {
                                setOf(option.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FavoritePickerPage(
    request: FavoritePickerRequest,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val favoriteApi = WeChatApis.media()?.favorites()
    val initialCache = remember(request) { favoriteApi?.cachedList() }
    var loading by remember(request) { mutableStateOf(initialCache?.items.isNullOrEmpty()) }
    var error by remember(request) { mutableStateOf("") }
    var favorites by remember(request) { mutableStateOf(initialCache?.items.orEmpty()) }
    var backgroundLoading by remember(request) { mutableStateOf(false) }
    var query by remember(request) { mutableStateOf("") }
    var filter by remember(request) { mutableStateOf(FavoritePickerFilter.ALL) }
    var preview by remember(request) { mutableStateOf<FavoriteMediaPreview?>(null) }
    val initialSelectedIds = remember(request) { parseFavoriteIds(request.existingValue) }
    var selectedIds by remember(request) { mutableStateOf(initialSelectedIds) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loading = favorites.isEmpty()
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching {
                val api = favoriteApi
                    ?: throw IllegalStateException("收藏 API 未就绪")
                if (!api.canList()) {
                    throw IllegalStateException("收藏列表不可用")
                }
                var page = h.Hchat.hooks.api.media.WeChatFavoriteApi.FavoritePage(emptyList(), false)
                for (attempt in 0..2) {
                    page = api.openCachedList()
                    if (page.items.isNotEmpty() || attempt == 2) break
                    delay(350L)
                }
                page
            }
        }
        loading = false
        result.onSuccess {
            favorites = it.items
            if (it.hasMore) {
                backgroundLoading = true
                runCatching {
                    while (true) {
                        val page = withContext(Dispatchers.IO) {
                            favoriteApi?.loadNextPage()
                                ?: throw IllegalStateException("收藏 API 未就绪")
                        }
                        favorites = page.items
                        if (!page.hasMore) break
                        delay(FAVORITE_BACKGROUND_BATCH_DELAY_MS)
                    }
                }.onFailure {
                    error = it.message ?: "后台读取收藏失败"
                }
                backgroundLoading = false
            }
        }
            .onFailure {
                error = it.message ?: "读取收藏失败"
                if (favorites.isEmpty()) favorites = emptyList()
                backgroundLoading = false
            }
    }

    val lower = query.trim().lowercase(Locale.US)
    val visible = favorites.filter { item ->
        filter.matches(item.type) && (
            lower.isEmpty() ||
                item.displayTitle().lowercase(Locale.US).contains(lower) ||
                item.displaySummary().lowercase(Locale.US).contains(lower) ||
                item.tags.any { it.lowercase(Locale.US).contains(lower) }
            )
    }.selectedFirst { initialSelectedIds.contains(it.localId.toString()) }
    val visibleIds = visible.map { it.localId.toString() }.toSet()
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all { selectedIds.contains(it) }
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = {
                    if (!request.multiSelect && selectedIds.isEmpty()) {
                        Toast.makeText(context, "请选择收藏", Toast.LENGTH_SHORT).show()
                    } else {
                        request.onValue(formatFavoriteIds(selectedIds, request.delimiter, request.multiSelect))
                        onBack()
                    }
                },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (request.multiSelect && visibleIds.isNotEmpty()) {
                    if (visibleAllSelected) "取消全选" else "全选"
                } else {
                    null
                },
                onMiddleClick = if (request.multiSelect && visibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (visibleAllSelected) {
                            selectedIds - visibleIds
                        } else {
                            selectedIds + visibleIds
                        }
                    }
                } else {
                    null
                }
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
            item {
                SettingsCard {
                    InputRow("搜索", "标题 / 来源 / 标签", query) { query = it }
                    FavoritePickerFilterRow(selected = filter, onSelected = { filter = it })
                }
            }
            item {
                PickerSectionHeader(
                    text = if (backgroundLoading) {
                        "${filter.label}收藏 · ${visible.size} 项 · 后台加载中（${favorites.size}）"
                    } else {
                        "${filter.label}收藏 · ${visible.size} / ${favorites.size} 项"
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入收藏...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() && !backgroundLoading -> item {
                    SettingsCard {
                        EmptyText(if (query.isBlank()) "没有可选收藏" else "没有匹配收藏")
                    }
                }
                visible.isEmpty() -> item { SettingsCard { EmptyText("正在后台查找收藏...") } }
                else -> visible.forEach { favorite ->
                    item {
                        val id = favorite.localId.toString()
                        FavoriteListCard(
                            item = favorite,
                            selected = selectedIds.contains(id),
                            multiSelect = request.multiSelect,
                            onPreview = { path -> preview = FavoriteMediaPreview(favorite, path) },
                            onClick = {
                                selectedIds = if (request.multiSelect) {
                                    if (selectedIds.contains(id)) selectedIds - id else selectedIds + id
                                } else {
                                    setOf(id)
                                }
                            }
                        )
                    }
                }
            }
            if (backgroundLoading && visible.isNotEmpty()) {
                item { SettingsCard { EmptyText("正在后台加载更多收藏...") } }
            }
        }
    }
    preview?.let { media ->
        FavoriteMediaPreviewDialog(media = media, onClose = { preview = null })
    }
}

@Composable
internal fun GroupMemberPickerPage(
    context: Context,
    request: GroupMemberPickerRequest,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var groupLoading by remember(request) { mutableStateOf(true) }
    var groupError by remember(request) { mutableStateOf("") }
    var groups by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var selectedGroup by remember(request) { mutableStateOf<ContactOption?>(null) }
    var memberLoading by remember(request) { mutableStateOf(false) }
    var memberError by remember(request) { mutableStateOf("") }
    var members by remember(request) { mutableStateOf(emptyList<ContactOption>()) }
    var query by remember(request) { mutableStateOf("") }
    val initialSelectedEntries = remember(request) {
        parseGroupMemberEntries(request.existingValue).filterTo(linkedSetOf()) { entry ->
            request.allowedGroupIds?.let { entry.substringBefore('/') in it } != false
        }
    }
    var selectedEntries by remember(request) { mutableStateOf<Set<String>>(initialSelectedEntries) }
    var selectionEdited by remember(request) { mutableStateOf(initialSelectedEntries.isNotEmpty()) }
    val groupListState = rememberLazyListState()
    val memberListState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(request) {
        loadContacts(context, ContactPickerMode.GROUPS) { result, throwable ->
            groupLoading = false
            if (throwable != null) {
                groupError = throwable.message ?: "读取群聊失败"
            } else {
                groups = request.allowedGroupIds?.let { allowed ->
                    result.filter { it.id in allowed }
                } ?: result
            }
        }
    }
    LaunchedEffect(selectedGroup?.id) {
        val group = selectedGroup
        if (group == null) {
            members = emptyList()
            memberError = ""
            memberLoading = false
            return@LaunchedEffect
        }
        memberLoading = true
        memberError = ""
        members = emptyList()
        query = ""
        memberListState.scrollToItem(0)
        loadGroupMembers(context, group) { result, throwable ->
            memberLoading = false
            if (throwable != null) {
                memberError = throwable.message ?: "读取群成员失败"
            } else {
                members = result
            }
        }
    }

    val group = selectedGroup
    PageScaffold(
        title = group?.label ?: request.title,
        largeTitle = group?.label ?: request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = if (group == null && selectedEntries.isEmpty() && !selectionEdited) "选择群聊" else "保存选择",
                onPrimaryClick = {
                    if (selectedGroup == null && selectedEntries.isEmpty() && !selectionEdited) {
                        Toast.makeText(context, "请先选择群聊", Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirm(selectedEntries.toList())
                    }
                },
                secondaryText = if (group == null) "返回" else "重新选群",
                onSecondaryClick = {
                    if (selectedGroup == null) {
                        onBack()
                    } else {
                        selectedGroup = null
                        query = ""
                    }
                },
                middleText = if (selectedEntries.isEmpty()) null else "清空选择",
                onMiddleClick = if (selectedEntries.isEmpty()) null else {
                    {
                        selectedEntries = emptySet()
                        selectionEdited = true
                    }
                }
            )
        }
    ) { padding ->
        val listRoute = group?.let { GroupMemberPickerRoute.Members(it) } ?: GroupMemberPickerRoute.Groups
        SettingsRouteTransition(
            targetState = listRoute,
            label = "GroupMemberPickerTransition",
            depthOf = { it.depth() }
        ) { route ->
            val routeGroup = (route as? GroupMemberPickerRoute.Members)?.group
            val lower = query.trim().lowercase(Locale.US)
            val rows = if (routeGroup == null) groups else members
            val visible = rows.filter {
                it.matchesSearch(lower)
            }.selectedFirst { option ->
                if (routeGroup == null) {
                    initialSelectedEntries.any { it.substringBefore('/') == option.id }
                } else {
                    initialSelectedEntries.contains(groupMemberEntry(routeGroup.id, option.id))
                }
            }
            val routeListState = if (routeGroup == null) groupListState else memberListState
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                state = routeListState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 84.dp
                )
            ) {
                item {
                    SettingsCard {
                        InputRow(
                            title = "搜索",
                            summary = if (routeGroup == null) "群名称 / 群聊备注 / 群号" else "群昵称 / wxid",
                            value = query,
                            onValueChange = { query = it }
                        )
                    }
                }
                item {
                    SmallTitle(
                        modifier = Modifier.padding(top = 10.dp),
                        text = if (routeGroup == null) "群聊" else "群成员"
                    )
                }
                when {
                    routeGroup == null && groupLoading -> item { SettingsCard { EmptyText("正在载入群聊...") } }
                    routeGroup == null && groupError.isNotEmpty() -> item { SettingsCard { EmptyText(groupError) } }
                    routeGroup != null && memberLoading -> item { SettingsCard { EmptyText("正在载入群成员...") } }
                    routeGroup != null && memberError.isNotEmpty() -> item { SettingsCard { EmptyText(memberError) } }
                    visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                    else -> visible.forEach { option ->
                        item {
                            val entry = routeGroup?.let { groupMemberEntry(it.id, option.id) }.orEmpty()
                            ContactListCard(
                                option = option,
                                selected = if (routeGroup == null) false else selectedEntries.contains(entry),
                                multiSelect = routeGroup != null,
                                onClick = {
                                    if (routeGroup == null) {
                                        selectedGroup = option
                                    } else if (selectedEntries.contains(entry)) {
                                        selectedEntries = selectedEntries - entry
                                        selectionEdited = true
                                    } else {
                                        selectedEntries = selectedEntries + entry
                                        selectionEdited = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun OptionPickerPage(
    request: OptionPickerRequest,
    onBack: () -> Unit,
    onSelected: (OptionItem) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    var selectedValue by remember(request) { mutableStateOf(request.currentValue) }
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "选项") }
            item {
                SettingsCard {
                    request.options.forEachIndexed { index, item ->
                        OptionChoiceRow(
                            item = item,
                            selected = item.value == selectedValue,
                            onClick = {
                                selectedValue = item.value
                                onSelected(item)
                            }
                        )
                        if (index < request.options.lastIndex) InsetDivider()
                    }
                }
            }
        }
    }
}

@Composable
internal fun UnsupportedFeaturePage(provider: FeatureSettingsProvider, onBack: () -> Unit) {
    val scrollBehavior = MiuixScrollBehavior()
    val listState = rememberLazyListState()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar("返回", onBack)
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
            item {
                SettingsCard {
                    EmptyText("这个模块暂时没有可配置项")
                }
            }
        }
    }
}

@Composable
internal fun PageScaffold(
    title: String,
    largeTitle: String,
    scrollBehavior: ScrollBehavior,
    onBack: (() -> Unit)? = null,
    topBarActions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable ((Backdrop) -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    RegisterSettingsBackHandler(onBack)
    val graphicsLayer = rememberGraphicsLayer()
    val backdrop = rememberLayerBackdrop(graphicsLayer)
    val navigationInset = navigationButtonBottomInset()
    val navigationGap = if (navigationInset > 0.dp) NAVIGATION_BUTTON_EXTRA_GAP else 0.dp
    val bottomAvoidance = navigationInset + navigationGap
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
        ) {
            top.yukonga.miuix.kmp.basic.Scaffold(
                topBar = {
                    TopAppBar(
                        title = title,
                        largeTitle = largeTitle,
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            onBack?.let { back ->
                                Box(
                                    modifier = Modifier.size(40.dp).responsiveTap(onClick = back),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = NavIcons.Back,
                                        contentDescription = "返回",
                                        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        },
                        actions = topBarActions,
                        defaultWindowInsetsPadding = true
                    )
                },
                bottomBar = {},
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                content = { padding ->
                    content(
                        PaddingValues(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding() + bottomAvoidance
                        )
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomAvoidance)
                .align(Alignment.BottomCenter)
        ) {
            bottomBar?.invoke(backdrop)
        }
    }
}

@Composable
internal fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        // 垂直 6dp 让相邻卡片之间自然留出 12dp 分组间距：之前是 0，
        // 连续两个卡片会紧贴在一起，看起来像一张卡被切开。
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        cornerRadius = 18.dp
    ) {
        content()
    }
}

@Composable
internal fun SwitchRow(
    sp: SharedPreferences,
    key: String,
    title: String,
    summary: String,
    defaultValue: Boolean
) {
    var checked by remember { mutableStateOf(sp.getBoolean(key, defaultValue)) }
    // 用组件库的 SwitchPreference：行内边距、标题/摘要字号、点击波纹都由库统一，
    // 避免各页面手写过一遍导致细节不一致。
    SwitchPreference(
        checked = checked,
        onCheckedChange = {
            checked = it
            sp.edit().putBoolean(key, it).apply()
        },
        title = title,
        summary = summary.takeIf { it.isNotBlank() }
    )
}

@Composable
internal fun SwitchRow(
    checked: Boolean,
    title: String,
    summary: String,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    SwitchPreference(
        checked = checked,
        onCheckedChange = { if (enabled) onCheckedChange(it) },
        title = title,
        summary = summary.takeIf { it.isNotBlank() },
        enabled = enabled
    )
}

@Composable
internal fun OptionRow(
    sp: SharedPreferences,
    key: String,
    title: String,
    options: List<OptionItem>,
    defaultValue: Int,
    onValueChanged: (Int) -> Unit = {}
) {
    var currentValue by remember {
        mutableStateOf(sp.getInt(key, defaultValue))
    }
    PopupOptionRow(
        title = title,
        summary = options.firstOrNull { it.value == currentValue }?.label
            ?: options.firstOrNull { it.value == defaultValue }?.label
            ?: "",
        options = options,
        currentValue = currentValue,
        onValueChanged = {
            currentValue = it
            onValueChanged(it)
            sp.edit().putInt(key, it).apply()
        }
    )
}

@Composable
internal fun PopupOptionRow(
    title: String,
    summary: String,
    options: List<OptionItem>,
    currentValue: Int,
    onValueChanged: (Int) -> Unit,
    enabled: Boolean = true
) {
    val selectedIndex = options.indexOfFirst { it.value == currentValue }.takeIf { it >= 0 } ?: 0
    val labels = options.map { it.label }
    WindowDropdownMenu(
        entry = DropdownEntry(
            items = labels.mapIndexed { index, label ->
                DropdownItem(
                    text = label,
                    selected = index == selectedIndex,
                    onClick = { options.getOrNull(index)?.let { onValueChanged(it.value) } }
                )
            }
        ),
        title = title,
        summary = summary,
        enabled = enabled,
        collapseOnSelection = true
    )
}

@Composable
internal fun InputRow(
    title: String,
    summary: String,
    value: String,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = MiuixTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            minLines = minLines,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
internal fun PopupChoiceRow(
    title: String,
    summary: String,
    options: List<PopupChoice<String>>,
    currentValue: String,
    onValueChanged: (String) -> Unit,
    enabled: Boolean = true
) {
    val selectedIndex = options.indexOfFirst { it.value == currentValue }.takeIf { it >= 0 } ?: 0
    WindowDropdownMenu(
        entry = DropdownEntry(
            items = options.mapIndexed { index, option ->
                DropdownItem(
                    text = option.label,
                    selected = index == selectedIndex,
                    onClick = { onValueChanged(option.value) }
                )
            }
        ),
        title = title,
        summary = summary,
        enabled = enabled,
        collapseOnSelection = true
    )
}

@Composable
internal fun ColorPickerRow(
    title: String,
    summary: String,
    value: String,
    allowGradient: Boolean = true,
    onReset: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentValue by rememberUpdatedState(value)
    val colorParts = if (allowGradient) {
        colorSpecParts(value)
    } else {
        MemberTitleStore.cleanColor(value) to ""
    }
    var editEnd by remember { mutableStateOf(colorParts.second.isNotEmpty()) }
    val selectedColor = if (allowGradient && editEnd) colorParts.second.ifEmpty { colorParts.first } else colorParts.first
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        color = MiuixTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    ClickHintTag()
                }
                Text(
                    text = if (allowGradient) {
                        "$summary，支持 #RRGGBB / #AARRGGBB / #A,#B 渐变"
                    } else {
                        "$summary，支持 #RRGGBB / #AARRGGBB"
                    },
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp
                )
            }
            ColorPreviewDot(if (allowGradient) value else colorParts.first)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = { onValueChange(it.take(if (allowGradient) 19 else 9)) },
                textStyle = TextStyle(
                    color = MiuixTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MiuixTheme.colorScheme.secondaryVariant)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            )
            if (onReset != null) {
                Text(
                    text = "重置",
                    color = MiuixTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onReset)
                        .background(MiuixTheme.colorScheme.secondaryVariant)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }
        if (expanded) {
            if (allowGradient) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColorSpecChip(
                        label = "起始色",
                        value = colorParts.first,
                        selected = !editEnd,
                        onClick = { editEnd = false },
                        modifier = Modifier.weight(1f)
                    )
                    ColorSpecChip(
                        label = "结束色",
                        value = colorParts.second,
                        selected = editEnd,
                        onClick = { editEnd = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            PsColorPicker(
                value = selectedColor,
                onValueChange = { picked ->
                    val next = if (allowGradient) {
                        val latestParts = colorSpecParts(currentValue)
                        if (editEnd) {
                            composeColorSpec(latestParts.first.ifEmpty { picked }, picked)
                        } else {
                            composeColorSpec(picked, latestParts.second)
                        }
                    } else {
                        picked
                    }
                    onValueChange(next)
                },
                modifier = Modifier.padding(top = if (allowGradient) 12.dp else 8.dp)
            )
            Text(
                text = if (allowGradient) {
                    "先选起始色或结束色，再用色盘取色；清空输入框可恢复默认/跟随昵称"
                } else {
                    "可直接输入颜色值，也可以用色盘取色；清空输入框可恢复默认色"
                },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
internal fun ColorSpecChip(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.outline.copy(alpha = 0.45f)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorPreviewDot(value, size = 24.dp)
        Column {
            Text(text = label, color = MiuixTheme.colorScheme.onSurface, fontSize = 12.sp)
            Text(
                text = value.ifEmpty { "未设置" },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
internal fun ColorPreviewDot(value: String, size: Dp = 34.dp) {
    val colorParts = colorSpecParts(value)
    val startColor = composeColorFromHex(colorParts.first) ?: MiuixTheme.colorScheme.secondaryVariant
    val endColor = composeColorFromHex(colorParts.second)
    val shape = RoundedCornerShape(size / 2)
    val colorBackground = if (endColor != null && endColor != startColor) {
        Modifier.background(Brush.horizontalGradient(listOf(startColor, endColor)))
    } else {
        Modifier.background(startColor)
    }
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .then(colorBackground)
            .border(2.dp, MiuixTheme.colorScheme.outline.copy(alpha = 0.45f), shape),
        contentAlignment = Alignment.Center
    ) {
        if (value.isEmpty()) {
            Text(text = "-", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 15.sp)
        }
    }
}

@Composable
internal fun PsColorPicker(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = colorPickerSelection(value)
    val markerColor = MiuixTheme.colorScheme.onSurface
    val density = LocalDensity.current
    val paletteSize = 228.dp
    val hueBarWidth = 40.dp
    val paletteSizePx = remember(density) { with(density) { paletteSize.roundToPx() } }
    val hueBarWidthPx = remember(density) { with(density) { hueBarWidth.roundToPx() } }
    val paletteBitmap = remember(selected.hue, paletteSizePx) {
        buildSvPaletteBitmap(selected.hue, paletteSizePx)
    }
    val hueBitmap = remember(paletteSizePx, hueBarWidthPx) {
        buildHuePaletteBitmap(hueBarWidthPx, paletteSizePx)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(paletteSize)
                .pointerInput(selected.hue) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        onValueChange(
                            colorFromSvOffset(
                                selected.hue,
                                down.position,
                                size.width.toFloat(),
                                size.height.toFloat()
                            )
                        )
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            onValueChange(
                                colorFromSvOffset(
                                    selected.hue,
                                    change.position,
                                    size.width.toFloat(),
                                    size.height.toFloat()
                                )
                            )
                            change.consume()
                            if (!change.pressed) break
                        }
                    }
                }
        ) {
            drawImage(paletteBitmap)
            val marker = Offset(
                selected.saturation * size.width,
                (1f - selected.value) * size.height
            )
            drawCircle(color = markerColor, radius = 9f, center = marker)
            drawCircle(color = Color.White, radius = 5.5f, center = marker)
        }
        Canvas(
            modifier = Modifier
                .padding(start = 14.dp)
                .size(width = hueBarWidth, height = paletteSize)
                .clip(RoundedCornerShape(14.dp))
                .pointerInput(selected.saturation, selected.value) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        onValueChange(
                            colorFromHueOffset(
                                selected.saturation,
                                selected.value,
                                down.position.y,
                                size.height.toFloat()
                            )
                        )
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            onValueChange(
                                colorFromHueOffset(
                                    selected.saturation,
                                    selected.value,
                                    change.position.y,
                                    size.height.toFloat()
                                )
                            )
                            change.consume()
                            if (!change.pressed) break
                        }
                    }
                }
        ) {
            drawImage(hueBitmap)
            val markerY = (selected.hue / 360f).coerceIn(0f, 1f) * size.height
            drawCircle(color = markerColor, radius = 10f, center = Offset(size.width / 2f, markerY))
            drawCircle(color = Color.White, radius = 6f, center = Offset(size.width / 2f, markerY))
        }
    }
}

