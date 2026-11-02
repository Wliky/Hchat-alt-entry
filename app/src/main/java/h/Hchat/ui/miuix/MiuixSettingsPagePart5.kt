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
internal fun ZombieCheckResultRow(name: String, summary: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            color = MiuixTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = summary,
            modifier = Modifier.fillMaxWidth(),
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
    }
}

@Composable
internal fun QuoteDeleteClearMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, QuoteDeleteClearSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "聊天输入框") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        QuoteDeleteClearSettings.KEY_ENABLE,
                        "删除键清引用",
                        "输入框为空且已引用消息时，按输入法删除键直接取消引用",
                        QuoteDeleteClearSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun EmojiSaveMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, EmojiSaveSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "聊天表情") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        EmojiSaveSettings.KEY_ENABLE,
                        "保存表情",
                        "长按聊天表情后显示保存入口",
                        EmojiSaveSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun SwipeQuoteMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, SwipeQuoteSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "引用") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_ENABLE,
                        "左滑引用回复",
                        "左滑消息后调用微信原生引用入口",
                        SwipeQuoteSettings.DEFAULT_ENABLE
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "复读触发方式") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_REPEAT_ENABLE,
                        "右滑复读",
                        "右滑消息后复读到当前聊天",
                        SwipeQuoteSettings.DEFAULT_REPEAT_ENABLE
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        SwipeQuoteSettings.KEY_REPEAT_MENU_ENABLE,
                        "长按菜单复读",
                        "长按消息后点击 +1 复读到当前聊天",
                        SwipeQuoteSettings.DEFAULT_REPEAT_MENU_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun FakeVoiceDurationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeVoiceDurationSettings.PREFS_NAME) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                FakeVoiceDurationSettings.KEY_ENABLE,
                FakeVoiceDurationSettings.DEFAULT_ENABLE
            )
        )
    }
    var durationSeconds by remember {
        mutableStateOf(
            sp.getInt(
                FakeVoiceDurationSettings.KEY_DURATION_SECONDS,
                FakeVoiceDurationSettings.DEFAULT_DURATION_SECONDS
            ).toString()
        )
    }

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
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "伪造语音时长",
                        summary = "发送语音时使用自定义显示时长",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(FakeVoiceDurationSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        NumberInputRow("显示时长", "单位秒，1-60", durationSeconds) { next ->
                            val value = next.toIntOrNull()
                            durationSeconds = value?.coerceIn(
                                FakeVoiceDurationSettings.MIN_DURATION_SECONDS,
                                FakeVoiceDurationSettings.MAX_DURATION_SECONDS
                            )?.toString() ?: next
                            if (value == null) return@NumberInputRow
                            sp.edit().putInt(
                                FakeVoiceDurationSettings.KEY_DURATION_SECONDS,
                                value.coerceIn(
                                    FakeVoiceDurationSettings.MIN_DURATION_SECONDS,
                                    FakeVoiceDurationSettings.MAX_DURATION_SECONDS
                                )
                            ).apply()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun VoicePreviewMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, VoicePreviewSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoicePreviewSettings.KEY_ENABLE,
                        "语音消息预览",
                        "长按聊天语音后查看时长并播放",
                        VoicePreviewSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun SkipMiniProgramVideoAdsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember {
        HchatStorage.preferences(context, SkipMiniProgramVideoAdsSettings.PREFS_NAME)
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SkipMiniProgramVideoAdsSettings.KEY_ENABLE,
                SkipMiniProgramVideoAdsSettings.DEFAULT_ENABLE
            )
        )
    }
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
            item { SmallTitle(text = "小程序") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "跳过小程序视频广告",
                        summary = "自动跳过小程序视频广告",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(
                                SkipMiniProgramVideoAdsSettings.KEY_ENABLE,
                                it
                            ).apply()
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun SkipGlobalMiniProgramSplashAdsMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember {
        HchatStorage.preferences(context, SkipGlobalMiniProgramSplashAdsSettings.PREFS_NAME)
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                SkipGlobalMiniProgramSplashAdsSettings.KEY_ENABLE,
                SkipGlobalMiniProgramSplashAdsSettings.DEFAULT_ENABLE
            )
        )
    }
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
            item { SmallTitle(text = "小程序") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "跳过全局小程序开屏广告",
                        summary = "阻止所有小程序展示启动开屏广告，修改后需重启微信",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(
                                SkipGlobalMiniProgramSplashAdsSettings.KEY_ENABLE,
                                it
                            ).apply()
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FakeLocationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeLocationSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(FakeLocationSettings.KEY_ENABLE, FakeLocationSettings.DEFAULT_ENABLE))
    }
    var latitude by remember {
        mutableStateOf(sp.getString(FakeLocationSettings.KEY_LATITUDE, FakeLocationSettings.DEFAULT_LATITUDE).orEmpty())
    }
    var longitude by remember {
        mutableStateOf(sp.getString(FakeLocationSettings.KEY_LONGITUDE, FakeLocationSettings.DEFAULT_LONGITUDE).orEmpty())
    }
    var showManualInput by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    fun saveLocation(lat: Double, lon: Double) {
        latitude = formatFakeLocationCoordinate(lat)
        longitude = formatFakeLocationCoordinate(lon)
        sp.edit()
            .putString(FakeLocationSettings.KEY_LATITUDE, latitude)
            .putString(FakeLocationSettings.KEY_LONGITUDE, longitude)
            .apply()
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
            item { SmallTitle(text = "定位") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用虚拟定位",
                        summary = "同时覆盖微信和小程序获取的位置",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(FakeLocationSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    InsetDivider()
                    ActionRow("搜索并选择位置", "打开微信选择位置页，可搜索地点") {
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开微信地图", Toast.LENGTH_SHORT).show()
                        } else {
                            NativeFakeLocationPickerBridge.launch(activity) { lat, lon ->
                                saveLocation(lat, lon)
                                Toast.makeText(context, "已保存虚拟定位", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    InsetDivider()
                    ActionRow("手动输入经纬度", "纬度 -90~90，经度 -180~180") {
                        showManualInput = true
                    }
                    InsetDivider()
                    InfoRow("当前纬度", latitude) {
                        copyFakeLocationCoordinate(context, "Hchat 纬度", latitude)
                    }
                    InsetDivider()
                    InfoRow("当前经度", longitude) {
                        copyFakeLocationCoordinate(context, "Hchat 经度", longitude)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "说明") }
            item {
                SettingsCard {
                    InfoRow("生效范围", "微信和小程序定位")
                }
            }
        }
    }

    if (showManualInput) {
        FakeLocationManualInputDialog(
            initialLatitude = latitude,
            initialLongitude = longitude,
            onDismiss = { showManualInput = false },
            onConfirm = { lat, lon ->
                saveLocation(lat, lon)
                showManualInput = false
                Toast.makeText(context, "已保存虚拟定位", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
internal fun GameEmojiMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, GameEmojiSettings.PREFS_NAME) }
    var fixedResult by remember {
        mutableStateOf(
            sp.getBoolean(
                GameEmojiSettings.KEY_FIXED_RESULT,
                GameEmojiSettings.DEFAULT_FIXED_RESULT
            )
        )
    }
    var pickBeforeSend by remember {
        mutableStateOf(
            sp.getBoolean(
                GameEmojiSettings.KEY_PICK_BEFORE_SEND,
                GameEmojiSettings.DEFAULT_PICK_BEFORE_SEND
            )
        )
    }
    var diceResult by remember {
        mutableStateOf(
            sp.getInt(GameEmojiSettings.KEY_DICE_RESULT, GameEmojiSettings.DEFAULT_DICE_RESULT)
                .coerceIn(1, 6)
        )
    }
    var rpsResult by remember {
        mutableStateOf(
            sp.getInt(GameEmojiSettings.KEY_RPS_RESULT, GameEmojiSettings.DEFAULT_RPS_RESULT)
                .coerceIn(GameEmojiSettings.RPS_SCISSORS, GameEmojiSettings.RPS_PAPER)
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
            item { SmallTitle(text = "发送方式") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = fixedResult,
                        title = "使用固定结果",
                        summary = "直接发送下方设置的骰子点数或猜拳结果",
                        onCheckedChange = { checked ->
                            fixedResult = checked
                            if (checked) pickBeforeSend = false
                            sp.edit()
                                .putBoolean(GameEmojiSettings.KEY_FIXED_RESULT, checked)
                                .putBoolean(
                                    GameEmojiSettings.KEY_PICK_BEFORE_SEND,
                                    if (checked) false else pickBeforeSend
                                )
                                .apply()
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = pickBeforeSend,
                        title = "发送时选择",
                        summary = "每次发送骰子或猜拳前弹出结果选择",
                        onCheckedChange = { checked ->
                            pickBeforeSend = checked
                            if (checked) fixedResult = false
                            sp.edit()
                                .putBoolean(GameEmojiSettings.KEY_PICK_BEFORE_SEND, checked)
                                .putBoolean(
                                    GameEmojiSettings.KEY_FIXED_RESULT,
                                    if (checked) false else fixedResult
                                )
                                .apply()
                        }
                    )
                }
            }
            if (fixedResult) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "固定结果") }
                item {
                    SettingsCard {
                        PopupOptionRow(
                            title = "骰子点数",
                            summary = "$diceResult 点",
                            options = (1..6).map { OptionItem("$it 点", it, "") },
                            currentValue = diceResult,
                            onValueChanged = { value ->
                                diceResult = value
                                sp.edit().putInt(GameEmojiSettings.KEY_DICE_RESULT, value).apply()
                            }
                        )
                        InsetDivider()
                        PopupOptionRow(
                            title = "猜拳结果",
                            summary = GameEmojiSettings.rpsLabel(rpsResult),
                            options = listOf(
                                OptionItem("剪刀", GameEmojiSettings.RPS_SCISSORS, ""),
                                OptionItem("石头", GameEmojiSettings.RPS_ROCK, ""),
                                OptionItem("布", GameEmojiSettings.RPS_PAPER, "")
                            ),
                            currentValue = rpsResult,
                            onValueChanged = { value ->
                                rpsResult = value
                                sp.edit().putInt(GameEmojiSettings.KEY_RPS_RESULT, value).apply()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun FakeLocationManualInputDialog(
    initialLatitude: String,
    initialLongitude: String,
    onDismiss: () -> Unit,
    onConfirm: (Double, Double) -> Unit
) {
    var latitude by remember(initialLatitude) { mutableStateOf(initialLatitude) }
    var longitude by remember(initialLongitude) { mutableStateOf(initialLongitude) }
    val parsed = validFakeLocation(latitude.toDoubleOrNull(), longitude.toDoubleOrNull())
    WindowDialog(
        show = true,
        title = "手动输入经纬度",
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InputRow("纬度", "范围 -90 到 90", latitude) { latitude = cleanCoordinateInput(it) }
                InputRow("经度", "范围 -180 到 180", longitude) { longitude = cleanCoordinateInput(it) }
                if (parsed == null) {
                    Text(
                        text = "请输入有效的纬度和经度",
                        color = Color(0xFFD93025),
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "保存",
                        onClick = { parsed?.let { onConfirm(it.first, it.second) } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        }
    )
}

internal fun cleanCoordinateInput(value: String): String {
    return value.filterIndexed { index, char ->
        char.isDigit() || char == '.' || (char == '-' && index == 0)
    }
}

internal fun validFakeLocation(latitude: Double?, longitude: Double?): Pair<Double, Double>? {
    if (latitude == null || longitude == null || !latitude.isFinite() || !longitude.isFinite()) return null
    if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return null
    return latitude to longitude
}

internal fun parseNativeFakeLocation(text: String): Pair<Double, Double>? {
    val latitude = Regex("""(?:lat|latitude)\s*[:= ]\s*([-+]?[0-9]*\.?[0-9]+)""", RegexOption.IGNORE_CASE)
        .find(text)?.groupValues?.getOrNull(1)?.toDoubleOrNull()
    val longitude = Regex("""(?:lng|lon|longitude)\s*[:= ]\s*([-+]?[0-9]*\.?[0-9]+)""", RegexOption.IGNORE_CASE)
        .find(text)?.groupValues?.getOrNull(1)?.toDoubleOrNull()
    return validFakeLocation(latitude, longitude)
}

internal fun formatFakeLocationCoordinate(value: Double): String {
    return String.format(Locale.US, "%.6f", value)
}

internal fun copyFakeLocationCoordinate(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
}

@Composable
internal fun HideChatAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, HideChatAvatarSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "聊天头像") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        HideChatAvatarSettings.KEY_HIDE_SELF,
                        "隐藏自己的头像",
                        "在群聊和私聊中隐藏自己发送消息的头像",
                        HideChatAvatarSettings.DEFAULT_HIDE_SELF
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        HideChatAvatarSettings.KEY_HIDE_OTHER,
                        "隐藏对方的头像",
                        "在群聊和私聊中隐藏对方发送消息的头像",
                        HideChatAvatarSettings.DEFAULT_HIDE_OTHER
                    )
                }
            }
        }
    }
}

@Composable
internal fun RoundAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RoundAvatarSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(RoundAvatarSettings.KEY_ENABLE, RoundAvatarSettings.DEFAULT_ENABLE))
    }
    var radiusFactor by remember {
        mutableStateOf(
            RoundAvatarSettings.normalizeRadiusFactor(
                sp.getFloat(RoundAvatarSettings.KEY_RADIUS_FACTOR, RoundAvatarSettings.DEFAULT_RADIUS_FACTOR)
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
            item { SmallTitle(text = "全局头像") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用圆角头像",
                        summary = "统一应用到微信界面和通知头像",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(RoundAvatarSettings.KEY_ENABLE, it).apply()
                        }
                    )
                    if (enabled) {
                        InsetDivider()
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                text = "圆角弧度 ${RoundAvatarSettings.displayPercent(radiusFactor)}%",
                                color = MiuixTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            Slider(
                                value = radiusFactor,
                                onValueChange = {
                                    radiusFactor = RoundAvatarSettings.normalizeRadiusFactor(it)
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                valueRange = RoundAvatarSettings.MIN_RADIUS_FACTOR..RoundAvatarSettings.MAX_RADIUS_FACTOR,
                                steps = 39,
                                onValueChangeFinished = {
                                    sp.edit()
                                        .putFloat(
                                            RoundAvatarSettings.KEY_RADIUS_FACTOR,
                                            RoundAvatarSettings.normalizeRadiusFactor(radiusFactor)
                                        )
                                        .apply()
                                },
                                showKeyPoints = true,
                                keyPoints = listOf(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

internal enum class CustomFriendAvatarRoute {
    MAIN,
    CONTACT_PICKER
}

@Composable
internal fun CustomFriendAvatarMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, CustomFriendAvatarSettings.PREFS_NAME) }
    var enabled by remember {
        mutableStateOf(sp.getBoolean(CustomFriendAvatarSettings.KEY_ENABLE, CustomFriendAvatarSettings.DEFAULT_ENABLE))
    }
    var route by remember { mutableStateOf(CustomFriendAvatarRoute.MAIN) }
    var selectedWxid by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(emptyList<ContactOption>()) }
    var revision by remember { mutableStateOf(0) }

    LaunchedEffect(revision) {
        loadContacts(context, ContactPickerMode.BOTH, false) { result, _ -> contacts = result }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "custom_friend_avatar_route",
        depthOf = { if (it == CustomFriendAvatarRoute.MAIN) 0 else 1 }
    ) { current ->
        when (current) {
            CustomFriendAvatarRoute.MAIN -> {
                val selected = contacts.firstOrNull { it.id == selectedWxid }
                val configuredIds = remember(revision) { CustomFriendAvatarStore.configuredFriends(context) }
                val configured = contacts.filter { it.id in configuredIds }
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
                        item { SmallTitle(text = "自定义头像") }
                        item {
                            SettingsCard {
                                SwitchRow(
                                    checked = enabled,
                                    title = "启用自定义头像",
                                    summary = "仅修改本机显示，不会更改微信中的原头像",
                                    onCheckedChange = {
                                        enabled = it
                                        sp.edit().putBoolean(CustomFriendAvatarSettings.KEY_ENABLE, it).apply()
                                    }
                                )
                                if (enabled) {
                                    InsetDivider()
                                    ActionRow(
                                        title = "选择好友或群聊",
                                        summary = selected?.label ?: "选择要设置头像的好友或群聊",
                                        onClick = { route = CustomFriendAvatarRoute.CONTACT_PICKER }
                                    )
                                    if (selected != null) {
                                        InsetDivider()
                                        ActionRow(
                                            title = if (CustomFriendAvatarStore.hasAvatar(context, selected.id)) "更换头像" else "设置头像",
                                            summary = "从系统相册或文件中选择图片",
                                            onClick = {
                                                val activity = context as? Activity
                                                if (activity == null) {
                                                    Toast.makeText(context, "当前页面无法打开图片选择器", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    CustomFriendAvatarPicker.launch(activity, selected.id) { success ->
                                                        Toast.makeText(
                                                            context,
                                                            if (success) "自定义头像已保存" else "头像设置失败",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        if (success) revision++
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        if (enabled) {
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "应用范围") }
                            item {
                                SettingsCard {
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CHAT, "聊天消息", "替换群聊和私聊消息头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONVERSATION, "会话列表", "替换微信首页的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONTACTS, "通讯录", "替换通讯录中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_PROFILE, "资料页", "替换好友和群聊资料页中的头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_MOMENTS, "朋友圈", "替换朋友圈中的好友头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_OTHER_UI, "其他微信界面", "替换收藏、搜索等界面中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_DESKTOP_SHORTCUT, "桌面快捷方式", "替换添加到桌面的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_NOTIFICATIONS, "消息通知", "替换微信原生通知和 Hchat 通知中的好友和群聊头像", true)
                                    InsetDivider()
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_MOMENTS_NOTIFICATIONS, "朋友圈通知", "替换朋友圈发布通知中的好友头像", true)
                                }
                            }
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "快捷入口") }
                            item {
                                SettingsCard {
                                    SwitchRow(sp, CustomFriendAvatarSettings.KEY_CONVERSATION_MENU, "会话列表长按菜单", "长按好友或群聊会话时显示设置头像入口，聊天分组内同样生效", true)
                                }
                            }
                            if (configured.isNotEmpty()) {
                                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "已设置头像") }
                                item {
                                    SettingsCard {
                                        configured.forEachIndexed { index, option ->
                                            CustomFriendAvatarRow(context, option, revision) {
                                                selectedWxid = option.id
                                                val activity = context as? Activity
                                                if (activity != null) {
                                                    VoiceForwardMiuixDialog.showChoices(
                                                        activity = activity,
                                                        title = option.label,
                                                        summary = "管理自定义头像",
                                                        choices = listOf(
                                                            "更换头像" to "重新选择一张本地图片",
                                                            "恢复微信头像" to "删除当前自定义头像"
                                                        ),
                                                        onSelected = { choice ->
                                                            if (choice == 0) {
                                                                CustomFriendAvatarPicker.launch(activity, option.id) { success ->
                                                                    if (success) revision++
                                                                }
                                                            } else {
                                                                CustomFriendAvatarStore.remove(context, option.id)
                                                                revision++
                                                            }
                                                        },
                                                        onDismiss = {}
                                                    )
                                                }
                                            }
                                            if (index != configured.lastIndex) InsetDivider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            CustomFriendAvatarRoute.CONTACT_PICKER -> ContactPickerPage(
                context = context,
                request = ContactPickerRequest(
                    title = "选择好友或群聊",
                    mode = ContactPickerMode.BOTH,
                    multiSelect = false,
                    existingValue = selectedWxid,
                    onValue = {},
                    enableLabels = false,
                    enableGroupLabels = false,
                    singleConfirmText = "确定"
                ),
                onBack = { route = CustomFriendAvatarRoute.MAIN },
                onConfirm = { selected ->
                    selected.firstOrNull()?.let { selectedWxid = it.id }
                    route = CustomFriendAvatarRoute.MAIN
                }
            )
        }
    }
}

@Composable
internal fun CustomFriendAvatarRow(
    context: Context,
    option: ContactOption,
    revision: Int,
    onClick: () -> Unit
) {
    val bitmap = remember(option.id, revision) {
        CustomFriendAvatarStore.loadBitmap(context, option.id)?.asImageBitmap()
    }
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(bitmap = bitmap, contentDescription = null, modifier = Modifier.fillMaxSize())
            } else {
                Text(if (option.group) "群" else option.label.take(1).ifEmpty { "友" })
            }
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(option.label, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text("点击更换或恢复微信头像", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
    }
}

@Composable
internal fun MessageBlockMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, MessageBlockSettings.PREFS_NAME) }
    val initialState = remember { initialMessageBlockState(sp) }
    var templates by remember {
        mutableStateOf(initialState.templates)
    }
    var bindings by remember {
        mutableStateOf(initialState.bindings)
    }
    var defaultPrivateRule by remember { mutableStateOf(MessageBlockSettings(context).defaultPrivateRule()) }
    var defaultGroupRule by remember { mutableStateOf(MessageBlockSettings(context).defaultGroupRule()) }
    var defaultOfficialRule by remember { mutableStateOf(MessageBlockSettings(context).defaultOfficialRule()) }
    var templateEditor by remember { mutableStateOf<MessageBlockTemplateEditorRequest?>(null) }
    var bindingEditor by remember { mutableStateOf<MessageBlockBindingEditorRequest?>(null) }
    var batchBindingEditor by remember { mutableStateOf<MessageBlockBatchBindingEditorRequest?>(null) }
    var bindingPicker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var groupMemberBindingPicker by remember { mutableStateOf<GroupMemberPickerRequest?>(null) }
    var defaultRuleEditor by remember { mutableStateOf<MessageBlockDefaultRuleKind?>(null) }
    var showTemplateManager by remember { mutableStateOf(false) }
    var showListManager by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(Unit) {
        if (initialState.shouldPersist) {
            sp.edit()
                .putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(templates))
                .putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(bindings))
                .apply()
        }
    }

    val route = when {
        templateEditor != null -> MessageBlockRoute.TemplateEditor(templateEditor!!)
        bindingEditor != null -> MessageBlockRoute.BindingEditor(bindingEditor!!)
        batchBindingEditor != null -> MessageBlockRoute.BatchBindingEditor(batchBindingEditor!!)
        bindingPicker != null -> MessageBlockRoute.ContactPicker(bindingPicker!!)
        groupMemberBindingPicker != null -> MessageBlockRoute.GroupMemberPicker(groupMemberBindingPicker!!)
        defaultRuleEditor != null -> MessageBlockRoute.DefaultRuleEditor(defaultRuleEditor!!)
        showTemplateManager -> MessageBlockRoute.TemplateManager
        showListManager -> MessageBlockRoute.ListManager
        else -> MessageBlockRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "MessageBlockRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is MessageBlockRoute.TemplateEditor -> {
                val request = currentRoute.request
                MessageBlockTemplatePage(
                    context = context,
                    request = request,
                    onBack = { templateEditor = null },
                    onSave = { updated ->
                        val next = if (request.index in templates.indices) {
                            templates.toMutableList().also { it[request.index] = MessageBlockSettings.clearTemplateTargets(updated) }
                        } else {
                            templates + MessageBlockSettings.clearTemplateTargets(updated)
                        }
                        templates = next
                        sp.edit().putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(next)).apply()
                        templateEditor = null
                    },
                    onDelete = {
                        if (request.index in templates.indices) {
                            val deletedId = templates[request.index].id
                            val next = templates.toMutableList().also { it.removeAt(request.index) }
                            val nextBindings = bindings.map { binding ->
                                binding.copy(templateIds = binding.templateIds - deletedId)
                            }
                            fun withoutDeletedTemplate(rule: MessageBlockDefaultRule): MessageBlockDefaultRule {
                                val nextIds = rule.templateIds - deletedId
                                return rule.copy(
                                    enabled = rule.enabled && (rule.customRules || nextIds.isNotEmpty()),
                                    templateIds = nextIds
                                )
                            }
                            val nextDefaultPrivate = withoutDeletedTemplate(defaultPrivateRule)
                            val nextDefaultGroup = withoutDeletedTemplate(defaultGroupRule)
                            val nextDefaultOfficial = withoutDeletedTemplate(defaultOfficialRule)
                            templates = next
                            bindings = nextBindings
                            defaultPrivateRule = nextDefaultPrivate
                            defaultGroupRule = nextDefaultGroup
                            defaultOfficialRule = nextDefaultOfficial
                            sp.edit()
                                .putString(MessageBlockSettings.KEY_TEMPLATES, MessageBlockSettings.encodeTemplates(next))
                                .putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(nextBindings))
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_PRIVATE,
                                    MessageBlockSettings.encodeDefaultRule(nextDefaultPrivate, group = false)
                                )
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_GROUP,
                                    MessageBlockSettings.encodeDefaultRule(nextDefaultGroup, group = true)
                                )
                                .putString(
                                    MessageBlockSettings.KEY_DEFAULT_OFFICIAL,
                                    MessageBlockSettings.encodeDefaultRule(
                                        nextDefaultOfficial,
                                        group = false,
                                        official = true
                                    )
                                )
                                .apply()
                        }
                        templateEditor = null
                    }
                )
            }
            is MessageBlockRoute.BindingEditor -> {
                val request = currentRoute.request
                MessageBlockBindingPage(
                    context = context,
                    request = request,
                    templates = templates,
                    onBack = { bindingEditor = null },
                    onSave = { updated ->
                        val normalized = normalizedMessageBlockBinding(updated)
                        val base = if (request.index in bindings.indices) {
                            bindings.toMutableList().also { it.removeAt(request.index) }
                        } else {
                            bindings
                        }
                        val next = upsertMessageBlockBindings(base, listOf(normalized))
                        bindings = next
                        sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        bindingEditor = null
                    },
                    onDelete = {
                        if (request.index in bindings.indices) {
                            val next = bindings.toMutableList().also { it.removeAt(request.index) }
                            bindings = next
                            sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        }
                        bindingEditor = null
                    }
                )
            }
            is MessageBlockRoute.BatchBindingEditor -> {
                val request = currentRoute.request
                MessageBlockBatchBindingPage(
                    context = context,
                    request = request,
                    templates = templates,
                    onBack = { batchBindingEditor = null },
                    onSave = { updated ->
                        val next = upsertMessageBlockBindings(bindings, updated)
                        bindings = next
                        sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                        Toast.makeText(context, "已保存 ${updated.size} 个名单项", Toast.LENGTH_SHORT).show()
                        batchBindingEditor = null
                    }
                )
            }
            is MessageBlockRoute.DefaultRuleEditor -> {
                val rule = when (currentRoute.kind) {
                    MessageBlockDefaultRuleKind.PRIVATE -> defaultPrivateRule
                    MessageBlockDefaultRuleKind.GROUP -> defaultGroupRule
                    MessageBlockDefaultRuleKind.OFFICIAL -> defaultOfficialRule
                }
                MessageBlockDefaultRulePage(
                    context = context,
                    rule = rule,
                    templates = templates,
                    onBack = { defaultRuleEditor = null },
                    onSave = { updated ->
                        val normalized = when (currentRoute.kind) {
                            MessageBlockDefaultRuleKind.PRIVATE -> updated.copy(group = false, official = false, label = "默认私聊规则")
                            MessageBlockDefaultRuleKind.GROUP -> updated.copy(group = true, official = false, label = "默认群聊规则")
                            MessageBlockDefaultRuleKind.OFFICIAL -> updated.copy(group = false, official = true, label = "默认公众号规则")
                        }
                        when (currentRoute.kind) {
                            MessageBlockDefaultRuleKind.PRIVATE -> {
                                defaultPrivateRule = normalized
                                MessageBlockSettings(context).saveDefaultPrivateRule(normalized)
                            }
                            MessageBlockDefaultRuleKind.GROUP -> {
                                defaultGroupRule = normalized
                                MessageBlockSettings(context).saveDefaultGroupRule(normalized)
                            }
                            MessageBlockDefaultRuleKind.OFFICIAL -> {
                                defaultOfficialRule = normalized
                                MessageBlockSettings(context).saveDefaultOfficialRule(normalized)
                            }
                        }
                        Toast.makeText(context, "默认规则已保存", Toast.LENGTH_SHORT).show()
                        defaultRuleEditor = null
                    }
                )
            }
            is MessageBlockRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { bindingPicker = null },
                    onConfirm = { selected ->
                        val additions = selected.map {
                            messageBlockBindingFromContact(
                                option = it,
                                templates = templates,
                                existing = findMessageBlockBinding(bindings, MessageBlockSettings.TARGET_CONTACT, it.id)
                            )
                        }
                        if (additions.size == 1) {
                            val existingIndex = bindings.indexOfFirst {
                                it.targetType == additions.first().targetType &&
                                    it.targetId == additions.first().targetId
                            }
                            bindingEditor = MessageBlockBindingEditorRequest(
                                index = existingIndex.takeIf { it >= 0 } ?: bindings.size,
                                binding = additions.first(),
                                canDelete = existingIndex >= 0
                            )
                        } else if (additions.isNotEmpty()) {
                            batchBindingEditor = MessageBlockBatchBindingEditorRequest(
                                title = "批量添加名单",
                                bindings = additions
                            )
                        }
                        bindingPicker = null
                    }
                )
            }
            is MessageBlockRoute.GroupMemberPicker -> {
                val request = currentRoute.request
                GroupMemberPickerPage(
                    context = context,
                    request = request,
                    onBack = { groupMemberBindingPicker = null },
                    onConfirm = { selected ->
                        val additions = selected.map {
                            messageBlockBindingFromGroupMember(
                                entry = it,
                                templates = templates,
                                existing = findMessageBlockBinding(bindings, MessageBlockSettings.TARGET_GROUP_MEMBER, it)
                            )
                        }
                        if (additions.size == 1) {
                            val binding = additions.first()
                            val existingIndex = bindings.indexOfFirst {
                                it.targetType == binding.targetType &&
                                    it.targetId == binding.targetId
                            }
                            bindingEditor = MessageBlockBindingEditorRequest(
                                index = existingIndex.takeIf { it >= 0 } ?: bindings.size,
                                binding = binding,
                                canDelete = existingIndex >= 0
                            )
                        } else if (additions.isNotEmpty()) {
                            batchBindingEditor = MessageBlockBatchBindingEditorRequest(
                                title = "批量添加群成员",
                                bindings = additions
                            )
                        }
                        groupMemberBindingPicker = null
                    }
                )
            }
            MessageBlockRoute.TemplateManager -> {
                MessageBlockTemplateListPage(
                    templates = templates,
                    onBack = { showTemplateManager = false },
                    onOpenTemplate = { index, template ->
                        templateEditor = MessageBlockTemplateEditorRequest(index, template, canDelete = true)
                    },
                    onAddTemplate = {
                        templateEditor = MessageBlockTemplateEditorRequest(
                            templates.size,
                            newMessageBlockTemplate(templates.size + 1),
                            canDelete = false
                        )
                    }
                )
            }
            MessageBlockRoute.ListManager -> {
                MessageBlockListManagerPage(
                    bindings = bindings,
                    templates = templates,
                    onBack = { showListManager = false },
                    onOpenBinding = { index, binding ->
                        bindingEditor = MessageBlockBindingEditorRequest(index, binding, canDelete = true)
                    },
                    onAddContact = {
                        bindingPicker = ContactPickerRequest(
                            title = "选择名单",
                            mode = ContactPickerMode.ALL_CHATS,
                            multiSelect = true,
                            existingValue = "",
                            onValue = {},
                            enableLabels = true
                        )
                    },
                    onAddGroupMember = {
                        groupMemberBindingPicker = GroupMemberPickerRequest(
                            title = "选择群成员",
                            existingValue = "",
                            onValue = {}
                        )
                    },
                    onDeleteBindings = { targets ->
                        if (targets.isEmpty()) {
                            Toast.makeText(context, "没有可删除的名单", Toast.LENGTH_SHORT).show()
                        } else {
                            val removeKeys = targets.map {
                                MessageBlockSettings.bindingKey(it.targetType, it.targetId)
                            }.toSet()
                            val next = bindings.filterNot {
                                removeKeys.contains(MessageBlockSettings.bindingKey(it.targetType, it.targetId))
                            }
                            bindings = next
                            sp.edit().putString(MessageBlockSettings.KEY_BINDINGS, MessageBlockSettings.encodeBindings(next)).apply()
                            Toast.makeText(context, "已删除 ${targets.size} 个名单项", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            MessageBlockRoute.Main -> PageScaffold(
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
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        MessageBlockSettings.KEY_ENABLE,
                        "启用屏蔽消息",
                        "总开关开启后，仅命中启用模板的新消息会被拦截",
                        MessageBlockSettings.DEFAULT_ENABLE
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "管理") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "默认私聊规则",
                        summary = describeMessageBlockDefaultRule(defaultPrivateRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.PRIVATE }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "默认群聊规则",
                        summary = describeMessageBlockDefaultRule(defaultGroupRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.GROUP }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "默认公众号规则",
                        summary = describeMessageBlockDefaultRule(defaultOfficialRule, templates),
                        onClick = { defaultRuleEditor = MessageBlockDefaultRuleKind.OFFICIAL }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "模板管理",
                        summary = if (templates.isEmpty()) "暂无模板，进入后添加屏蔽规则" else "${templates.size} 个模板，进入后添加或修改规则",
                        onClick = { showTemplateManager = true }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "名单管理",
                        summary = if (bindings.isEmpty()) "暂无名单，进入后添加好友、群聊、公众号或群成员" else "${bindings.size} 个名单项，进入后分配模板",
                        onClick = { showListManager = true }
                    )
                }
            }
        }
    }
        }
    }
}

@Composable
internal fun MessageBlockTemplateListPage(
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onOpenTemplate: (Int, MessageBlockTemplate) -> Unit,
    onAddTemplate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "模板管理",
        largeTitle = "模板管理",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增模板",
                onPrimaryClick = onAddTemplate,
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
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。模板只配置消息类型和关键词，名单在“名单管理”里分配模板。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onClick = { onOpenTemplate(index, template) }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockListManagerPage(
    bindings: List<MessageBlockBinding>,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onOpenBinding: (Int, MessageBlockBinding) -> Unit,
    onAddContact: () -> Unit,
    onAddGroupMember: () -> Unit,
    onDeleteBindings: (List<MessageBlockBinding>) -> Unit
) {
    val context = LocalContext.current
    var category by remember { mutableStateOf(ConversationRuleCategory.ALL) }
    var query by remember { mutableStateOf("") }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val lower = query.trim().lowercase(Locale.US)
    val visibleBindings = bindings.mapIndexed { index, binding -> index to binding }
        .filter { (_, binding) ->
            conversationRuleCategoryMatches(binding, category) &&
                (lower.isEmpty() || messageBlockBindingMatchesQuery(binding, templates, lower))
        }
    val listTitle = when {
        bindings.isEmpty() -> "名单"
        lower.isEmpty() -> "名单 · ${bindings.size} 项"
        else -> "名单 · ${visibleBindings.size}/${bindings.size} 项"
    }
    val visibleIds = visibleBindings.mapTo(LinkedHashSet()) { it.second.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedBindings = bindings.filter { it.id in selectedIds }
    PageScaffold(
        title = "名单管理",
        largeTitle = "名单管理",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedBindings.size}）",
                    onPrimaryClick = {
                        if (selectedBindings.isEmpty()) {
                            Toast.makeText(context, "请先选择名单", Toast.LENGTH_SHORT).show()
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
                    primaryText = "返回",
                    onPrimaryClick = onBack,
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
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            item {
                SettingsCard {
                    ConversationRuleCategoryTabs(
                        selected = category,
                        onSelected = { category = it }
                    )
                }
            }
            item {
                SettingsCard {
                    InputRow(
                        title = "搜索名单",
                        summary = "昵称 / wxid / 群号 / 模板名",
                        value = query,
                        onValueChange = { query = it }
                    )
                }
            }
            if (!batchDeleteMode) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "添加") }
                item {
                    SettingsCard {
                        ActionRow("添加好友/群聊/公众号", "批量添加后可一次选择模板") {
                            onAddContact()
                        }
                        InsetDivider()
                        ActionRow("添加群成员", "批量添加后可一次选择模板") {
                            onAddGroupMember()
                        }
                    }
                }
            }
            item { SmallTitle(text = listTitle) }
            item {
                SettingsCard {
                    if (bindings.isEmpty()) {
                        EmptyText("暂无名单。添加好友、群聊、公众号或群成员后，再给名单分配模板。")
                    } else if (visibleBindings.isEmpty()) {
                        EmptyText("没有匹配名单。可按昵称、ID 或模板名搜索。")
                    } else {
                        visibleBindings.forEachIndexed { rowIndex, (index, binding) ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = binding.label.ifBlank { binding.targetId },
                                        value = index,
                                        summary = describeMessageBlockBinding(binding, templates)
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
                                    title = binding.label.ifBlank { binding.targetId },
                                    summary = describeMessageBlockBinding(binding, templates),
                                    onClick = { onOpenBinding(index, binding) }
                                )
                            }
                            if (rowIndex < visibleBindings.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedBindings.size} 个名单项，此操作不可撤销。",
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
internal fun MessageBlockBatchBindingPage(
    context: Context,
    request: MessageBlockBatchBindingEditorRequest,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (List<MessageBlockBinding>) -> Unit
) {
    var enabled by remember(request) { mutableStateOf(true) }
    var action by remember(request) { mutableStateOf(MessageBlockSettings.ACTION_BLOCK) }
    var templateIds by remember(request) { mutableStateOf(defaultMessageBlockTemplateIds(templates)) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = request.title,
        largeTitle = request.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存名单",
                onPrimaryClick = {
                    if (templateIds.isEmpty()) {
                        Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(
                            request.bindings.map {
                                it.copy(
                                    enabled = enabled,
                                    action = action,
                                    templateIds = templateIds,
                                    customRules = false,
                                    typeAll = false,
                                    types = emptySet(),
                                    textKeywords = ""
                                )
                            }
                        )
                    }
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
            item { SmallTitle(text = "批量配置") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "已选择",
                        summary = "${request.bindings.size} 个名单项",
                        onClick = {}
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用名单",
                        summary = "关闭后保留名单但不参与匹配",
                        onCheckedChange = { enabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = action == MessageBlockSettings.ACTION_EXCLUDE,
                        title = "排除名单",
                        summary = "开启后这些名单命中时跳过屏蔽",
                        onCheckedChange = {
                            action = if (it) MessageBlockSettings.ACTION_EXCLUDE else MessageBlockSettings.ACTION_BLOCK
                        }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，再回来批量添加名单。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "名单") }
            item {
                SettingsCard {
                    request.bindings.take(30).forEachIndexed { index, binding ->
                        SelectRow(
                            title = binding.label.ifBlank { binding.targetId },
                            summary = messageBlockBindingTargetSummary(binding),
                            onClick = {}
                        )
                        if (index < minOf(request.bindings.size, 30) - 1) InsetDivider()
                    }
                    if (request.bindings.size > 30) {
                        InsetDivider()
                        EmptyText("还有 ${request.bindings.size - 30} 个名单项未展开显示")
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockDefaultRulePage(
    context: Context,
    rule: MessageBlockDefaultRule,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (MessageBlockDefaultRule) -> Unit
) {
    var enabled by remember(rule) { mutableStateOf(rule.enabled) }
    var templateIds by remember(rule) { mutableStateOf(rule.templateIds) }
    var customRules by remember(rule) { mutableStateOf(rule.customRules) }
    var typeAll by remember(rule) { mutableStateOf(rule.typeAll) }
    var types by remember(rule) { mutableStateOf(rule.types) }
    var textKeywords by remember(rule) { mutableStateOf(rule.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val title = when {
        rule.official -> "默认公众号规则"
        rule.group -> "默认群聊规则"
        else -> "默认私聊规则"
    }
    val scopeSummary = when {
        rule.official -> "未配置名单的公众号按此规则屏蔽"
        rule.group -> "未配置名单的群聊按此规则屏蔽"
        else -> "未配置名单的私聊按此规则屏蔽"
    }
    fun copySelectedTemplateRules() {
        val rules = messageBlockRuleStateFromTemplates(templateIds, templates)
        typeAll = rules.typeAll
        types = rules.types
        textKeywords = rules.textKeywords
    }

    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存规则",
                onPrimaryClick = {
                    if (enabled && !customRules && templateIds.isEmpty()) {
                        Toast.makeText(context, "请先选择模板", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(
                            rule.copy(
                                enabled = enabled,
                                templateIds = templateIds,
                                customRules = customRules,
                                typeAll = typeAll,
                                types = types,
                                textKeywords = if (customRules && !typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
                            )
                        )
                    }
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
            item { SmallTitle(text = "默认规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用默认规则",
                        summary = scopeSummary,
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，或开启下方专属规则。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                    if (!customRules) copySelectedTemplateRules()
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = customRules,
                        title = "默认专属规则",
                        summary = "开启后不依赖模板，直接按下方类型和关键词屏蔽",
                        onCheckedChange = { checked ->
                            customRules = checked
                            if (checked && !typeAll && types.isEmpty() && textKeywords.isBlank()) {
                                copySelectedTemplateRules()
                            }
                        }
                    )
                    if (customRules) {
                        InsetDivider()
                        ActionRow("套用已选模板规则", "把上方模板的类型和关键词复制到这里") {
                            copySelectedTemplateRules()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = typeAll,
                            title = "所有消息",
                            summary = "开启后默认范围内全部类型生效",
                            onCheckedChange = {
                                typeAll = it
                                if (it) textKeywords = ""
                            }
                        )
                        if (!typeAll) {
                            messageBlockTypeOptions().forEach { option ->
                                InsetDivider()
                                SwitchRow(
                                    checked = types.contains(option.key),
                                    title = option.title,
                                    summary = option.summary,
                                    onCheckedChange = { checked ->
                                        types = if (checked) {
                                            types + option.key
                                        } else {
                                            if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                            types - option.key
                                        }
                                    }
                                )
                            }
                            if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                                InsetDivider()
                                InputRow(
                                    "文字关键词",
                                    "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                    textKeywords,
                                    minLines = 2
                                ) { textKeywords = it }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageBlockTemplatePage(
    context: Context,
    request: MessageBlockTemplateEditorRequest,
    onBack: () -> Unit,
    onSave: (MessageBlockTemplate) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(request) { mutableStateOf(request.template.name) }
    var enabled by remember(request) { mutableStateOf(request.template.enabled) }
    var typeAll by remember(request) { mutableStateOf(request.template.typeAll) }
    var types by remember(request) { mutableStateOf(request.template.types) }
    var textKeywords by remember(request) { mutableStateOf(request.template.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = name.ifBlank { "模板" },
        largeTitle = name.ifBlank { "模板" },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存模板",
                onPrimaryClick = {
                    onSave(
                        MessageBlockTemplate(
                            id = request.template.id,
                            name = name.trim().ifBlank { "模板 ${request.index + 1}" },
                            enabled = enabled,
                            mode = MessageBlockSettings.MODE_TARGETS,
                            targets = "",
                            targetGroupMembers = "",
                            excludes = "",
                            excludeGroupMembers = "",
                            typeAll = typeAll,
                            types = types,
                            textKeywords = if (!typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
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
            item { SmallTitle(text = "模板") }
            item {
                SettingsCard {
                    InputRow("模板名称", "用于区分不同屏蔽模板", name) { name = it }
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用模板",
                        summary = "关闭后使用该模板的名单不会按此规则屏蔽",
                        onCheckedChange = { enabled = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "类型") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = typeAll,
                        title = "所有消息",
                        summary = "开启后该模板命中范围内全部类型生效",
                        onCheckedChange = {
                            typeAll = it
                            if (it) textKeywords = ""
                        }
                    )
                    if (!typeAll) {
                        messageBlockTypeOptions().forEach { option ->
                            InsetDivider()
                            SwitchRow(
                                checked = types.contains(option.key),
                                title = option.title,
                                summary = option.summary,
                                onCheckedChange = { checked ->
                                    types = if (checked) {
                                        types + option.key
                                    } else {
                                        if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                        types - option.key
                                    }
                                }
                            )
                        }
                        if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                            InsetDivider()
                            InputRow(
                                "文字关键词",
                                "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                textKeywords,
                                minLines = 2
                            ) { textKeywords = it }
                        }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除模板", "删除后立即从模板列表移除") {
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
internal fun MessageBlockBindingPage(
    context: Context,
    request: MessageBlockBindingEditorRequest,
    templates: List<MessageBlockTemplate>,
    onBack: () -> Unit,
    onSave: (MessageBlockBinding) -> Unit,
    onDelete: () -> Unit
) {
    var enabled by remember(request) { mutableStateOf(request.binding.enabled) }
    var action by remember(request) { mutableStateOf(request.binding.action) }
    var templateIds by remember(request) { mutableStateOf(request.binding.templateIds) }
    var customRules by remember(request) { mutableStateOf(request.binding.customRules) }
    var typeAll by remember(request) { mutableStateOf(request.binding.typeAll) }
    var types by remember(request) { mutableStateOf(request.binding.types) }
    var textKeywords by remember(request) { mutableStateOf(request.binding.textKeywords) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val targetTitle = request.binding.label.ifBlank { request.binding.targetId }
    fun copySelectedTemplateRules() {
        val rules = messageBlockRuleStateFromTemplates(templateIds, templates)
        typeAll = rules.typeAll
        types = rules.types
        textKeywords = rules.textKeywords
    }

    PageScaffold(
        title = targetTitle,
        largeTitle = targetTitle,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存名单",
                onPrimaryClick = {
                    onSave(
                        request.binding.copy(
                            enabled = enabled,
                            action = action,
                            templateIds = templateIds,
                            customRules = customRules,
                            typeAll = typeAll,
                            types = types,
                            textKeywords = if (customRules && !typeAll && types.contains(MessageBlockSettings.TYPE_TEXT)) textKeywords else ""
                        )
                    )
                    Toast.makeText(context, "名单已保存", Toast.LENGTH_SHORT).show()
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
            item { SmallTitle(text = "名单信息") }
            item {
                SettingsCard {
                    SelectRow(
                        title = targetTitle,
                        summary = messageBlockBindingTargetSummary(request.binding),
                        onClick = {}
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = enabled,
                        title = "启用名单",
                        summary = "关闭后该名单不参与屏蔽消息匹配",
                        onCheckedChange = { enabled = it }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = action == MessageBlockSettings.ACTION_EXCLUDE,
                        title = "排除名单",
                        summary = "开启后命中该名单时跳过屏蔽；关闭时按下方模板屏蔽",
                        onCheckedChange = {
                            action = if (it) MessageBlockSettings.ACTION_EXCLUDE else MessageBlockSettings.ACTION_BLOCK
                        }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模板") }
            item {
                SettingsCard {
                    if (templates.isEmpty()) {
                        EmptyText("暂无模板。先新增模板，再回到这里选择。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SwitchRow(
                                checked = templateIds.contains(template.id),
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeMessageBlockTemplate(template),
                                onCheckedChange = { checked ->
                                    templateIds = if (checked) {
                                        templateIds + template.id
                                    } else {
                                        templateIds - template.id
                                    }
                                    if (!customRules) copySelectedTemplateRules()
                                }
                            )
                            if (index < templates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = customRules,
                        title = "本名单专属规则",
                        summary = if (customRules) {
                            "开启后只按下方勾选屏蔽，不改动模板本身"
                        } else {
                            "关闭时跟随上方模板；开启后可直接给这个名单勾选文字、红包等类型"
                        },
                        onCheckedChange = { checked ->
                            customRules = checked
                            if (checked && !typeAll && types.isEmpty() && textKeywords.isBlank()) {
                                copySelectedTemplateRules()
                            }
                        }
                    )
                    if (customRules) {
                        InsetDivider()
                        ActionRow("套用已选模板规则", "把上方模板的类型和关键词复制到这里") {
                            copySelectedTemplateRules()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = typeAll,
                            title = "所有消息",
                            summary = "开启后该名单命中范围内全部类型生效",
                            onCheckedChange = {
                                typeAll = it
                                if (it) textKeywords = ""
                            }
                        )
                        if (!typeAll) {
                            messageBlockTypeOptions().forEach { option ->
                                InsetDivider()
                                SwitchRow(
                                    checked = types.contains(option.key),
                                    title = option.title,
                                    summary = option.summary,
                                    onCheckedChange = { checked ->
                                        types = if (checked) {
                                            types + option.key
                                        } else {
                                            if (option.key == MessageBlockSettings.TYPE_TEXT) textKeywords = ""
                                            types - option.key
                                        }
                                    }
                                )
                            }
                            if (types.contains(MessageBlockSettings.TYPE_TEXT)) {
                                InsetDivider()
                                InputRow(
                                    "文字关键词",
                                    "文字和文字引用消息生效；多个用 |、逗号或换行分隔，留空则全部命中",
                                    textKeywords,
                                    minLines = 2
                                ) { textKeywords = it }
                            }
                        }
                    }
                }
            }
            if (request.canDelete) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除名单", "删除后该名单不再套用模板") {
                            onDelete()
                            Toast.makeText(context, "名单已删除", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ConversationGroupMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    ConversationGroupSettingsUi.Page(context, provider, onBack)
}

internal object ConversationGroupSettingsUi {
@Composable
fun Page(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    var enabled by remember { mutableStateOf(ConversationGroupStore.isEnabled(context)) }
    var groups by remember { mutableStateOf(ConversationGroupStore.load(context)) }
    var route by remember { mutableStateOf<ConversationGroupRoute>(ConversationGroupRoute.Main) }

    fun reload() {
        groups = ConversationGroupStore.load(context)
        ConversationGroupRuntime.syncAsync(context)
    }

    fun saveGroup(group: ConversationGroup, existing: Boolean): Boolean {
        val duplicate = groups.any {
            it.id != group.id && it.parentId == group.parentId &&
                it.name.equals(group.name.trim(), ignoreCase = true)
        }
        if (duplicate) {
            Toast.makeText(context, "同一层级已存在同名分组", Toast.LENGTH_SHORT).show()
            return false
        }
        val success = if (existing) {
            ConversationGroupStore.updateGroup(context, group)
        } else {
            ConversationGroupStore.addGroup(context, group)
        }
        Toast.makeText(context, if (success) "聊天分组已保存" else "聊天分组保存失败", Toast.LENGTH_SHORT).show()
        if (success) reload()
        return success
    }

    SettingsRouteTransition(
        targetState = route,
        label = "ConversationGroupRoute",
        depthOf = {
            when (it) {
                ConversationGroupRoute.Main -> 0
                is ConversationGroupRoute.Editor -> 1
                is ConversationGroupRoute.AutomaticGrouping -> 1
                is ConversationGroupRoute.ConversationPicker,
                is ConversationGroupRoute.ParentPicker,
                is ConversationGroupRoute.AutomaticPicker -> 2
            }
        }
    ) { currentRoute ->
        when (currentRoute) {
            ConversationGroupRoute.Main -> ConversationGroupListPage(
                title = provider.title(),
                enabled = enabled,
                groups = groups,
                onEnabledChange = {
                    enabled = it
                    ConversationGroupStore.setEnabled(context, it)
                    ConversationGroupRuntime.syncAsync(context)
                },
                onBack = onBack,
                onAdd = {
                    route = ConversationGroupRoute.Editor(
                        ConversationGroupStore.newGroup(parentId = null),
                        existing = false
                    )
                },
                onOpenAutomatic = {
                    val target = groups.firstOrNull { it.automaticGroupingEnabled } ?: groups.firstOrNull()
                    if (target == null) {
                        Toast.makeText(context, "请先新增一个聊天分组", Toast.LENGTH_SHORT).show()
                    } else {
                        route = ConversationGroupRoute.AutomaticGrouping(target)
                    }
                },
                onOpen = { route = ConversationGroupRoute.Editor(it, existing = true) }
            )
            is ConversationGroupRoute.Editor -> ConversationGroupEditorPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                existing = currentRoute.existing,
                onBack = { route = ConversationGroupRoute.Main },
                onPickParent = {
                    route = ConversationGroupRoute.ParentPicker(it, currentRoute.existing)
                },
                onPickConversations = {
                    route = ConversationGroupRoute.ConversationPicker(it, currentRoute.existing)
                },
                onSelectPathGroup = { draft, groupId ->
                    route = ConversationGroupRoute.Editor(
                        draft.copy(parentId = groupId),
                        currentRoute.existing
                    )
                },
                onSave = { draft ->
                    if (saveGroup(draft, currentRoute.existing)) route = ConversationGroupRoute.Main
                },
                onReorder = { action, draft ->
                    val success = ConversationGroupStore.reorderGroup(
                        context,
                        currentRoute.group.id,
                        action
                    )
                    Toast.makeText(
                        context,
                        if (success) "分组顺序已更新" else "分组排序失败",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (success) {
                        reload()
                        ConversationGroupStore.load(context)
                            .firstOrNull { it.id == currentRoute.group.id }
                            ?.let { latest ->
                                route = ConversationGroupRoute.Editor(
                                    draft.copy(order = latest.order),
                                    existing = true
                                )
                            }
                    }
                },
                onDelete = {
                    val success = ConversationGroupStore.deleteGroup(context, currentRoute.group.id)
                    Toast.makeText(context, if (success) "分组已删除" else "分组删除失败", Toast.LENGTH_SHORT).show()
                    if (success) {
                        reload()
                        route = ConversationGroupRoute.Main
                    }
                }
            )
            is ConversationGroupRoute.ConversationPicker -> ConversationGroupConversationPickerPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                onBack = {
                    route = ConversationGroupRoute.Editor(currentRoute.group, currentRoute.existing)
                },
                onConfirm = { selected ->
                    route = ConversationGroupRoute.Editor(
                        currentRoute.group.copy(conversationIds = selected),
                        currentRoute.existing
                    )
                }
            )
            is ConversationGroupRoute.ParentPicker -> ConversationGroupParentPickerPage(
                group = currentRoute.group,
                groups = groups,
                onBack = {
                    route = ConversationGroupRoute.Editor(currentRoute.group, currentRoute.existing)
                },
                onSelected = { parentId ->
                    route = ConversationGroupRoute.Editor(
                        currentRoute.group.copy(parentId = parentId),
                        currentRoute.existing
                    )
                }
            )
            is ConversationGroupRoute.AutomaticGrouping -> ConversationGroupAutomaticGroupingPage(
                context = context,
                group = currentRoute.group,
                groups = groups,
                onBack = { route = ConversationGroupRoute.Main },
                onTargetChanged = { group ->
                    route = ConversationGroupRoute.AutomaticGrouping(group)
                },
                onPick = { group, kind ->
                    route = ConversationGroupRoute.AutomaticPicker(group, kind)
                },
                onSave = { group ->
                    if (saveGroup(group, existing = true)) {
                        route = ConversationGroupRoute.Main
                    }
                }
            )
            is ConversationGroupRoute.AutomaticPicker -> ConversationGroupAutomaticPickerPage(
                context = context,
                group = currentRoute.group,
                kind = currentRoute.kind,
                onBack = {
                    route = ConversationGroupRoute.AutomaticGrouping(currentRoute.group)
                },
                onConfirm = { selected ->
                    val updated = when (currentRoute.kind) {
                        ConversationGroupAutomaticPickerKind.GROUPS -> {
                            currentRoute.group.copy(automaticGroupIds = selected)
                        }
                        ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE -> {
                            currentRoute.group.copy(automaticOfficialIncludeIds = selected)
                        }
                        ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
                            currentRoute.group.copy(automaticOfficialExcludeIds = selected)
                        }
                    }
                    route = ConversationGroupRoute.AutomaticGrouping(updated)
                }
            )
        }
    }
}

@Composable
private fun ConversationGroupListPage(
    title: String,
    enabled: Boolean,
    groups: List<ConversationGroup>,
    onEnabledChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpenAutomatic: () -> Unit,
    onOpen: (ConversationGroup) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val flattened = remember(groups) { flattenConversationGroups(groups) }
    val lower = query.trim().lowercase(Locale.US)
    val visible = flattened.filter { row ->
        lower.isEmpty() || row.group.name.lowercase(Locale.US).contains(lower) ||
            row.path.lowercase(Locale.US).contains(lower)
    }
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增分组",
                onPrimaryClick = onAdd,
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
            item { SmallTitle(text = "功能") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "聊天分组",
                        summary = if (enabled) "已归拢会话从微信首页隐藏" else "分组配置保留，会话恢复在微信首页显示",
                        onCheckedChange = onEnabledChange
                    )
                    InsetDivider()
                    ActionRow(
                        title = "群聊归拢",
                        summary = "按规则自动归拢群聊和公众号到指定分组"
                    ) { onOpenAutomatic() }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "分组") }
            item {
                SettingsCard {
                    InputRow("搜索", "分组名称 / 所属路径", query) { query = it }
                }
            }
            item {
                SettingsCard {
                    when {
                        groups.isEmpty() -> EmptyText("暂无聊天分组")
                        visible.isEmpty() -> EmptyText("没有匹配的聊天分组")
                        else -> visible.forEachIndexed { index, row ->
                            SelectRow(
                                title = "${"  ".repeat(row.depth)}${row.group.name}",
                                summary = buildString {
                                    append(row.group.conversationIds.size).append(" 个直属会话")
                                    val childCount = groups.count { it.parentId == row.group.id }
                                    if (childCount > 0) append(" · ").append(childCount).append(" 个子分组")
                                    if (row.depth > 0) append("\n").append(row.path)
                                },
                                onClick = { onOpen(row.group) }
                            )
                            if (index < visible.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationGroupAutomaticGroupingPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onTargetChanged: (ConversationGroup) -> Unit,
    onPick: (ConversationGroup, ConversationGroupAutomaticPickerKind) -> Unit,
    onSave: (ConversationGroup) -> Unit
) {
    var draft by remember(group) { mutableStateOf(group) }
    val labels = remember(context) { GroupChatLabelStore.load(context) }
    val targetChoices = remember(groups) {
        groups.map { item ->
            PopupChoice(item.name, item.id)
        }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "群聊归拢",
        largeTitle = "群聊归拢",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存设置",
                onPrimaryClick = { onSave(draft) },
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
            item { SmallTitle(text = "归拢目标") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "目标聊天分组",
                        summary = "匹配到的会话会自动放入此分组",
                        options = targetChoices,
                        currentValue = group.id,
                        onValueChanged = { id ->
                            groups.firstOrNull { it.id == id }?.let(onTargetChanged)
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = draft.automaticGroupingEnabled,
                        title = "启用群聊归拢",
                        summary = "关闭后自动归拢的会话立即恢复原位置",
                        onCheckedChange = { draft = draft.copy(automaticGroupingEnabled = it) }
                    )
                }
            }
            if (draft.automaticGroupingEnabled) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群聊规则") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = draft.automaticAllGroups,
                            title = "自动归拢所有群聊",
                            summary = "包含普通群和企业微信群"
                        ) { draft = draft.copy(automaticAllGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticNewGroups,
                            title = "自动归拢新群聊",
                            summary = "首次发现的新群聊自动加入目标分组"
                        ) { draft = draft.copy(automaticNewGroups = it) }
                        InsetDivider()
                        ActionRow(
                            title = "选择群聊",
                            summary = if (draft.automaticGroupIds.isEmpty()) {
                                "未选择群聊"
                            } else {
                                "已选择 ${draft.automaticGroupIds.size} 个群聊"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.GROUPS) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticMutedGroups,
                            title = "自动归拢免打扰群聊",
                            summary = "仅归拢已开启微信消息免打扰的群聊"
                        ) { draft = draft.copy(automaticMutedGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticOwnedGroups,
                            title = "自动归拢自己创建的群聊",
                            summary = "按群资料中的群主账号识别"
                        ) { draft = draft.copy(automaticOwnedGroups = it) }
                        InsetDivider()
                        SwitchRow(
                            checked = draft.automaticEnterpriseGroups,
                            title = "自动归拢企业微信群聊",
                            summary = "仅归拢企业微信互通群"
                        ) { draft = draft.copy(automaticEnterpriseGroups = it) }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群聊标签") }
                item {
                    SettingsCard {
                        if (labels.isEmpty()) {
                            EmptyText("暂无群聊标签")
                        } else {
                            labels.forEachIndexed { index, label ->
                                SwitchRow(
                                    checked = label.id in draft.automaticGroupLabelIds,
                                    title = label.name,
                                    summary = "归拢该标签中的 ${label.groupIds.size} 个群聊"
                                ) { checked ->
                                    draft = draft.copy(
                                        automaticGroupLabelIds = if (checked) {
                                            (draft.automaticGroupLabelIds + label.id).distinct()
                                        } else {
                                            draft.automaticGroupLabelIds.filterNot { it == label.id }
                                        }
                                    )
                                }
                                if (index < labels.lastIndex) InsetDivider()
                            }
                        }
                    }
                }
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "公众号") }
                item {
                    SettingsCard {
                        SwitchRow(
                            checked = draft.automaticOfficialAccounts,
                            title = "自动归拢公众号",
                            summary = "归拢当前账号可识别的公众号会话"
                        ) { draft = draft.copy(automaticOfficialAccounts = it) }
                        InsetDivider()
                        ActionRow(
                            title = "包含公众号",
                            summary = if (draft.automaticOfficialIncludeIds.isEmpty()) {
                                "未额外包含公众号"
                            } else {
                                "已包含 ${draft.automaticOfficialIncludeIds.size} 个公众号"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE) }
                        InsetDivider()
                        ActionRow(
                            title = "排除公众号",
                            summary = if (draft.automaticOfficialExcludeIds.isEmpty()) {
                                "未排除公众号"
                            } else {
                                "已排除 ${draft.automaticOfficialExcludeIds.size} 个公众号"
                            }
                        ) { onPick(draft, ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationGroupAutomaticPickerPage(
    context: Context,
    group: ConversationGroup,
    kind: ConversationGroupAutomaticPickerKind,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    val initialSelected = remember(group, kind) {
        when (kind) {
            ConversationGroupAutomaticPickerKind.GROUPS -> group.automaticGroupIds.toSet()
            ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE -> {
                group.automaticOfficialIncludeIds.toSet()
            }
            ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
                group.automaticOfficialExcludeIds.toSet()
            }
        }
    }
    var selectedIds by remember(group, kind) { mutableStateOf(initialSelected) }
    var options by remember(kind) { mutableStateOf(emptyList<ContactOption>()) }
    var loading by remember(kind) { mutableStateOf(true) }
    var error by remember(kind) { mutableStateOf("") }
    var query by remember(kind) { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    LaunchedEffect(kind) {
        loading = true
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching { loadConversationGroupAutomaticOptions(kind) }
        }
        loading = false
        result.onSuccess { options = it }
            .onFailure { error = it.message ?: "读取联系人失败" }
    }
    val visible = options.filter { it.matchesSearch(query.trim().lowercase(Locale.US)) }
        .selectedFirst { it.id in initialSelected }
    val visibleIds = visible.mapTo(linkedSetOf()) { it.id }
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all(selectedIds::contains)
    PageScaffold(
        title = kind.title,
        largeTitle = kind.title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = { onConfirm(selectedIds.toList()) },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isEmpty()) null else if (visibleAllSelected) "取消全选" else "全选",
                onMiddleClick = if (visibleIds.isEmpty()) null else {
                    {
                        selectedIds = if (visibleAllSelected) {
                            selectedIds - visibleIds
                        } else {
                            selectedIds + visibleIds
                        }
                    }
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
                    InputRow("搜索", "名称 / 备注 / wxid", query) { query = it }
                }
            }
            item {
                PickerSectionHeader(
                    text = if (visible.isEmpty()) kind.title else "${kind.title} · ${visible.size} 项"
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入...") } }
                error.isNotBlank() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText(kind.emptyText) } }
                else -> visible.forEach { option ->
                    item {
                        ContactListCard(
                            option = option,
                            selected = option.id in selectedIds,
                            multiSelect = true,
                            onClick = {
                                selectedIds = if (option.id in selectedIds) {
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

private fun loadConversationGroupAutomaticOptions(
    kind: ConversationGroupAutomaticPickerKind
): List<ContactOption> {
    val contacts = WeChatApis.contacts() ?: throw IllegalStateException("联系人列表不可用")
    val rows = when (kind) {
        ConversationGroupAutomaticPickerKind.GROUPS -> {
            contacts.getPickerGroups().mapNotNull { it.toOption(group = true) }
        }
        ConversationGroupAutomaticPickerKind.OFFICIAL_INCLUDE,
        ConversationGroupAutomaticPickerKind.OFFICIAL_EXCLUDE -> {
            contacts.getPickerOfficialAccounts().mapNotNull { it.toOption(group = false, official = true) }
        }
    }
    return rows.distinctBy { it.id }
        .sortedBy { it.label.lowercase(Locale.US) }
}

@Composable
private fun ConversationGroupEditorPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    existing: Boolean,
    onBack: () -> Unit,
    onPickParent: (ConversationGroup) -> Unit,
    onPickConversations: (ConversationGroup) -> Unit,
    onSelectPathGroup: (ConversationGroup, String?) -> Unit,
    onSave: (ConversationGroup) -> Unit,
    onReorder: (ConversationGroupStore.ReorderAction, ConversationGroup) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(group) { mutableStateOf(group.name) }
    var pinned by remember(group) { mutableStateOf(group.pinned) }
    var showDeleteConfirm by remember(group.id) { mutableStateOf(false) }
    var showReorderChoices by remember(group.id) { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val draft = group.copy(name = name.trim(), pinned = pinned)
    val pathGroups = remember(groups, group.parentId) {
        conversationGroupPathItems(groups, group.parentId)
    }
    val siblings = remember(groups, group.parentId) {
        groups.filter { it.parentId == group.parentId && it.pinned == group.pinned }
            .sortedBy { it.order }
    }
    val siblingIndex = siblings.indexOfFirst { it.id == group.id }
    PageScaffold(
        title = if (existing) "编辑聊天分组" else "新增聊天分组",
        largeTitle = if (existing) "编辑聊天分组" else "新增聊天分组",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存分组",
                onPrimaryClick = {
                    if (draft.name.isBlank()) {
                        Toast.makeText(context, "请输入分组名称", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(draft)
                    }
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
            item {
                BreadcrumbBar(
                    items = listOf(BreadcrumbItem(path = "", text = "微信首页")) +
                        pathGroups.map { BreadcrumbItem(path = it.id, text = it.name) },
                    onItemClick = { index ->
                        onSelectPathGroup(
                            draft,
                            if (index == 0) null else pathGroups.getOrNull(index - 1)?.id
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { SmallTitle(text = "分组内容") }
            item {
                SettingsCard {
                    InputRow("分组名称", "同一层级内不能重名", name) { name = it }
                    if (!existing) {
                        InsetDivider()
                        SwitchRow(
                            checked = pinned,
                            title = "主页置顶",
                            summary = "新建后显示在当前层级的置顶分组区域",
                            onCheckedChange = { pinned = it }
                        )
                    }
                    InsetDivider()
                    ActionRow(
                        title = "上级分组",
                        summary = pathGroups.joinToString(" / ") { it.name }.ifBlank { "微信首页" }
                    ) { onPickParent(draft) }
                    InsetDivider()
                    ActionRow(
                        title = "会话",
                        summary = if (group.conversationIds.isEmpty()) "未选择会话" else "已选择 ${group.conversationIds.size} 个会话"
                    ) { onPickConversations(draft) }
                }
            }
            if (existing) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        if (siblings.size > 1 && siblingIndex >= 0) {
                            ActionRow(
                                title = "分组位置",
                                summary = "当前${if (group.pinned) "置顶" else "普通"}分组第 " +
                                    "${siblingIndex + 1} 个，共 ${siblings.size} 个"
                            ) { showReorderChoices = true }
                            InsetDivider()
                        }
                        ActionRow(
                            title = "删除分组",
                            summary = "子分组移到上一级，直属会话移到上级分组或微信首页"
                        ) { showDeleteConfirm = true }
                    }
                }
            }
        }
    }
    if (showReorderChoices) {
        val actions = buildList {
            if (siblingIndex > 0) {
                add(Triple("移到顶部", "设为当前层级的第一个分组", ConversationGroupStore.ReorderAction.TOP))
                add(Triple("上移", "向前移动一个位置", ConversationGroupStore.ReorderAction.UP))
            }
            if (siblingIndex in 0 until siblings.lastIndex) {
                add(Triple("下移", "向后移动一个位置", ConversationGroupStore.ReorderAction.DOWN))
                add(Triple("移到底部", "设为当前层级的最后一个分组", ConversationGroupStore.ReorderAction.BOTTOM))
            }
        }
        WindowDialog(
            show = true,
            title = "分组位置",
            onDismissRequest = { showReorderChoices = false },
            content = {
                Column {
                    actions.forEachIndexed { index, (title, summary, action) ->
                        SelectRow(title = title, summary = summary) {
                            showReorderChoices = false
                            onReorder(action, draft)
                        }
                        if (index < actions.lastIndex) InsetDivider()
                    }
                }
            }
        )
    }
    if (showDeleteConfirm) {
        WindowDialog(
            show = true,
            title = "删除聊天分组",
            onDismissRequest = { showDeleteConfirm = false },
            content = {
                Column {
                    Text(
                        text = "确定删除“${group.name}”？聊天记录、联系人和群聊不会被删除。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            text = "取消",
                            onClick = { showDeleteConfirm = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary()
                        )
                        TextButton(
                            text = "确认删除",
                            onClick = {
                                showDeleteConfirm = false
                                onDelete()
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
internal fun ConversationGroupConversationPickerPage(
    context: Context,
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var loading by remember(group.id) { mutableStateOf(true) }
    var error by remember(group.id) { mutableStateOf("") }
    var pickerData by remember(group.id) { mutableStateOf(ConversationGroupPickerData()) }
    var query by remember(group.id) { mutableStateOf("") }
    var contactFilter by remember(group.id) { mutableStateOf(ContactPickerFilter.ALL) }
    var selectedLabel by remember(group.id) { mutableStateOf("") }
    var selectedConversationGroupId by remember(group.id) { mutableStateOf("") }
    val initialSelected = remember(group) { group.conversationIds.toSet() }
    var selectedIds by remember(group) { mutableStateOf(initialSelected) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(group.id, groups) {
        loading = true
        error = ""
        val result = withContext(Dispatchers.IO) {
            runCatching { loadConversationGroupOptions(groups, group.id) }
        }
        loading = false
        result.onSuccess { pickerData = it }
            .onFailure { error = it.message ?: "读取会话列表失败" }
    }
    val conversations = pickerData.options
    val conversationGroupFilters = remember(groups, conversations) {
        ConversationGroupPickerSupport.filters(groups, conversations.map { it.id })
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
    val scopedConversations = remember(conversations, selectedConversationIds) {
        selectedConversationIds?.let { ids -> conversations.filter { it.id in ids } } ?: conversations
    }
    val labels = remember(scopedConversations) {
        scopedConversations.flatMap { it.labels }
            .distinct()
            .sortedWith(compareBy { it.lowercase(Locale.US) })
    }
    LaunchedEffect(contactFilter, labels) {
        if (contactFilter == ContactPickerFilter.LABELS) {
            if (selectedLabel.isBlank() || selectedLabel !in labels) {
                selectedLabel = labels.firstOrNull().orEmpty()
            }
        } else if (selectedLabel.isNotBlank()) {
            selectedLabel = ""
        }
    }
    val lower = query.trim().lowercase(Locale.US)
    val filtered = scopedConversations.filter {
        when (contactFilter) {
            ContactPickerFilter.FRIENDS -> it.id in pickerData.friendIds
            ContactPickerFilter.GROUPS -> it.group
            ContactPickerFilter.OFFICIALS -> it.id in pickerData.officialIds
            ContactPickerFilter.LABELS -> selectedLabel.isNotBlank() &&
                it.id in pickerData.friendIds &&
                selectedLabel in it.labels
            ContactPickerFilter.ALL -> true
        }
    }
    val visible = filtered.filter { it.matchesSearch(lower) }
        .selectedFirst { initialSelected.contains(it.id) }
    val visibleIds = visible.map { it.id }.toSet()
    val visibleAllSelected = visibleIds.isNotEmpty() && visibleIds.all(selectedIds::contains)
    PageScaffold(
        title = "选择分组会话",
        largeTitle = "选择分组会话",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存选择",
                onPrimaryClick = { onConfirm(selectedIds.toList()) },
                secondaryText = "返回",
                onSecondaryClick = onBack,
                middleText = if (visibleIds.isNotEmpty()) {
                    if (visibleAllSelected) "取消全选" else "全选"
                } else null,
                onMiddleClick = if (visibleIds.isNotEmpty()) {
                    {
                        selectedIds = if (visibleAllSelected) selectedIds - visibleIds else selectedIds + visibleIds
                    }
                } else null
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
                    ContactFilterRow(
                        selected = contactFilter,
                        enableLabels = true,
                        enableOfficials = true,
                        onSelected = {
                            contactFilter = it
                            query = ""
                        }
                    )
                }
            }
            if (contactFilter == ContactPickerFilter.LABELS) {
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
                    InputRow("搜索", "会话名称 / 备注 / wxid", query) { query = it }
                }
            }
            item {
                PickerSectionHeader(
                    text = if (visible.isNotEmpty()) {
                        "${contactFilter.title} · ${visible.size} 项"
                    } else {
                        contactFilter.title
                    }
                )
            }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入会话...") } }
                error.isNotBlank() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配的会话") } }
                else -> visible.forEach { option ->
                    item {
                        ContactListCard(
                            option = option,
                            selected = selectedIds.contains(option.id),
                            multiSelect = true,
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

@Composable
private fun ConversationGroupParentPickerPage(
    group: ConversationGroup,
    groups: List<ConversationGroup>,
    onBack: () -> Unit,
    onSelected: (String?) -> Unit
) {
    val excluded = remember(groups, group.id) {
        ConversationGroupStore.descendantIds(groups, group.id) + group.id
    }
    val candidates = remember(groups, excluded) {
        flattenConversationGroups(groups).filterNot { it.group.id in excluded }
    }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "选择上级分组",
        largeTitle = "选择上级分组",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "微信首页",
                onPrimaryClick = { onSelected(null) },
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
            item { SmallTitle(text = "可用分组") }
            item {
                SettingsCard {
                    if (candidates.isEmpty()) {
                        EmptyText("没有其它可用分组")
                    } else {
                        candidates.forEachIndexed { index, row ->
                            SelectRow(
                                title = "${"  ".repeat(row.depth)}${row.group.name}",
                                summary = row.path,
                                onClick = { onSelected(row.group.id) }
                            )
                            if (index < candidates.lastIndex) InsetDivider()
                        }
                    }
                }
            }
        }
    }
}

private data class FlattenedConversationGroup(
    val group: ConversationGroup,
    val depth: Int,
    val path: String
)

private fun flattenConversationGroups(groups: List<ConversationGroup>): List<FlattenedConversationGroup> {
    val normalized = ConversationGroupStore.normalize(groups)
    val byParent = normalized.groupBy { it.parentId }
    val result = arrayListOf<FlattenedConversationGroup>()
    val visited = hashSetOf<String>()
    fun visit(parentId: String?, depth: Int, path: List<String>) {
        byParent[parentId].orEmpty().sortedBy { it.order }.forEach { group ->
            if (!visited.add(group.id)) return@forEach
            val nextPath = path + group.name
            result += FlattenedConversationGroup(group, depth, nextPath.joinToString(" / "))
            visit(group.id, depth + 1, nextPath)
        }
    }
    visit(null, 0, emptyList())
    return result
}

private fun conversationGroupPathItems(
    groups: List<ConversationGroup>,
    groupId: String?
): List<ConversationGroup> {
    if (groupId.isNullOrBlank()) return emptyList()
    val byId = groups.associateBy { it.id }
    val path = ArrayDeque<ConversationGroup>()
    val visited = HashSet<String>()
    var current = byId[groupId]
    while (current != null && visited.add(current.id)) {
        path.addFirst(current)
        current = current.parentId?.let(byId::get)
    }
    return path.toList()
}

private fun conversationGroupPath(groups: List<ConversationGroup>, groupId: String): String {
    return flattenConversationGroups(groups).firstOrNull { it.group.id == groupId }?.path.orEmpty()
}

private data class ConversationGroupPickerData(
    val options: List<ContactOption> = emptyList(),
    val friendIds: Set<String> = emptySet(),
    val officialIds: Set<String> = emptySet()
)

private fun loadConversationGroupOptions(
    groups: List<ConversationGroup>,
    currentGroupId: String
): ConversationGroupPickerData {
    val conversationApi = WeChatApis.conversations() ?: throw IllegalStateException("会话列表不可用")
    val contactApi = WeChatApis.contact().contacts()
    val assignedIds = groups.flatMap { it.conversationIds }.toSet()
    val friendIds = runCatching {
        contactApi?.getPickerContacts().orEmpty()
            .map { it.wxId.trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }.getOrDefault(emptySet())
    val officialIds = runCatching {
        contactApi?.getPickerOfficialAccounts().orEmpty()
            .map { it.wxId.trim() }
            .filter { it.isNotBlank() }
            .toSet()
    }.getOrDefault(emptySet())
    val labelsByUser = linkedMapOf<String, MutableList<String>>()
    runCatching { contactApi?.getContactLabelList().orEmpty() }
        .getOrDefault(emptyList())
        .forEach { label ->
            val labelName = label.labelName.ifBlank { label.labelId }
            if (labelName.isBlank()) return@forEach
            label.userNameList.forEach { wxId ->
                if (wxId.isNotBlank()) {
                    labelsByUser.getOrPut(wxId) { arrayListOf() }.add(labelName)
                }
            }
        }
    val paths = flattenConversationGroups(groups).associate { it.group.id to it.path }
    val ownerLabels = buildMap {
        groups.filterNot { it.id == currentGroupId }.forEach { owner ->
            owner.conversationIds.forEach { talker ->
                paths[owner.id]?.takeIf(String::isNotBlank)?.let { put(talker, "已在：$it") }
            }
        }
    }
    val rows = conversationApi.getRecentConversationUsernames(10000)
    val result = linkedMapOf<String, ContactOption>()
    rows.forEach { username ->
        val talker = username.trim()
        if (talker.isBlank() || ConversationGroupRuntime.isVirtualTalker(talker) ||
            talker in CONVERSATION_GROUP_EXCLUDED_TALKERS
        ) return@forEach
        val contact = runCatching { contactApi?.getContact(talker) }.getOrNull()
        if (contact == null && talker != "filehelper" && talker !in assignedIds) return@forEach
        val group = contact?.isGroup() == true
        result[talker] = ContactOption(
            id = talker,
            label = contact?.pickerDisplayName(group)
                .orEmpty()
                .ifBlank { conversationApi.getConversationTitle(talker).ifBlank { talker } },
            group = group,
            avatarUrl = contact?.avatarUrl.orEmpty(),
            avatarBackupUrl = contact?.avatarBackupUrl.orEmpty(),
            labels = labelsByUser[talker].orEmpty().distinct(),
            official = contact?.isOfficialAccount() == true,
            extraSummary = ownerLabels[talker].orEmpty(),
            searchAliases = listOfNotNull(
                contact?.remarkName,
                contact?.nickname,
                contact?.customWxId,
                talker
            ).filter { it.isNotBlank() }.distinct()
        )
    }
    assignedIds.filterNot(result::containsKey).forEach { talker ->
        val contact = runCatching { contactApi?.getContact(talker) }.getOrNull()
        val group = contact?.isGroup() == true ||
            talker.endsWith("@chatroom") ||
            talker.endsWith("@im.chatroom")
        result[talker] = ContactOption(
            id = talker,
            label = contact?.pickerDisplayName(group)
                .orEmpty()
                .ifBlank { conversationApi.getConversationTitle(talker).ifBlank { talker } },
            group = group,
            avatarUrl = contact?.avatarUrl.orEmpty(),
            avatarBackupUrl = contact?.avatarBackupUrl.orEmpty(),
            labels = labelsByUser[talker].orEmpty().distinct(),
            official = contact?.isOfficialAccount() == true || talker.startsWith("gh_"),
            extraSummary = ownerLabels[talker].orEmpty(),
            searchAliases = listOfNotNull(
                contact?.remarkName,
                contact?.nickname,
                contact?.customWxId,
                talker
            ).filter { it.isNotBlank() }.distinct()
        )
    }
    return ConversationGroupPickerData(
        options = result.values.toList(),
        friendIds = friendIds,
        officialIds = officialIds + result.values.filter { it.official }.map { it.id }
    )
}

private val CONVERSATION_GROUP_EXCLUDED_TALKERS = setOf(
    "message_fold",
    "conversationboxservice",
    "officialaccounts",
    "appbrand_notify_message",
    "notifymessage",
    "qmessage",
    "floatbottle"
)
}

