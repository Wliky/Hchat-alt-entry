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
import h.Hchat.crash.CrashReportRuntime
import h.Hchat.crash.CrashReportSettings
import h.Hchat.crash.CrashReportSettingsProvider
import h.Hchat.hooks.api.ui.HchatAgentIconDrawable
import h.Hchat.hooks.items.floatingshortcut.FloatingShortcutFeature
import h.Hchat.hooks.items.script.market.PluginMarketClient
import h.Hchat.hooks.items.script.market.PluginMarketSettings
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
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardFeature
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardRule
import h.Hchat.hooks.items.automessageforward.AutoMessageForwardSettings
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitFeature
import h.Hchat.hooks.items.callmedialimit.CallMediaLimitSettings
import h.Hchat.hooks.items.callmedialimit.CallRingtoneBlockFeature
import h.Hchat.hooks.items.chattime.ChatTimeStyleFeature
import h.Hchat.hooks.items.chattime.ChatTimeStyleSettings
import h.Hchat.hooks.items.conversationgroup.ConversationGroup
import h.Hchat.hooks.items.conversationgroup.ConversationGroupFeature
import h.Hchat.hooks.items.conversationgroup.ConversationGroupPickerSupport
import h.Hchat.hooks.items.conversationgroup.ConversationGroupRuntime
import h.Hchat.hooks.items.conversationgroup.ConversationGroupStore
import h.Hchat.hooks.items.conversationtabs.ConversationTabsFeature
import h.Hchat.hooks.items.conversationtabs.ConversationTabsStore
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
import h.Hchat.hooks.items.hometextcolor.HomeTextColorFeature
import h.Hchat.hooks.items.hometextcolor.HomeTextColorSettings
import h.Hchat.hooks.items.inputhint.InputHintFeature
import h.Hchat.hooks.items.inputhint.InputHintSettings
import h.Hchat.hooks.items.inputhint.InputHintStats
import h.Hchat.hooks.items.inputhint.OutgoingMessageStatsRepository
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveFeature
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveRuntime
import h.Hchat.hooks.items.keepalive.WeChatKeepAliveSettings
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationFeature
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationRuntime
import h.Hchat.hooks.items.keywordnotify.KeywordNotificationSettings
import h.Hchat.hooks.items.keywordnotify.KeywordRule
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
import h.Hchat.hooks.items.messagebubble.MessageBubbleFeature
import h.Hchat.hooks.items.messagebubble.MessageBubblePickResult
import h.Hchat.hooks.items.messagebubble.MessageBubblePicker
import h.Hchat.hooks.items.messagebubble.MessageBubbleSettings
import h.Hchat.hooks.items.messagebubble.MessageBubbleSlot
import h.Hchat.hooks.items.messagebubble.MessageBubbleStore
import h.Hchat.hooks.items.messagebubble.MessageBubbleKind
import h.Hchat.hooks.items.messagetextcolor.MessageTextColorFeature
import h.Hchat.hooks.items.messagetextcolor.MessageTextColorSettings
import h.Hchat.hooks.items.messageblock.MessageBlockBinding
import h.Hchat.hooks.items.messageblock.MessageBlockDefaultRule
import h.Hchat.hooks.items.messageblock.MessageBlockFeature
import h.Hchat.hooks.items.messageblock.MessageBlockSettings
import h.Hchat.hooks.items.messageblock.MessageBlockTemplate
import h.Hchat.hooks.items.messageforward.MessageForwardFeature
import h.Hchat.hooks.items.messageforward.MessageForwardSettings
import h.Hchat.hooks.items.miniprogrambaselib.FakeMiniProgramBaseLibFeature
import h.Hchat.hooks.items.miniprogrambaselib.FakeMiniProgramBaseLibSettings
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsFeature
import h.Hchat.hooks.items.miniprogramsplashad.SkipGlobalMiniProgramSplashAdsSettings
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsFeature
import h.Hchat.hooks.items.miniprogramvideoad.SkipMiniProgramVideoAdsSettings
import h.Hchat.hooks.items.musicorder.QQMusicOrderFeature
import h.Hchat.hooks.items.musicorder.QQMusicOrderSettings
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
import h.Hchat.hooks.items.protobuf.ProtobufPacketFeature
import h.Hchat.hooks.items.protobuf.ProtobufPacketRuntime
import h.Hchat.hooks.items.protobuf.ProtobufPacketSettings
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
import h.Hchat.hooks.items.script.ScriptPluginFeature
import h.Hchat.hooks.items.script.ScriptPluginManager
import h.Hchat.hooks.items.script.ScriptPluginRuntime
import h.Hchat.hooks.items.script.ScriptPluginSettings
import h.Hchat.hooks.items.script.market.PluginMarketHistoryVersion
import h.Hchat.hooks.items.script.market.PluginMarketInstaller
import h.Hchat.hooks.items.script.market.PluginMarketException
import h.Hchat.hooks.items.script.market.PluginMarketFile
import h.Hchat.hooks.items.script.market.PluginMarketComment
import h.Hchat.hooks.items.script.market.PluginMarketNotification
import h.Hchat.hooks.items.script.market.PluginMarketPlugin
import h.Hchat.hooks.items.script.market.PluginMarketRepository
import h.Hchat.hooks.items.script.market.PluginMarketReviewStatus
import h.Hchat.hooks.items.script.market.PluginMarketUserIdentity
import h.Hchat.hooks.items.script.agent.CodexSettings
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentClient
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentAttachment
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentChatMessage
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentConfig
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentCancellation
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentContext
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentDiff
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentDraft
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentEventIds
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentLocalFiles
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentMcpServer
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentReasoning
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentRequest
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentResumeState
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSession
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSessionStore
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSettings
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentProfile
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentQuotedMessage
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentSpeech
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentTextMerge
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentToolEvent
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentToolResultStore
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentValidator
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceChange
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceToolConfirmation
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceWriteDecision
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWorkspaceTools
import h.Hchat.hooks.items.script.agent.ScriptPluginAgentWriter
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskContentItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskFeature
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskItem
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskRuntimeCoordinator
import h.Hchat.hooks.items.scheduledtask.ScheduledTaskSettings
import h.Hchat.hooks.items.selectedmessages.SelectedMessageSnapshot
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesFeature
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesRuntimeCoordinator
import h.Hchat.hooks.items.selectedmessages.SelectedMessagesSettings
import h.Hchat.hooks.items.settings.PluginAgentEntryProvider
import h.Hchat.hooks.items.settings.SettingsEntrySettings
import h.Hchat.hooks.items.settings.SettingsFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadFeature
import h.Hchat.hooks.items.shortvideo.FinderMediaDownloadSettings
import h.Hchat.hooks.items.statuslimit.StatusTextLimitFeature
import h.Hchat.hooks.items.statuslimit.StatusTextLimitSettings
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
import h.Hchat.preferences.TermsGate
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
internal fun AutoReplyRuleEditorPage(
    rule: AutoReplyRule,
    listState: LazyListState,
    onBack: () -> Unit,
    onSave: (AutoReplyRule) -> Unit,
    onDraftChange: (AutoReplyRule) -> Unit,
    onEditSteps: (AutoReplyRule) -> Unit
) {
    val context = LocalContext.current
    var current by remember(rule) { mutableStateOf(rule) }
    var optionPicker by remember { mutableStateOf<OptionPickerRequest?>(null) }
    var contactPicker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var groupMemberPicker by remember { mutableStateOf<GroupMemberPickerRequest?>(null) }
    val editorRoute = when {
        optionPicker != null -> AutoReplyEditorRoute.OptionPicker(optionPicker!!)
        contactPicker != null -> AutoReplyEditorRoute.ContactPicker(contactPicker!!)
        groupMemberPicker != null -> AutoReplyEditorRoute.GroupMemberPicker(groupMemberPicker!!)
        else -> AutoReplyEditorRoute.Main
    }
    SettingsRouteTransition(
        targetState = editorRoute,
        label = "AutoReplyEditorRoute",
        depthOf = { it.depth() }
    ) { route ->
        when (route) {
            is AutoReplyEditorRoute.OptionPicker -> {
                val request = route.request
                OptionPickerPage(
                    request = request,
                    onBack = { optionPicker = null },
                    onSelected = { request.onSelected(it); optionPicker = null }
                )
            }
            is AutoReplyEditorRoute.ContactPicker -> {
                val request = route.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { contactPicker = null },
                    onConfirm = { selected ->
                        request.onValue(formatIds(selected.map { it.id }))
                        Toast.makeText(context, "已选择 ${selected.size} 项", Toast.LENGTH_SHORT).show()
                        contactPicker = null
                    }
                )
            }
            is AutoReplyEditorRoute.GroupMemberPicker -> {
                val request = route.request
                GroupMemberPickerPage(
                    context = context,
                    request = request,
                    onBack = { groupMemberPicker = null },
                    onConfirm = { entries ->
                        request.onValue(formatIds(entries))
                        Toast.makeText(context, "已选择 ${entries.size} 个群成员", Toast.LENGTH_SHORT).show()
                        groupMemberPicker = null
                    }
                )
            }
            AutoReplyEditorRoute.Main -> AutoReplyRuleEditorContent(
                rule = current,
                listState = listState,
                onRuleChange = {
                    current = it
                    onDraftChange(it)
                },
                onBack = onBack,
                onSave = { onSave(current) },
                onEditSteps = { onEditSteps(current) },
                onOptionPicker = { optionPicker = it },
                onContactPicker = { contactPicker = it },
                onGroupMemberPicker = { groupMemberPicker = it }
            )
        }
    }
}

@Composable
internal fun AutoReplyRuleEditorContent(
    rule: AutoReplyRule,
    listState: LazyListState,
    onRuleChange: (AutoReplyRule) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onEditSteps: () -> Unit,
    onOptionPicker: (OptionPickerRequest) -> Unit,
    onContactPicker: (ContactPickerRequest) -> Unit,
    onGroupMemberPicker: (GroupMemberPickerRequest) -> Unit
) {
    val current = rule
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "编辑规则",
        largeTitle = "编辑规则",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = onSave,
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
        ) {
            item { SmallTitle(text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(current.enabled, "启用规则", "关闭后保留配置但不触发") { onRuleChange(current.copy(enabled = it)) }
                    InsetDivider()
                    InputRow("规则名称", "用于列表里识别规则", current.name) { onRuleChange(current.copy(name = it)) }
                    InsetDivider()
                    PopupOptionRow(
                        title = "匹配方式",
                        summary = autoReplyMatchLabel(current.matchType),
                        options = autoReplyMatchOptions(),
                        currentValue = current.matchType,
                        onValueChanged = {
                            onRuleChange(
                                current.copy(
                                    matchType = it,
                                    keyword = if (it == AutoReplySettings.MATCH_ANY) "" else current.keyword
                                )
                            )
                        }
                    )
                    if (current.matchType != AutoReplySettings.MATCH_ANY) {
                        InsetDivider()
                        InputRow("关键词", "多个关键词用 |、逗号或换行分隔", current.keyword, minLines = 2) {
                            onRuleChange(current.copy(keyword = it))
                        }
                    } else {
                        InsetDivider()
                        InputRow("排除关键词", "包含任一关键词时不触发，多个用 |、逗号或换行分隔", current.excludedKeywords, minLines = 2) {
                            onRuleChange(current.copy(excludedKeywords = it))
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "范围") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "生效范围",
                        summary = autoReplyTargetLabel(current.targetMode),
                        options = autoReplyTargetOptions(),
                        currentValue = current.targetMode,
                        onValueChanged = { onRuleChange(current.copy(targetMode = it)) }
                    )
                    if (current.targetMode == AutoReplySettings.TARGET_SPECIFIC) {
                        InsetDivider()
                        ActionRow("选择指定好友", autoReplySelectedIdSummary(formatIds(current.targetIds.filter { !isAutoReplyGroupId(it) }))) {
                            onContactPicker(ContactPickerRequest(
                                title = "选择指定好友",
                                mode = ContactPickerMode.FRIENDS,
                                multiSelect = true,
                                existingValue = formatIds(current.targetIds.filter { !isAutoReplyGroupId(it) }),
                                onValue = { onRuleChange(current.copy(targetIds = replaceAutoReplyTypedIds(current.targetIds, parseIds(it), group = false))) },
                                enableLabels = true
                            ))
                        }
                        InsetDivider()
                        ActionRow("选择指定群聊", autoReplySelectedIdSummary(formatIds(current.targetIds.filter { isAutoReplyGroupId(it) }))) {
                            onContactPicker(ContactPickerRequest(
                                title = "选择指定群聊",
                                mode = ContactPickerMode.GROUPS,
                                multiSelect = true,
                                existingValue = formatIds(current.targetIds.filter { isAutoReplyGroupId(it) }),
                                onValue = { onRuleChange(current.copy(targetIds = replaceAutoReplyTypedIds(current.targetIds, parseIds(it), group = true))) }
                            ))
                        }
                        InsetDivider()
                        ActionRow("选择指定群成员", autoReplySelectedIdSummary(formatIds(current.includedGroupMembers))) {
                            onGroupMemberPicker(GroupMemberPickerRequest(
                                title = "选择指定群成员",
                                existingValue = formatIds(current.includedGroupMembers),
                                onValue = { onRuleChange(current.copy(includedGroupMembers = parseIds(it))) }
                            ))
                        }
                    } else {
                        InsetDivider()
                        ActionRow("选择排除好友", autoReplySelectedIdSummary(formatIds(current.excludedIds.filter { !isAutoReplyGroupId(it) }))) {
                            onContactPicker(ContactPickerRequest(
                                title = "选择排除好友",
                                mode = ContactPickerMode.FRIENDS,
                                multiSelect = true,
                                existingValue = formatIds(current.excludedIds.filter { !isAutoReplyGroupId(it) }),
                                onValue = { onRuleChange(current.copy(excludedIds = replaceAutoReplyTypedIds(current.excludedIds, parseIds(it), group = false))) },
                                enableLabels = true
                            ))
                        }
                        InsetDivider()
                        ActionRow("选择排除群聊", autoReplySelectedIdSummary(formatIds(current.excludedIds.filter { isAutoReplyGroupId(it) }))) {
                            onContactPicker(ContactPickerRequest(
                                title = "选择排除群聊",
                                mode = ContactPickerMode.GROUPS,
                                multiSelect = true,
                                existingValue = formatIds(current.excludedIds.filter { isAutoReplyGroupId(it) }),
                                onValue = { onRuleChange(current.copy(excludedIds = replaceAutoReplyTypedIds(current.excludedIds, parseIds(it), group = true))) }
                            ))
                        }
                        InsetDivider()
                        ActionRow("选择排除群成员", autoReplySelectedIdSummary(formatIds(current.excludedGroupMembers))) {
                            onGroupMemberPicker(GroupMemberPickerRequest(
                                title = "选择排除群成员",
                                existingValue = formatIds(current.excludedGroupMembers),
                                onValue = { onRuleChange(current.copy(excludedGroupMembers = parseIds(it))) }
                            ))
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "触发") }
            item {
                SettingsCard {
                    PopupOptionRow(
                        title = "@ 触发",
                        summary = autoReplyAtLabel(current.atTrigger),
                        options = autoReplyAtOptions(),
                        currentValue = current.atTrigger,
                        onValueChanged = { onRuleChange(current.copy(atTrigger = it)) }
                    )
                    InsetDivider()
                    PopupOptionRow(
                        title = "拍一拍",
                        summary = autoReplyPatLabel(current.patTrigger),
                        options = autoReplyPatOptions(),
                        currentValue = current.patTrigger,
                        onValueChanged = { onRuleChange(current.copy(patTrigger = it)) }
                    )
                    InsetDivider()
                    TimeOfDayPickerRow("开始时间", current.startTime, allowEmpty = true) {
                        onRuleChange(current.copy(startTime = it))
                    }
                    InsetDivider()
                    TimeOfDayPickerRow("结束时间", current.endTime, allowEmpty = true) {
                        onRuleChange(current.copy(endTime = it))
                    }
                    InsetDivider()
                    NumberInputRow("最大回复次数", "0 表示不限制，按规则/会话/发送者统计", current.maxReplyCount.toString()) {
                        onRuleChange(current.copy(maxReplyCount = it.toIntOrNull()?.coerceAtLeast(0) ?: 0))
                    }
                    InsetDivider()
                    NumberInputRow("回复冷却时间", "单位秒，0 表示不限制；同一规则在同一会话内冷却", current.cooldownSeconds.toString()) {
                        onRuleChange(current.copy(cooldownSeconds = it.toLongOrNull()?.coerceAtLeast(0L) ?: 0L))
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "回复") }
            item {
                SettingsCard {
                    SwitchRow(current.replyAsQuote, "引用回复", "文本和 AI 可引用原消息回复") { onRuleChange(current.copy(replyAsQuote = it)) }
                    InsetDivider()
                    ActionRow("回复步骤", describeAutoReplySteps(current.steps), onEditSteps)
                }
            }
        }
    }
}

@Composable
internal fun AutoReplyFriendPage(
    context: Context,
    title: String,
    sp: SharedPreferences,
    enabledKey: String,
    delayKey: String,
    tagEnabledKey: String,
    tagNameKey: String,
    steps: List<AutoReplyStep>,
    listState: LazyListState,
    onBack: () -> Unit,
    automationKeys: AutoReplyFriendAutomationKeys?,
    onSelectLabels: (() -> Unit)?,
    onEditSteps: () -> Unit
) {
    var delayMs by remember { mutableStateOf(sp.getLong(delayKey, 2000L).coerceAtLeast(0L).toString()) }
    var tagName by remember { mutableStateOf(sp.getString(tagNameKey, "") ?: "") }
    var labelDateFormat by remember {
        mutableStateOf(sp.getString(automationKeys?.labelDateFormat ?: "", "yyyy-MM-dd") ?: "yyyy-MM-dd")
    }
    val selectedLabelNames = sp.getString(automationKeys?.labelSelectedNames ?: "", "") ?: ""
    var remarkDateFormat by remember {
        mutableStateOf(sp.getString(automationKeys?.remarkDateFormat ?: "", "yyMMdd") ?: "yyMMdd")
    }
    var remarkCustomText by remember {
        mutableStateOf(sp.getString(automationKeys?.remarkCustomText ?: "", "") ?: "")
    }
    val hasAutomation = automationKeys != null && onSelectLabels != null
    var labelDateEnabled by remember { mutableStateOf(automationKeys?.let { sp.getBoolean(it.labelDateEnable, false) } ?: false) }
    var labelExistingEnabled by remember { mutableStateOf(automationKeys?.let { sp.getBoolean(it.labelExistingEnable, false) } ?: false) }
    var remarkDateEnabled by remember { mutableStateOf(automationKeys?.let { sp.getBoolean(it.remarkDateEnable, false) } ?: false) }
    var remarkCustomEnabled by remember { mutableStateOf(automationKeys?.let { sp.getBoolean(it.remarkCustomEnable, false) } ?: false) }
    var tagEnabled by remember { mutableStateOf(sp.getBoolean(tagEnabledKey, false)) }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = title,
        largeTitle = title,
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = {
                    sp.edit()
                        .putLong(delayKey, delayMs.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L)
                        .putString(tagNameKey, tagName.trim())
                        .also { editor ->
                            automationKeys?.let { keys ->
                                editor.putString(keys.labelDateFormat, labelDateFormat.trim().ifBlank { "yyyy-MM-dd" })
                                editor.putString(keys.remarkDateFormat, remarkDateFormat.trim().ifBlank { "yyMMdd" })
                                editor.putString(keys.remarkCustomText, remarkCustomText.trim())
                            }
                        }
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
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
        ) {
            item {
                SettingsCard {
                    SwitchRow(sp, enabledKey, "启用", "开启后自动处理这个场景", false)
                    InsetDivider()
                    NumberInputRow("延迟发送", "单位 ms，通过好友后等待再发欢迎内容", delayMs) { delayMs = it }
                    if (hasAutomation) {
                        val keys = automationKeys!!
                        InsetDivider()
                        SwitchRow(sp, keys.labelNewFriendEnable, "自动标签新加好友", "给好友添加“新加好友”标签", false)
                        InsetDivider()
                        SwitchRow(labelDateEnabled, "自动标签日期", "按日期格式生成一个标签") {
                            labelDateEnabled = it
                            sp.edit().putBoolean(keys.labelDateEnable, it).apply()
                        }
                        if (labelDateEnabled) {
                            InsetDivider()
                            InputRow("标签日期格式", "例如 yyyy-MM-dd", labelDateFormat) { labelDateFormat = it }
                        }
                        InsetDivider()
                        SwitchRow(labelExistingEnabled, "自动标签已有标签", "把好友加入已选择的微信标签") {
                            labelExistingEnabled = it
                            sp.edit().putBoolean(keys.labelExistingEnable, it).apply()
                        }
                        if (labelExistingEnabled) {
                            InsetDivider()
                            ActionRow("选择标签", autoReplyLabelSummary(selectedLabelNames), onSelectLabels!!)
                        }
                        InsetDivider()
                        SwitchRow(sp, keys.remarkNewFriendEnable, "自动备注新加好友", "备注里加入“新加好友”", false)
                        InsetDivider()
                        SwitchRow(sp, keys.remarkNicknameSuffixEnable, "加昵称后面", "备注格式为微信昵称 + 备注内容", false)
                        InsetDivider()
                        SwitchRow(remarkDateEnabled, "自动备注日期", "备注里加入日期") {
                            remarkDateEnabled = it
                            sp.edit().putBoolean(keys.remarkDateEnable, it).apply()
                        }
                        if (remarkDateEnabled) {
                            InsetDivider()
                            InputRow("备注日期格式", "例如 yyMMdd", remarkDateFormat) { remarkDateFormat = it }
                        }
                        InsetDivider()
                        SwitchRow(remarkCustomEnabled, "自动备注自定义文本", "备注里加入自定义内容") {
                            remarkCustomEnabled = it
                            sp.edit().putBoolean(keys.remarkCustomEnable, it).apply()
                        }
                        if (remarkCustomEnabled) {
                            InsetDivider()
                            InputRow("备注自定义文本", "例如 渠道A", remarkCustomText) { remarkCustomText = it }
                        }
                    } else {
                        InsetDivider()
                        SwitchRow(tagEnabled, "自动打标签", "处理后把好友加入指定标签") {
                            tagEnabled = it
                            sp.edit().putBoolean(tagEnabledKey, it).apply()
                        }
                        if (tagEnabled) {
                            InsetDivider()
                            InputRow("标签名称", "标签需要微信里已存在", tagName) { tagName = it }
                        }
                    }
                    InsetDivider()
                    ActionRow("回复步骤", describeAutoReplySteps(steps), onEditSteps)
                }
            }
        }
    }
}

@Composable
internal fun AutoReplyLabelPickerPage(
    context: Context,
    sp: SharedPreferences,
    selectedKey: String,
    listState: LazyListState,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    var labels by remember { mutableStateOf<List<ContactLabelBean>>(emptyList()) }
    var selected by remember {
        mutableStateOf(splitAutoReplyNames(sp.getString(selectedKey, "") ?: "").toSet())
    }
    LaunchedEffect(Unit) {
        loading = true
        error = ""
        val api = WeChatApis.contact().contacts()
        if (api == null || !api.isAvailable) {
            labels = emptyList()
            error = "联系人标签不可用"
            loading = false
        } else {
            Thread({
                val result = runCatching { api.getContactLabelList() }
                Handler(Looper.getMainLooper()).post {
                    labels = result.getOrDefault(emptyList())
                        .filter { it.labelName.isNotBlank() || it.labelId.isNotBlank() }
                        .sortedBy { it.labelName.ifBlank { it.labelId }.lowercase(Locale.US) }
                    error = result.exceptionOrNull()?.message.orEmpty()
                    loading = false
                }
            }, "HchatAutoReplyLabels").start()
        }
    }
    val visible = labels.filter { label ->
        val name = label.labelName.ifBlank { label.labelId }
        val key = query.trim().lowercase(Locale.US)
        key.isBlank() || name.lowercase(Locale.US).contains(key)
    }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "选择标签",
        largeTitle = "选择标签",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = {
                    sp.edit()
                        .putString(selectedKey, selected.joinToString(";;;"))
                        .apply()
                    Toast.makeText(context, "标签已保存", Toast.LENGTH_SHORT).show()
                    onBack()
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            state = listState,
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
        ) {
            item { SmallTitle(text = "搜索") }
            item {
                SettingsCard {
                    InputRow("关键词", "按标签名筛选", query) { query = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "标签") }
            when {
                loading -> item { SettingsCard { EmptyText("正在载入标签...") } }
                error.isNotEmpty() -> item { SettingsCard { EmptyText(error) } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配标签") } }
                else -> visible.forEach { label ->
                    val name = label.labelName.ifBlank { label.labelId }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
                            cornerRadius = 14.dp
                        ) {
                            AutoReplyLabelRow(
                                name = name,
                                summary = "${label.userNameList.size} 人",
                                selected = selected.contains(name),
                                onClick = {
                                    selected = if (selected.contains(name)) selected - name else selected + name
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
internal fun AutoReplyLabelRow(
    name: String,
    summary: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressFeedbackColor = rememberPressFeedbackColor(pressed)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(pressFeedbackColor)
            .responsiveTap(
                onClick = onClick,
                onPressedChange = { pressed = it }
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MiuixTheme.colorScheme.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).ifEmpty { "签" },
                color = MiuixTheme.colorScheme.onSecondaryVariant,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = name, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(text = summary, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        SelectionMark(selected = selected, multiSelect = true)
    }
}

internal fun autoReplyLabelSummary(value: String): String {
    val names = splitAutoReplyNames(value)
    return if (names.isEmpty()) "未选择" else names.take(3).joinToString("、") + if (names.size > 3) " 等 ${names.size} 个" else ""
}

@Composable
internal fun XiaozhiCaptchaSvgView(svg: String) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(72.dp).padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MiuixTheme.colorScheme.secondaryVariant),
        factory = { context ->
            WebView(context).apply {
                setBackgroundColor(AndroidColor.TRANSPARENT)
                settings.javaScriptEnabled = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
            }
        },
        update = { view ->
            view.loadDataWithBaseURL(null, svg, "image/svg+xml", "UTF-8", null)
        }
    )
}

internal fun xiaozhiAgentSummary(agentId: String, agents: List<XiaozhiAgentOption>): String {
    if (agentId.isBlank()) return "未选择，点击拉取"
    val agent = agents.firstOrNull { it.id == agentId }
    return if (agent == null) "已保存 ID: $agentId" else "${agent.name} / ${agent.assistantName.ifBlank { agent.id }}"
}

internal fun xiaozhiModelSummary(model: String, models: List<XiaozhiModelOption>): String {
    if (model.isBlank()) return "未选择，点击拉取"
    val item = models.firstOrNull { it.id == model }
    return if (item == null) "已保存: $model" else "${item.name} / ${item.id}"
}

internal fun xiaozhiVoiceSummary(voice: String, voices: List<XiaozhiVoiceOption>): String {
    if (voice.isBlank()) return "未选择，留空使用控制台当前角色"
    val item = voices.firstOrNull { it.id == voice }
    return if (item == null) "已保存: $voice" else "${item.name} / ${item.id}"
}

internal fun splitAutoReplyNames(value: String): List<String> =
    value.split(";;;", "|", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .distinct()

@Composable
internal fun AutoReplyAiPage(context: Context, sp: SharedPreferences, onBack: () -> Unit) {
    val settings = remember { AutoReplySettings(context) }
    val scope = rememberCoroutineScope()
    var xiaozhiServe by remember { mutableStateOf(settings.xiaozhiConfig().serveUrl) }
    var xiaozhiOta by remember { mutableStateOf(settings.xiaozhiConfig().otaUrl) }
    var xiaozhiConsole by remember { mutableStateOf(settings.xiaozhiConfig().consoleUrl) }
    var xiaozhiConsolePhone by remember { mutableStateOf(settings.xiaozhiConfig().consolePhone) }
    var xiaozhiConsoleToken by remember { mutableStateOf(settings.xiaozhiConfig().consoleToken) }
    var xiaozhiConsoleAgentId by remember { mutableStateOf(settings.xiaozhiConfig().consoleAgentId) }
    var xiaozhiConsoleModel by remember { mutableStateOf(settings.xiaozhiConfig().consoleModel) }
    var xiaozhiVoiceRole by remember { mutableStateOf(settings.xiaozhiConfig().voiceRole) }
    var xiaozhiMusicMcp by remember { mutableStateOf(settings.xiaozhiConfig().musicMcpEnabled) }
    var xiaozhiMcpBridge by remember { mutableStateOf(settings.xiaozhiConfig().mcpBridgeEnabled) }
    var xiaozhiMcpEndpoint by remember { mutableStateOf(settings.xiaozhiConfig().mcpEndpointUrl) }
    var xiaozhiMcpKugouEnabled by remember { mutableStateOf(settings.xiaozhiConfig().mcpKugouEnabled) }
    var xiaozhiMcpKugouPluginId by remember { mutableStateOf(settings.xiaozhiConfig().mcpKugouPluginId) }
    var xiaozhiMcpKugouFunction by remember { mutableStateOf(settings.xiaozhiConfig().mcpKugouFunctionName) }
    var xiaozhiMcpReadySeconds by remember { mutableStateOf(settings.xiaozhiConfig().mcpReadySeconds.toString()) }
    var xiaozhiMcpIdleSeconds by remember { mutableStateOf(settings.xiaozhiConfig().mcpIdleSeconds.toString()) }
    var xiaozhiMcpStatus by remember { mutableStateOf(XiaozhiMcpStatus(false, "未查询", "点击刷新查询小智控制台")) }
    var configs by remember { mutableStateOf(settings.zhiliaConfigs()) }
    var activeName by remember { mutableStateOf(settings.activeZhiliaName()) }
    val activeConfig = configs.firstOrNull { it.name == activeName } ?: configs.firstOrNull() ?: settings.activeZhiliaConfig()
    var cfgName by remember(activeName, configs) { mutableStateOf(activeConfig.name) }
    var apiKey by remember(activeName, configs) { mutableStateOf(activeConfig.apiKey) }
    var apiBase by remember(activeName, configs) { mutableStateOf(activeConfig.apiBaseUrl) }
    var apiPath by remember(activeName, configs) { mutableStateOf(activeConfig.apiPath) }
    var model by remember(activeName, configs) { mutableStateOf(activeConfig.model) }
    var prompt by remember(activeName, configs) { mutableStateOf(activeConfig.systemPrompt) }
    var contextLimit by remember(activeName, configs) { mutableStateOf(activeConfig.contextLimit.toString()) }
    var modelQuery by remember { mutableStateOf("") }
    var fetchedModels by remember { mutableStateOf<List<String>>(emptyList()) }
    var favoriteModels by remember(apiBase) { mutableStateOf(settings.favoriteModels(apiBase)) }
    var loadingText by remember { mutableStateOf("") }
    var testResult by remember { mutableStateOf("") }
    var xiaozhiBindResult by remember { mutableStateOf("") }
    var xiaozhiCaptchaSvg by remember { mutableStateOf("") }
    var xiaozhiCaptchaCode by remember { mutableStateOf("") }
    var xiaozhiSmsCode by remember { mutableStateOf("") }
    var xiaozhiConsoleResult by remember { mutableStateOf("") }
    var xiaozhiAgents by remember { mutableStateOf<List<XiaozhiAgentOption>>(emptyList()) }
    var xiaozhiModels by remember { mutableStateOf<List<XiaozhiModelOption>>(emptyList()) }
    var xiaozhiVoices by remember { mutableStateOf<List<XiaozhiVoiceOption>>(emptyList()) }
    var route by remember { mutableStateOf<AutoReplyAiRoute>(AutoReplyAiRoute.Main) }
    var zhiliaPendingName by remember { mutableStateOf(activeName) }
    var pendingModel by remember { mutableStateOf(model) }
    val mainListState = rememberLazyListState()
    val xiaozhiListState = rememberLazyListState()
    val zhiliaListState = rememberLazyListState()
    val zhiliaConfigListState = rememberLazyListState()
    val zhiliaModelListState = rememberLazyListState()

    fun currentZhiliaConfig(): AutoReplyZhiliaConfig {
        return AutoReplyZhiliaConfig(
            name = cfgName,
            apiKey = apiKey.trim(),
            apiBaseUrl = apiBase.trim(),
            apiPath = apiPath.trim(),
            model = model.trim(),
            systemPrompt = prompt,
            contextLimit = contextLimit.toIntOrNull() ?: AutoReplySettings.DEFAULT_AI_CONTEXT_LIMIT
        ).normalized()
    }

    fun currentXiaozhiConfig(): AutoReplyXiaozhiConfig {
        return AutoReplyXiaozhiConfig(
            serveUrl = xiaozhiServe,
            otaUrl = xiaozhiOta,
            consoleUrl = xiaozhiConsole,
            consolePhone = xiaozhiConsolePhone,
            consoleToken = xiaozhiConsoleToken,
            consoleAgentId = xiaozhiConsoleAgentId,
            consoleModel = xiaozhiConsoleModel,
            voiceRole = xiaozhiVoiceRole,
            musicMcpEnabled = xiaozhiMusicMcp,
            mcpBridgeEnabled = xiaozhiMcpBridge,
            mcpEndpointUrl = xiaozhiMcpEndpoint,
            mcpKugouEnabled = xiaozhiMcpKugouEnabled,
            mcpKugouPluginId = xiaozhiMcpKugouPluginId,
            mcpKugouFunctionName = xiaozhiMcpKugouFunction,
            mcpReadySeconds = xiaozhiMcpReadySeconds.toIntOrNull()?.coerceIn(1, 30)
                ?: AutoReplySettings.DEFAULT_XIAOZHI_MCP_READY_SECONDS,
            mcpIdleSeconds = xiaozhiMcpIdleSeconds.toIntOrNull()?.coerceIn(10, 600)
                ?: AutoReplySettings.DEFAULT_XIAOZHI_MCP_IDLE_SECONDS
        )
    }

    fun saveXiaozhi(showToast: Boolean) {
        settings.saveXiaozhiConfig(currentXiaozhiConfig())
        if (showToast) Toast.makeText(context, "小智AI配置已保存", Toast.LENGTH_SHORT).show()
    }

    fun xiaozhiNeedToken(): Boolean {
        if (xiaozhiConsoleToken.trim().isBlank()) {
            xiaozhiConsoleResult = "请先登录小智控制台"
            Toast.makeText(context, "请先登录小智控制台", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun refreshXiaozhiCaptcha(updateStatus: Boolean = true) {
        if (updateStatus) xiaozhiConsoleResult = "正在刷新图形验证码..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.fetchCaptchaSvg() }
            }
            result.onSuccess {
                xiaozhiCaptchaSvg = it
                xiaozhiCaptchaCode = ""
                if (updateStatus) xiaozhiConsoleResult = "请输入图形验证码"
            }.onFailure {
                xiaozhiConsoleResult = "刷新图形验证码失败: ${it.message}"
            }
        }
    }

    fun sendXiaozhiSmsCode() {
        val phone = xiaozhiConsolePhone.trim()
        val captcha = xiaozhiCaptchaCode.trim()
        if (phone.isBlank() || captcha.isBlank()) {
            Toast.makeText(context, "请填写手机号和图形验证码", Toast.LENGTH_SHORT).show()
            return
        }
        settings.saveXiaozhiConfig(currentXiaozhiConfig())
        xiaozhiConsoleResult = "正在发送短信验证码..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.sendSmsCode(phone, captcha) }
            }
            result.onSuccess {
                xiaozhiConsoleResult = it
                xiaozhiCaptchaCode = ""
                refreshXiaozhiCaptcha(updateStatus = false)
            }.onFailure {
                xiaozhiConsoleResult = "发送短信验证码失败: ${it.message}"
                xiaozhiCaptchaCode = ""
                refreshXiaozhiCaptcha(updateStatus = false)
            }
            Toast.makeText(context, xiaozhiConsoleResult, Toast.LENGTH_SHORT).show()
        }
    }

    fun loginXiaozhiConsole() {
        val phone = xiaozhiConsolePhone.trim()
        val sms = xiaozhiSmsCode.trim()
        if (phone.isBlank() || sms.isBlank()) {
            Toast.makeText(context, "请填写手机号和短信验证码", Toast.LENGTH_SHORT).show()
            return
        }
        xiaozhiConsoleResult = "正在登录小智控制台..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.phoneLogin(phone, sms) }
            }
            result.onSuccess { token ->
                xiaozhiConsoleToken = token
                xiaozhiSmsCode = ""
                xiaozhiCaptchaCode = ""
                settings.saveXiaozhiConfig(currentXiaozhiConfig())
                xiaozhiConsoleResult = "登录成功，token 已保存"
                Toast.makeText(context, "小智控制台登录成功", Toast.LENGTH_SHORT).show()
                val agentsResult = withContext(Dispatchers.IO) {
                    runCatching { XiaozhiConsoleApi.fetchAgents(token) }
                }
                agentsResult.onSuccess { agents ->
                    xiaozhiAgents = agents
                    agents.firstOrNull()?.let { agent ->
                        if (xiaozhiConsoleAgentId.isBlank()) xiaozhiConsoleAgentId = agent.id
                        if (xiaozhiConsoleModel.isBlank()) xiaozhiConsoleModel = agent.model
                        if (xiaozhiVoiceRole.isBlank()) xiaozhiVoiceRole = agent.voice
                        settings.saveXiaozhiConfig(currentXiaozhiConfig())
                    }
                    xiaozhiConsoleResult = "登录成功，已拉取 ${agents.size} 个智能体"
                }.onFailure {
                    xiaozhiConsoleResult = "登录成功，拉取智能体失败: ${it.message}"
                }
            }.onFailure {
                xiaozhiConsoleResult = "登录失败: ${it.message}"
            }
        }
    }

    fun fetchXiaozhiAgents(openPicker: Boolean) {
        if (xiaozhiNeedToken()) return
        xiaozhiConsoleResult = "正在拉取智能体..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.fetchAgents(xiaozhiConsoleToken) }
            }
            result.onSuccess { agents ->
                xiaozhiAgents = agents
                xiaozhiConsoleResult = "已拉取 ${agents.size} 个智能体"
                if (openPicker && agents.isNotEmpty()) {
                    val currentIndex = agents.indexOfFirst { it.id == xiaozhiConsoleAgentId }.coerceAtLeast(0)
                    route = AutoReplyAiRoute.OptionPicker(
                        OptionPickerRequest(
                            title = "选择小智智能体",
                            options = agents.mapIndexed { index, agent ->
                                OptionItem(
                                    label = agent.name,
                                    value = index,
                                    summary = listOf(agent.assistantName, agent.model, agent.voice).filter { it.isNotBlank() }.joinToString(" / ")
                                )
                            },
                            currentValue = currentIndex,
                            onSelected = { selected ->
                                agents.getOrNull(selected.value)?.let { agent ->
                                    xiaozhiConsoleAgentId = agent.id
                                    xiaozhiConsoleModel = agent.model
                                    xiaozhiVoiceRole = agent.voice
                                    settings.saveXiaozhiConfig(currentXiaozhiConfig())
                                }
                                route = AutoReplyAiRoute.Xiaozhi
                            }
                        )
                    )
                }
            }.onFailure {
                xiaozhiConsoleResult = "拉取智能体失败: ${it.message}"
            }
        }
    }

    fun fetchXiaozhiModels(openPicker: Boolean) {
        if (xiaozhiNeedToken()) return
        xiaozhiConsoleResult = "正在拉取模型列表..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.fetchModels(xiaozhiConsoleToken) }
            }
            result.onSuccess { models ->
                xiaozhiModels = models
                xiaozhiConsoleResult = "已拉取 ${models.size} 个模型"
                if (openPicker && models.isNotEmpty()) {
                    val currentIndex = models.indexOfFirst { it.id == xiaozhiConsoleModel }.coerceAtLeast(0)
                    route = AutoReplyAiRoute.OptionPicker(
                        OptionPickerRequest(
                            title = "选择小智模型",
                            options = models.mapIndexed { index, item -> OptionItem(item.name, index, item.id) },
                            currentValue = currentIndex,
                            onSelected = { selected ->
                                models.getOrNull(selected.value)?.let { item ->
                                    xiaozhiConsoleModel = item.id
                                    settings.saveXiaozhiConfig(currentXiaozhiConfig())
                                }
                                route = AutoReplyAiRoute.Xiaozhi
                            }
                        )
                    )
                }
            }.onFailure {
                xiaozhiConsoleResult = "拉取模型失败: ${it.message}"
            }
        }
    }

    fun fetchXiaozhiVoices(openPicker: Boolean) {
        if (xiaozhiNeedToken()) return
        xiaozhiConsoleResult = "正在拉取语音角色..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { XiaozhiConsoleApi.fetchVoices(xiaozhiConsoleToken) }
            }
            result.onSuccess { voices ->
                xiaozhiVoices = voices
                xiaozhiConsoleResult = "已拉取 ${voices.size} 个语音角色"
                if (openPicker && voices.isNotEmpty()) {
                    val currentIndex = voices.indexOfFirst { it.id == xiaozhiVoiceRole }.coerceAtLeast(0)
                    route = AutoReplyAiRoute.OptionPicker(
                        OptionPickerRequest(
                            title = "选择语音角色",
                            options = voices.mapIndexed { index, item ->
                                OptionItem(item.name, index, listOf(item.id, item.languages.joinToString(",")).filter { it.isNotBlank() }.joinToString(" / "))
                            },
                            currentValue = currentIndex,
                            onSelected = { selected ->
                                voices.getOrNull(selected.value)?.let { item ->
                                    xiaozhiVoiceRole = item.id
                                    settings.saveXiaozhiConfig(currentXiaozhiConfig())
                                }
                                route = AutoReplyAiRoute.Xiaozhi
                            }
                        )
                    )
                }
            }.onFailure {
                xiaozhiConsoleResult = "拉取语音角色失败: ${it.message}"
            }
        }
    }

    fun saveXiaozhiConsoleAgentConfig() {
        if (xiaozhiNeedToken()) return
        val agentId = xiaozhiConsoleAgentId.trim()
        if (agentId.isBlank()) {
            Toast.makeText(context, "请先选择智能体", Toast.LENGTH_SHORT).show()
            return
        }
        settings.saveXiaozhiConfig(currentXiaozhiConfig())
        xiaozhiConsoleResult = "正在保存到小智控制台..."
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    XiaozhiConsoleApi.saveAgentConfig(
                        token = xiaozhiConsoleToken,
                        agentId = agentId,
                        model = xiaozhiConsoleModel,
                        voice = xiaozhiVoiceRole
                    )
                }
            }
            result.onSuccess { agent ->
                xiaozhiConsoleAgentId = agent.id
                xiaozhiConsoleModel = agent.model
                xiaozhiVoiceRole = agent.voice
                settings.saveXiaozhiConfig(currentXiaozhiConfig())
                xiaozhiConsoleResult = "已保存到控制台: ${agent.name}"
                Toast.makeText(context, "小智控制台配置已保存", Toast.LENGTH_SHORT).show()
            }.onFailure {
                xiaozhiConsoleResult = "保存到控制台失败: ${it.message}"
            }
        }
    }

    fun saveZhilia(showToast: Boolean) {
        val saved = currentZhiliaConfig()
        val next = configs.filterNot { it.name == activeName || it.name == saved.name } + saved
        settings.saveZhiliaConfigs(next, saved.name)
        configs = settings.zhiliaConfigs()
        activeName = saved.name
        if (sp.getBoolean(AutoReplySettings.KEY_AI_CLEAR_CONTEXT_ON_SAVE, AutoReplySettings.DEFAULT_AI_CLEAR_CONTEXT_ON_SAVE)) {
            AutoReplyRuntime.clearAiHistories()
        }
        if (showToast) Toast.makeText(context, "智聊AI配置已保存", Toast.LENGTH_SHORT).show()
    }

    fun switchZhiliaTo(name: String) {
        if (name.isBlank()) {
            Toast.makeText(context, "请选择配置", Toast.LENGTH_SHORT).show()
            return
        }
        if (name == activeName) {
            zhiliaPendingName = activeName
            Toast.makeText(context, "已是当前配置", Toast.LENGTH_SHORT).show()
            return
        }
        saveZhilia(showToast = false)
        settings.saveZhiliaConfigs(settings.zhiliaConfigs(), name)
        configs = settings.zhiliaConfigs()
        activeName = settings.activeZhiliaName()
        zhiliaPendingName = activeName
        Toast.makeText(context, "已切换到 $activeName", Toast.LENGTH_SHORT).show()
    }

    fun uniqueConfigName(base: String): String {
        val names = configs.map { it.name }.toSet()
        if (base !in names) return base
        var index = 2
        while ("$base$index" in names) index++
        return "$base$index"
    }

    val visibleModels = remember(fetchedModels, modelQuery, favoriteModels) {
        val keyword = modelQuery.trim().lowercase(Locale.US)
        fetchedModels
            .filter { keyword.isBlank() || it.lowercase(Locale.US).contains(keyword) }
            .sortedWith(compareByDescending<String> { it in favoriteModels }.thenBy { it.lowercase(Locale.US) })
    }

    SettingsRouteTransition(
        targetState = route,
        label = "AutoReplyAiRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            AutoReplyAiRoute.Main -> {
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = "AI 配置",
                    largeTitle = "AI 配置",
                    scrollBehavior = scrollBehavior,
                    bottomBar = { BottomActionBar("返回", onBack) }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = mainListState,
                        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                    ) {
                        item { SmallTitle(text = "配置分组") }
                        item {
                            SettingsCard {
                                ActionRow("小智AI配置", xiaozhiServe.ifBlank { "未设置 WebSocket 地址" }) {
                                    route = AutoReplyAiRoute.Xiaozhi
                                }
                                InsetDivider()
                                ActionRow("智聊AI配置", "当前启用：$activeName") {
                                    route = AutoReplyAiRoute.Zhilia
                                }
                            }
                        }
                    }
                }
            }
            AutoReplyAiRoute.Xiaozhi -> {
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = "小智AI配置",
                    largeTitle = "小智AI配置",
                    scrollBehavior = scrollBehavior,
                    bottomBar = {
                        BottomActionBar(
                            primaryText = "保存小智",
                            onPrimaryClick = { saveXiaozhi(showToast = true) },
                            secondaryText = "返回",
                            onSecondaryClick = { route = AutoReplyAiRoute.Main }
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = xiaozhiListState,
                        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                    ) {
                        item { SmallTitle(text = "连接地址") }
                        item {
                            SettingsCard {
                                InputRow("WebSocket 地址", "小智服务地址", xiaozhiServe) { xiaozhiServe = it }
                                InsetDivider()
                                InputRow("OTA 地址", "小智 OTA 地址", xiaozhiOta) { xiaozhiOta = it }
                                InsetDivider()
                                InputRow("控制台地址", "用于查看或绑定设备", xiaozhiConsole) { xiaozhiConsole = it }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "设备") }
                        item {
                            SettingsCard {
                                InfoRow("UUID", AutoReplyRuntime.xiaozhiDeviceUuid(context))
                                InsetDivider()
                                InfoRow("MAC 地址", AutoReplyRuntime.xiaozhiDeviceMac(context))
                                InsetDivider()
                                ActionRow("绑定设备", xiaozhiBindResult.ifBlank { "请求 OTA 获取验证码或绑定状态" }) {
                                    xiaozhiBindResult = "正在请求..."
                                    scope.launch {
                                        val result = withContext(Dispatchers.IO) {
                                            AutoReplyRuntime.bindXiaozhiDevice(context, currentXiaozhiConfig())
                                        }
                                        xiaozhiBindResult = result
                                        Toast.makeText(context, result.lineSequence().firstOrNull().orEmpty(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                val xiaozhiBindLink = remember(xiaozhiBindResult) { extractFirstHttpUrl(xiaozhiBindResult) }
                                if (xiaozhiBindLink != null) {
                                    InsetDivider()
                                    InfoRow("绑定链接", xiaozhiBindLink) {
                                        openExternalLink(context, xiaozhiBindLink)
                                    }
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "控制台登录") }
                        item {
                            SettingsCard {
                                InfoRow("登录状态", if (xiaozhiConsoleToken.isBlank()) "未登录" else "已保存 token")
                                InsetDivider()
                                InputRow("手机号", "登录成功后会记住手机号", xiaozhiConsolePhone) { xiaozhiConsolePhone = it }
                                InsetDivider()
                                ActionRow("刷新图形验证码", if (xiaozhiCaptchaSvg.isBlank()) "先刷新，再输入图片上的字符" else "已刷新，输入下方图形验证码") {
                                    refreshXiaozhiCaptcha()
                                }
                                if (xiaozhiCaptchaSvg.isNotBlank()) {
                                    InsetDivider()
                                    XiaozhiCaptchaSvgView(xiaozhiCaptchaSvg)
                                }
                                InsetDivider()
                                InputRow("图形验证码", "输入上方图片里的字符", xiaozhiCaptchaCode) { xiaozhiCaptchaCode = it }
                                InsetDivider()
                                ActionRow("发送短信验证码", "需要先填写手机号和图形验证码") {
                                    sendXiaozhiSmsCode()
                                }
                                InsetDivider()
                                InputRow("短信验证码", "输入手机收到的验证码", xiaozhiSmsCode) { xiaozhiSmsCode = it }
                                InsetDivider()
                                ActionRow("登录并保存 token", "登录成功后自动保存 token 并拉取智能体") {
                                    loginXiaozhiConsole()
                                }
                                if (xiaozhiConsoleResult.isNotBlank()) {
                                    InsetDivider()
                                    InfoRow("控制台状态", xiaozhiConsoleResult)
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "控制台配置") }
                        item {
                            SettingsCard {
                                ActionRow("选择智能体", xiaozhiAgentSummary(xiaozhiConsoleAgentId, xiaozhiAgents)) {
                                    fetchXiaozhiAgents(openPicker = true)
                                }
                                InsetDivider()
                                InfoRow("Agent ID", xiaozhiConsoleAgentId.ifBlank { "未选择" })
                                InsetDivider()
                                ActionRow("选择模型", xiaozhiModelSummary(xiaozhiConsoleModel, xiaozhiModels)) {
                                    fetchXiaozhiModels(openPicker = true)
                                }
                                InsetDivider()
                                ActionRow("选择语音角色", xiaozhiVoiceSummary(xiaozhiVoiceRole, xiaozhiVoices)) {
                                    fetchXiaozhiVoices(openPicker = true)
                                }
                                InsetDivider()
                                ActionRow("保存到小智控制台", "读取当前智能体配置后只替换模型和语音角色") {
                                    saveXiaozhiConsoleAgentConfig()
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "回复") }
                        item {
                            SettingsCard {
                                InputRow("语音角色ID", "留空使用控制台当前角色", xiaozhiVoiceRole) { xiaozhiVoiceRole = it }
                                InsetDivider()
                                SwitchRow(
                                    checked = xiaozhiMusicMcp,
                                    title = "提示官方 Music 工具",
                                    summary = "点歌类问题会提示小智优先使用控制台启用的官方 Music MCP",
                                    onCheckedChange = { xiaozhiMusicMcp = it }
                                )
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "MCP桥接") }
                        item {
                            SettingsCard {
                                SwitchRow(
                                    checked = xiaozhiMcpBridge,
                                    title = "启用MCP桥接实验",
                                    summary = "把 Hchat 能力暴露给小智调用；端点地址包含 token，请勿外传",
                                    onCheckedChange = { xiaozhiMcpBridge = it }
                                )
                                InsetDivider()
                                InputRow(
                                    "MCP Endpoint",
                                    "wss://api.xiaozhi.me/mcp/?token=...；留空不连接",
                                    xiaozhiMcpEndpoint
                                ) { xiaozhiMcpEndpoint = it }
                                InsetDivider()
                                XiaozhiMcpStatusRow(xiaozhiMcpStatus)
                                InsetDivider()
                                ActionRow("刷新连接状态", xiaozhiMcpStatus.detail) {
                                    if (xiaozhiConsoleToken.trim().isBlank() || xiaozhiConsoleAgentId.trim().isBlank()) {
                                        Toast.makeText(context, "请先登录并选择智能体", Toast.LENGTH_SHORT).show()
                                    } else {
                                        xiaozhiMcpStatus = XiaozhiMcpStatus(false, "查询中", "正在查询小智控制台...")
                                        scope.launch {
                                            val result = withContext(Dispatchers.IO) {
                                                runCatching {
                                                    XiaozhiConsoleApi.fetchMcpEndpointStatus(
                                                        xiaozhiConsoleToken,
                                                        xiaozhiConsoleAgentId
                                                    )
                                                }
                                            }
                                            result.onSuccess {
                                                xiaozhiMcpStatus = it
                                            }.onFailure {
                                                xiaozhiMcpStatus = XiaozhiMcpStatus(false, "查询失败", it.message.orEmpty())
                                            }
                                        }
                                    }
                                }
                                InsetDivider()
                                InputRow(
                                    "就绪等待时长",
                                    "断开后首次触发等待 MCP ready 的秒数，默认 5，可填 1-30",
                                    xiaozhiMcpReadySeconds
                                ) { xiaozhiMcpReadySeconds = it.filter { ch -> ch.isDigit() }.take(2) }
                                InsetDivider()
                                InputRow(
                                    "自动断开时长",
                                    "空闲多少秒后断开 MCP，默认 90，可填 10-600",
                                    xiaozhiMcpIdleSeconds
                                ) { xiaozhiMcpIdleSeconds = it.filter { ch -> ch.isDigit() }.take(3) }
                                InsetDivider()
                                SwitchRow(
                                    checked = xiaozhiMcpKugouEnabled,
                                    title = "调用点歌工具",
                                    summary = "让小智理解自然语言后调用本地点歌插件发送音乐卡片",
                                    onCheckedChange = { xiaozhiMcpKugouEnabled = it }
                                )
                                if (xiaozhiMcpKugouEnabled) {
                                    InsetDivider()
                                    InputRow(
                                        "点歌插件ID",
                                        "例如 QQ点歌",
                                        xiaozhiMcpKugouPluginId
                                    ) { xiaozhiMcpKugouPluginId = it }
                                    InsetDivider()
                                    InputRow(
                                        "点歌函数名",
                                        "例如 queryKugouMusic",
                                        xiaozhiMcpKugouFunction
                                    ) { xiaozhiMcpKugouFunction = it }
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "运行状态") }
                        item {
                            SettingsCard {
                                ActionRow("立即清空上下文", "清除当前运行中的小智对话记忆") {
                                    AutoReplyRuntime.clearAiHistories()
                                    Toast.makeText(context, "小智上下文已清空", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
            AutoReplyAiRoute.Zhilia -> {
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = "智聊AI配置",
                    largeTitle = "智聊AI配置",
                    scrollBehavior = scrollBehavior,
                    bottomBar = {
                        BottomActionBar(
                            primaryText = "保存智聊",
                            onPrimaryClick = { saveZhilia(showToast = true) },
                            secondaryText = "返回",
                            onSecondaryClick = { route = AutoReplyAiRoute.Main }
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = zhiliaListState,
                        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                    ) {
                        item { SmallTitle(text = "当前配置") }
                        item {
                            SettingsCard {
                                InfoRow("当前启用", activeName)
                                InsetDivider()
                                InputRow("配置名称", "例如 DeepSeek 主账号", cfgName) { cfgName = it }
                                InsetDivider()
                                InputRow("API Key", "OpenAI 兼容接口密钥", apiKey) { apiKey = it }
                                InsetDivider()
                                InputRow("API 地址", "例如 https://api.xxx.com/v1", apiBase) { apiBase = it }
                                InsetDivider()
                                InputRow("API 路径", "默认 /chat/completions", apiPath) { apiPath = it }
                                InsetDivider()
                                InputRow("模型", "例如 deepseek-ai/DeepSeek-V3", model) { model = it }
                                InsetDivider()
                                InputRow("系统指令", "AI 角色设定和回复要求", prompt, minLines = 4) { prompt = it }
                                InsetDivider()
                                NumberInputRow("上下文轮数", "0 表示不保留上下文", contextLimit) { contextLimit = it }
                                InsetDivider()
                                SwitchRow(sp, AutoReplySettings.KEY_AI_STREAM, "流式请求", "失败时会自动尝试非流式", AutoReplySettings.DEFAULT_AI_STREAM)
                                InsetDivider()
                                SwitchRow(sp, AutoReplySettings.KEY_AI_CLEAR_CONTEXT_ON_SAVE, "保存后清空上下文", "修改人设或模型后避免继续沿用旧对话", AutoReplySettings.DEFAULT_AI_CLEAR_CONTEXT_ON_SAVE)
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "配置管理") }
                        item {
                            SettingsCard {
                                ActionRow("配置列表", "${configs.size} 个配置，当前启用：$activeName") {
                                    zhiliaPendingName = activeName
                                    route = AutoReplyAiRoute.ZhiliaConfigs
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模型工具") }
                        item {
                            SettingsCard {
                                ActionRow("模型选择", model.ifBlank { "未设置模型" }) {
                                    pendingModel = model
                                    route = AutoReplyAiRoute.ZhiliaModels
                                }
                                InsetDivider()
                                ActionRow("测试连通性", testResult.ifBlank { "测试当前模型的流式和非流式请求" }) {
                                    val config = currentZhiliaConfig().toAiConfig(sp.getBoolean(AutoReplySettings.KEY_AI_STREAM, AutoReplySettings.DEFAULT_AI_STREAM))
                                    testResult = "正在测试..."
                                    scope.launch {
                                        testResult = withContext(Dispatchers.IO) {
                                            AutoReplyRuntime.testAiConnectivity(config)
                                        }
                                        Toast.makeText(context, testResult, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "运行状态") }
                        item {
                            SettingsCard {
                                ActionRow("立即清空上下文", "清除当前运行中的 AI 对话记忆") {
                                    AutoReplyRuntime.clearAiHistories()
                                    Toast.makeText(context, "AI 上下文已清空", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
            AutoReplyAiRoute.ZhiliaConfigs -> {
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = "智聊配置列表",
                    largeTitle = "智聊配置列表",
                    scrollBehavior = scrollBehavior,
                    bottomBar = {
                        BottomActionBar(
                            primaryText = "切换到所选",
                            onPrimaryClick = { switchZhiliaTo(zhiliaPendingName) },
                            secondaryText = "返回",
                            onSecondaryClick = { route = AutoReplyAiRoute.Zhilia }
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = zhiliaConfigListState,
                        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                    ) {
                        item { SmallTitle(text = "配置") }
                        configs.forEach { config ->
                            item {
                                SettingsCard(modifier = Modifier.padding(bottom = 8.dp)) {
                                    ActionRow(
                                        title = config.name,
                                        summary = buildString {
                                            append(config.model.ifBlank { "未设置模型" })
                                            if (config.name == activeName) append(" · 当前")
                                            if (config.name == zhiliaPendingName && config.name != activeName) append(" · 已选择")
                                        }
                                    ) {
                                        zhiliaPendingName = config.name
                                        Toast.makeText(context, "已选择 ${config.name}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "管理") }
                        item {
                            SettingsCard {
                                ActionRow("新增空配置", "创建后留在此页，可再手动切换") {
                                    val newName = uniqueConfigName("新配置")
                                    val next = configs + AutoReplyZhiliaConfig(newName, "", AutoReplySettings.DEFAULT_AI_API_BASE, AutoReplySettings.DEFAULT_AI_API_PATH, AutoReplySettings.DEFAULT_AI_MODEL, "", AutoReplySettings.DEFAULT_AI_CONTEXT_LIMIT)
                                    settings.saveZhiliaConfigs(next, activeName)
                                    configs = settings.zhiliaConfigs()
                                    zhiliaPendingName = newName
                                    Toast.makeText(context, "已新增 $newName", Toast.LENGTH_SHORT).show()
                                }
                                InsetDivider()
                                ActionRow("复制当前输入", "按智聊编辑页当前输入复制配置") {
                                    val newName = uniqueConfigName("${cfgName.ifBlank { activeName }}_副本")
                                    val next = configs + currentZhiliaConfig().copy(name = newName)
                                    settings.saveZhiliaConfigs(next, activeName)
                                    configs = settings.zhiliaConfigs()
                                    zhiliaPendingName = newName
                                    Toast.makeText(context, "已复制 $newName", Toast.LENGTH_SHORT).show()
                                }
                                if (configs.size > 1) {
                                    InsetDivider()
                                    ActionRow("删除当前配置", "删除正在编辑的配置，至少保留一个") {
                                        val next = configs.filterNot { it.name == activeName || it.name == cfgName }
                                        val nextActive = next.firstOrNull()?.name ?: AutoReplySettings.DEFAULT_ZHILIA_CONFIG_NAME
                                        settings.saveZhiliaConfigs(next, nextActive)
                                        configs = settings.zhiliaConfigs()
                                        activeName = settings.activeZhiliaName()
                                        zhiliaPendingName = activeName
                                        Toast.makeText(context, "已删除当前配置", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AutoReplyAiRoute.ZhiliaModels -> {
                val scrollBehavior = MiuixScrollBehavior()
                PageScaffold(
                    title = "智聊模型选择",
                    largeTitle = "智聊模型选择",
                    scrollBehavior = scrollBehavior,
                    bottomBar = {
                        BottomActionBar(
                            primaryText = "使用所选模型",
                            onPrimaryClick = {
                                model = pendingModel
                                Toast.makeText(context, "已选择模型: $pendingModel", Toast.LENGTH_SHORT).show()
                            },
                            secondaryText = "返回",
                            onSecondaryClick = { route = AutoReplyAiRoute.Zhilia }
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                        state = zhiliaModelListState,
                        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                    ) {
                        item { SmallTitle(text = "拉取模型") }
                        item {
                            SettingsCard {
                                ActionRow("拉取模型列表", if (loadingText.isBlank()) "从当前 API 地址获取 /models" else loadingText) {
                                    val base = apiBase
                                    val key = apiKey
                                    loadingText = "正在拉取模型..."
                                    scope.launch {
                                        val models = withContext(Dispatchers.IO) {
                                            AutoReplyRuntime.fetchAiModels(base, key)
                                        }
                                        fetchedModels = models
                                        loadingText = ""
                                        Toast.makeText(context, if (models.isEmpty()) "未获取到模型" else "已获取 ${models.size} 个模型", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        if (visibleModels.isNotEmpty()) {
                            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "模型列表 · ${visibleModels.size}") }
                            item {
                                SettingsCard(modifier = Modifier.padding(bottom = 8.dp)) {
                                    InputRow("搜索", "输入模型名过滤", modelQuery) { modelQuery = it }
                                }
                            }
                            visibleModels.take(80).forEach { itemModel ->
                                item {
                                    ZhiliaModelSelectCard(
                                        modelName = itemModel,
                                        currentModel = model,
                                        pendingModel = pendingModel,
                                        favorite = itemModel in favoriteModels,
                                        onSelect = {
                                            pendingModel = itemModel
                                            Toast.makeText(context, "已选择 $itemModel", Toast.LENGTH_SHORT).show()
                                        },
                                        onToggleFavorite = {
                                            val next = if (itemModel in favoriteModels) favoriteModels - itemModel else favoriteModels + itemModel
                                            favoriteModels = next
                                            settings.saveFavoriteModels(apiBase, next)
                                            Toast.makeText(context, if (itemModel in next) "已收藏模型" else "已取消收藏", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        } else {
                            item { SettingsCard { EmptyText("暂无模型列表，先点击“拉取模型列表”。") } }
                        }
                    }
                }
            }
            is AutoReplyAiRoute.OptionPicker -> {
                OptionPickerPage(
                    request = currentRoute.request,
                    onBack = { route = AutoReplyAiRoute.Xiaozhi },
                    onSelected = { currentRoute.request.onSelected(it) }
                )
            }
        }
    }
}

@Composable
internal fun ZhiliaModelSelectCard(
    modelName: String,
    currentModel: String,
    pendingModel: String,
    favorite: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val selected = modelName == pendingModel
    val current = modelName == currentModel
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 3.dp),
        cornerRadius = 14.dp
    ) {
        var pressed by remember { mutableStateOf(false) }
        val pressFeedbackColor = rememberPressFeedbackColor(pressed)
        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(pressFeedbackColor)
                .responsiveTap(
                    onClick = onSelect,
                    onPressedChange = { pressed = it }
                )
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = modelName, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                    val state = buildString {
                        if (current) append("当前")
                        if (selected && !current) {
                            if (isNotEmpty()) append(" · ")
                            append("已选择")
                        }
                        if (favorite) {
                            if (isNotEmpty()) append(" · ")
                            append("已收藏")
                        }
                        if (isEmpty()) append("点击选择")
                    }
                    Text(text = state, color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
                }
                Text(
                    text = if (selected) "已选" else "选择",
                    color = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            InsetDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onToggleFavorite() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    text = if (favorite) "取消收藏" else "收藏",
                    color = MiuixTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
internal fun AutoReplyStepsPage(
    title: String,
    initialSteps: List<AutoReplyStep>,
    onBack: () -> Unit,
    onSave: (List<AutoReplyStep>) -> Unit
) {
    val context = LocalContext.current
    var steps by remember(initialSteps) { mutableStateOf(initialSteps.ifEmpty { listOf(AutoReplyStep()) }) }
    var optionPicker by remember { mutableStateOf<OptionPickerRequest?>(null) }
    var contactPicker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var favoritePicker by remember { mutableStateOf<FavoritePickerRequest?>(null) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    fun update(index: Int, step: AutoReplyStep) {
        steps = steps.toMutableList().also { if (index in it.indices) it[index] = step }
    }
    val stepsRoute = when {
        favoritePicker != null -> AutoReplyStepsRoute.FavoritePicker(favoritePicker!!)
        optionPicker != null -> AutoReplyStepsRoute.OptionPicker(optionPicker!!)
        contactPicker != null -> AutoReplyStepsRoute.ContactPicker(contactPicker!!)
        else -> AutoReplyStepsRoute.Main
    }
    SettingsRouteTransition(
        targetState = stepsRoute,
        label = "AutoReplyStepsRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is AutoReplyStepsRoute.OptionPicker -> {
                val request = currentRoute.request
                OptionPickerPage(
                    request = request,
                    onBack = { optionPicker = null },
                    onSelected = { request.onSelected(it); optionPicker = null }
                )
            }
            is AutoReplyStepsRoute.ContactPicker -> {
                val request = currentRoute.request
                ContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { contactPicker = null },
                    onConfirm = { selected ->
                        request.onValue(formatIds(selected.map { it.id }))
                        Toast.makeText(context, "已选择 ${selected.size} 项", Toast.LENGTH_SHORT).show()
                        contactPicker = null
                    }
                )
            }
            is AutoReplyStepsRoute.FavoritePicker -> {
                FavoritePickerPage(
                    request = currentRoute.request,
                    onBack = { favoritePicker = null }
                )
            }
            AutoReplyStepsRoute.Main -> PageScaffold(
                title = title,
                largeTitle = title,
                scrollBehavior = scrollBehavior,
                bottomBar = {
                    BottomActionBar(
                        primaryText = "保存回复",
                        onPrimaryClick = { onSave(steps.filter { AutoReplySettings.isAiReplyMode(it.mode) || it.content.isNotBlank() }) },
                        secondaryText = "返回",
                        onSecondaryClick = onBack
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                    state = listState,
                    contentPadding = PaddingValues(top = padding.calculateTopPadding() + 8.dp, bottom = padding.calculateBottomPadding() + 84.dp)
                ) {
                    steps.forEachIndexed { index, step ->
                        item { SmallTitle(modifier = Modifier.padding(top = if (index == 0) 0.dp else 10.dp), text = "第 ${index + 1} 条") }
                        item {
                            SettingsCard {
                                PopupOptionRow(
                                    title = "回复类型",
                                    summary = autoReplyStepLabel(step.mode),
                                    options = autoReplyStepOptions(),
                                    currentValue = step.mode,
                                    onValueChanged = {
                                        update(
                                            index,
                                            step.copy(
                                                mode = it,
                                                content = when {
                                                    AutoReplySettings.isAiReplyMode(it) -> ""
                                                    it == step.mode -> step.content
                                                    it == AutoReplySettings.REPLY_FAVORITE ||
                                                        step.mode == AutoReplySettings.REPLY_FAVORITE -> ""
                                                    else -> step.content
                                                }
                                            )
                                        )
                                    }
                                )
                                InsetDivider()
                                if (!AutoReplySettings.isAiReplyMode(step.mode)) {
                                    AutoReplyStepContentEditor(
                                        context = context,
                                        step = step,
                                        onStepChange = { update(index, it) },
                                        onContactPicker = { contactPicker = it },
                                        onFavoritePicker = { favoritePicker = it }
                                    )
                                    InsetDivider()
                                }
                                NumberInputRow("发送前延迟", "单位 ms", step.delayMs.toString()) {
                                    update(index, step.copy(delayMs = it.toLongOrNull()?.coerceIn(0L, 600000L) ?: 0L))
                                }
                                InsetDivider()
                                SwitchRow(step.randomDelay, "随机追加延迟", "发送前额外随机等待 0-2 秒") {
                                    update(index, step.copy(randomDelay = it))
                                }
                                InsetDivider()
                                RedPacketReplyStepActions(
                                    canMoveUp = index > 0,
                                    canMoveDown = index < steps.lastIndex,
                                    onMoveUp = {
                                        steps = steps.toMutableList().also {
                                            val item = it.removeAt(index)
                                            it.add(index - 1, item)
                                        }
                                    },
                                    onMoveDown = {
                                        steps = steps.toMutableList().also {
                                            val item = it.removeAt(index)
                                            it.add(index + 1, item)
                                        }
                                    },
                                    onDelete = { steps = steps.toMutableList().also { it.removeAt(index) }.ifEmpty { listOf(AutoReplyStep()) } }
                                )
                            }
                        }
                    }
                    item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                    item {
                        SettingsCard {
                            ActionRow("新增回复", "添加一条按顺序发送的回复") {
                                steps = steps + AutoReplyStep(id = System.currentTimeMillis().toString(), content = "你好")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun AutoReplyStepContentEditor(
    context: Context,
    step: AutoReplyStep,
    onStepChange: (AutoReplyStep) -> Unit,
    onContactPicker: (ContactPickerRequest) -> Unit,
    onFavoritePicker: (FavoritePickerRequest) -> Unit
) {
    when {
        step.mode == AutoReplySettings.REPLY_TEXT || step.mode == AutoReplySettings.REPLY_XML -> {
            VariableInputRow(
                title = if (step.mode == AutoReplySettings.REPLY_XML) "XML 内容" else "回复内容",
                summary = autoReplyStepContentHint(step.mode),
                value = step.content,
                variables = autoReplyTemplateVariables,
                minLines = 4
            ) {
                onStepChange(step.copy(content = it))
            }
        }
        autoReplyStepUsesContactPicker(step.mode) -> {
            val mode = if (step.mode == AutoReplySettings.REPLY_CARD) ContactPickerMode.FRIENDS else ContactPickerMode.GROUPS
            ActionRow(
                title = autoReplyPickerTitle(step.mode),
                summary = autoReplySelectedIdSummary(step.content)
            ) {
                onContactPicker(
                    ContactPickerRequest(
                        title = autoReplyPickerTitle(step.mode),
                        mode = mode,
                        multiSelect = true,
                        existingValue = formatIds(autoReplySplitContent(step.content)),
                        onValue = { onStepChange(step.copy(content = it)) },
                        enableLabels = step.mode == AutoReplySettings.REPLY_CARD
                    )
                )
            }
            if (step.content.isNotBlank()) {
                InsetDivider()
                ActionRow("清空已选", "移除当前已选择内容") {
                    onStepChange(step.copy(content = ""))
                }
            }
        }
        step.mode == AutoReplySettings.REPLY_FAVORITE -> {
            ActionRow(
                title = autoReplyPickerTitle(step.mode),
                summary = favoriteSummary(step.content)
            ) {
                onFavoritePicker(
                    FavoritePickerRequest(
                        title = autoReplyPickerTitle(step.mode),
                        existingValue = step.content,
                        onValue = { onStepChange(step.copy(content = it)) },
                        multiSelect = true,
                        delimiter = ";;;"
                    )
                )
            }
            if (step.content.isNotBlank()) {
                InsetDivider()
                ActionRow("清空已选收藏", "移除当前回复类型的收藏") {
                    onStepChange(step.copy(content = ""))
                }
            }
        }
        autoReplyStepUsesFilePicker(step.mode) -> {
            ActionRow(
                title = autoReplyPickerTitle(step.mode),
                summary = autoReplyFileSummary(step.content)
            ) {
                val activity = context as? Activity
                if (activity == null) {
                    Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                    return@ActionRow
                }
                AutoReplyFilePickerBridge.launch(activity, step.mode) { paths ->
                    val merged = (autoReplySplitContent(step.content) + paths)
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .distinct()
                    if (merged.isNotEmpty()) {
                        onStepChange(step.copy(content = merged.joinToString(";;;")))
                        Toast.makeText(context, "已选择 ${paths.size} 个文件", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (step.content.isNotBlank()) {
                InsetDivider()
                ActionRow("清空已选文件", "移除当前回复类型的文件") {
                    onStepChange(step.copy(content = ""))
                }
            }
        }
        else -> {
            InputRow("回复内容", autoReplyStepContentHint(step.mode), step.content, minLines = 2) {
                onStepChange(step.copy(content = it))
            }
        }
    }
}

internal fun describeAutoReplyRule(rule: AutoReplyRule): String {
    val state = if (rule.enabled) "启用" else "关闭"
    val keyword = if (rule.matchType == AutoReplySettings.MATCH_ANY) "任意消息" else rule.keyword.ifBlank { "未填关键词" }
    return buildList {
        add(state)
        add(autoReplyMatchLabel(rule.matchType))
        add(keyword)
        add(autoReplyTargetLabel(rule.targetMode))
        if (rule.cooldownSeconds > 0L) add("冷却 ${rule.cooldownSeconds} 秒")
        add(describeAutoReplySteps(rule.steps))
    }.joinToString(" · ")
}

internal fun describeAutoReplySteps(steps: List<AutoReplyStep>): String {
    if (steps.isEmpty()) return "暂无回复"
    return steps.take(3).joinToString("、") { autoReplyStepLabel(it.mode) } +
        if (steps.size > 3) " 等 ${steps.size} 条" else "，共 ${steps.size} 条"
}

internal fun replaceAutoReplyTypedIds(old: Set<String>, picked: Set<String>, group: Boolean): Set<String> {
    return (old.filter { isAutoReplyGroupId(it) != group } + picked)
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

internal fun isAutoReplyGroupId(value: String): Boolean =
    value.endsWith("@chatroom") || value.endsWith("@im.chatroom")

internal fun conversationRuleCategoryForId(value: String): ConversationRuleCategory {
    val normalized = value.trim().lowercase(Locale.US)
    return when {
        normalized.startsWith("gh_") || normalized.endsWith("@app") || normalized == "newsapp" -> {
            ConversationRuleCategory.OFFICIAL
        }
        normalized.endsWith("@chatroom") || normalized.endsWith("@im.chatroom") -> {
            ConversationRuleCategory.GROUP
        }
        else -> ConversationRuleCategory.FRIEND
    }
}

internal fun conversationRuleCategoryForTarget(value: String): ConversationRuleCategory {
    return conversationRuleCategoryForId(value.substringBefore('/'))
}

internal fun conversationRuleCategoryMatches(
    rule: CustomNotificationRule,
    category: ConversationRuleCategory
): Boolean {
    if (category == ConversationRuleCategory.ALL) return true
    val actual = when {
        rule.official -> ConversationRuleCategory.OFFICIAL
        rule.group -> ConversationRuleCategory.GROUP
        else -> ConversationRuleCategory.FRIEND
    }
    return actual == category
}

internal fun conversationRuleCategoryMatches(
    binding: MessageBlockBinding,
    category: ConversationRuleCategory
): Boolean {
    if (category == ConversationRuleCategory.ALL) return true
    val actual = if (binding.targetType == MessageBlockSettings.TARGET_GROUP_MEMBER) {
        ConversationRuleCategory.GROUP
    } else {
        conversationRuleCategoryForTarget(binding.targetId)
    }
    return actual == category
}

internal fun conversationRuleCategoryMatches(
    binding: RedPacketRuleBinding,
    category: ConversationRuleCategory
): Boolean {
    return category == ConversationRuleCategory.ALL ||
        conversationRuleCategoryForId(binding.targetId) == category
}

internal fun conversationRuleCategoryMatches(
    binding: TransferRuleBinding,
    category: ConversationRuleCategory
): Boolean {
    return category == ConversationRuleCategory.ALL ||
        conversationRuleCategoryForId(binding.targetId) == category
}

internal fun autoReplyMatchOptions(): List<OptionItem> = listOf(
    OptionItem("模糊匹配", AutoReplySettings.MATCH_FUZZY, "内容包含关键词就触发"),
    OptionItem("全字匹配", AutoReplySettings.MATCH_EXACT, "消息内容与关键词完全相同"),
    OptionItem("正则匹配", AutoReplySettings.MATCH_REGEX, "关键词按正则表达式处理"),
    OptionItem("任意消息", AutoReplySettings.MATCH_ANY, "收到符合范围的消息就触发")
)

internal fun autoReplyMatchLabel(value: Int): String = autoReplyMatchOptions().firstOrNull { it.value == value }?.label ?: "模糊匹配"

internal fun autoReplyTargetOptions(): List<OptionItem> = listOf(
    OptionItem("全部聊天", AutoReplySettings.TARGET_ALL, "好友、群聊和公众号都可触发"),
    OptionItem("仅私聊", AutoReplySettings.TARGET_PRIVATE, "只回复好友私聊"),
    OptionItem("仅群聊", AutoReplySettings.TARGET_GROUP, "只回复群消息"),
    OptionItem("指定名单", AutoReplySettings.TARGET_SPECIFIC, "只回复指定 wxid、群号或群成员"),
    OptionItem("仅公众号", AutoReplySettings.TARGET_OFFICIAL, "只回复公众号消息")
)

internal fun autoReplyTargetLabel(value: Int): String = autoReplyTargetOptions().firstOrNull { it.value == value }?.label ?: "全部聊天"

internal fun autoReplyAtOptions(): List<OptionItem> = listOf(
    OptionItem("不限", AutoReplySettings.AT_NONE),
    OptionItem("@我触发", AutoReplySettings.AT_ME),
    OptionItem("@全体触发", AutoReplySettings.AT_ALL)
)

internal fun autoReplyAtLabel(value: Int): String = autoReplyAtOptions().firstOrNull { it.value == value }?.label ?: "不限"

internal fun autoReplyPatOptions(): List<OptionItem> = listOf(
    OptionItem("不限", AutoReplySettings.PAT_NONE),
    OptionItem("被拍一拍触发", AutoReplySettings.PAT_ME)
)

internal fun autoReplyPatLabel(value: Int): String = autoReplyPatOptions().firstOrNull { it.value == value }?.label ?: "不限"

internal fun autoReplyStepOptions(): List<OptionItem> = listOf(
    OptionItem("发送文字", AutoReplySettings.REPLY_TEXT, "支持变量和 | 分隔多条"),
    OptionItem("发送图片", AutoReplySettings.REPLY_IMAGE, "从系统文件管理器选择"),
    OptionItem("发送语音", AutoReplySettings.REPLY_VOICE, "从系统文件管理器选择"),
    OptionItem("随机语音", AutoReplySettings.REPLY_VOICE_RANDOM_FOLDER, "从已选语音随机发送"),
    OptionItem("发送表情", AutoReplySettings.REPLY_EMOJI, "从系统文件管理器选择"),
    OptionItem("发送视频", AutoReplySettings.REPLY_VIDEO, "从系统文件管理器选择"),
    OptionItem("发送名片", AutoReplySettings.REPLY_CARD, "从好友列表选择"),
    OptionItem("发送文件", AutoReplySettings.REPLY_FILE, "从系统文件管理器选择"),
    OptionItem("发送收藏", AutoReplySettings.REPLY_FAVORITE, "从最近收藏选择"),
    OptionItem("邀请进群", AutoReplySettings.REPLY_INVITE_GROUP, "从群聊列表选择"),
    OptionItem("发送 XML", AutoReplySettings.REPLY_XML, "AppMsg/XML 原文"),
    OptionItem("智聊AI回复", AutoReplySettings.REPLY_ZHILIA_AI, "使用智聊/OpenAI 兼容配置生成回复"),
    OptionItem("小智AI回复", AutoReplySettings.REPLY_XIAOZHI_AI, "使用小智 WebSocket 生成文字回复"),
    OptionItem("小智语音回复", AutoReplySettings.REPLY_XIAOZHI_VOICE, "使用小智 TTS 生成语音回复")
)

internal fun autoReplyStepLabel(value: Int): String = autoReplyStepOptions().firstOrNull { it.value == value }?.label ?: "发送文字"

internal fun autoReplyStepContentHint(value: Int): String = when (value) {
    AutoReplySettings.REPLY_TEXT -> "多条文本用 | 分隔；下方变量可点击插入"
    AutoReplySettings.REPLY_XML -> "填写 XML 原文；下方变量可点击插入"
    AutoReplySettings.REPLY_CARD -> "选择要分享的好友名片"
    AutoReplySettings.REPLY_INVITE_GROUP -> "选择要邀请对方加入的群聊"
    AutoReplySettings.REPLY_FAVORITE -> "从最近收藏选择，可多选后按顺序发送"
    AutoReplySettings.REPLY_VOICE_RANDOM_FOLDER -> "选择多个语音文件后随机发送一个"
    else -> "填写文件绝对路径，每行一个或用 ;;; 分隔"
}

internal fun autoReplyStepUsesFilePicker(mode: Int): Boolean = mode in setOf(
    AutoReplySettings.REPLY_IMAGE,
    AutoReplySettings.REPLY_VOICE,
    AutoReplySettings.REPLY_VOICE_RANDOM_FOLDER,
    AutoReplySettings.REPLY_EMOJI,
    AutoReplySettings.REPLY_VIDEO,
    AutoReplySettings.REPLY_FILE
)

internal fun autoReplyStepUsesContactPicker(mode: Int): Boolean =
    mode == AutoReplySettings.REPLY_CARD || mode == AutoReplySettings.REPLY_INVITE_GROUP

internal fun autoReplyPickerTitle(mode: Int): String = when (mode) {
    AutoReplySettings.REPLY_IMAGE -> "选择图片"
    AutoReplySettings.REPLY_VOICE -> "选择语音"
    AutoReplySettings.REPLY_VOICE_RANDOM_FOLDER -> "选择随机语音"
    AutoReplySettings.REPLY_EMOJI -> "选择表情文件"
    AutoReplySettings.REPLY_VIDEO -> "选择视频"
    AutoReplySettings.REPLY_CARD -> "选择名片好友"
    AutoReplySettings.REPLY_FILE -> "选择文件"
    AutoReplySettings.REPLY_FAVORITE -> "选择收藏"
    AutoReplySettings.REPLY_INVITE_GROUP -> "选择群聊"
    else -> "选择文件"
}

internal fun scheduledTaskPickerTitle(taskType: Int): String = when (taskType) {
    ScheduledTaskSettings.TYPE_IMAGE -> "选择图片"
    ScheduledTaskSettings.TYPE_VIDEO -> "选择视频"
    ScheduledTaskSettings.TYPE_VOICE -> "选择语音文件"
    ScheduledTaskSettings.TYPE_EMOJI -> "选择表情文件"
    ScheduledTaskSettings.TYPE_FILE -> "选择文件"
    ScheduledTaskSettings.TYPE_FAVORITE -> "选择收藏"
    else -> "选择文件"
}

internal fun autoReplyFileSummary(value: String): String {
    val items = autoReplySplitContent(value)
    if (items.isEmpty()) return "未选择"
    val names = items.take(3).map { File(it).name.ifBlank { it } }
    return names.joinToString("、") + if (items.size > 3) " 等 ${items.size} 个文件" else "，共 ${items.size} 个文件"
}

internal fun autoReplySelectedIdSummary(value: String): String {
    val count = autoReplySplitContent(value).size
    return if (count == 0) "未选择" else "已选择 $count 项"
}

internal fun autoReplySplitContent(value: String): List<String> {
    return value.split(";;;")
        .flatMap { it.split('\n') }
        .flatMap { it.split('|') }
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}

@Composable
internal fun CustomNotificationMainPage(
    context: Context,
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    rules: List<CustomNotificationRule>,
    defaultPrivateRule: CustomNotificationRule,
    defaultGroupRule: CustomNotificationRule,
    defaultOfficialRule: CustomNotificationRule,
    onBack: () -> Unit,
    onOpenRules: () -> Unit,
    onEditDefault: (CustomNotificationDefaultKind) -> Unit
) {
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
            item { SmallTitle(text = "自定义通知") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        CustomNotificationSettings.KEY_ENABLE,
                        "启用自定义通知",
                        "开启后按会话专属规则或默认规则接管微信通知",
                        CustomNotificationSettings.DEFAULT_ENABLE
                    )
                    InsetDivider()
                    ActionRow(
                        "默认私聊通知",
                        customNotificationRuleSummary(context, defaultPrivateRule)
                    ) {
                        onEditDefault(CustomNotificationDefaultKind.PRIVATE)
                    }
                    InsetDivider()
                    ActionRow(
                        "默认群聊通知",
                        customNotificationRuleSummary(context, defaultGroupRule)
                    ) {
                        onEditDefault(CustomNotificationDefaultKind.GROUP)
                    }
                    InsetDivider()
                    ActionRow(
                        "默认公众号通知",
                        customNotificationRuleSummary(context, defaultOfficialRule)
                    ) {
                        onEditDefault(CustomNotificationDefaultKind.OFFICIAL)
                    }
                    InsetDivider()
                    ActionRow(
                        "会话规则",
                        if (rules.isEmpty()) "未配置会话" else "${rules.size} 个会话，${rules.count { it.enabled }} 个启用"
                    ) {
                        onOpenRules()
                    }
                }
            }
        }
    }
}

@Composable
internal fun CustomNotificationRuleListPage(
    context: Context,
    rules: List<CustomNotificationRule>,
    query: String,
    onQueryChange: (String) -> Unit,
    listState: LazyListState,
    onBack: () -> Unit,
    onSaveRules: (List<CustomNotificationRule>) -> Unit,
    onEditRule: (String) -> Unit,
    onToggleRule: (CustomNotificationRule, Boolean) -> Unit,
    onBatchEdit: () -> Unit,
    onAddRules: () -> Unit
) {
    var category by remember { mutableStateOf(ConversationRuleCategory.ALL) }
    var batchDeleteMode by remember { mutableStateOf(false) }
    var selectedTalkers by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val scrollBehavior = MiuixScrollBehavior()
    val lower = query.trim().lowercase(Locale.US)
    val visible = rules.filter {
        conversationRuleCategoryMatches(it, category) && (
            lower.isBlank() ||
                it.label.lowercase(Locale.US).contains(lower) ||
                it.talker.lowercase(Locale.US).contains(lower)
            )
    }.sortedWith(compareBy<CustomNotificationRule> { !it.enabled }.thenBy { it.group }.thenBy { it.label.lowercase(Locale.US) })
    val visibleTalkers = visible.mapTo(LinkedHashSet()) { it.talker }
    val allVisibleSelected = visibleTalkers.isNotEmpty() && visibleTalkers.all { it in selectedTalkers }
    val selectedRules = rules.filter { it.talker in selectedTalkers }
    PageScaffold(
        title = "会话规则",
        largeTitle = "会话规则",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            if (batchDeleteMode) {
                BottomActionBar(
                    primaryText = "删除所选（${selectedRules.size}）",
                    onPrimaryClick = {
                        if (selectedRules.isEmpty()) {
                            Toast.makeText(context, "请先选择会话规则", Toast.LENGTH_SHORT).show()
                        } else {
                            showDeleteConfirm = true
                        }
                    },
                    secondaryText = "取消",
                    onSecondaryClick = {
                        batchDeleteMode = false
                        selectedTalkers = emptySet()
                    },
                    middleText = if (visibleTalkers.isEmpty()) null else if (allVisibleSelected) "取消全选" else "全选",
                    onMiddleClick = if (visibleTalkers.isEmpty()) null else {
                        {
                            selectedTalkers = if (allVisibleSelected) {
                                selectedTalkers - visibleTalkers
                            } else {
                                selectedTalkers + visibleTalkers
                            }
                        }
                    }
                )
            } else {
                BottomActionBar(
                    primaryText = "添加会话",
                    onPrimaryClick = onAddRules,
                    secondaryText = "返回",
                    onSecondaryClick = onBack,
                    middleText = if (rules.isEmpty()) null else "批量删除",
                    onMiddleClick = if (rules.isEmpty()) null else {
                        {
                            batchDeleteMode = true
                            selectedTalkers = emptySet()
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
                    InputRow("搜索", "昵称 / wxid / 群号", query, onValueChange = onQueryChange)
                }
            }
            if (rules.isNotEmpty() && !batchDeleteMode) {
                item {
                    SettingsCard {
                        ActionRow("批量配置", "统一修改全部 ${rules.size} 个会话规则", onBatchEdit)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = if (visible.isEmpty()) "规则" else "规则 · ${visible.size} 项") }
            when {
                rules.isEmpty() -> item { SettingsCard { EmptyText("暂无会话规则，点击底部“添加会话”。") } }
                visible.isEmpty() -> item { SettingsCard { EmptyText("没有匹配结果") } }
                else -> visible.forEach { rule ->
                    item {
                        SettingsCard(modifier = Modifier.padding(bottom = 8.dp)) {
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = customNotificationRuleTitle(rule),
                                        value = 0,
                                        summary = customNotificationRuleSummary(context, rule)
                                    ),
                                    selected = rule.talker in selectedTalkers,
                                    onClick = {
                                        selectedTalkers = if (rule.talker in selectedTalkers) {
                                            selectedTalkers - rule.talker
                                        } else {
                                            selectedTalkers + rule.talker
                                        }
                                    }
                                )
                            } else {
                                ActionRow(
                                    title = customNotificationRuleTitle(rule),
                                    summary = customNotificationRuleSummary(context, rule)
                                ) {
                                    onEditRule(rule.talker)
                                }
                                InsetDivider()
                                SwitchRow(
                                    checked = rule.enabled,
                                    title = "启用此规则",
                                    summary = if (rule.enabled) "当前会话已接管通知" else "当前会话不接管通知"
                                ) {
                                    onToggleRule(rule, it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    BatchDeleteConfirmDialog(
        show = showDeleteConfirm,
        message = "将删除已选的 ${selectedRules.size} 个会话规则，此操作不可撤销。",
        labels = selectedRules.map { customNotificationRuleTitle(it) },
        onDismiss = { showDeleteConfirm = false },
        onConfirm = {
            val targets = selectedRules
            val targetTalkers = targets.mapTo(HashSet()) { it.talker }
            showDeleteConfirm = false
            batchDeleteMode = false
            selectedTalkers = emptySet()
            onSaveRules(rules.filterNot { it.talker in targetTalkers })
            Toast.makeText(context, "已删除 ${targets.size} 个会话规则", Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
internal fun CustomNotificationBatchEditorPage(
    ruleCount: Int,
    onBack: () -> Unit,
    onApply: (CustomNotificationBatchUpdate) -> Unit
) {
    var applyEnabled by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var applyMode by remember { mutableStateOf(false) }
    var dnd by remember { mutableStateOf(false) }
    var applyVibrate by remember { mutableStateOf(false) }
    var vibrate by remember { mutableStateOf(true) }
    var applySound by remember { mutableStateOf(false) }
    var sound by remember { mutableStateOf(true) }
    var applyMarkRead by remember { mutableStateOf(false) }
    var markRead by remember { mutableStateOf(true) }
    var applyQuickReply by remember { mutableStateOf(false) }
    var quickReply by remember { mutableStateOf(false) }
    var applyQuoteQuickReply by remember { mutableStateOf(false) }
    var quoteQuickReply by remember { mutableStateOf(false) }
    var applyMergeByTalker by remember { mutableStateOf(false) }
    var mergeByTalker by remember { mutableStateOf(false) }
    var applyShowDetail by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(true) }
    var applyIgnoreWechatDnd by remember { mutableStateOf(false) }
    var ignoreWechatDnd by remember { mutableStateOf(false) }
    var applyMute by remember { mutableStateOf(false) }
    var muteEnable by remember { mutableStateOf(false) }
    var muteStart by remember { mutableStateOf(CustomNotificationSettings.DEFAULT_MUTE_START) }
    var muteEnd by remember { mutableStateOf(CustomNotificationSettings.DEFAULT_MUTE_END) }
    var applyAtRules by remember { mutableStateOf(false) }
    var blockAtAll by remember { mutableStateOf(false) }
    var blockAtMe by remember { mutableStateOf(false) }
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "批量配置",
        largeTitle = "批量配置",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "应用到 $ruleCount 项",
                onPrimaryClick = {
                    onApply(
                        CustomNotificationBatchUpdate(
                            applyEnabled = applyEnabled,
                            enabled = enabled,
                            applyMode = applyMode,
                            dnd = dnd,
                            applyVibrate = applyVibrate,
                            vibrate = vibrate,
                            applySound = applySound,
                            sound = sound,
                            applyMarkRead = applyMarkRead,
                            markRead = markRead,
                            applyQuickReply = applyQuickReply,
                            quickReply = quickReply,
                            applyQuoteQuickReply = applyQuoteQuickReply,
                            quoteQuickReply = quoteQuickReply,
                            applyMergeByTalker = applyMergeByTalker,
                            mergeByTalker = mergeByTalker,
                            applyShowDetail = applyShowDetail,
                            showDetail = showDetail,
                            applyIgnoreWechatDnd = applyIgnoreWechatDnd,
                            ignoreWechatDnd = ignoreWechatDnd,
                            applyMute = applyMute,
                            muteEnable = muteEnable,
                            muteStart = muteStart,
                            muteEnd = muteEnd,
                            applyAtRules = applyAtRules,
                            blockAtAll = blockAtAll,
                            blockAtMe = blockAtMe
                        )
                    )
                },
                secondaryText = "返回",
                onSecondaryClick = onBack
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
            item { SmallTitle(text = "应用范围") }
            item { SettingsCard { EmptyText("只会修改已勾选“应用”的项目，未勾选的设置保持原样。群聊专属的 @ 规则只影响群聊规则。") } }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "基础") }
            item {
                SettingsCard {
                    SwitchRow(applyEnabled, "应用启用状态", "批量开关会话规则") { applyEnabled = it }
                    if (applyEnabled) {
                        InsetDivider()
                        SwitchRow(enabled, "启用规则", "关闭后不再接管对应会话通知") { enabled = it }
                    }
                    InsetDivider()
                    SwitchRow(applyMode, "应用免打扰", "批量设置是否弹自定义通知") { applyMode = it }
                    if (applyMode) {
                        InsetDivider()
                        SwitchRow(dnd, "免打扰", "开启后不弹自定义通知，只拦截原生通知") { dnd = it }
                    }
                    InsetDivider()
                    SwitchRow(applyIgnoreWechatDnd, "应用微信免打扰策略", "批量设置是否忽略微信原生会话免打扰") {
                        applyIgnoreWechatDnd = it
                    }
                    if (applyIgnoreWechatDnd) {
                        InsetDivider()
                        SwitchRow(
                            ignoreWechatDnd,
                            "忽略微信自带的消息免打扰",
                            "开启后，对应会话在微信中设置免打扰仍会弹出自定义通知"
                        ) { ignoreWechatDnd = it }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "提醒") }
            item {
                SettingsCard {
                    SwitchRow(applyVibrate, "应用震动", "批量修改震动状态") { applyVibrate = it }
                    if (applyVibrate) {
                        InsetDivider()
                        SwitchRow(vibrate, "震动", "通知时震动") { vibrate = it }
                    }
                    InsetDivider()
                    SwitchRow(applySound, "应用铃声", "批量修改铃声状态，不修改每个会话已选铃声文件") { applySound = it }
                    if (applySound) {
                        InsetDivider()
                        SwitchRow(sound, "铃声", "通知时播放铃声") { sound = it }
                    }
                    InsetDivider()
                    SwitchRow(applyMarkRead, "应用已读按钮", "批量修改通知栏已读按钮") { applyMarkRead = it }
                    if (applyMarkRead) {
                        InsetDivider()
                        SwitchRow(markRead, "已读按钮", "通知栏直接标记当前会话已读") { markRead = it }
                    }
                    InsetDivider()
                    SwitchRow(applyQuickReply, "应用快捷回复", "批量修改通知栏快捷回复") { applyQuickReply = it }
                    if (applyQuickReply) {
                        InsetDivider()
                        SwitchRow(quickReply, "快捷回复", "通知栏直接回复文本消息") { quickReply = it }
                    }
                    InsetDivider()
                    SwitchRow(applyQuoteQuickReply, "应用引用消息回复", "批量修改快捷回复是否引用原消息") {
                        applyQuoteQuickReply = it
                    }
                    if (applyQuoteQuickReply) {
                        InsetDivider()
                        SwitchRow(quoteQuickReply, "引用消息回复", "快捷回复时引用触发通知的原消息") {
                            quoteQuickReply = it
                        }
                    }
                    InsetDivider()
                    SwitchRow(applyMergeByTalker, "应用通知展示方式", "批量设置同会话通知合并或分散") { applyMergeByTalker = it }
                    if (applyMergeByTalker) {
                        InsetDivider()
                        SwitchRow(mergeByTalker, "合并同会话通知", "同一 wxid 的新消息更新到一条通知") { mergeByTalker = it }
                    }
                    InsetDivider()
                    SwitchRow(applyShowDetail, "应用消息详情", "批量修改通知内容是否显示详情") { applyShowDetail = it }
                    if (applyShowDetail) {
                        InsetDivider()
                        SwitchRow(showDetail, "显示消息详情", "关闭后只显示收到一条新消息") { showDetail = it }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "静默与群聊") }
            item {
                SettingsCard {
                    SwitchRow(applyMute, "应用静默时段", "批量修改时段静默") { applyMute = it }
                    if (applyMute) {
                        InsetDivider()
                        SwitchRow(muteEnable, "开启时段静默", "指定时间内不弹通知") { muteEnable = it }
                        if (muteEnable) {
                            InsetDivider()
                            TimeOfDayPickerRow("开始时间", muteStart) { muteStart = it }
                            InsetDivider()
                            TimeOfDayPickerRow("结束时间", muteEnd) { muteEnd = it }
                        }
                    }
                    InsetDivider()
                    SwitchRow(applyAtRules, "应用群聊 @ 规则", "只对群聊规则生效") { applyAtRules = it }
                    if (applyAtRules) {
                        InsetDivider()
                        SwitchRow(blockAtAll, "屏蔽@所有人", "命中 @所有人 时不弹通知") { blockAtAll = it }
                        InsetDivider()
                        SwitchRow(blockAtMe, "屏蔽@我", "命中 @我 时不弹通知") { blockAtMe = it }
                    }
                }
            }
        }
    }
}

@Composable
internal fun CustomNotificationMissingRulePage(onBack: () -> Unit) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "会话规则",
        largeTitle = "会话规则",
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
            item { SettingsCard { EmptyText("该规则已不存在") } }
        }
    }
}

@Composable
internal fun CustomNotificationRuleEditorPage(
    context: Context,
    initialRule: CustomNotificationRule,
    listState: LazyListState,
    isDefault: Boolean = false,
    onBack: () -> Unit,
    onSave: (CustomNotificationRule) -> Unit,
    onDelete: () -> Unit,
    onPickOnlyMembers: (String) -> Unit,
    onPickBlockMembers: (String) -> Unit
) {
    var enabled by remember(initialRule) { mutableStateOf(initialRule.enabled) }
    var dnd by remember(initialRule) { mutableStateOf(initialRule.mode == CustomNotificationSettings.MODE_DND) }
    var vibrate by remember(initialRule) { mutableStateOf(initialRule.vibrate) }
    var sound by remember(initialRule) { mutableStateOf(initialRule.sound) }
    var markRead by remember(initialRule) { mutableStateOf(initialRule.markRead) }
    var quickReply by remember(initialRule) { mutableStateOf(initialRule.quickReply) }
    var quoteQuickReply by remember(initialRule) { mutableStateOf(initialRule.quoteQuickReply) }
    var mergeByTalker by remember(initialRule) { mutableStateOf(initialRule.mergeByTalker) }
    var showDetail by remember(initialRule) { mutableStateOf(initialRule.showDetail) }
    var ignoreWechatDnd by remember(initialRule) { mutableStateOf(initialRule.ignoreWechatDnd) }
    var muteEnable by remember(initialRule) { mutableStateOf(initialRule.muteEnable) }
    var muteStart by remember(initialRule) { mutableStateOf(initialRule.muteStart) }
    var muteEnd by remember(initialRule) { mutableStateOf(initialRule.muteEnd) }
    var ringtone by remember(initialRule) { mutableStateOf(initialRule.ringtone) }
    var blockAtAll by remember(initialRule) { mutableStateOf(initialRule.blockAtAll) }
    var blockAtMe by remember(initialRule) { mutableStateOf(initialRule.blockAtMe) }
    var onlyMembers by remember(initialRule) { mutableStateOf(initialRule.onlyMembers) }
    var blockMembers by remember(initialRule) { mutableStateOf(initialRule.blockMembers) }
    val scrollBehavior = MiuixScrollBehavior()

    PageScaffold(
        title = when {
            isDefault && initialRule.official -> "默认公众号通知"
            isDefault && initialRule.group -> "默认群聊通知"
            isDefault -> "默认私聊通知"
            initialRule.group -> "群聊通知配置"
            initialRule.official -> "公众号通知配置"
            else -> "私聊通知配置"
        },
        largeTitle = initialRule.label.ifBlank { initialRule.talker },
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存",
                onPrimaryClick = {
                    onSave(
                        initialRule.copy(
                            enabled = enabled,
                            mode = if (dnd) CustomNotificationSettings.MODE_DND else CustomNotificationSettings.MODE_NOTIFY,
                            vibrate = vibrate,
                            sound = sound,
                            markRead = markRead,
                            quickReply = quickReply,
                            quoteQuickReply = quoteQuickReply,
                            mergeByTalker = mergeByTalker,
                            showDetail = showDetail,
                            ignoreWechatDnd = ignoreWechatDnd,
                            muteEnable = muteEnable,
                            muteStart = CustomNotificationSettings.normalizeTime(muteStart, CustomNotificationSettings.DEFAULT_MUTE_START),
                            muteEnd = CustomNotificationSettings.normalizeTime(muteEnd, CustomNotificationSettings.DEFAULT_MUTE_END),
                            ringtone = ringtone,
                            blockAtAll = blockAtAll,
                            blockAtMe = blockAtMe,
                            onlyMembers = if (isDefault) "" else CustomNotificationSettings.normalizeMemberRules(onlyMembers),
                            blockMembers = if (isDefault) "" else CustomNotificationSettings.normalizeMemberRules(blockMembers)
                        )
                    )
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
            item { SmallTitle(text = "会话") }
            item {
                SettingsCard {
                    if (isDefault) {
                        InfoRow(
                            "类型",
                            when {
                                initialRule.official -> "未单独配置的公众号"
                                initialRule.group -> "未单独配置的群聊"
                                else -> "未单独配置的私聊"
                            }
                        )
                    } else {
                        InfoRow("名称", initialRule.label.ifBlank { initialRule.talker })
                        InsetDivider()
                        InfoRow("ID", initialRule.talker)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "通知") }
            item {
                SettingsCard {
                    SwitchRow(
                        enabled,
                        if (isDefault) "启用默认规则" else "启用此会话规则",
                        if (isDefault) "开启后接管未单独配置的对应会话通知" else "关闭后不再接管该会话通知"
                    ) { enabled = it }
                    InsetDivider()
                    SwitchRow(dnd, "免打扰", "开启后不弹自定义通知，也会拦截原生通知") { dnd = it }
                    InsetDivider()
                    SwitchRow(
                        ignoreWechatDnd,
                        "忽略微信自带的消息免打扰",
                        "开启后，该会话在微信中设置免打扰仍会弹出自定义通知"
                    ) { ignoreWechatDnd = it }
                    InsetDivider()
                    SwitchRow(vibrate, "震动", "通知时震动") { vibrate = it }
                    InsetDivider()
                    SwitchRow(sound, "铃声", "通知时播放系统或自定义铃声") { sound = it }
                    InsetDivider()
                    SwitchRow(markRead, "已读按钮", "通知栏直接标记当前会话已读") { markRead = it }
                    InsetDivider()
                    SwitchRow(quickReply, "快捷回复", "通知栏直接回复文本消息") { quickReply = it }
                    if (quickReply) {
                        InsetDivider()
                        SwitchRow(quoteQuickReply, "引用消息回复", "快捷回复时引用触发通知的原消息") {
                            quoteQuickReply = it
                        }
                    }
                    InsetDivider()
                    SwitchRow(mergeByTalker, "合并同会话通知", "同一 wxid 的新消息更新到一条通知") { mergeByTalker = it }
                    InsetDivider()
                    SwitchRow(showDetail, "通知显示消息详情", "关闭后只显示收到一条新消息") { showDetail = it }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "铃声") }
            item {
                SettingsCard {
                    ActionRow("选择系统铃声", ringtoneDisplayName(context, ringtone, RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM)) {
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                        } else {
                            RingtonePickerBridge.launchSystem(activity, ringtone) { picked ->
                                ringtone = CustomNotificationRuntime.freezeRingtoneUri(context, picked)
                                Toast.makeText(context, "铃声已选择，保存后生效", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    InsetDivider()
                    ActionRow("从文件选择铃声", if (ringtone.isBlank()) "未选择" else ringtoneDisplayName(context, ringtone, RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM)) {
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                        } else {
                            RingtonePickerBridge.launchFile(activity) { picked ->
                                ringtone = CustomNotificationRuntime.freezeRingtoneUri(context, picked)
                                Toast.makeText(context, "铃声已选择，保存后生效", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (ringtone.isNotBlank()) {
                        InsetDivider()
                        ActionRow("清空铃声", "恢复跟随系统") {
                            ringtone = ""
                            Toast.makeText(context, "铃声已清空，保存后生效", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "静默时段") }
            item {
                SettingsCard {
                    SwitchRow(muteEnable, "开启时段静默", "指定时间内不弹通知") { muteEnable = it }
                    if (muteEnable) {
                        InsetDivider()
                        TimeOfDayPickerRow("开始时间", muteStart) { muteStart = it }
                        InsetDivider()
                        TimeOfDayPickerRow("结束时间", muteEnd) { muteEnd = it }
                    }
                }
            }
            if (initialRule.group) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "群聊过滤") }
                item {
                    SettingsCard {
                        if (!isDefault) {
                            ActionRow("仅显示成员通知", memberRulesSummary(onlyMembers)) {
                                onPickOnlyMembers(onlyMembers)
                            }
                            InsetDivider()
                            ActionRow("屏蔽成员通知", memberRulesSummary(blockMembers)) {
                                onPickBlockMembers(blockMembers)
                            }
                            InsetDivider()
                        }
                        SwitchRow(blockAtAll, "屏蔽@所有人", "命中 @所有人 时不弹通知") { blockAtAll = it }
                        InsetDivider()
                        SwitchRow(blockAtMe, "屏蔽@我", "命中 @我 时不弹通知") { blockAtMe = it }
                    }
                }
            }
            if (!isDefault) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        ActionRow("删除规则", "移除该会话自定义通知配置") {
                            onDelete()
                        }
                    }
                }
            }
        }
    }
}

internal fun customNotificationRuleFromContact(option: ContactOption): CustomNotificationRule {
    return CustomNotificationRule(
        id = option.id,
        talker = option.id,
        label = option.label.ifBlank { option.id },
        group = option.group,
        official = option.official
    )
}

internal fun customNotificationRuleTitle(rule: CustomNotificationRule): String {
    val prefix = when {
        rule.group -> "群聊"
        rule.official -> "公众号"
        else -> "私聊"
    }
    val state = if (rule.enabled) "" else "（关闭）"
    return "$prefix · ${rule.label.ifBlank { rule.talker }}$state"
}

internal fun customNotificationRuleSummary(context: Context, rule: CustomNotificationRule): String {
    val items = ArrayList<String>()
    if (!rule.enabled) items += "未启用"
    if (rule.mode == CustomNotificationSettings.MODE_DND) items += "免打扰"
    if (rule.sound) items += if (rule.ringtone.isBlank()) "铃声" else ringtoneDisplayName(context, rule.ringtone, RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM)
    if (rule.vibrate) items += "震动"
    if (rule.markRead) items += "已读按钮"
    if (rule.quickReply) items += if (rule.quoteQuickReply) "引用快捷回复" else "快捷回复"
    if (rule.mergeByTalker) items += "合并通知"
    if (rule.ignoreWechatDnd) items += "忽略微信免打扰"
    if (rule.muteEnable) items += "${rule.muteStart}-${rule.muteEnd}静默"
    if (rule.group) {
        if (rule.onlyMembers.isNotBlank()) items += "仅${CustomNotificationSettings.splitMemberRules(rule.onlyMembers).size}成员"
        if (rule.blockMembers.isNotBlank()) items += "屏蔽${CustomNotificationSettings.splitMemberRules(rule.blockMembers).size}成员"
        if (rule.blockAtAll) items += "屏蔽@所有人"
        if (rule.blockAtMe) items += "屏蔽@我"
    }
    return items.ifEmpty { listOf("点击配置通知规则") }.joinToString(" / ")
}

internal fun memberRulesSummary(value: String): String {
    val count = CustomNotificationSettings.splitMemberRules(value).size
    return if (count == 0) "未设置" else "已设置 $count 项"
}

internal fun memberRulesToGroupEntries(groupId: String, value: String): String {
    return CustomNotificationSettings.splitMemberRules(value)
        .map { member -> groupMemberEntry(groupId, member) }
        .joinToString(",")
}

internal fun groupEntriesToMemberRules(groupId: String, entries: List<String>): String {
    val members = entries.mapNotNull { entry ->
        val normalized = entry.trim()
        val separators = charArrayOf('/', '#', ':', '：')
        val index = separators.map { normalized.indexOf(it) }
            .filter { it > 0 }
            .minOrNull()
            ?: return@mapNotNull normalized.takeIf { it.isNotBlank() }
        val group = normalized.substring(0, index).trim()
        val member = normalized.substring(index + 1).trim()
        if (group == groupId && member.isNotBlank()) member else null
    }
    return members.distinct().joinToString(",")
}

internal data class CustomNotificationBatchUpdate(
    val applyEnabled: Boolean,
    val enabled: Boolean,
    val applyMode: Boolean,
    val dnd: Boolean,
    val applyVibrate: Boolean,
    val vibrate: Boolean,
    val applySound: Boolean,
    val sound: Boolean,
    val applyMarkRead: Boolean,
    val markRead: Boolean,
    val applyQuickReply: Boolean,
    val quickReply: Boolean,
    val applyQuoteQuickReply: Boolean,
    val quoteQuickReply: Boolean,
    val applyMergeByTalker: Boolean,
    val mergeByTalker: Boolean,
    val applyShowDetail: Boolean,
    val showDetail: Boolean,
    val applyIgnoreWechatDnd: Boolean,
    val ignoreWechatDnd: Boolean,
    val applyMute: Boolean,
    val muteEnable: Boolean,
    val muteStart: String,
    val muteEnd: String,
    val applyAtRules: Boolean,
    val blockAtAll: Boolean,
    val blockAtMe: Boolean
) {
    fun hasChanges(): Boolean {
        return applyEnabled || applyMode || applyVibrate || applySound || applyMarkRead || applyQuickReply || applyQuoteQuickReply || applyMergeByTalker ||
            applyShowDetail || applyIgnoreWechatDnd || applyMute || applyAtRules
    }

    fun apply(rule: CustomNotificationRule): CustomNotificationRule {
        var next = rule
        if (applyEnabled) next = next.copy(enabled = enabled)
        if (applyMode) next = next.copy(mode = if (dnd) CustomNotificationSettings.MODE_DND else CustomNotificationSettings.MODE_NOTIFY)
        if (applyVibrate) next = next.copy(vibrate = vibrate)
        if (applySound) next = next.copy(sound = sound)
        if (applyMarkRead) next = next.copy(markRead = markRead)
        if (applyQuickReply) next = next.copy(quickReply = quickReply)
        if (applyQuoteQuickReply) next = next.copy(quoteQuickReply = quoteQuickReply)
        if (applyMergeByTalker) next = next.copy(mergeByTalker = mergeByTalker)
        if (applyShowDetail) next = next.copy(showDetail = showDetail)
        if (applyIgnoreWechatDnd) next = next.copy(ignoreWechatDnd = ignoreWechatDnd)
        if (applyMute) {
            next = next.copy(
                muteEnable = muteEnable,
                muteStart = CustomNotificationSettings.normalizeTime(muteStart, CustomNotificationSettings.DEFAULT_MUTE_START),
                muteEnd = CustomNotificationSettings.normalizeTime(muteEnd, CustomNotificationSettings.DEFAULT_MUTE_END)
            )
        }
        if (applyAtRules && next.group) {
            next = next.copy(blockAtAll = blockAtAll, blockAtMe = blockAtMe)
        }
        return next
    }
}

@Composable
internal fun KeywordNotificationMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { KeywordNotificationSettings(context) }
    val sp = remember { HchatStorage.preferences(context, KeywordNotificationSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<KeywordNotificationRoute>(KeywordNotificationRoute.Main) }
    var keywords by remember { mutableStateOf(settings.keywords()) }
    var excludeContacts by remember { mutableStateOf(settings.excludeContacts()) }
    var includeContacts by remember { mutableStateOf(settings.includeContacts()) }
    val keywordMainListState = rememberLazyListState()
    val keywordListState = rememberLazyListState()
    var keywordQuery by remember { mutableStateOf("") }

    fun saveKeywords(next: List<KeywordRule>) {
        val clean = next.map { it.copy(keyword = it.keyword.trim()) }
            .filter { it.keyword.isNotBlank() }
            .distinctBy { it.keyword }
        keywords = clean
        settings.saveKeywords(clean)
    }

    fun saveContacts(includeMode: Boolean, ids: Set<String>) {
        if (includeMode) {
            includeContacts = ids
            settings.saveIncludeContacts(ids)
        } else {
            excludeContacts = ids
            settings.saveExcludeContacts(ids)
        }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "KeywordNotificationRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            KeywordNotificationRoute.Main -> KeywordNotificationMainPage(
                context = context,
                provider = provider,
                sp = sp,
                keywords = keywords,
                excludeContacts = excludeContacts,
                includeContacts = includeContacts,
                listState = keywordMainListState,
                onBack = onBack,
                onOpenKeywords = { route = KeywordNotificationRoute.KeywordList },
                onOpenTemplates = { route = KeywordNotificationRoute.TemplateEditor },
                onPickContacts = { includeMode ->
                    val current = if (includeMode) includeContacts else excludeContacts
                    route = KeywordNotificationRoute.ContactPicker(
                        request = ContactPickerRequest(
                            title = if (includeMode) "选择仅生效聊天" else "选择排除聊天",
                            mode = ContactPickerMode.BOTH,
                            multiSelect = true,
                            existingValue = current.joinToString(","),
                            onValue = {},
                            enableLabels = true
                        ),
                        includeMode = includeMode
                    )
                },
                onClearContacts = { includeMode ->
                    saveContacts(includeMode, emptySet())
                    Toast.makeText(context, if (includeMode) "已清空仅生效名单" else "已清空排除名单", Toast.LENGTH_SHORT).show()
                }
            )
            KeywordNotificationRoute.KeywordList -> KeywordNotificationKeywordListPage(
                keywords = keywords,
                query = keywordQuery,
                onQueryChange = { keywordQuery = it },
                listState = keywordListState,
                onBack = { route = KeywordNotificationRoute.Main },
                onAdd = { route = KeywordNotificationRoute.KeywordEditor(null) },
                onEdit = { route = KeywordNotificationRoute.KeywordEditor(it.keyword) },
                onClear = {
                    saveKeywords(emptyList())
                    Toast.makeText(context, "已清空关键词", Toast.LENGTH_SHORT).show()
                }
            )
            is KeywordNotificationRoute.KeywordEditor -> {
                val old = currentRoute.oldKeyword?.let { oldKeyword -> keywords.firstOrNull { it.keyword == oldKeyword } }
                KeywordNotificationKeywordEditorPage(
                    initial = old,
                    onBack = { route = KeywordNotificationRoute.KeywordList },
                    onSave = { saved ->
                        if (saved.keyword.isBlank()) {
                            Toast.makeText(context, "关键词不能为空", Toast.LENGTH_SHORT).show()
                        } else if (currentRoute.oldKeyword != saved.keyword && keywords.any { it.keyword == saved.keyword }) {
                            Toast.makeText(context, "该关键词已存在", Toast.LENGTH_SHORT).show()
                        } else {
                            val next = if (currentRoute.oldKeyword == null) {
                                keywords + saved
                            } else {
                                keywords.map { if (it.keyword == currentRoute.oldKeyword) saved else it }
                            }
                            saveKeywords(next)
                            Toast.makeText(context, "关键词已保存", Toast.LENGTH_SHORT).show()
                            route = KeywordNotificationRoute.KeywordList
                        }
                    },
                    onDelete = if (old == null) {
                        null
                    } else {
                        {
                            saveKeywords(keywords.filterNot { it.keyword == old.keyword })
                            Toast.makeText(context, "关键词已删除", Toast.LENGTH_SHORT).show()
                            route = KeywordNotificationRoute.KeywordList
                        }
                    }
                )
            }
            KeywordNotificationRoute.TemplateEditor -> KeywordNotificationTemplatePage(
                context = context,
                sp = sp,
                onBack = { route = KeywordNotificationRoute.Main }
            )
            is KeywordNotificationRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = KeywordNotificationRoute.Main },
                onConfirm = { picked ->
                    val ids = picked.map { it.id }.filter { it.isNotBlank() }.toSet()
                    saveContacts(currentRoute.includeMode, ids)
                    Toast.makeText(context, "名单已保存", Toast.LENGTH_SHORT).show()
                    route = KeywordNotificationRoute.Main
                }
            )
        }
    }
}

@Composable
internal fun TextSpeechMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { TextSpeechSettings(context) }
    val sp = remember { HchatStorage.preferences(context, TextSpeechSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<TextSpeechRoute>(TextSpeechRoute.Main) }
    var allowedContacts by remember { mutableStateOf(settings.allowedContacts()) }
    val mainListState = rememberLazyListState()

    fun saveAllowedContacts(ids: Set<String>) {
        allowedContacts = ids
        settings.saveAllowedContacts(ids)
    }

    SettingsRouteTransition(
        targetState = route,
        label = "TextSpeechRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            TextSpeechRoute.Main -> TextSpeechMainPage(
                provider = provider,
                sp = sp,
                allowedContacts = allowedContacts,
                listState = mainListState,
                onBack = onBack,
                onPickContacts = {
                    route = TextSpeechRoute.ContactPicker(
                        ContactPickerRequest(
                            title = "设置允许名单",
                            mode = ContactPickerMode.BOTH,
                            multiSelect = true,
                            existingValue = allowedContacts.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                }
            )
            is TextSpeechRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = TextSpeechRoute.Main },
                onConfirm = { picked ->
                    saveAllowedContacts(
                        picked.map { it.id }
                            .filter { it.isNotBlank() }
                            .toSet()
                    )
                    Toast.makeText(context, "允许名单已保存", Toast.LENGTH_SHORT).show()
                    route = TextSpeechRoute.Main
                }
            )
        }
    }
}

@Composable
internal fun TextSpeechMainPage(
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    allowedContacts: Set<String>,
    listState: LazyListState,
    onBack: () -> Unit,
    onPickContacts: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = MiuixScrollBehavior()
    var enabled by remember {
        mutableStateOf(sp.getBoolean(TextSpeechSettings.KEY_ENABLE, TextSpeechSettings.DEFAULT_ENABLE))
    }
    var ttsEngine by remember {
        mutableStateOf(
            sp.getString(TextSpeechSettings.KEY_TTS_ENGINE, TextSpeechSettings.DEFAULT_TTS_ENGINE)
                ?: TextSpeechSettings.DEFAULT_TTS_ENGINE
        )
    }
    var ttsVoice by remember {
        mutableStateOf(
            sp.getString(TextSpeechSettings.KEY_TTS_VOICE, TextSpeechSettings.DEFAULT_TTS_VOICE)
                ?: TextSpeechSettings.DEFAULT_TTS_VOICE
        )
    }
    var ttsVoiceChoices by remember { mutableStateOf<List<PopupChoice<String>>>(emptyList()) }
    var ttsVoiceLoading by remember { mutableStateOf(true) }
    var ttsVoiceError by remember { mutableStateOf("") }
    var ttsFallbackEnginePackage by remember { mutableStateOf("") }
    val installedTtsEngines = remember(context) { TextSpeechEngineCatalog.installed(context) }
    val ttsFallbackEngineLabel = remember(installedTtsEngines, ttsFallbackEnginePackage) {
        installedTtsEngines.firstOrNull { it.packageName == ttsFallbackEnginePackage }
            ?.label
            ?: ttsFallbackEnginePackage
    }
    val ttsEngineOptions = remember(installedTtsEngines, ttsEngine) {
        buildList {
            add(PopupChoice("跟随系统默认", TextSpeechSettings.DEFAULT_TTS_ENGINE))
            installedTtsEngines.forEach { add(PopupChoice(it.label, it.packageName)) }
            if (ttsEngine.isNotBlank() && installedTtsEngines.none { it.packageName == ttsEngine }) {
                add(PopupChoice("已不可用（$ttsEngine）", ttsEngine))
            }
        }
    }
    val ttsVoiceOptions = remember(ttsVoiceChoices, ttsVoice) {
        buildList {
            add(PopupChoice("跟随引擎默认", TextSpeechSettings.DEFAULT_TTS_VOICE))
            addAll(ttsVoiceChoices)
            if (ttsVoice.isNotBlank() && ttsVoiceChoices.none { it.value == ttsVoice }) {
                add(PopupChoice("已不可用（$ttsVoice）", ttsVoice))
            }
        }
    }
    DisposableEffect(context, ttsEngine) {
        ttsVoiceLoading = true
        ttsVoiceError = ""
        ttsFallbackEnginePackage = ""
        ttsVoiceChoices = emptyList()
        val handle = TextSpeechVoiceCatalog.load(context, ttsEngine) { result ->
            ttsVoiceChoices = result.options.map { PopupChoice(it.label, it.name) }
            ttsVoiceError = result.error
            ttsFallbackEnginePackage = if (result.usedFallback) {
                result.activeEnginePackage.ifBlank { "其它可用引擎" }
            } else {
                ""
            }
            ttsVoiceLoading = false
        }
        onDispose { handle.cancel() }
    }
    var volumeControl by remember {
        mutableStateOf(
            sp.getBoolean(
                TextSpeechSettings.KEY_VOLUME_CONTROL,
                TextSpeechSettings.DEFAULT_VOLUME_CONTROL
            )
        )
    }
    var playVoiceMessages by remember {
        mutableStateOf(
            sp.getBoolean(
                TextSpeechSettings.KEY_PLAY_VOICE_MESSAGES,
                TextSpeechSettings.DEFAULT_PLAY_VOICE_MESSAGES
            )
        )
    }
    var customAnnouncement by remember {
        mutableStateOf(
            sp.getBoolean(
                TextSpeechSettings.KEY_ANNOUNCE_SENDER,
                TextSpeechSettings.DEFAULT_ANNOUNCE_SENDER
            )
        )
    }
    var announcementTemplate by remember {
        mutableStateOf(
            sp.getString(
                TextSpeechSettings.KEY_ANNOUNCEMENT_TEMPLATE,
                TextSpeechSettings.DEFAULT_ANNOUNCEMENT_TEMPLATE
            ) ?: TextSpeechSettings.DEFAULT_ANNOUNCEMENT_TEMPLATE
        )
    }
    var timeFormat by remember {
        mutableStateOf(
            sp.getString(TextSpeechSettings.KEY_TIME_FORMAT, TextSpeechSettings.DEFAULT_TIME_FORMAT)
                ?: TextSpeechSettings.DEFAULT_TIME_FORMAT
        )
    }
    var respectWechatDnd by remember {
        mutableStateOf(
            sp.getBoolean(
                TextSpeechSettings.KEY_RESPECT_WECHAT_DND,
                TextSpeechSettings.DEFAULT_RESPECT_WECHAT_DND
            )
        )
    }
    var quietEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                TextSpeechSettings.KEY_QUIET_ENABLE,
                TextSpeechSettings.DEFAULT_QUIET_ENABLE
            )
        )
    }
    var quietStart by remember {
        mutableStateOf(
            sp.getString(TextSpeechSettings.KEY_QUIET_START, TextSpeechSettings.DEFAULT_QUIET_START)
                ?: TextSpeechSettings.DEFAULT_QUIET_START
        )
    }
    var quietEnd by remember {
        mutableStateOf(
            sp.getString(TextSpeechSettings.KEY_QUIET_END, TextSpeechSettings.DEFAULT_QUIET_END)
                ?: TextSpeechSettings.DEFAULT_QUIET_END
        )
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
            item { SmallTitle(text = "播报") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "自动播放语音",
                        summary = "播报允许名单内收到的消息"
                    ) {
                        enabled = it
                        sp.edit().putBoolean(TextSpeechSettings.KEY_ENABLE, it).apply()
                    }
                    if (enabled) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "语音引擎",
                            summary = "选择 TTS 播报使用的语音引擎",
                            options = ttsEngineOptions,
                            currentValue = ttsEngine,
                            onValueChanged = {
                                ttsEngine = it
                                ttsVoice = TextSpeechSettings.DEFAULT_TTS_VOICE
                                sp.edit()
                                    .putString(TextSpeechSettings.KEY_TTS_ENGINE, it)
                                    .putString(
                                        TextSpeechSettings.KEY_TTS_VOICE,
                                        TextSpeechSettings.DEFAULT_TTS_VOICE
                                    )
                                    .apply()
                            }
                        )
                        InsetDivider()
                        PopupChoiceRow(
                            title = "播报角色",
                            summary = when {
                                ttsVoiceLoading -> "正在读取所选引擎的播报角色"
                                ttsVoiceError.isNotBlank() -> "读取失败：$ttsVoiceError"
                                ttsFallbackEnginePackage.isNotBlank() ->
                                    "系统默认不可用，已临时使用 $ttsFallbackEngineLabel"
                                ttsVoiceChoices.isEmpty() -> "所选引擎未提供可选角色，将跟随引擎默认"
                                else -> "选择所选引擎提供的播报角色"
                            },
                            options = ttsVoiceOptions,
                            currentValue = ttsVoice,
                            enabled = !ttsVoiceLoading,
                            onValueChanged = {
                                ttsVoice = it
                                sp.edit().putString(TextSpeechSettings.KEY_TTS_VOICE, it).apply()
                            }
                        )
                        InsetDivider()
                        SwitchRow(
                            checked = playVoiceMessages,
                            title = "播放语音消息",
                            summary = "收到语音时直接播放原语音，不使用 TTS"
                        ) {
                            playVoiceMessages = it
                            sp.edit()
                                .putBoolean(TextSpeechSettings.KEY_PLAY_VOICE_MESSAGES, it)
                                .apply()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = volumeControl,
                            title = "音键控制",
                            summary = "音量减：暂停\n音量加：继续\n暂停时再按音量减：跳过"
                        ) {
                            volumeControl = it
                            sp.edit().putBoolean(TextSpeechSettings.KEY_VOLUME_CONTROL, it).apply()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = customAnnouncement,
                            title = "自定义播报内容",
                            summary = "分别使用消息对应的昵称、群聊和正文变量"
                        ) {
                            customAnnouncement = it
                            sp.edit().putBoolean(TextSpeechSettings.KEY_ANNOUNCE_SENDER, it).apply()
                        }
                        if (customAnnouncement) {
                            InsetDivider()
                            VariableInputRow(
                                title = "播报内容格式",
                                summary = "点击下方中文变量插入到光标位置",
                                value = announcementTemplate,
                                variables = textSpeechTemplateVariables,
                                minLines = 3
                            ) {
                                announcementTemplate = it
                                sp.edit()
                                    .putString(TextSpeechSettings.KEY_ANNOUNCEMENT_TEMPLATE, it)
                                    .apply()
                            }
                            if (announcementTemplate.contains(TextSpeechSettings.VAR_MESSAGE_TIME)) {
                                InsetDivider()
                                InputRow(
                                    title = "消息时间格式",
                                    summary = if (TextSpeechSettings.isValidTimeFormat(timeFormat)) {
                                        "例如 HH:mm:ss 或 yyyy-MM-dd HH:mm:ss"
                                    } else {
                                        "格式无效，当前输入不会保存"
                                    },
                                    value = timeFormat
                                ) {
                                    timeFormat = it
                                    if (TextSpeechSettings.isValidTimeFormat(it)) {
                                        sp.edit().putString(
                                            TextSpeechSettings.KEY_TIME_FORMAT,
                                            TextSpeechSettings.normalizeTimeFormat(it)
                                        ).apply()
                                    }
                                }
                            }
                            InsetDivider()
                            ActionRow(
                                title = "恢复默认格式",
                                summary = TextSpeechSettings.DEFAULT_ANNOUNCEMENT_TEMPLATE
                            ) {
                                announcementTemplate = TextSpeechSettings.DEFAULT_ANNOUNCEMENT_TEMPLATE
                                timeFormat = TextSpeechSettings.DEFAULT_TIME_FORMAT
                                sp.edit()
                                    .putString(
                                        TextSpeechSettings.KEY_ANNOUNCEMENT_TEMPLATE,
                                        TextSpeechSettings.DEFAULT_ANNOUNCEMENT_TEMPLATE
                                    )
                                    .putString(
                                        TextSpeechSettings.KEY_TIME_FORMAT,
                                        TextSpeechSettings.DEFAULT_TIME_FORMAT
                                    )
                                    .apply()
                            }
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = respectWechatDnd,
                            title = "免打扰不播报",
                            summary = "私聊或群聊开启微信消息免打扰后停止播报"
                        ) {
                            respectWechatDnd = it
                            sp.edit()
                                .putBoolean(TextSpeechSettings.KEY_RESPECT_WECHAT_DND, it)
                                .apply()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = quietEnabled,
                            title = "定时免打扰",
                            summary = "指定时段内暂停自动播报"
                        ) {
                            quietEnabled = it
                            sp.edit().putBoolean(TextSpeechSettings.KEY_QUIET_ENABLE, it).apply()
                        }
                        if (quietEnabled) {
                            InsetDivider()
                            TimeOfDayPickerRow("开始时间", quietStart) {
                                quietStart = it
                                sp.edit().putString(TextSpeechSettings.KEY_QUIET_START, it).apply()
                            }
                            InsetDivider()
                            TimeOfDayPickerRow("结束时间", quietEnd) {
                                quietEnd = it
                                sp.edit().putString(TextSpeechSettings.KEY_QUIET_END, it).apply()
                            }
                        }
                        InsetDivider()
                        ActionRow(
                            title = "设置允许名单",
                            summary = if (allowedContacts.isEmpty()) {
                                "未设置"
                            } else {
                                "已选择 ${allowedContacts.size} 个聊天"
                            },
                            onClick = onPickContacts
                        )
                    }
                }
            }
        }
    }
}

internal enum class ZombieCheckSelection {
    TARGETS,
    EXCLUDED,
    DELETE
}

internal data class ZombieCheckDeleteTarget(val wxid: String, val name: String)

internal sealed class ZombieCheckRoute {
    object Main : ZombieCheckRoute()
    data class ContactPicker(
        val selection: ZombieCheckSelection,
        val request: ContactPickerRequest
    ) : ZombieCheckRoute()
}

@Composable
internal fun ZombieCheckMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { ZombieCheckSettings(context) }
    var route by remember { mutableStateOf<ZombieCheckRoute>(ZombieCheckRoute.Main) }
    var targetIds by remember { mutableStateOf(settings.targetIds()) }
    var excludedIds by remember { mutableStateOf(settings.excludedIds()) }
    var pendingDeleteTargets by remember { mutableStateOf(emptyList<ZombieCheckDeleteTarget>()) }

    SettingsRouteTransition(
        targetState = route,
        label = "ZombieCheckRoute",
        depthOf = { if (it is ZombieCheckRoute.Main) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            ZombieCheckRoute.Main -> ZombieCheckMainPage(
                context = context,
                provider = provider,
                targetIds = targetIds,
                excludedIds = excludedIds,
                pendingDeleteTargets = pendingDeleteTargets,
                onBack = onBack,
                onPickTargets = {
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.TARGETS,
                        ContactPickerRequest(
                            title = "选择检测好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = targetIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onPickExcluded = {
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.EXCLUDED,
                        ContactPickerRequest(
                            title = "选择排除好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = excludedIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onPickDelete = {
                    val detectedIds = ZombieCheckController.snapshot().results
                        .filter { it.type == ZombieCheckResultType.DEAD }
                        .map { it.wxid }
                        .toSet()
                    route = ZombieCheckRoute.ContactPicker(
                        ZombieCheckSelection.DELETE,
                        ContactPickerRequest(
                            title = "批量删除好友",
                            mode = ContactPickerMode.FRIENDS,
                            multiSelect = true,
                            existingValue = detectedIds.joinToString("|"),
                            onValue = {},
                            enableLabels = true
                        )
                    )
                },
                onDismissDelete = { pendingDeleteTargets = emptyList() },
                onConfirmDelete = { clearRecord, delaySeconds ->
                    val result = ZombieCheckController.deleteFriends(
                        pendingDeleteTargets.map { it.wxid },
                        clearRecord,
                        delaySeconds
                    )
                    pendingDeleteTargets = emptyList()
                    result
                }
            )
            is ZombieCheckRoute.ContactPicker -> ContactPickerPage(
                context = context,
                request = currentRoute.request,
                onBack = { route = ZombieCheckRoute.Main },
                onConfirm = { picked ->
                    val ids = picked.map { it.id }.filter { it.isNotBlank() }.toSet()
                    when (currentRoute.selection) {
                        ZombieCheckSelection.TARGETS -> {
                            targetIds = ids
                            settings.saveTargetIds(ids)
                        }
                        ZombieCheckSelection.EXCLUDED -> {
                            excludedIds = ids
                            settings.saveExcludedIds(ids)
                        }
                        ZombieCheckSelection.DELETE -> {
                            pendingDeleteTargets = picked.map {
                                ZombieCheckDeleteTarget(it.id, it.label.ifBlank { it.id })
                            }
                        }
                    }
                    route = ZombieCheckRoute.Main
                }
            )
        }
    }
}

@Composable
internal fun ZombieCheckMainPage(
    context: Context,
    provider: FeatureSettingsProvider,
    targetIds: Set<String>,
    excludedIds: Set<String>,
    pendingDeleteTargets: List<ZombieCheckDeleteTarget>,
    onBack: () -> Unit,
    onPickTargets: () -> Unit,
    onPickExcluded: () -> Unit,
    onPickDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: (Boolean, Int) -> ZombieCheckActionResult
) {
    val sp = remember { HchatStorage.preferences(context, ZombieCheckSettings.PREFS_NAME) }
    var snapshot by remember { mutableStateOf(ZombieCheckController.snapshot()) }
    var enabled by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_ENABLE, ZombieCheckSettings.DEFAULT_ENABLE)) }
    var autoTag by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_AUTO_TAG, ZombieCheckSettings.DEFAULT_AUTO_TAG)) }
    var labelName by remember { mutableStateOf(sp.getString(ZombieCheckSettings.KEY_LABEL_NAME, ZombieCheckSettings.DEFAULT_LABEL_NAME).orEmpty()) }
    var autoDelete by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, ZombieCheckSettings.DEFAULT_AUTO_DELETE)) }
    var clearRecord by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_CLEAR_RECORD, ZombieCheckSettings.DEFAULT_CLEAR_RECORD)) }
    var keepAwake by remember { mutableStateOf(sp.getBoolean(ZombieCheckSettings.KEY_KEEP_AWAKE, ZombieCheckSettings.DEFAULT_KEEP_AWAKE)) }
    var minDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MIN_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_MIN_DELAY_SECONDS).toString()) }
    var maxDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MAX_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_MAX_DELAY_SECONDS).toString()) }
    var timeout by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_TIMEOUT_SECONDS, ZombieCheckSettings.DEFAULT_TIMEOUT_SECONDS).toString()) }
    var retries by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_MAX_RETRIES, ZombieCheckSettings.DEFAULT_MAX_RETRIES).toString()) }
    var deleteDelay by remember { mutableStateOf(sp.getInt(ZombieCheckSettings.KEY_DELETE_DELAY_SECONDS, ZombieCheckSettings.DEFAULT_DELETE_DELAY_SECONDS).toString()) }
    var confirmAutoDelete by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()

    LaunchedEffect(Unit) {
        while (true) {
            snapshot = ZombieCheckController.snapshot()
            delay(500L)
        }
    }

    fun saveInt(key: String, value: String, range: IntRange) {
        value.toIntOrNull()?.coerceIn(range.first, range.last)?.let {
            sp.edit().putInt(key, it).apply()
        }
    }

    fun toastResult(result: ZombieCheckActionResult) {
        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        snapshot = ZombieCheckController.snapshot()
    }

    val notableResults = snapshot.results.filter { it.type != ZombieCheckResultType.NORMAL }.asReversed()

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
            item { SmallTitle(text = "检测") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用僵尸粉检测",
                        summary = "使用微信转账下单接口核验好友关系，不会确认付款",
                        onCheckedChange = {
                            enabled = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_ENABLE, it).apply()
                            if (!it && (snapshot.running || snapshot.deleting)) {
                                toastResult(ZombieCheckController.pause())
                            }
                        }
                    )
                    InsetDivider()
                    InfoRow("状态", if (snapshot.ready) snapshot.status else "检测接口定位中")
                    InsetDivider()
                    InfoRow(
                        "进度",
                        "已检测 ${snapshot.checkedCount}/${snapshot.totalCount}，异常 ${snapshot.deadCount}，失败 ${snapshot.unknownCount}"
                    )
                    if (snapshot.deleting || snapshot.deleteTotalCount > 0) {
                        InsetDivider()
                        InfoRow(
                            "删除进度",
                            "已处理 ${snapshot.deleteCompletedCount}/${snapshot.deleteTotalCount}，已提交 ${snapshot.deleteSuccessCount}，失败 ${snapshot.deleteFailureCount}"
                        )
                    }
                    if (snapshot.currentName.isNotBlank()) {
                        InsetDivider()
                        InfoRow("当前好友", snapshot.currentName)
                    }
                    InsetDivider()
                    ActionRow("检测范围", if (targetIds.isEmpty()) "全部好友" else "已选择 ${targetIds.size} 位好友", onPickTargets)
                    InsetDivider()
                    ActionRow("排除好友", if (excludedIds.isEmpty()) "未排除" else "已排除 ${excludedIds.size} 位好友", onPickExcluded)
                    InsetDivider()
                    ActionRow(
                        if (snapshot.running || snapshot.deleting) "停止当前任务" else "开始 / 继续检测",
                        when {
                            snapshot.deleting -> "停止后不再删除剩余好友"
                            snapshot.running -> "暂停后保留当前队列"
                            else -> "按当前范围继续未完成进度"
                        }
                    ) {
                        toastResult(
                            if (snapshot.running || snapshot.deleting) {
                                ZombieCheckController.pause()
                            } else {
                                ZombieCheckController.start()
                            }
                        )
                    }
                    InsetDivider()
                    ActionRow("重置检测进度", "清空断点、结果和运行日志") {
                        toastResult(ZombieCheckController.reset())
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "异常处理") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = autoTag,
                        title = "自动追加标签",
                        summary = "保留好友现有标签后追加指定标签",
                        onCheckedChange = {
                            autoTag = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_TAG, it).apply()
                        }
                    )
                    if (autoTag) {
                        InsetDivider()
                        InputRow("标签名称", "检测到好友关系异常时追加", labelName) {
                            labelName = it
                            sp.edit().putString(ZombieCheckSettings.KEY_LABEL_NAME, it.trim()).apply()
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = autoDelete,
                        title = "自动删除异常好友",
                        summary = "高风险操作，默认关闭；暂停检测会取消尚未执行的删除",
                        onCheckedChange = {
                            if (it) {
                                confirmAutoDelete = true
                            } else {
                                autoDelete = false
                                sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, false).apply()
                            }
                        }
                    )
                    InsetDivider()
                    SwitchRow(
                        checked = clearRecord,
                        title = "同时清理聊天记录",
                        summary = "同时用于自动删除和批量删除",
                        onCheckedChange = {
                            clearRecord = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_CLEAR_RECORD, it).apply()
                        }
                    )
                    InsetDivider()
                    NumberInputRow("删除间隔", "自动删除和批量删除的等待秒数，0-300", deleteDelay) {
                        deleteDelay = it
                        saveInt(ZombieCheckSettings.KEY_DELETE_DELAY_SECONDS, it, 0..300)
                    }
                    InsetDivider()
                    ActionRow(
                        "批量删除好友",
                        when {
                            snapshot.deleting -> "正在处理 ${snapshot.deleteCompletedCount}/${snapshot.deleteTotalCount}"
                            snapshot.deadCount > 0 -> "可按标签筛选，默认选中 ${snapshot.deadCount} 位异常好友"
                            else -> "从好友或微信标签中多选后删除"
                        }
                    ) {
                        if (snapshot.running || snapshot.deleting) {
                            Toast.makeText(context, "请先停止当前任务", Toast.LENGTH_SHORT).show()
                        } else {
                            onPickDelete()
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = keepAwake,
                        title = "检测期间保持 CPU 运行",
                        summary = "使用 WakeLock，长时间检测会增加耗电",
                        onCheckedChange = {
                            keepAwake = it
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_KEEP_AWAKE, it).apply()
                        }
                    )
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "请求参数") }
            item {
                SettingsCard {
                    NumberInputRow("最小间隔", "单位秒，0-60", minDelay) {
                        minDelay = it
                        saveInt(ZombieCheckSettings.KEY_MIN_DELAY_SECONDS, it, 0..60)
                    }
                    InsetDivider()
                    NumberInputRow("最大间隔", "单位秒，0-120", maxDelay) {
                        maxDelay = it
                        saveInt(ZombieCheckSettings.KEY_MAX_DELAY_SECONDS, it, 0..120)
                    }
                    InsetDivider()
                    NumberInputRow("请求超时", "单位秒，5-60", timeout) {
                        timeout = it
                        saveInt(ZombieCheckSettings.KEY_TIMEOUT_SECONDS, it, 5..60)
                    }
                    InsetDivider()
                    NumberInputRow("超时重试", "每位好友最多重试 0-5 次", retries) {
                        retries = it
                        saveInt(ZombieCheckSettings.KEY_MAX_RETRIES, it, 0..5)
                    }
                }
            }

            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "异常结果 · ${notableResults.size}") }
            item {
                SettingsCard {
                    if (notableResults.isEmpty()) {
                        InfoRow("暂无异常结果", "正常好友不会逐项显示")
                    } else {
                        notableResults.take(50).forEachIndexed { index, result ->
                            if (index > 0) InsetDivider()
                            val resultType = if (result.type == ZombieCheckResultType.DEAD) "好友关系异常" else "检测失败"
                            val resultSummary = if (result.message.isBlank()) resultType else "$resultType · ${result.message}"
                            ZombieCheckResultRow(
                                result.name,
                                resultSummary
                            )
                        }
                    }
                }
            }

            if (snapshot.logs.isNotEmpty()) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "运行日志") }
                item {
                    SettingsCard {
                        snapshot.logs.takeLast(12).asReversed().forEachIndexed { index, log ->
                            if (index > 0) InsetDivider()
                            InfoRow("记录", log)
                        }
                    }
                }
            }
        }
    }

    if (confirmAutoDelete) {
        WindowDialog(
            show = true,
            title = "启用自动删除",
            onDismissRequest = { confirmAutoDelete = false },
            content = {
                Column {
                    Text(
                        text = "检测依据来自微信支付接口返回文案。支付风控或服务端文案变化可能产生检测失败，请先小范围验证结果。",
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    TextButton(
                        text = "确认启用",
                        onClick = {
                            autoDelete = true
                            sp.edit().putBoolean(ZombieCheckSettings.KEY_AUTO_DELETE, true).apply()
                            confirmAutoDelete = false
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "取消",
                        onClick = { confirmAutoDelete = false },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        )
    }

    if (pendingDeleteTargets.isNotEmpty()) {
        WindowDialog(
            show = true,
            title = "确认批量删除",
            onDismissRequest = onDismissDelete,
            content = {
                Column {
                    Text(
                        text = buildString {
                            append("将删除已选的 ${pendingDeleteTargets.size} 位好友")
                            if (clearRecord) append("，并同时清理聊天记录")
                            append("。此操作不可撤销。")
                            val names = pendingDeleteTargets.take(6).joinToString("、") { it.name }
                            if (names.isNotBlank()) append("\n\n$names")
                            if (pendingDeleteTargets.size > 6) append(" 等")
                        },
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    TextButton(
                        text = "确认删除",
                        onClick = {
                            val result = onConfirmDelete(
                                clearRecord,
                                deleteDelay.toIntOrNull()?.coerceIn(0, 300)
                                    ?: ZombieCheckSettings.DEFAULT_DELETE_DELAY_SECONDS
                            )
                            toastResult(result)
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                    TextButton(
                        text = "取消",
                        onClick = onDismissDelete,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.textButtonColorsPrimary()
                    )
                }
            }
        )
    }
}

