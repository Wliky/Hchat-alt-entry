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
internal fun SnsAntiRecallMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, SnsAntiRecallSettings.PREFS_NAME) }
    var momentMarkText by remember {
        mutableStateOf(
            sp.getString(
                SnsAntiRecallSettings.KEY_CUSTOM_MARK_TEXT,
                SnsAntiRecallSettings.DEFAULT_CUSTOM_MARK_TEXT
            ).orEmpty()
        )
    }
    var commentMarkText by remember {
        mutableStateOf(
            sp.getString(
                SnsAntiRecallSettings.KEY_COMMENT_CUSTOM_MARK_TEXT,
                SnsAntiRecallSettings.DEFAULT_COMMENT_CUSTOM_MARK_TEXT
            ).orEmpty()
        )
    }
    var momentMarkEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SnsAntiRecallSettings.KEY_CUSTOM_MARK_ENABLE,
                SnsAntiRecallSettings.DEFAULT_CUSTOM_MARK_ENABLE
            )
        )
    }
    var commentMarkEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SnsAntiRecallSettings.KEY_COMMENT_CUSTOM_MARK_ENABLE,
                SnsAntiRecallSettings.DEFAULT_COMMENT_CUSTOM_MARK_ENABLE
            )
        )
    }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "朋友圈") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SnsAntiRecallSettings.KEY_ENABLE,
                        "朋友圈防撤回",
                        "已缓存的朋友圈在对方删除或限制可见范围后继续显示",
                        SnsAntiRecallSettings.DEFAULT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = momentMarkEnabled,
                        title = "自定义朋友圈撤回提示",
                        summary = "开启后可自定义朋友圈正文被删除时的提示文案"
                    ) { checked ->
                        momentMarkEnabled = checked
                        sp.edit().putBoolean(SnsAntiRecallSettings.KEY_CUSTOM_MARK_ENABLE, checked).apply()
                    }
                    if (momentMarkEnabled) {
                        InsetDivider()
                        InputRow("朋友圈提示文案", "默认 [已删除]", momentMarkText) { momentMarkText = it }
                    }
                    InsetDivider()
                    SwitchRow(
                        sp,
                        SnsAntiRecallSettings.KEY_COMMENT_ENABLE,
                        "朋友圈评论防撤回",
                        "已缓存的朋友圈评论被删除后继续显示，并标记已删除",
                        SnsAntiRecallSettings.DEFAULT_COMMENT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = commentMarkEnabled,
                        title = "自定义评论撤回提示",
                        summary = "开启后可自定义朋友圈评论被删除时的提示文案"
                    ) { checked ->
                        commentMarkEnabled = checked
                        sp.edit().putBoolean(SnsAntiRecallSettings.KEY_COMMENT_CUSTOM_MARK_ENABLE, checked).apply()
                    }
                    if (commentMarkEnabled) {
                        InsetDivider()
                        InputRow("评论提示文案", "默认 [已删除]", commentMarkText) { commentMarkText = it }
                    }
                    InsetDivider()
                    SwitchRow(
                        sp,
                        SnsAntiRecallSettings.KEY_FORCE_LEGACY_PROFILE,
                        "强制旧版个人主页朋友圈",
                        "Flutter 个人主页看不到已删除朋友圈时，改用微信旧版 SnsUserUI",
                        SnsAntiRecallSettings.DEFAULT_FORCE_LEGACY_PROFILE
                    )
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            sp.edit()
                .putString(
                    SnsAntiRecallSettings.KEY_CUSTOM_MARK_TEXT,
                    momentMarkText.ifBlank { SnsAntiRecallSettings.DEFAULT_CUSTOM_MARK_TEXT }
                )
                .putString(
                    SnsAntiRecallSettings.KEY_COMMENT_CUSTOM_MARK_TEXT,
                    commentMarkText.ifBlank { SnsAntiRecallSettings.DEFAULT_COMMENT_CUSTOM_MARK_TEXT }
                )
                .apply()
        }
    }
}

@Composable
internal fun MomentsFakeInteractionMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember {
        HchatStorage.preferences(context, MomentsFakeInteractionSettings.PREFS_NAME)
    }
    val isLike = provider.featureId() == MomentsFakeLikeSettingsProvider.ID
    val isForward = provider.featureId() == MomentsFakeForwardSettingsProvider.ID
    val enableKey = when {
        isLike -> MomentsFakeInteractionSettings.KEY_FAKE_LIKE_ENABLE
        isForward -> MomentsFakeInteractionSettings.KEY_FAKE_FORWARD_ENABLE
        else -> MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_ENABLE
    }
    val defaultEnabled = when {
        isLike -> MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_ENABLE
        isForward -> MomentsFakeInteractionSettings.DEFAULT_FAKE_FORWARD_ENABLE
        else -> MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_ENABLE
    }
    val pendingRestoreKey = if (isLike) {
        MomentsFakeInteractionSettings.KEY_PENDING_RESTORE_LIKES
    } else {
        MomentsFakeInteractionSettings.KEY_PENDING_RESTORE_COMMENTS
    }
    val useNonFriendsKey = if (isLike) {
        MomentsFakeInteractionSettings.KEY_FAKE_LIKE_USE_NON_FRIENDS
    } else {
        MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_USE_NON_FRIENDS
    }
    val defaultUseNonFriends = if (isLike) {
        MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_USE_NON_FRIENDS
    } else {
        MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_USE_NON_FRIENDS
    }
    var enabled by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(enableKey, defaultEnabled)
        )
    }
    var useNonFriends by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(useNonFriendsKey, defaultUseNonFriends)
        )
    }
    var randomCommentContent by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(
                MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_RANDOM_CONTENT,
                MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_RANDOM_CONTENT
            )
        )
    }
    var commentContents by remember(provider.featureId()) {
        mutableStateOf(MomentsFakeInteractionSettings.commentContents(sp))
    }
    var randomOrder by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(
                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_RANDOM_ORDER,
                MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_RANDOM_ORDER
            )
        )
    }
    var autoSelect by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(
                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT,
                MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_AUTO_SELECT
            )
        )
    }
    var autoSelectCount by remember(provider.featureId()) {
        mutableStateOf(
            sp.getInt(
                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT_COUNT,
                MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_AUTO_SELECT_COUNT
            ).toString()
        )
    }
    var excludedIds by remember(provider.featureId()) {
        mutableStateOf(
            sp.getStringSet(
                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_EXCLUDED_IDS,
                emptySet()
            ).orEmpty().toSet()
        )
    }
    val menuTextKey = when {
        isLike -> MomentsFakeInteractionSettings.KEY_FAKE_LIKE_MENU_TEXT
        isForward -> MomentsFakeInteractionSettings.KEY_FAKE_FORWARD_MENU_TEXT
        else -> MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_MENU_TEXT
    }
    val defaultMenuText = when {
        isLike -> MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_MENU_TEXT
        isForward -> MomentsFakeInteractionSettings.DEFAULT_FAKE_FORWARD_MENU_TEXT
        else -> MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_MENU_TEXT
    }
    val hideMenuKey = when {
        isLike -> MomentsFakeInteractionSettings.KEY_FAKE_LIKE_HIDE_MENU
        isForward -> MomentsFakeInteractionSettings.KEY_FAKE_FORWARD_HIDE_MENU
        else -> MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_HIDE_MENU
    }
    val defaultHideMenu = when {
        isLike -> MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_HIDE_MENU
        isForward -> MomentsFakeInteractionSettings.DEFAULT_FAKE_FORWARD_HIDE_MENU
        else -> MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_HIDE_MENU
    }
    var hideMenu by remember(provider.featureId()) {
        mutableStateOf(sp.getBoolean(hideMenuKey, defaultHideMenu))
    }
    var menuText by remember(provider.featureId()) {
        mutableStateOf(sp.getString(menuTextKey, defaultMenuText).orEmpty().ifBlank { defaultMenuText })
    }
    var forwardDebugLog by remember(provider.featureId()) {
        mutableStateOf(
            sp.getBoolean(
                MomentsFakeInteractionSettings.KEY_FAKE_FORWARD_DEBUG_LOG,
                MomentsFakeInteractionSettings.DEFAULT_FAKE_FORWARD_DEBUG_LOG
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = provider.title()) }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = provider.title(),
                        summary = provider.subtitle()
                    ) {
                        enabled = it
                        sp.edit().putBoolean(enableKey, it).commit()
                        MomentsFakeInteractionRuntimeRegistry.reapplyAll()
                    }
                }
            }
            if (enabled && isLike) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "生成方式") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = useNonFriends,
                            title = "使用非好友",
                            summary = "仅在伪集赞选择器中额外加入群成员"
                        ) {
                            useNonFriends = it
                            sp.edit().putBoolean(useNonFriendsKey, it).commit()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = randomOrder,
                            title = "随机排序",
                            summary = "保存时随机排列点赞人的显示顺序"
                        ) {
                            randomOrder = it
                            sp.edit().putBoolean(
                                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_RANDOM_ORDER,
                                it
                            ).commit()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = autoSelect,
                            title = "自动勾选好友",
                            summary = "长按伪集赞后自动勾选指定人数"
                        ) {
                            autoSelect = it
                            sp.edit().putBoolean(
                                MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT,
                                it
                            ).commit()
                        }
                        if (autoSelect) {
                            InsetDivider()
                            NumberInputRow(
                                title = "自动勾选数量",
                                summary = "最少 1 人，不设上限",
                                value = autoSelectCount
                            ) { next ->
                                val count = next.toIntOrNull()?.coerceAtLeast(1)
                                autoSelectCount = count?.toString().orEmpty()
                                count?.let {
                                    sp.edit().putInt(
                                        MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT_COUNT,
                                        it
                                    ).apply()
                                }
                            }
                        }
                        InsetDivider()
                        ActionRow(
                            title = "设置排除名单",
                            summary = if (excludedIds.isEmpty()) "未设置" else "已排除 ${excludedIds.size} 人",
                            onClick = {
                                showMomentsFakeLikeExclusionPicker(
                                    context = context,
                                    includeNonFriends = useNonFriends,
                                    selectedIds = excludedIds
                                ) { selected ->
                                    excludedIds = selected
                                    sp.edit().putStringSet(
                                        MomentsFakeInteractionSettings.KEY_FAKE_LIKE_EXCLUDED_IDS,
                                        selected
                                    ).commit()
                                }
                            }
                        )
                    }
                }
            }
            if (enabled && !isLike && !isForward) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "评论人范围") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = useNonFriends,
                            title = "使用非好友",
                            summary = "仅在伪评论选择器中额外加入群成员"
                        ) {
                            useNonFriends = it
                            sp.edit().putBoolean(useNonFriendsKey, it).commit()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = randomCommentContent,
                            title = "随机评论",
                            summary = if (commentContents.isEmpty()) {
                                "评论池为空时仍使用手动输入"
                            } else {
                                "为每位选中的好友从 ${commentContents.size} 条评论中随机选择"
                            }
                        ) {
                            randomCommentContent = it
                            sp.edit().putBoolean(
                                MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_RANDOM_CONTENT,
                                it
                            ).commit()
                        }
                        InsetDivider()
                        ActionRow(
                            title = "添加新评论",
                            summary = if (commentContents.isEmpty()) "尚未添加" else "当前 ${commentContents.size} 条"
                        ) {
                            showFakeCommentContentEditor(context, "") { content ->
                                val next = commentContents + content
                                if (MomentsFakeInteractionSettings.saveCommentContents(sp, next)) {
                                    commentContents = next
                                }
                            }
                        }
                        commentContents.forEachIndexed { index, content ->
                            InsetDivider()
                            ActionRow(
                                title = "评论 ${index + 1}",
                                summary = content
                            ) {
                                showFakeCommentContentActions(
                                    context = context,
                                    content = content,
                                    onEdit = { edited ->
                                        val next = commentContents.toMutableList().also { it[index] = edited }
                                        if (MomentsFakeInteractionSettings.saveCommentContents(sp, next)) {
                                            commentContents = next
                                        }
                                    },
                                    onDelete = {
                                        val next = commentContents.toMutableList().also { it.removeAt(index) }
                                        if (MomentsFakeInteractionSettings.saveCommentContents(sp, next)) {
                                            commentContents = next
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            if (enabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "长按菜单") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = hideMenu,
                            title = "隐藏长按菜单",
                            summary = when {
                                isLike -> "隐藏伪集赞入口，已伪造的点赞仍然显示"
                                isForward -> "隐藏伪转发入口，已保存的本地朋友圈仍然显示"
                                else -> "隐藏伪评论入口，已伪造的评论仍然显示"
                            }
                        ) {
                            hideMenu = it
                            sp.edit().putBoolean(hideMenuKey, it).commit()
                        }
                        if (!hideMenu) {
                            InsetDivider()
                            InputRow(
                                title = when {
                                    isLike -> "伪集赞自定义文本"
                                    isForward -> "伪转发自定义文本"
                                    else -> "伪评论自定义文本"
                                },
                                summary = "完整替换朋友圈长按菜单文字",
                                value = menuText,
                                onValueChange = { next ->
                                    menuText = next
                                    sp.edit().putString(menuTextKey, next).apply()
                                }
                            )
                        }
                    }
                }
            }
            if (isForward) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "诊断") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = forwardDebugLog,
                            title = "调试日志",
                            summary = "输出伪转发创建、分页、刷新和清理诊断到 LSPosed 日志"
                        ) {
                            if (sp.edit().putBoolean(
                                    MomentsFakeInteractionSettings.KEY_FAKE_FORWARD_DEBUG_LOG,
                                    it
                                ).commit()
                            ) {
                                forwardDebugLog = it
                            }
                        }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow(
                            title = "清空伪转发",
                            summary = "删除全部已登记的本地伪转发，不影响真实朋友圈",
                            onClick = {
                                confirmClearMomentsFakeForwards(context) {
                                    val scheduled = MomentsFakeForwardRuntimeRegistry.clearAll { success ->
                                        Toast.makeText(
                                            context,
                                            if (success) "已清空伪转发" else "部分记录删除失败，请稍后重试",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Toast.makeText(
                                        context,
                                        if (scheduled) "正在清理..." else "微信运行时尚未就绪",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                }
            } else {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow(
                            title = if (isLike) "清除伪集赞" else "清除伪评论",
                        summary = if (isLike) {
                            "清空已保存的本地点赞，保留当前功能设置"
                        } else {
                            "清空已保存的本地评论，保留当前功能设置"
                        },
                        onClick = {
                            confirmClearMomentsFakeInteractions(context, isLike) {
                                scheduleMomentsFakeInteractionRestore(context, sp, isLike)
                            }
                        }
                    )
                    InsetDivider()
                    ActionRow(
                        title = "恢复默认",
                        summary = if (isLike) {
                            "关闭伪集赞并清空已保存的本地点赞"
                        } else {
                            "关闭伪评论并清空已保存的本地评论"
                        },
                        onClick = {
                            val editor = sp.edit()
                                .putBoolean(enableKey, defaultEnabled)
                                .putBoolean(pendingRestoreKey, true)
                            if (isLike) {
                                editor
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_USE_NON_FRIENDS)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_RANDOM_ORDER)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_AUTO_SELECT_COUNT)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_EXCLUDED_IDS)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_HIDE_MENU)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_LIKE_MENU_TEXT)
                            } else {
                                editor
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_USE_NON_FRIENDS)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_HIDE_MENU)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_MENU_TEXT)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_RANDOM_CONTENT)
                                    .remove(MomentsFakeInteractionSettings.KEY_FAKE_COMMENT_CONTENTS)
                            }
                            val settingsRestored = editor.commit()
                            if (!settingsRestored) {
                                Toast.makeText(context, "恢复默认失败", Toast.LENGTH_SHORT).show()
                            } else {
                                enabled = defaultEnabled
                                hideMenu = defaultHideMenu
                                menuText = defaultMenuText
                                useNonFriends = defaultUseNonFriends
                                randomCommentContent = MomentsFakeInteractionSettings.DEFAULT_FAKE_COMMENT_RANDOM_CONTENT
                                commentContents = emptyList()
                                if (isLike) {
                                    randomOrder = MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_RANDOM_ORDER
                                    autoSelect = MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_AUTO_SELECT
                                    autoSelectCount = MomentsFakeInteractionSettings.DEFAULT_FAKE_LIKE_AUTO_SELECT_COUNT.toString()
                                    excludedIds = emptySet()
                                }
                                scheduleMomentsFakeInteractionRestore(context, sp, isLike, "已恢复默认")
                            }
                        }
                    )
                }
            }
            }
        }
    }
}

internal fun confirmClearMomentsFakeForwards(
    context: Context,
    onConfirmed: () -> Unit
) {
    val activity = context as? Activity
        ?: WeChatApis.currentActivity()?.currentActivity() as? Activity
        ?: return
    VoiceForwardMiuixDialog.showConfirm(
        activity = activity,
        title = "清空伪转发",
        message = "删除全部已登记的本地伪转发？真实朋友圈不会受影响。",
        onResult = { if (it) onConfirmed() },
        onDismiss = {}
    )
}

internal fun showMomentsFakeLikeExclusionPicker(
    context: Context,
    includeNonFriends: Boolean,
    selectedIds: Set<String>,
    onConfirm: (Set<String>) -> Unit
) {
    val activity = context as? Activity
        ?: WeChatApis.currentActivity()?.currentActivity() as? Activity
        ?: return
    val loading = VoiceForwardMiuixDialog.showLoading(
        activity = activity,
        title = "设置排除名单",
        message = if (includeNonFriends) "正在载入好友和非好友..." else "正在载入好友列表...",
        onDismiss = {}
    )
    Thread({
        val contacts = runCatching {
            MomentsFakeLikeCandidateRepository.loadForLikes(includeNonFriends)
        }.getOrDefault(emptyList())
        activity.runOnUiThread {
            loading.close()
            activity.window?.decorView?.postOnAnimation {
                if (activity.isFinishing || activity.isDestroyed) return@postOnAnimation
                if (contacts.isEmpty()) {
                    Toast.makeText(activity, "没有可选择的联系人", Toast.LENGTH_SHORT).show()
                    return@postOnAnimation
                }
                val visibleIds = contacts.mapTo(hashSetOf()) { it.id }
                val hiddenSelectedIds = selectedIds.filterNotTo(linkedSetOf()) {
                    visibleIds.contains(it)
                }
                VoiceForwardMiuixDialog.showContacts(
                    activity = activity,
                    contacts = contacts,
                    title = "设置排除名单",
                    confirmText = "保存",
                    showGroupFilter = false,
                    initialSelectedIds = selectedIds,
                    allowEmpty = true,
                    showClearSelectionAction = true,
                    onConfirm = { selected ->
                        onConfirm(hiddenSelectedIds + selected.mapTo(linkedSetOf()) { it.id })
                    },
                    onDismiss = {}
                )
            }
        }
    }, "Hchat-MomentsFakeLikeExclude").apply { isDaemon = true }.start()
}

internal fun confirmClearMomentsFakeInteractions(
    context: Context,
    isLike: Boolean,
    onConfirmed: () -> Unit
) {
    val activity = context as? Activity
        ?: WeChatApis.currentActivity()?.currentActivity() as? Activity
        ?: return
    VoiceForwardMiuixDialog.showConfirm(
        activity = activity,
        title = if (isLike) "清除伪集赞" else "清除伪评论",
        message = if (isLike) "清空全部朋友圈已保存的本地点赞？" else "清空全部朋友圈已保存的本地评论？",
        onResult = { if (it) onConfirmed() },
        onDismiss = {}
    )
}

internal fun showFakeCommentContentEditor(
    context: Context,
    initialValue: String,
    onSaved: (String) -> Unit
) {
    val activity = context as? Activity
        ?: WeChatApis.currentActivity()?.currentActivity() as? Activity
        ?: return
    VoiceForwardMiuixDialog.showTextInput(
        activity = activity,
        title = if (initialValue.isBlank()) "添加新评论" else "修改评论内容",
        summary = "保存后可用于随机评论",
        initialValue = initialValue,
        placeholder = "请输入评论内容",
        maxLength = 1000,
        singleLine = false,
        onConfirm = onSaved,
        onDismiss = {}
    )
}

internal fun showFakeCommentContentActions(
    context: Context,
    content: String,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit
) {
    val activity = context as? Activity
        ?: WeChatApis.currentActivity()?.currentActivity() as? Activity
        ?: return
    VoiceForwardMiuixDialog.showChoices(
        activity = activity,
        title = "评论内容",
        summary = content,
        choices = listOf(
            "修改" to "编辑这条随机评论内容",
            "删除" to "从评论池移除这条内容"
        ),
        onSelected = { index ->
            if (index == 0) {
                showFakeCommentContentEditor(context, content, onEdit)
            } else {
                VoiceForwardMiuixDialog.showConfirm(
                    activity = activity,
                    title = "删除评论内容",
                    message = "从随机评论池删除这条内容？",
                    onResult = { if (it) onDelete() },
                    onDismiss = {}
                )
            }
        },
        onDismiss = {}
    )
}

internal fun scheduleMomentsFakeInteractionRestore(
    context: Context,
    sp: SharedPreferences,
    isLike: Boolean,
    successText: String = "已清除"
) {
    val pendingKey = if (isLike) {
        MomentsFakeInteractionSettings.KEY_PENDING_RESTORE_LIKES
    } else {
        MomentsFakeInteractionSettings.KEY_PENDING_RESTORE_COMMENTS
    }
    if (!sp.edit().putBoolean(pendingKey, true).commit()) {
        Toast.makeText(context, "保存清理状态失败", Toast.LENGTH_SHORT).show()
        return
    }
    val onRestored: (Boolean) -> Unit = { restored ->
        Toast.makeText(
            context,
            if (restored) successText else "缓存将在下次启动时继续清理",
            Toast.LENGTH_SHORT
        ).show()
    }
    val scheduled = if (isLike) {
        MomentsFakeInteractionRuntimeRegistry.restoreLikes(onRestored)
    } else {
        MomentsFakeInteractionRuntimeRegistry.restoreComments(onRestored)
    }
    Toast.makeText(
        context,
        if (scheduled) "正在清理..." else "缓存将在下次启动时清理",
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
internal fun RemoveMomentsAdsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RemoveMomentsAdsSettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "朋友圈") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        RemoveMomentsAdsSettings.KEY_ENABLE,
                        "去除朋友圈广告",
                        "阻止朋友圈广告信息解析和展示",
                        RemoveMomentsAdsSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun FinderMediaDownloadMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FinderMediaDownloadSettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "视频号") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        FinderMediaDownloadSettings.KEY_ENABLE,
                        "视频号媒体下载",
                        "在视频号分享菜单增加复制链接和下载入口，媒体保存到 Hchat/Finder",
                        FinderMediaDownloadSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun HchatExtraToggleMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    key: String,
    defaultValue: Boolean,
    summary: String,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, HchatExtraSettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "功能开关") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        key,
                        provider.title(),
                        summary,
                        defaultValue
                    )
                }
            }
        }
    }
}

@Composable
internal fun MessageDetailsConfigPage(
    context: Context,
    sp: SharedPreferences,
    onBack: () -> Unit
) {
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                HchatExtraSettings.KEY_MESSAGE_DETAILS,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS
            )
        )
    }
    var lightBg by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_LIGHT_BG,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_BG
            ).orEmpty()
        )
    }
    var lightText by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_LIGHT_TEXT,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_TEXT
            ).orEmpty()
        )
    }
    var darkBg by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_DARK_BG,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_BG
            ).orEmpty()
        )
    }
    var darkText by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_DARK_TEXT,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_TEXT
            ).orEmpty()
        )
    }
    var format by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_FORMAT,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_FORMAT
            ).orEmpty()
        )
    }
    var timeFormat by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_TIME_FORMAT,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TIME_FORMAT
            ).orEmpty()
        )
    }
    var position by remember {
        mutableStateOf(
            sp.getString(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_POSITION,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_POSITION
            ).orEmpty().ifBlank { HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_POSITION }
        )
    }
    var avatarGap by remember {
        mutableStateOf(
            sp.getInt(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_AVATAR_GAP,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_AVATAR_GAP
            ).toString()
        )
    }
    var textSize by remember {
        mutableStateOf(
            sp.getInt(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_TEXT_SIZE,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TEXT_SIZE
            ).toString()
        )
    }
    var clickShow by remember {
        mutableStateOf(
            sp.getBoolean(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_CLICK_SHOW,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_CLICK_SHOW
            )
        )
    }
    var formatContent by remember {
        mutableStateOf(
            sp.getBoolean(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_FORMAT_CONTENT,
                HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_FORMAT_CONTENT
            )
        )
    }

    fun resetValues() {
        enabled = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS
        lightBg = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_BG
        lightText = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_TEXT
        darkBg = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_BG
        darkText = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_TEXT
        format = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_FORMAT
        timeFormat = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TIME_FORMAT
        position = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_POSITION
        avatarGap = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_AVATAR_GAP.toString()
        textSize = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TEXT_SIZE.toString()
        clickShow = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_CLICK_SHOW
        formatContent = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_FORMAT_CONTENT
    }

    fun saveValues() {
        val savedLightBg = cleanMessageDetailsColor(lightBg, HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_BG)
        val savedLightText = cleanMessageDetailsColor(lightText, HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_TEXT)
        val savedDarkBg = cleanMessageDetailsColor(darkBg, HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_BG)
        val savedDarkText = cleanMessageDetailsColor(darkText, HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_TEXT)
        val savedPosition = position.takeIf { it in messageDetailsPositionValues() }
            ?: HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_POSITION
        val savedAvatarGap = (avatarGap.toIntOrNull() ?: HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_AVATAR_GAP)
            .coerceIn(0, 64)
        sp.edit()
            .putBoolean(HchatExtraSettings.KEY_MESSAGE_DETAILS, enabled)
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_LIGHT_BG, savedLightBg)
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_LIGHT_TEXT, savedLightText)
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_DARK_BG, savedDarkBg)
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_DARK_TEXT, savedDarkText)
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_FORMAT, format.ifBlank { HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_FORMAT })
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_TIME_FORMAT, timeFormat.ifBlank { HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TIME_FORMAT })
            .putString(HchatExtraSettings.KEY_MESSAGE_DETAILS_POSITION, savedPosition)
            .putInt(HchatExtraSettings.KEY_MESSAGE_DETAILS_AVATAR_GAP, savedAvatarGap)
            .putInt(
                HchatExtraSettings.KEY_MESSAGE_DETAILS_TEXT_SIZE,
                textSize.toIntOrNull() ?: HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_TEXT_SIZE
            )
            .putBoolean(HchatExtraSettings.KEY_MESSAGE_DETAILS_CLICK_SHOW, clickShow)
            .putBoolean(HchatExtraSettings.KEY_MESSAGE_DETAILS_FORMAT_CONTENT, formatContent)
            .apply()
        lightBg = savedLightBg
        lightText = savedLightText
        darkBg = savedDarkBg
        darkText = savedDarkText
        position = savedPosition
        avatarGap = savedAvatarGap.toString()
        Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
    }

    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "消息显示时间",
        largeTitle = "消息显示时间",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = { saveValues() },
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
                    SwitchRow(
                        checked = enabled,
                        title = "消息显示时间",
                        summary = "开启后按所选位置显示自定义消息时间",
                        onCheckedChange = { enabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = clickShow,
                        title = "点击显详情",
                        summary = "点击详情文字后打开消息内容详情",
                        onCheckedChange = { clickShow = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = formatContent,
                        title = "内容格式化",
                        summary = "打开详情时格式化 XML 内容",
                        onCheckedChange = { formatContent = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "颜色") }
            item {
                SettingsCard {
                    ColorPickerRow(
                        "浅色背景",
                        "浅色模式消息时间背景",
                        lightBg,
                        allowGradient = false,
                        onReset = { lightBg = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_BG }
                    ) { lightBg = it.take(9) }
                    InsetDivider()
                    ColorPickerRow(
                        "浅色文字",
                        "浅色模式消息时间文字",
                        lightText,
                        allowGradient = false,
                        onReset = { lightText = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_LIGHT_TEXT }
                    ) { lightText = it.take(9) }
                    InsetDivider()
                    ColorPickerRow(
                        "深色背景",
                        "深色模式消息时间背景",
                        darkBg,
                        allowGradient = false,
                        onReset = { darkBg = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_BG }
                    ) { darkBg = it.take(9) }
                    InsetDivider()
                    ColorPickerRow(
                        "深色文字",
                        "深色模式消息时间文字",
                        darkText,
                        allowGradient = false,
                        onReset = { darkText = HchatExtraSettings.DEFAULT_MESSAGE_DETAILS_DARK_TEXT }
                    ) { darkText = it.take(9) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文本") }
            item {
                SettingsCard {
                    VariableInputRow(
                        title = "文本格式",
                        summary = "显示在消息旁的内容模板",
                        value = format,
                        variables = messageDetailsVariables(),
                        onValueChange = { format = it }
                    )
                    InsetDivider()
                    PopupChoiceRow(
                        title = "时间显示",
                        summary = messageDetailsTimeFormatChoices(timeFormat).first { it.value == timeFormat }.label,
                        options = messageDetailsTimeFormatChoices(timeFormat),
                        currentValue = timeFormat,
                        onValueChanged = { timeFormat = it }
                    )
                    InsetDivider()
                    InputRow("自定义时间格式", "yyyy 年、MM 月、dd 日；例如 yyyy-MM-dd HH:mm:ss", timeFormat, onValueChange = { timeFormat = it })
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "布局") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "显示位置",
                        summary = messageDetailsPositionLabel(position),
                        options = messageDetailsPositionChoices(),
                        currentValue = position,
                        onValueChanged = { position = it }
                    )
                    InsetDivider()
                    NumberInputRow("与头像间距", "单位 dp，可设置 0-64，越小越贴近头像", avatarGap, onValueChange = { avatarGap = it })
                    InsetDivider()
                    NumberInputRow("字体大小", "单位 sp", textSize, onValueChange = { textSize = it })
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    ActionRow("恢复默认", "重置本页全部消息显示时间设置") {
                        resetValues()
                        Toast.makeText(context, "已恢复默认，保存后生效", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

internal fun cleanMessageDetailsColor(value: String, fallback: String): String {
    return MemberTitleStore.cleanColor(value).ifEmpty { fallback }
}

internal fun messageDetailsTimeFormatChoices(current: String): List<PopupChoice<String>> {
    val choices = listOf(
        PopupChoice(label = "时分秒 · 14:30:05", value = "HH:mm:ss"),
        PopupChoice(label = "月日 时分秒 · 09-11 14:30:05", value = "MM-dd HH:mm:ss"),
        PopupChoice(label = "年月日 时分秒 · 2026-09-11 14:30:05", value = "yyyy-MM-dd HH:mm:ss"),
        PopupChoice(label = "中文年月日 · 2026年9月11日 14:30:05", value = "yyyy年M月d日 HH:mm:ss"),
        PopupChoice(label = "仅年月日 · 2026-09-11", value = "yyyy-MM-dd")
    )
    return if (choices.any { it.value == current }) choices
    else choices + PopupChoice(label = "自定义 · "+ current.ifBlank { "默认时分秒" }, value = current)
}

internal fun messageDetailsPositionValues(): Set<String> = setOf(
    HchatExtraSettings.POSITION_AVATAR_ABOVE,
    HchatExtraSettings.POSITION_AVATAR_BELOW
)

internal fun messageDetailsPositionChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice(label = "头像上方", value = HchatExtraSettings.POSITION_AVATAR_ABOVE),
    PopupChoice(label = "头像下方", value = HchatExtraSettings.POSITION_AVATAR_BELOW)
)

internal fun messageDetailsPositionLabel(value: String): String = when (value) {
    HchatExtraSettings.POSITION_AVATAR_ABOVE -> "头像上方"
    else -> "头像下方"
}

internal fun messageDetailsVariables(): List<TemplateVariable> {
    return listOf(
        TemplateVariable("\${time}", "时间"),
        TemplateVariable("\${relativeTime}", "相对时间"),
        TemplateVariable("\${type}", "消息类型（含文件、红包、小程序等）"),
        TemplateVariable("\${appType}", "卡片子类型编号（非卡片为空）"),
        TemplateVariable("\${baseType}", "基础消息类型编号"),
        TemplateVariable("\${direction}", "收发方向"),
        TemplateVariable("\${talker}", "会话 ID"),
        TemplateVariable("\${createTime}", "消息时间戳（毫秒）"),
        TemplateVariable("\${rawAtUserList}", "原始艾特名单"),
        TemplateVariable("\${typeDec}", "类型编号（十进制）"),
        TemplateVariable("\${typeHex}", "类型编号（十六进制）"),
        TemplateVariable("\${msgId}", "本地消息编号"),
        TemplateVariable("\${msgSvrId}", "服务端消息编号"),
        TemplateVariable("\${atUserList}", "艾特对象"),
        TemplateVariable("\${mentionedUsers}", "提及摘要")
    )
}

@Composable
internal fun ProfileIdMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, ProfileIdSettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "资料页") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        ProfileIdSettings.KEY_ENABLE,
                        "显示好友/群聊ID",
                        "在好友和群聊资料页显示可点击复制的 ID",
                        ProfileIdSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun HideChatMenuMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HideChatMenuSettings.preferences(context) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(HideChatMenuSettings.KEY_ENABLE, HideChatMenuSettings.DEFAULT_ENABLE)
        )
    }
    var titles by remember {
        mutableStateOf(
            sp.getString(HideChatMenuSettings.KEY_TITLES, HideChatMenuSettings.DEFAULT_TITLES)
                .orEmpty()
        )
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun save() {
        val normalized = HideChatMenuSettings.parseTitles(titles).joinToString(",")
        titles = normalized
        sp.edit()
            .putBoolean(HideChatMenuSettings.KEY_ENABLE, enabled)
            .putString(HideChatMenuSettings.KEY_TITLES, normalized)
            .apply()
        Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
    }

    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = { save() },
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天菜单") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "隐藏长按菜单",
                        summary = "隐藏聊天消息长按菜单中的指定项目",
                        onCheckedChange = { enabled = it }
                    )
                    if (enabled) {
                        InsetDivider()
                        InputRow(
                            "隐藏菜单项",
                            "输入菜单显示名称，多个项目用逗号、分号或换行分隔",
                            titles,
                            minLines = 2,
                            onValueChange = { titles = it }
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
            item {
                SettingsCard {
                    ActionRow("恢复默认", "恢复初始菜单名称示例") {
                        titles = HideChatMenuSettings.DEFAULT_TITLES
                        Toast.makeText(context, "已恢复默认，保存后生效", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsEntryMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, SettingsEntrySettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "入口") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SettingsEntrySettings.KEY_PLUS_MENU_ENABLE,
                        "注入加号菜单",
                        "在微信右上角加号菜单中显示 Hchat 入口，重启微信后生效",
                        SettingsEntrySettings.DEFAULT_PLUS_MENU_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        SettingsEntrySettings.KEY_PLUS_LONG_PRESS_ENABLE,
                        "长按加号入口",
                        "长按微信右上角加号打开 Hchat 设置，重启微信后生效",
                        SettingsEntrySettings.DEFAULT_PLUS_LONG_PRESS_ENABLE
                    )
                }
            }
        }
    }
}
internal data class FloatingShortcutEditorRequest(
    val original: FloatingShortcutItem?,
    val draft: FloatingShortcutItem
)

internal enum class FloatingShortcutRoute {
    MAIN,
    APPEARANCE
}


@Composable
internal fun FloatingShortcutMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FloatingShortcutSettings.PREFS_NAME) }
    var enabled by remember { mutableStateOf(FloatingShortcutRuntime.isEnabled(context)) }
    var scope by remember {
        mutableStateOf(
            sp.getString(FloatingShortcutSettings.KEY_SCOPE, FloatingShortcutSettings.DEFAULT_SCOPE)
                ?: FloatingShortcutSettings.DEFAULT_SCOPE
        )
    }
    var displayMode by remember {
        mutableStateOf(
            sp.getString(
                FloatingShortcutSettings.KEY_DISPLAY_MODE,
                FloatingShortcutSettings.DEFAULT_DISPLAY_MODE
            ) ?: FloatingShortcutSettings.DEFAULT_DISPLAY_MODE
        )
    }
    var expandDirection by remember {
        mutableStateOf(
            sp.getString(
                FloatingShortcutSettings.KEY_EXPAND_DIRECTION,
                FloatingShortcutSettings.DEFAULT_EXPAND_DIRECTION
            ) ?: FloatingShortcutSettings.DEFAULT_EXPAND_DIRECTION
        )
    }
    var bubbleIcon by remember {
        mutableStateOf(sp.getString(FloatingShortcutSettings.KEY_BUBBLE_ICON, "").orEmpty())
    }
    var bubbleDarkIcon by remember {
        mutableStateOf(sp.getString(FloatingShortcutSettings.KEY_BUBBLE_DARK_ICON, "").orEmpty())
    }
    var shortcuts by remember { mutableStateOf(FloatingShortcutSettings.loadItems(context)) }
    var editor by remember { mutableStateOf<FloatingShortcutEditorRequest?>(null) }
    var route by remember { mutableStateOf(FloatingShortcutRoute.MAIN) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun saveShortcuts(value: List<FloatingShortcutItem>) {
        shortcuts = value
        FloatingShortcutSettings.saveItems(context, value)
    }

    fun pickIcon(key: String, onSaved: (String) -> Unit) {
        val activity = (context as? Activity) ?: WeChatApis.currentActivity()?.currentActivity()
        if (activity == null) {
            Toast.makeText(context, "当前页面无法打开图片选择器", Toast.LENGTH_SHORT).show()
            return
        }
        FloatingShortcutIconPicker.launch(activity, key) { result ->
            when (result) {
                is FloatingShortcutIconPickResult.Saved -> onSaved(result.path)
                FloatingShortcutIconPickResult.FAILED ->
                    Toast.makeText(context, "图标读取失败", Toast.LENGTH_SHORT).show()
                FloatingShortcutIconPickResult.CANCELLED -> Unit
            }
        }
    }

    fun dismissEditor() {
        val request = editor ?: return
        if (request.draft.iconPath != request.original?.iconPath) {
            FloatingShortcutIconStore.delete(context, request.draft.iconPath)
        }
        if (request.draft.darkIconPath != request.original?.darkIconPath) {
            FloatingShortcutIconStore.delete(context, request.draft.darkIconPath)
        }
        editor = null
    }

    SettingsRouteTransition(
        targetState = route,
        label = "FloatingShortcutRoute",
        depthOf = { if (it == FloatingShortcutRoute.MAIN) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            FloatingShortcutRoute.MAIN -> PageScaffold(
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "悬浮入口") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "悬浮快捷菜单",
                        summary = "在微信页面显示可自由拖动和停放的快捷入口",
                        onCheckedChange = {
                            enabled = it
                            FloatingShortcutRuntime.setEnabled(context, it)
                        }
                    )
                }
            }
            if (enabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "显示方式") }
                item {
                    SettingsCard {
                        PopupChoiceRow(
                            title = "显示范围",
                            summary = if (scope == FloatingShortcutSettings.SCOPE_ALL) {
                                "微信所有页面"
                            } else {
                                "仅微信主页"
                            },
                            options = listOf(
                                PopupChoice("仅微信主页", FloatingShortcutSettings.SCOPE_HOME),
                                PopupChoice("所有微信页面", FloatingShortcutSettings.SCOPE_ALL)
                            ),
                            currentValue = scope,
                            onValueChanged = {
                                scope = it
                                sp.edit().putString(FloatingShortcutSettings.KEY_SCOPE, it).apply()
                            }
                        )
                        InsetDivider()
                        PopupChoiceRow(
                            title = "快捷项样式",
                            summary = when (displayMode) {
                                FloatingShortcutSettings.DISPLAY_TEXT -> "仅文字"
                                FloatingShortcutSettings.DISPLAY_BOTH -> "图标和文字"
                                else -> "仅图标"
                            },
                            options = listOf(
                                PopupChoice("仅图标", FloatingShortcutSettings.DISPLAY_ICON),
                                PopupChoice("仅文字", FloatingShortcutSettings.DISPLAY_TEXT),
                                PopupChoice("图标和文字", FloatingShortcutSettings.DISPLAY_BOTH)
                            ),
                            currentValue = displayMode,
                            onValueChanged = {
                                displayMode = it
                                sp.edit().putString(FloatingShortcutSettings.KEY_DISPLAY_MODE, it).apply()
                            }
                        )
                        InsetDivider()
                        PopupChoiceRow(
                            title = "展开方向",
                            summary = when (expandDirection) {
                                FloatingShortcutSettings.EXPAND_DOWN -> "向下展开"
                                FloatingShortcutSettings.EXPAND_LEFT -> "向左展开"
                                FloatingShortcutSettings.EXPAND_RIGHT -> "向右展开"
                                else -> "向上展开"
                            },
                            options = listOf(
                                PopupChoice("向上展开", FloatingShortcutSettings.EXPAND_UP),
                                PopupChoice("向下展开", FloatingShortcutSettings.EXPAND_DOWN),
                                PopupChoice("向左展开", FloatingShortcutSettings.EXPAND_LEFT),
                                PopupChoice("向右展开", FloatingShortcutSettings.EXPAND_RIGHT)
                            ),
                            currentValue = expandDirection,
                            onValueChanged = {
                                expandDirection = it
                                sp.edit().putString(FloatingShortcutSettings.KEY_EXPAND_DIRECTION, it).apply()
                            }
                        )
                        InsetDivider()
                        ActionRow(
                            title = "按钮外观",
                            summary = "设置按钮渐变、大小和菜单名称样式",
                            onClick = { route = FloatingShortcutRoute.APPEARANCE }
                        )
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "主按钮图标") }
                item {
                    SettingsCard {
                        FloatingShortcutIconActionRow(
                            title = "浅色模式图标",
                            iconPath = bubbleIcon,
                            defaultSummary = "未选择时使用内置快捷菜单图标",
                            onSelect = {
                                pickIcon("bubble-${System.nanoTime()}") { path ->
                                    val previous = bubbleIcon
                                    bubbleIcon = path
                                    sp.edit().putString(FloatingShortcutSettings.KEY_BUBBLE_ICON, path).apply()
                                    FloatingShortcutIconStore.delete(context, previous)
                                }
                            },
                            onReset = if (bubbleIcon.isBlank()) null else {
                                {
                                    val previous = bubbleIcon
                                    bubbleIcon = ""
                                    sp.edit().remove(FloatingShortcutSettings.KEY_BUBBLE_ICON).apply()
                                    FloatingShortcutIconStore.delete(context, previous)
                                }
                            }
                        )
                        InsetDivider()
                        FloatingShortcutIconActionRow(
                            title = "深色模式图标",
                            iconPath = bubbleDarkIcon,
                            defaultSummary = "未选择时沿用浅色模式图标",
                            onSelect = {
                                pickIcon("bubble-dark-${System.nanoTime()}") { path ->
                                    val previous = bubbleDarkIcon
                                    bubbleDarkIcon = path
                                    sp.edit().putString(FloatingShortcutSettings.KEY_BUBBLE_DARK_ICON, path).apply()
                                    FloatingShortcutIconStore.delete(context, previous)
                                }
                            },
                            onReset = if (bubbleDarkIcon.isBlank()) null else {
                                {
                                    val previous = bubbleDarkIcon
                                    bubbleDarkIcon = ""
                                    sp.edit().remove(FloatingShortcutSettings.KEY_BUBBLE_DARK_ICON).apply()
                                    FloatingShortcutIconStore.delete(context, previous)
                                }
                            }
                        )
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "快捷项") }
                item {
                    SettingsCard {
                        if (shortcuts.isEmpty()) {
                            Text(
                                text = "暂无快捷项",
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            shortcuts.forEachIndexed { index, item ->
                                if (index > 0) InsetDivider()
                                FloatingShortcutSettingsItemRow(
                                    item = item,
                                    canMoveUp = index > 0,
                                    canMoveDown = index < shortcuts.lastIndex,
                                    onEnabledChange = { checked ->
                                        saveShortcuts(shortcuts.toMutableList().apply {
                                            this[index] = item.copy(enabled = checked)
                                        })
                                    },
                                    onMoveUp = {
                                        if (index > 0) {
                                            saveShortcuts(shortcuts.toMutableList().apply {
                                                val moved = removeAt(index)
                                                add(index - 1, moved)
                                            })
                                        }
                                    },
                                    onMoveDown = {
                                        if (index < shortcuts.lastIndex) {
                                            saveShortcuts(shortcuts.toMutableList().apply {
                                                val moved = removeAt(index)
                                                add(index + 1, moved)
                                            })
                                        }
                                    },
                                    onEdit = { editor = FloatingShortcutEditorRequest(item, item) },
                                    onDelete = {
                                        FloatingShortcutIconStore.delete(context, item.iconPath)
                                        FloatingShortcutIconStore.delete(context, item.darkIconPath)
                                        saveShortcuts(shortcuts.filterNot { it.id == item.id })
                                    }
                                )
                            }
                        }
                        InsetDivider()
                        ActionRow(
                            title = "新增快捷项",
                            summary = "添加微信页面或模块设置入口",
                            onClick = {
                                editor = FloatingShortcutEditorRequest(
                                    original = null,
                                    draft = FloatingShortcutItem(
                                        id = "shortcut_${System.currentTimeMillis()}_${shortcuts.size}",
                                        title = "",
                                        actionType = FloatingShortcutSettings.ACTION_ACTIVITY,
                                        target = ""
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
            FloatingShortcutRoute.APPEARANCE -> FloatingShortcutAppearanceMiuixPage(
                context = context,
                onBack = { route = FloatingShortcutRoute.MAIN }
            )
        }
    }

    editor?.let { request ->
        val draft = request.draft
        val valid = draft.title.isNotBlank() &&
            (draft.actionType != FloatingShortcutSettings.ACTION_ACTIVITY || draft.target.isNotBlank())
        WindowDialog(
            show = true,
            title = if (request.original == null) "新增快捷项" else "编辑快捷项",
            onDismissRequest = ::dismissEditor,
            content = {
                Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                    InputRow(
                        title = "名称",
                        summary = "显示在展开菜单中的名称",
                        value = draft.title,
                        onValueChange = { editor = request.copy(draft = draft.copy(title = it)) }
                    )
                    PopupChoiceRow(
                        title = "动作类型",
                        summary = when (draft.actionType) {
                            FloatingShortcutSettings.ACTION_MODULE_SETTINGS -> "打开 Hchat 设置"
                            else -> "打开微信页面"
                        },
                        options = listOf(
                            PopupChoice("微信页面", FloatingShortcutSettings.ACTION_ACTIVITY),
                            PopupChoice("模块设置", FloatingShortcutSettings.ACTION_MODULE_SETTINGS)
                        ),
                        currentValue = draft.actionType,
                        onValueChanged = { type ->
                            editor = request.copy(
                                draft = draft.copy(
                                    actionType = type,
                                    target = if (type == FloatingShortcutSettings.ACTION_ACTIVITY) draft.target else ""
                                )
                            )
                        }
                    )
                    if (draft.actionType == FloatingShortcutSettings.ACTION_ACTIVITY) {
                        InputRow(
                            title = "Activity 类名",
                            summary = "填写微信页面的完整 Activity 类名",
                            value = draft.target,
                            onValueChange = { editor = request.copy(draft = draft.copy(target = it)) }
                        )
                    }
                    FloatingShortcutIconActionRow(
                        title = "浅色模式图标",
                        iconPath = draft.iconPath,
                        defaultSummary = "未选择时使用内置图标",
                        onSelect = {
                            pickIcon("item-${draft.id}-${System.nanoTime()}") { path ->
                                if (draft.iconPath != request.original?.iconPath) {
                                    FloatingShortcutIconStore.delete(context, draft.iconPath)
                                }
                                editor = request.copy(draft = draft.copy(iconPath = path))
                            }
                        },
                        onReset = if (draft.iconPath.isBlank()) null else {
                            {
                                if (draft.iconPath != request.original?.iconPath) {
                                    FloatingShortcutIconStore.delete(context, draft.iconPath)
                                }
                                editor = request.copy(draft = draft.copy(iconPath = ""))
                            }
                        }
                    )
                    FloatingShortcutIconActionRow(
                        title = "深色模式图标",
                        iconPath = draft.darkIconPath,
                        defaultSummary = "未选择时沿用浅色模式图标",
                        onSelect = {
                            pickIcon("item-dark-${draft.id}-${System.nanoTime()}") { path ->
                                if (draft.darkIconPath != request.original?.darkIconPath) {
                                    FloatingShortcutIconStore.delete(context, draft.darkIconPath)
                                }
                                editor = request.copy(draft = draft.copy(darkIconPath = path))
                            }
                        },
                        onReset = if (draft.darkIconPath.isBlank()) null else {
                            {
                                if (draft.darkIconPath != request.original?.darkIconPath) {
                                    FloatingShortcutIconStore.delete(context, draft.darkIconPath)
                                }
                                editor = request.copy(draft = draft.copy(darkIconPath = ""))
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = ::dismissEditor,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "保存",
                            onClick = {
                                if (!valid) {
                                    Toast.makeText(context, "请填写完整的快捷项信息", Toast.LENGTH_SHORT).show()
                                    return@TextButton
                                }
                                val saved = draft.copy(
                                    title = draft.title.trim(),
                                    target = draft.target.trim()
                                )
                                val original = request.original
                                if (original != null && original.iconPath != saved.iconPath) {
                                    FloatingShortcutIconStore.delete(context, original.iconPath)
                                }
                                if (original != null && original.darkIconPath != saved.darkIconPath) {
                                    FloatingShortcutIconStore.delete(context, original.darkIconPath)
                                }
                                val next = shortcuts.toMutableList()
                                val index = next.indexOfFirst { it.id == saved.id }
                                if (index >= 0) next[index] = saved else next.add(saved)
                                saveShortcuts(next)
                                editor = null
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                    }
                }
            }
        )
    }
}

@Composable
internal fun FloatingShortcutAppearanceMiuixPage(
    context: Context,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FloatingShortcutSettings.PREFS_NAME) }
    var bubbleSize by remember {
        mutableStateOf(
            sp.getInt(
                FloatingShortcutSettings.KEY_BUBBLE_SIZE,
                FloatingShortcutSettings.DEFAULT_BUBBLE_SIZE
            ).coerceIn(FloatingShortcutSettings.MIN_BUTTON_SIZE, FloatingShortcutSettings.MAX_BUTTON_SIZE)
        )
    }
    var bubbleColor by remember {
        mutableStateOf(
            sp.getString(
                FloatingShortcutSettings.KEY_BUBBLE_COLOR,
                FloatingShortcutSettings.DEFAULT_BUBBLE_COLOR
            ) ?: FloatingShortcutSettings.DEFAULT_BUBBLE_COLOR
        )
    }
    var actionSize by remember {
        mutableStateOf(
            sp.getInt(
                FloatingShortcutSettings.KEY_ACTION_SIZE,
                FloatingShortcutSettings.DEFAULT_ACTION_SIZE
            ).coerceIn(FloatingShortcutSettings.MIN_BUTTON_SIZE, FloatingShortcutSettings.MAX_BUTTON_SIZE)
        )
    }
    var actionColor by remember {
        mutableStateOf(
            sp.getString(
                FloatingShortcutSettings.KEY_ACTION_COLOR,
                FloatingShortcutSettings.DEFAULT_ACTION_COLOR
            ) ?: FloatingShortcutSettings.DEFAULT_ACTION_COLOR
        )
    }
    var labelTextSize by remember {
        mutableStateOf(
            sp.getInt(
                FloatingShortcutSettings.KEY_LABEL_TEXT_SIZE,
                FloatingShortcutSettings.DEFAULT_LABEL_TEXT_SIZE
            ).coerceIn(
                FloatingShortcutSettings.MIN_LABEL_TEXT_SIZE,
                FloatingShortcutSettings.MAX_LABEL_TEXT_SIZE
            )
        )
    }
    var labelColor by remember {
        mutableStateOf(
            sp.getString(
                FloatingShortcutSettings.KEY_LABEL_COLOR,
                FloatingShortcutSettings.DEFAULT_LABEL_COLOR
            ) ?: FloatingShortcutSettings.DEFAULT_LABEL_COLOR
        )
    }
    val currentBubbleSize by rememberUpdatedState(bubbleSize)
    val currentBubbleColor by rememberUpdatedState(bubbleColor)
    val currentActionSize by rememberUpdatedState(actionSize)
    val currentActionColor by rememberUpdatedState(actionColor)
    val currentLabelTextSize by rememberUpdatedState(labelTextSize)
    val currentLabelColor by rememberUpdatedState(labelColor)
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun persistAppearance() {
        sp.edit()
            .putInt(FloatingShortcutSettings.KEY_BUBBLE_SIZE, currentBubbleSize)
            .putString(FloatingShortcutSettings.KEY_BUBBLE_COLOR, currentBubbleColor.trim())
            .putInt(FloatingShortcutSettings.KEY_ACTION_SIZE, currentActionSize)
            .putString(FloatingShortcutSettings.KEY_ACTION_COLOR, currentActionColor.trim())
            .putInt(FloatingShortcutSettings.KEY_LABEL_TEXT_SIZE, currentLabelTextSize)
            .putString(FloatingShortcutSettings.KEY_LABEL_COLOR, currentLabelColor.trim())
            .apply()
    }

    DisposableEffect(sp) {
        onDispose(::persistAppearance)
    }

    PageScaffold(
        title = "按钮外观",
        largeTitle = "按钮外观",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "返回",
                onPrimaryClick = {
                    persistAppearance()
                    onBack()
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "主按钮") }
            item {
                SettingsCard {
                    FloatingShortcutSizeSlider("主按钮大小", bubbleSize) { bubbleSize = it }
                    InsetDivider()
                    ColorPickerRow(
                        title = "主按钮颜色",
                        summary = "圆形背景色",
                        value = bubbleColor,
                        onReset = { bubbleColor = FloatingShortcutSettings.DEFAULT_BUBBLE_COLOR }
                    ) { bubbleColor = it.take(19) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "副按钮") }
            item {
                SettingsCard {
                    FloatingShortcutSizeSlider("副按钮大小", actionSize) { actionSize = it }
                    InsetDivider()
                    ColorPickerRow(
                        title = "副按钮颜色",
                        summary = "留空时跟随深浅色模式",
                        value = actionColor,
                        onReset = { actionColor = FloatingShortcutSettings.DEFAULT_ACTION_COLOR }
                    ) { actionColor = it.take(19) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "菜单名称") }
            item {
                SettingsCard {
                    FloatingShortcutTextSizeSlider("菜单名称大小", labelTextSize) { labelTextSize = it }
                    InsetDivider()
                    ColorPickerRow(
                        title = "菜单名称颜色",
                        summary = "留空时跟随深浅色模式",
                        value = labelColor,
                        onReset = { labelColor = FloatingShortcutSettings.DEFAULT_LABEL_COLOR }
                    ) { labelColor = it.take(19) }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "重置") }
            item {
                SettingsCard {
                    ActionRow(
                        title = "恢复默认外观",
                        summary = "恢复按钮和菜单名称的默认外观",
                        onClick = {
                            bubbleSize = FloatingShortcutSettings.DEFAULT_BUBBLE_SIZE
                            bubbleColor = FloatingShortcutSettings.DEFAULT_BUBBLE_COLOR
                            actionSize = FloatingShortcutSettings.DEFAULT_ACTION_SIZE
                            actionColor = FloatingShortcutSettings.DEFAULT_ACTION_COLOR
                            labelTextSize = FloatingShortcutSettings.DEFAULT_LABEL_TEXT_SIZE
                            labelColor = FloatingShortcutSettings.DEFAULT_LABEL_COLOR
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FloatingShortcutTextSizeSlider(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(
            text = "$title ${value}sp",
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            valueRange = FloatingShortcutSettings.MIN_LABEL_TEXT_SIZE.toFloat()..
                FloatingShortcutSettings.MAX_LABEL_TEXT_SIZE.toFloat(),
            steps = FloatingShortcutSettings.MAX_LABEL_TEXT_SIZE -
                FloatingShortcutSettings.MIN_LABEL_TEXT_SIZE - 1,
            showKeyPoints = true,
            keyPoints = listOf(10f, 12f, 14f, 16f, 18f, 20f, 24f)
        )
    }
}

@Composable
internal fun FloatingShortcutSizeSlider(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(
            text = "$title ${value}dp",
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            valueRange = FloatingShortcutSettings.MIN_BUTTON_SIZE.toFloat()..
                FloatingShortcutSettings.MAX_BUTTON_SIZE.toFloat(),
            steps = FloatingShortcutSettings.MAX_BUTTON_SIZE -
                FloatingShortcutSettings.MIN_BUTTON_SIZE - 1,
            showKeyPoints = true,
            keyPoints = listOf(36f, 44f, 52f, 60f, 64f)
        )
    }
}

@Composable
internal fun FloatingShortcutSettingsItemRow(
    item: FloatingShortcutItem,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FloatingShortcutIconPreview(
                if (isSystemInDarkTheme() && item.darkIconPath.isNotBlank()) item.darkIconPath else item.iconPath,
                item.title,
                defaultGlyph = FloatingShortcutGlyphs.forItem(item)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = item.title,
                    color = MiuixTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when (item.actionType) {
                        FloatingShortcutSettings.ACTION_MODULE_SETTINGS -> "模块设置"
                        else -> item.target
                    },
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Switch(checked = item.enabled, onCheckedChange = onEnabledChange)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingShortcutItemAction(NavIcons.MoveUp, "上移", canMoveUp, onMoveUp)
            FloatingShortcutItemAction(NavIcons.MoveDown, "下移", canMoveDown, onMoveDown)
            FloatingShortcutItemAction(NavIcons.Edit, "编辑", true, onEdit)
            FloatingShortcutItemAction(NavIcons.Delete, "删除", true, onDelete, destructive = true)
        }
    }
}

@Composable
internal fun FloatingShortcutItemAction(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    val color = when {
        !enabled -> MiuixTheme.colorScheme.onSurfaceVariantSummary.copy(alpha = 0.35f)
        destructive -> Color(0xFFD93025)
        else -> MiuixTheme.colorScheme.onSurfaceVariantSummary
    }
    Image(
        imageVector = icon,
        contentDescription = description,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier.padding(start = 4.dp).size(34.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(7.dp)
    )
}

@Composable
internal fun FloatingShortcutIconActionRow(
    title: String,
    iconPath: String,
    defaultSummary: String,
    onSelect: () -> Unit,
    onReset: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingShortcutIconPreview(iconPath, title)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(
                text = if (iconPath.isBlank()) defaultSummary else File(iconPath).name,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        TextButton(
            text = "选择",
            onClick = onSelect,
            colors = ButtonDefaults.textButtonColorsPrimary()
        )
        if (onReset != null) {
            TextButton(
                text = "默认",
                onClick = onReset,
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}

@Composable
internal fun FloatingShortcutIconPreview(
    path: String,
    description: String,
    defaultGlyph: FloatingShortcutGlyph? = null
) {
    val previewSizePx = with(LocalDensity.current) { 40.dp.roundToPx() }
    val bitmap = remember(path, previewSizePx) {
        path.takeIf { it.isNotBlank() }?.let { decodeFavoriteImage(it, previewSizePx) }
    }
    val fallbackTint = MiuixTheme.colorScheme.onSecondaryVariant.toArgb()
    Box(
        modifier = Modifier.size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MiuixTheme.colorScheme.secondaryVariant),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (defaultGlyph != null) {
            AndroidView(
                factory = { viewContext ->
                    ImageView(viewContext).apply { scaleType = ImageView.ScaleType.CENTER_INSIDE }
                },
                update = { imageView ->
                    imageView.contentDescription = description
                    imageView.setImageDrawable(FloatingShortcutGlyphDrawable(defaultGlyph, fallbackTint))
                },
                modifier = Modifier.size(24.dp)
            )
        } else {
            Image(
                imageVector = NavIcons.Practical,
                contentDescription = description,
                colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSecondaryVariant),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
internal fun QuickMarkReadMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, QuickMarkReadSettings.PREFS_NAME) }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "快捷已读") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        QuickMarkReadSettings.KEY_DRAG_READ,
                        "拖拽已读",
                        "在微信底部未读角标向上拖动后，清空全部会话未读",
                        QuickMarkReadSettings.DEFAULT_DRAG_READ
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        QuickMarkReadSettings.KEY_PLUS_MENU_READ,
                        "注入加号菜单已读",
                        "在右上角加号菜单添加“全部已读”，重启微信后生效",
                        QuickMarkReadSettings.DEFAULT_PLUS_MENU_READ
                    )
                }
            }
        }
    }
}

@Composable
internal fun CustomNotificationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { CustomNotificationSettings(context) }
    val sp = remember { HchatStorage.preferences(context, CustomNotificationSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<CustomNotificationRoute>(CustomNotificationRoute.Main) }
    var rules by remember { mutableStateOf(settings.rules()) }
    var defaultPrivateRule by remember { mutableStateOf(settings.defaultPrivateRule()) }
    var defaultGroupRule by remember { mutableStateOf(settings.defaultGroupRule()) }
    var defaultOfficialRule by remember { mutableStateOf(settings.defaultOfficialRule()) }
    val ruleListState = rememberLazyListState()
    var ruleQuery by remember { mutableStateOf("") }
    val ruleEditorListState = rememberLazyListState()
    val defaultEditorListState = rememberLazyListState()

    fun saveRules(next: List<CustomNotificationRule>) {
        val clean = next.distinctBy { it.talker }
        rules = clean
        settings.saveRules(clean)
    }

    fun saveDefaultRule(rule: CustomNotificationRule) {
        when {
            rule.official -> {
                defaultOfficialRule = rule
                settings.saveDefaultOfficialRule(rule)
            }
            rule.group -> {
                defaultGroupRule = rule
                settings.saveDefaultGroupRule(rule)
            }
            else -> {
                defaultPrivateRule = rule
                settings.saveDefaultPrivateRule(rule)
            }
        }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "CustomNotificationRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            CustomNotificationRoute.Main -> CustomNotificationMainPage(
                context = context,
                provider = provider,
                sp = sp,
                rules = rules,
                defaultPrivateRule = defaultPrivateRule,
                defaultGroupRule = defaultGroupRule,
                defaultOfficialRule = defaultOfficialRule,
                onBack = onBack,
                onOpenRules = { route = CustomNotificationRoute.RuleList },
                onEditDefault = { route = CustomNotificationRoute.DefaultEditor(it) }
            )
            is CustomNotificationRoute.DefaultEditor -> {
                val rule = when (currentRoute.kind) {
                    CustomNotificationDefaultKind.PRIVATE -> defaultPrivateRule
                    CustomNotificationDefaultKind.GROUP -> defaultGroupRule
                    CustomNotificationDefaultKind.OFFICIAL -> defaultOfficialRule
                }
                CustomNotificationRuleEditorPage(
                    context = context,
                    initialRule = rule,
                    listState = defaultEditorListState,
                    isDefault = true,
                    onBack = { route = CustomNotificationRoute.Main },
                    onSave = { saved ->
                        saveDefaultRule(saved)
                        Toast.makeText(context, "默认规则已保存", Toast.LENGTH_SHORT).show()
                        route = CustomNotificationRoute.Main
                    },
                    onDelete = {},
                    onPickOnlyMembers = {},
                    onPickBlockMembers = {}
                )
            }
            CustomNotificationRoute.RuleList -> CustomNotificationRuleListPage(
                context = context,
                rules = rules,
                query = ruleQuery,
                onQueryChange = { ruleQuery = it },
                listState = ruleListState,
                onBack = { route = CustomNotificationRoute.Main },
                onSaveRules = ::saveRules,
                onEditRule = { route = CustomNotificationRoute.RuleEditor(it) },
                onToggleRule = { rule, enabled ->
                    saveRules(rules.map { if (it.talker == rule.talker) it.copy(enabled = enabled) else it })
                    Toast.makeText(context, if (enabled) "已启用该会话规则" else "已关闭该会话规则", Toast.LENGTH_SHORT).show()
                },
                onBatchEdit = { route = CustomNotificationRoute.BatchEditor },
                onAddRules = {
                    route = CustomNotificationRoute.ContactPicker(
                        ContactPickerRequest(
                            title = "添加通知会话",
                            mode = ContactPickerMode.ALL_CHATS,
                            multiSelect = true,
                            existingValue = "",
                            onValue = {},
                            enableLabels = true
                        )
                    )
                }
            )
            CustomNotificationRoute.BatchEditor -> CustomNotificationBatchEditorPage(
                ruleCount = rules.size,
                onBack = { route = CustomNotificationRoute.RuleList },
                onApply = { update ->
                    if (!update.hasChanges()) {
                        Toast.makeText(context, "请选择要应用的配置", Toast.LENGTH_SHORT).show()
                    } else {
                        saveRules(rules.map { rule -> update.apply(rule) })
                        Toast.makeText(context, "批量配置已应用", Toast.LENGTH_SHORT).show()
                        route = CustomNotificationRoute.RuleList
                    }
                }
            )
            is CustomNotificationRoute.RuleEditor -> {
                val rule = rules.firstOrNull { it.talker == currentRoute.talker }
                if (rule == null) {
                    CustomNotificationMissingRulePage(
                        onBack = { route = CustomNotificationRoute.RuleList }
                    )
                } else {
                    CustomNotificationRuleEditorPage(
                        context = context,
                        initialRule = rule,
                        listState = ruleEditorListState,
                        onBack = { route = CustomNotificationRoute.RuleList },
                        onSave = { saved ->
                            saveRules(rules.map { if (it.talker == saved.talker) saved else it })
                            Toast.makeText(context, "规则已保存", Toast.LENGTH_SHORT).show()
                            route = CustomNotificationRoute.RuleList
                        },
                        onDelete = {
                            saveRules(rules.filterNot { it.talker == rule.talker })
                            Toast.makeText(context, "规则已删除", Toast.LENGTH_SHORT).show()
                            route = CustomNotificationRoute.RuleList
                        },
                        onPickOnlyMembers = { value ->
                            route = CustomNotificationRoute.GroupMemberPicker(
                                talker = rule.talker,
                                request = GroupMemberPickerRequest(
                                    title = "仅显示成员通知",
                                    existingValue = memberRulesToGroupEntries(rule.talker, value),
                                    onValue = {}
                                )
                            )
                        },
                        onPickBlockMembers = { value ->
                            route = CustomNotificationRoute.GroupMemberPicker(
                                talker = rule.talker,
                                request = GroupMemberPickerRequest(
                                    title = "屏蔽成员通知",
                                    existingValue = memberRulesToGroupEntries(rule.talker, value),
                                    onValue = {}
                                )
                            )
                        }
                    )
                }
            }
            is CustomNotificationRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = CustomNotificationRoute.RuleList },
                onConfirm = { picked ->
                    if (picked.isEmpty()) {
                        Toast.makeText(context, "未选择会话", Toast.LENGTH_SHORT).show()
                        route = CustomNotificationRoute.RuleList
                    } else {
                        val byTalker = rules.associateBy { it.talker }.toMutableMap()
                        var added = 0
                        var updated = 0
                        picked.forEach { option ->
                            val old = byTalker[option.id]
                            if (old == null) {
                                added++
                                byTalker[option.id] = customNotificationRuleFromContact(option)
                            } else {
                                updated++
                                byTalker[option.id] = old.copy(
                                    label = option.label,
                                    group = option.group,
                                    official = option.official
                                )
                            }
                        }
                        saveRules(byTalker.values.sortedWith(compareBy<CustomNotificationRule> { !it.enabled }.thenBy { it.label.lowercase(Locale.US) }))
                        val message = when {
                            added > 0 && updated > 0 -> "已添加 $added 个会话，更新 $updated 个会话"
                            added > 0 -> "已添加 $added 个会话"
                            else -> "已更新 $updated 个会话"
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        route = CustomNotificationRoute.RuleList
                    }
                }
            )
            is CustomNotificationRoute.GroupMemberPicker -> GroupMemberPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = CustomNotificationRoute.RuleEditor(currentRoute.talker) },
                onConfirm = { entries ->
                    val talker = currentRoute.talker
                    val memberValue = groupEntriesToMemberRules(talker, entries)
                    val title = currentRoute.request.title
                    rules.firstOrNull { it.talker == talker }?.let { old ->
                        val nextRule = if (title.contains("屏蔽")) {
                            old.copy(blockMembers = memberValue)
                        } else {
                            old.copy(onlyMembers = memberValue)
                        }
                        saveRules(rules.map { if (it.talker == talker) nextRule else it })
                        Toast.makeText(context, "成员规则已保存", Toast.LENGTH_SHORT).show()
                    }
                    route = CustomNotificationRoute.RuleEditor(talker)
                }
            )
        }
    }
}

@Composable
internal fun AutoReplyMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, AutoReplySettings.PREFS_NAME) }
    val settings = remember { AutoReplySettings(context) }
    var rules by remember { mutableStateOf(settings.rules()) }
    var route by remember { mutableStateOf("main") }
    var editingRuleId by remember { mutableStateOf<String?>(null) }
    var editingRuleDraft by remember { mutableStateOf<AutoReplyRule?>(null) }
    var editingSteps by remember { mutableStateOf<Pair<String, List<AutoReplyStep>>?>(null) }
    val ruleListState = rememberLazyListState()
    var ruleQuery by remember { mutableStateOf("") }
    val ruleEditorListState = rememberLazyListState()
    val autoAcceptListState = rememberLazyListState()
    val greetAcceptedListState = rememberLazyListState()
    val autoAcceptLabelListState = rememberLazyListState()
    val greetAcceptedLabelListState = rememberLazyListState()
    val autoAcceptAutomationKeys = remember {
        AutoReplyFriendAutomationKeys(
            AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_NEW_FRIEND_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_DATE_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_DATE_FORMAT,
            AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_EXISTING_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_SELECTED_NAMES,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_NEW_FRIEND_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_NICKNAME_SUFFIX_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_DATE_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_DATE_FORMAT,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_CUSTOM_ENABLE,
            AutoReplySettings.KEY_AUTO_ACCEPT_REMARK_CUSTOM_TEXT
        )
    }
    val greetAcceptedAutomationKeys = remember {
        AutoReplyFriendAutomationKeys(
            AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_NEW_FRIEND_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_DATE_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_DATE_FORMAT,
            AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_EXISTING_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_SELECTED_NAMES,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_NEW_FRIEND_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_NICKNAME_SUFFIX_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_DATE_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_DATE_FORMAT,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_CUSTOM_ENABLE,
            AutoReplySettings.KEY_GREET_ACCEPTED_REMARK_CUSTOM_TEXT
        )
    }

    fun saveRules(next: List<AutoReplyRule>) {
        rules = next
        settings.saveRules(next)
    }

    val autoReplyRoute = if (editingSteps != null) "steps" else route
    SettingsRouteTransition(
        targetState = autoReplyRoute,
        label = "AutoReplyRouteTransition",
        depthOf = { autoReplyRouteDepth(it) }
    ) { currentRoute ->
        when (currentRoute) {
            "steps" -> {
                val request = editingSteps ?: return@SettingsRouteTransition
            AutoReplyStepsPage(
                title = request.first,
                initialSteps = request.second,
                onBack = { editingSteps = null },
                onSave = { steps ->
                    when (route) {
                        "autoAccept" -> settings.saveAutoAcceptSteps(steps)
                        "greetAccepted" -> settings.saveGreetAcceptedSteps(steps)
                        else -> {
                            val id = editingRuleId
                            if (id != null) {
                                val updated = (editingRuleDraft ?: rules.firstOrNull { it.id == id })
                                    ?.copy(steps = steps)
                                if (updated != null) {
                                    editingRuleDraft = updated
                                    saveRules(rules.map { if (it.id == id) updated else it })
                                }
                            }
                        }
                    }
                    Toast.makeText(context, "回复步骤已保存", Toast.LENGTH_SHORT).show()
                    editingSteps = null
                }
            )
            }
        "rules" -> AutoReplyRuleListPage(
            rules = rules,
            query = ruleQuery,
            onQueryChange = { ruleQuery = it },
            listState = ruleListState,
            onBack = { route = "main" },
            onAdd = {
                val rule = AutoReplyRule(
                    id = System.currentTimeMillis().toString(),
                    name = "规则 ${rules.size + 1}"
                )
                saveRules(rules + rule)
                editingRuleId = rule.id
                editingRuleDraft = rule
                route = "ruleEditor"
            },
            onEdit = {
                editingRuleId = it.id
                editingRuleDraft = it
                route = "ruleEditor"
            },
            onToggle = { rule, enabled -> saveRules(rules.map { if (it.id == rule.id) it.copy(enabled = enabled) else it }) },
            onDelete = { rule ->
                saveRules(rules.filterNot { it.id == rule.id })
                Toast.makeText(context, "规则已删除", Toast.LENGTH_SHORT).show()
            },
            onBatchDelete = { targets ->
                val targetIds = targets.mapTo(HashSet()) { it.id }
                saveRules(rules.filterNot { it.id in targetIds })
                Toast.makeText(context, "已删除 ${targets.size} 条规则", Toast.LENGTH_SHORT).show()
            }
        )
        "ruleEditor" -> {
            val rule = editingRuleDraft ?: rules.firstOrNull { it.id == editingRuleId }
            if (rule == null) {
                editingRuleDraft = null
                route = "rules"
            } else {
                AutoReplyRuleEditorPage(
                    rule = rule,
                    listState = ruleEditorListState,
                    onBack = {
                        editingRuleDraft = null
                        route = "rules"
                    },
                    onSave = { saved ->
                        saveRules(rules.map { if (it.id == saved.id) saved else it })
                        editingRuleDraft = null
                        Toast.makeText(context, "规则已保存", Toast.LENGTH_SHORT).show()
                        route = "rules"
                    },
                    onDraftChange = { editingRuleDraft = it },
                    onEditSteps = { draft ->
                        editingRuleDraft = draft
                        editingSteps = "规则回复" to draft.steps
                    }
                )
            }
        }
        "autoAccept" -> AutoReplyFriendPage(
            context = context,
            title = "好友请求处理",
            sp = sp,
            enabledKey = AutoReplySettings.KEY_AUTO_ACCEPT_ENABLE,
            delayKey = AutoReplySettings.KEY_AUTO_ACCEPT_DELAY_MS,
            tagEnabledKey = AutoReplySettings.KEY_AUTO_ACCEPT_TAG_ENABLE,
            tagNameKey = AutoReplySettings.KEY_AUTO_ACCEPT_TAG_NAME,
            steps = settings.autoAcceptSteps(),
            listState = autoAcceptListState,
            onBack = { route = "main" },
            automationKeys = autoAcceptAutomationKeys,
            onSelectLabels = { route = "autoAcceptLabels" },
            onEditSteps = { editingSteps = "好友请求回复" to settings.autoAcceptSteps() }
        )
        "autoAcceptLabels" -> AutoReplyLabelPickerPage(
            context = context,
            sp = sp,
            selectedKey = AutoReplySettings.KEY_AUTO_ACCEPT_LABEL_SELECTED_NAMES,
            listState = autoAcceptLabelListState,
            onBack = { route = "autoAccept" }
        )
        "greetAccepted" -> AutoReplyFriendPage(
            context = context,
            title = "通过后欢迎语",
            sp = sp,
            enabledKey = AutoReplySettings.KEY_GREET_ACCEPTED_ENABLE,
            delayKey = AutoReplySettings.KEY_GREET_ACCEPTED_DELAY_MS,
            tagEnabledKey = AutoReplySettings.KEY_GREET_ACCEPTED_TAG_ENABLE,
            tagNameKey = AutoReplySettings.KEY_GREET_ACCEPTED_TAG_NAME,
            steps = settings.greetAcceptedSteps(),
            listState = greetAcceptedListState,
            onBack = { route = "main" },
            automationKeys = greetAcceptedAutomationKeys,
            onSelectLabels = { route = "greetAcceptedLabels" },
            onEditSteps = { editingSteps = "通过后欢迎语" to settings.greetAcceptedSteps() }
        )
        "greetAcceptedLabels" -> AutoReplyLabelPickerPage(
            context = context,
            sp = sp,
            selectedKey = AutoReplySettings.KEY_GREET_ACCEPTED_LABEL_SELECTED_NAMES,
            listState = greetAcceptedLabelListState,
            onBack = { route = "greetAccepted" }
        )
        "ai" -> AutoReplyAiPage(context, sp, onBack = { route = "main" })
        else -> AutoReplyMainPage(
            provider = provider,
            sp = sp,
            rules = rules,
            onBack = onBack,
            onOpenRules = { route = "rules" },
            onOpenAutoAccept = { route = "autoAccept" },
            onOpenGreetAccepted = { route = "greetAccepted" },
            onOpenAi = { route = "ai" }
        )
        }
    }
}

@Composable
internal fun AutoReplyMainPage(
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    rules: List<AutoReplyRule>,
    onBack: () -> Unit,
    onOpenRules: () -> Unit,
    onOpenAutoAccept: () -> Unit,
    onOpenGreetAccepted: () -> Unit,
    onOpenAi: () -> Unit
) {
    var excludedTalkersEnabled by remember {
        mutableStateOf(sp.getBoolean(AutoReplySettings.KEY_EXCLUDED_TALKERS_ENABLE, false))
    }
    var excludedTalkers by remember {
        mutableStateOf(sp.getString(AutoReplySettings.KEY_EXCLUDED_TALKERS, "").orEmpty())
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "聊天自动回复") }
            item {
                SettingsCard {
                    SwitchRow(sp, AutoReplySettings.KEY_ENABLE, "启用自动回复", "开启后按规则回复收到的消息", AutoReplySettings.DEFAULT_ENABLE)
                    InsetDivider()
                    SwitchRow(excludedTalkersEnabled, "排除指定会话", "开启后不回复指定会话的消息") {
                        excludedTalkersEnabled = it
                        sp.edit().putBoolean(AutoReplySettings.KEY_EXCLUDED_TALKERS_ENABLE, it).apply()
                    }
                    if (excludedTalkersEnabled) {
                        InsetDivider()
                        InputRow("排除会话 ID", "多个 ID 用逗号、分号或换行分隔", excludedTalkers, minLines = 2) {
                            excludedTalkers = it
                            sp.edit().putString(AutoReplySettings.KEY_EXCLUDED_TALKERS, it).apply()
                        }
                    }
                    InsetDivider()
                    ActionRow("回复规则", if (rules.isEmpty()) "暂无规则" else "${rules.size} 条规则，${rules.count { it.enabled }} 条启用", onOpenRules)
                    InsetDivider()
                    ActionRow("好友请求处理", "自动同意好友申请并按步骤发送欢迎内容", onOpenAutoAccept)
                    InsetDivider()
                    ActionRow("通过后欢迎语", "对方通过你的好友请求后发送欢迎内容", onOpenGreetAccepted)
                    InsetDivider()
                    ActionRow("AI 配置", "配置小智AI和智聊AI，多智聊模型可切换", onOpenAi)
                }
            }
        }
    }
}

@Composable
internal fun AutoReplyRuleListPage(
    rules: List<AutoReplyRule>,
    query: String,
    onQueryChange: (String) -> Unit,
    listState: LazyListState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (AutoReplyRule) -> Unit,
    onToggle: (AutoReplyRule, Boolean) -> Unit,
    onDelete: (AutoReplyRule) -> Unit,
    onBatchDelete: (List<AutoReplyRule>) -> Unit
) {
    val context = LocalContext.current
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val visible = rules.filter {
        val lower = query.trim().lowercase(Locale.US)
        lower.isBlank() ||
            it.name.lowercase(Locale.US).contains(lower) ||
            it.keyword.lowercase(Locale.US).contains(lower) ||
            it.excludedKeywords.lowercase(Locale.US).contains(lower)
    }
    val visibleIds = visible.mapTo(LinkedHashSet()) { it.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedRules = rules.filter { it.id in selectedIds }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "回复规则",
        largeTitle = "回复规则",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedRules.size}）",
                    onPrimaryClick = {
                        if (selectedRules.isEmpty()) {
                            Toast.makeText(context, "请先选择规则", Toast.LENGTH_SHORT).show()
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
                    primaryText = "新增规则",
                    onPrimaryClick = onAdd,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
                    middleText = if (rules.isEmpty()) null else "批量删除",
                    onMiddleClick = if (rules.isEmpty()) null else {
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
            state = listState,
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
        ) {
            item {
                SettingsCard {
                    InputRow("搜索", "按规则名或关键词筛选", query, onValueChange = onQueryChange)
                }
            }
            if (visible.isEmpty()) {
                item { SettingsCard { EmptyText("暂无规则") } }
            } else {
                visible.forEachIndexed { index, rule ->
                    item {
                        SmallTitle(
                            modifier = Modifier.padding(top = if (index == 0) 10.dp else 18.dp),
                            text = "规则 ${index + 1}"
                        )
                    }
                    item {
                        SettingsCard {
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = rule.name.ifBlank { "未命名规则" },
                                        value = index,
                                        summary = describeAutoReplyRule(rule)
                                    ),
                                    selected = rule.id in selectedIds,
                                    onClick = {
                                        selectedIds = if (rule.id in selectedIds) {
                                            selectedIds - rule.id
                                        } else {
                                            selectedIds + rule.id
                                        }
                                    }
                                )
                            } else {
                                SwitchRow(
                                    checked = rule.enabled,
                                    title = rule.name.ifBlank { "未命名规则" },
                                    summary = describeAutoReplyRule(rule),
                                    onCheckedChange = { onToggle(rule, it) }
                                )
                                InsetDivider()
                                ActionRow("编辑", "修改触发条件和回复步骤") { onEdit(rule) }
                                InsetDivider()
                                ActionRow("删除", "移除此规则") { onDelete(rule) }
                            }
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedRules.size} 条回复规则，此操作不可撤销。",
        labels = selectedRules.map { it.name.ifBlank { "未命名规则" } },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedRules
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedIds = emptySet()
            onBatchDelete(targets)
        }
    )
}

