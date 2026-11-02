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

internal fun groupSelectionSummary(value: String): String {
    val count = parseIds(value).size
    return if (count == 0) "未选择监听群" else "已选择 $count 个群"
}

internal fun systemNoticeScopeChoices(allValue: String, specificValue: String): List<PopupChoice<String>> = listOf(
    PopupChoice("全部群聊", allValue),
    PopupChoice("指定群聊", specificValue)
)

internal fun systemNoticeScopeLabel(value: String, specificValue: String): String {
    return if (value == specificValue) "指定群聊" else "全部群聊"
}

internal fun systemNoticeGroupSummary(value: String): String {
    val count = parseIds(value).size
    return if (count == 0) "未选择群聊" else "已选择 $count 个群"
}

internal fun describeGroupLeaveTemplate(template: GroupLeaveReplyTemplate): String {
    val join = if (template.joinEnabled) "进群" else ""
    val left = if (template.leftEnabled) "退群" else ""
    val scope = listOf(join, left).filter { it.isNotBlank() }.joinToString("、").ifBlank { "未启用事件" }
    val media = when (template.mediaMode) {
        GroupLeaveMonitorSettings.MEDIA_NONE -> "不发媒体"
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "自定义媒体"
        else -> "跟随全局媒体"
    }
    val delay = when (template.delayMode) {
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "自定义延迟"
        else -> "跟随全局延迟"
    }
    return "$scope · ${groupPromptTypeWithGlobalLabel(template.promptType)} · $media · $delay"
}

internal fun groupLeaveTemplateBindingSummary(
    bindings: List<GroupLeaveReplyTemplateBinding>,
    templates: List<GroupLeaveReplyTemplate>
): String {
    if (templates.isEmpty()) return "暂无模板"
    val count = bindings.size
    return if (count == 0) "暂无群已绑定模板" else "已绑定 $count 个群"
}

internal fun groupLeaveGroupBindingSummary(
    groupId: String,
    defaultLabel: String,
    bindings: List<GroupLeaveReplyTemplateBinding>,
    templates: List<GroupLeaveReplyTemplate>
): String {
    val binding = bindings.firstOrNull { it.groupId == groupId } ?: return "未绑定模板"
    val template = templates.firstOrNull { it.id == binding.templateId } ?: return "模板已不存在"
    return "模板：${template.name.ifBlank { binding.label.ifBlank { defaultLabel } }}"
}

internal fun newGroupLeaveTemplate(index: Int, sp: SharedPreferences): GroupLeaveReplyTemplate {
    return GroupLeaveReplyTemplate(
        id = System.currentTimeMillis().toString() + "_" + index,
        name = "模板 $index",
        enabled = true,
        joinEnabled = sp.getBoolean(GroupLeaveMonitorSettings.KEY_JOIN_REPLY_ENABLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_REPLY_ENABLE),
        leftEnabled = sp.getBoolean(GroupLeaveMonitorSettings.KEY_LEFT_REPLY_ENABLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_REPLY_ENABLE),
        promptType = sp.getString(GroupLeaveMonitorSettings.KEY_PROMPT_TYPE, GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE) ?: GroupLeaveMonitorSettings.DEFAULT_PROMPT_TYPE,
        bothOrder = sp.getString(GroupLeaveMonitorSettings.KEY_BOTH_ORDER, GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER,
        joinText = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_TEXT, GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_TEXT,
        leftText = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_TEXT, GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_TEXT,
        joinCardTitle = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_TITLE,
        joinCardDesc = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_JOIN_CARD_DESC,
        leftCardTitle = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_TITLE, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_TITLE,
        leftCardDesc = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_CARD_DESC, GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC) ?: GroupLeaveMonitorSettings.DEFAULT_LEFT_CARD_DESC,
        mediaMode = GroupLeaveMonitorSettings.MODE_CUSTOM,
        mediaOrder = sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_ORDER, GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_ORDER,
        mediaSequence = sp.getString(GroupLeaveMonitorSettings.KEY_MEDIA_SEQUENCE, GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE) ?: GroupLeaveMonitorSettings.DEFAULT_MEDIA_SEQUENCE,
        joinImages = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_IMAGE_PATHS, "").orEmpty(),
        leftImages = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_IMAGE_PATHS, "").orEmpty(),
        joinVoices = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VOICE_PATHS, "").orEmpty(),
        leftVoices = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VOICE_PATHS, "").orEmpty(),
        joinEmojis = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_EMOJI_PATHS, "").orEmpty(),
        leftEmojis = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_EMOJI_PATHS, "").orEmpty(),
        joinVideos = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_VIDEO_PATHS, "").orEmpty(),
        leftVideos = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_VIDEO_PATHS, "").orEmpty(),
        joinFiles = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FILE_PATHS, "").orEmpty(),
        leftFiles = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FILE_PATHS, "").orEmpty(),
        joinFavorites = sp.getString(GroupLeaveMonitorSettings.KEY_JOIN_FAVORITE_PATHS, "").orEmpty(),
        leftFavorites = sp.getString(GroupLeaveMonitorSettings.KEY_LEFT_FAVORITE_PATHS, "").orEmpty(),
        delayMode = GroupLeaveMonitorSettings.MODE_CUSTOM,
        promptDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_PROMPT_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_PROMPT_DELAY_MS),
        imageDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_IMAGE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        voiceDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_VOICE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        emojiDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_EMOJI_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        videoDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_VIDEO_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        fileDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_FILE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS),
        favoriteDelayMs = sp.getInt(GroupLeaveMonitorSettings.KEY_FAVORITE_DELAY_MS, GroupLeaveMonitorSettings.DEFAULT_MEDIA_DELAY_MS)
    )
}

internal fun upsertGroupLeaveTemplateBindings(
    current: List<GroupLeaveReplyTemplateBinding>,
    additions: List<GroupLeaveReplyTemplateBinding>
): List<GroupLeaveReplyTemplateBinding> {
    val result = linkedMapOf<String, GroupLeaveReplyTemplateBinding>()
    current.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId] = binding.copy(
                groupId = binding.groupId.trim(),
                label = binding.label.trim().ifBlank { binding.groupId.trim() },
                templateId = binding.templateId.trim()
            )
        }
    }
    additions.forEach { binding ->
        if (binding.groupId.isNotBlank() && binding.templateId.isNotBlank()) {
            result[binding.groupId.trim()] = binding.copy(
                groupId = binding.groupId.trim(),
                label = binding.label.trim().ifBlank { binding.groupId.trim() },
                templateId = binding.templateId.trim()
            )
        }
    }
    return result.values.toList()
}

internal fun groupPromptTypeLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.PROMPT_CARD -> "卡片"
        GroupLeaveMonitorSettings.PROMPT_BOTH -> "文本+卡片"
        else -> "文本"
    }
}

internal fun groupPromptTypeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("文本", GroupLeaveMonitorSettings.PROMPT_TEXT),
        PopupChoice("文本+卡片", GroupLeaveMonitorSettings.PROMPT_BOTH),
        PopupChoice("卡片", GroupLeaveMonitorSettings.PROMPT_CARD)
    )
}

internal fun groupBothOrderLabel(value: String): String {
    return if (value == GroupLeaveMonitorSettings.BOTH_CARD_FIRST) "先卡片后文本" else "先文本后卡片"
}

internal fun groupBothOrderChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("先文本后卡片", GroupLeaveMonitorSettings.BOTH_TEXT_FIRST),
        PopupChoice("先卡片后文本", GroupLeaveMonitorSettings.BOTH_CARD_FIRST)
    )
}

internal fun groupMediaOrderLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MEDIA_BEFORE -> "先媒体后提示"
        GroupLeaveMonitorSettings.MEDIA_AFTER -> "先提示后媒体"
        else -> "不发送媒体"
    }
}

internal fun groupMediaOrderChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("不发送媒体", GroupLeaveMonitorSettings.MEDIA_NONE),
        PopupChoice("先提示后媒体", GroupLeaveMonitorSettings.MEDIA_AFTER),
        PopupChoice("先媒体后提示", GroupLeaveMonitorSettings.MEDIA_BEFORE)
    )
}

internal fun groupPromptTypeWithGlobalLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MODE_GLOBAL -> "跟随全局"
        GroupLeaveMonitorSettings.PROMPT_CARD -> "卡片"
        GroupLeaveMonitorSettings.PROMPT_BOTH -> "文本+卡片"
        else -> "文本"
    }
}

internal fun groupPromptTypeWithGlobalChoices(): List<PopupChoice<String>> {
    return listOf(PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL)) + groupPromptTypeChoices()
}

internal fun groupModeLabel(value: String): String {
    return if (value == GroupLeaveMonitorSettings.MODE_CUSTOM) "单独设置" else "跟随全局"
}

internal fun groupModeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL),
        PopupChoice("单独设置", GroupLeaveMonitorSettings.MODE_CUSTOM)
    )
}

internal fun groupMediaModeLabel(value: String): String {
    return when (value) {
        GroupLeaveMonitorSettings.MODE_CUSTOM -> "单独设置"
        GroupLeaveMonitorSettings.MEDIA_NONE -> "不发媒体"
        else -> "跟随全局"
    }
}

internal fun groupMediaModeChoices(): List<PopupChoice<String>> {
    return listOf(
        PopupChoice("跟随全局", GroupLeaveMonitorSettings.MODE_GLOBAL),
        PopupChoice("单独设置", GroupLeaveMonitorSettings.MODE_CUSTOM),
        PopupChoice("不发媒体", GroupLeaveMonitorSettings.MEDIA_NONE)
    )
}

internal fun groupMemberGroupKey(key: String, groupId: String): String = "${key}_${groupId}"

internal fun groupMemberMediaModeKey(groupId: String): String = GroupLeaveMonitorSettings.KEY_MEDIA_MODE_PREFIX + groupId

internal fun groupMemberDelayModeKey(groupId: String): String = GroupLeaveMonitorSettings.KEY_DELAY_MODE_PREFIX + groupId

internal fun groupBothOrderGlobal(sp: SharedPreferences): String {
    return sp.getString(
        GroupLeaveMonitorSettings.KEY_BOTH_ORDER,
        GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER
    ) ?: GroupLeaveMonitorSettings.DEFAULT_BOTH_ORDER
}

internal fun groupMemberGlobalString(
    sp: SharedPreferences,
    key: String,
    fallback: String
): String {
    return sp.getString(key, fallback) ?: fallback
}

internal fun groupMemberString(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    fallback: String
): String {
    return sp.getString(groupMemberGroupKey(key, groupId), null) ?: fallback
}

internal fun groupMemberIntString(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    fallback: Int
): String {
    val global = sp.getInt(key, fallback)
    return sp.getInt(groupMemberGroupKey(key, groupId), global).toString()
}

internal fun updateGroupToggle(
    sp: SharedPreferences,
    key: String,
    groupId: String,
    enabled: Boolean
): Set<String> {
    val disabled = parseIds(sp.getString(key, "").orEmpty()).toMutableSet()
    if (enabled) {
        disabled.remove(groupId)
    } else {
        disabled.add(groupId)
    }
    return disabled
}

internal sealed class TextVoiceRoute {
    object Main : TextVoiceRoute()
    object VoicePicker : TextVoiceRoute()
}

@Composable
internal fun TextVoiceMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, TextVoiceSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<TextVoiceRoute>(TextVoiceRoute.Main) }
    var selectedVoice by remember {
        mutableStateOf(TextVoiceSettings.selectedVoiceKey(context))
    }
    var selectedEngine by remember {
        mutableStateOf(TextVoiceSettings.selectedEngine(context))
    }
    var selectedTtsVoice by remember {
        mutableStateOf(
            sp.getString(TextVoiceSettings.KEY_TTS_VOICE, TextVoiceSettings.DEFAULT_TTS_VOICE)
                ?: TextVoiceSettings.DEFAULT_TTS_VOICE
        )
    }
    SettingsRouteTransition(
        targetState = route,
        label = "TextVoiceRoute",
        depthOf = { if (it is TextVoiceRoute.Main) 0 else 1 }
    ) { currentRoute ->
        when (currentRoute) {
            TextVoiceRoute.Main -> TextVoiceMainPage(
                provider = provider,
                sp = sp,
                selectedVoice = selectedVoice,
                selectedEngine = selectedEngine,
                selectedTtsVoice = selectedTtsVoice,
                onBack = onBack,
                onPickVoice = { route = TextVoiceRoute.VoicePicker },
                onEngineChanged = { value ->
                    selectedEngine = value
                    selectedTtsVoice = TextVoiceSettings.DEFAULT_TTS_VOICE
                    sp.edit()
                        .putString(TextVoiceSettings.KEY_ENGINE, value)
                        .putString(TextVoiceSettings.KEY_TTS_VOICE, TextVoiceSettings.DEFAULT_TTS_VOICE)
                        .apply()
                },
                onTtsVoiceChanged = { value ->
                    selectedTtsVoice = value
                    sp.edit().putString(TextVoiceSettings.KEY_TTS_VOICE, value).apply()
                }
            )
            TextVoiceRoute.VoicePicker -> OptionPickerPage(
                request = OptionPickerRequest(
                    title = "选择在线语音",
                    options = TextVoiceSettings.voiceOptions.mapIndexed { index, option ->
                        OptionItem(option.label, index, option.voiceId)
                    },
                    currentValue = TextVoiceSettings.voiceOptions
                        .indexOfFirst { it.key == selectedVoice }
                        .coerceAtLeast(0),
                    onSelected = {}
                ),
                onBack = { route = TextVoiceRoute.Main },
                onSelected = { item ->
                    val option = TextVoiceSettings.voiceOptions.getOrNull(item.value)
                        ?: return@OptionPickerPage
                    selectedVoice = option.key
                    sp.edit().putString(TextVoiceSettings.KEY_VOICE, option.key).apply()
                }
            )
        }
    }
}

@Composable
internal fun TextVoiceMainPage(
    provider: FeatureSettingsProvider,
    sp: SharedPreferences,
    selectedVoice: String,
    selectedEngine: String,
    selectedTtsVoice: String,
    onBack: () -> Unit,
    onPickVoice: () -> Unit,
    onEngineChanged: (String) -> Unit,
    onTtsVoiceChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    val installedTtsEngines = remember(context) { TextSpeechEngineCatalog.installed(context) }
    val engineOptions = remember(installedTtsEngines, selectedEngine) {
        buildList {
            add(PopupChoice("在线语音", TextVoiceSettings.ENGINE_ONLINE))
            add(PopupChoice("跟随系统默认 TTS", TextVoiceSettings.ENGINE_TTS_PREFIX))
            installedTtsEngines.forEach { option ->
                add(PopupChoice(option.label, TextVoiceSettings.ENGINE_TTS_PREFIX + option.packageName))
            }
            val selectedPackage = TextVoiceSettings.ttsPackage(selectedEngine)
            if (TextVoiceSettings.isTtsEngine(selectedEngine) &&
                selectedPackage.isNotBlank() &&
                installedTtsEngines.none { it.packageName == selectedPackage }
            ) {
                add(PopupChoice("已不可用（$selectedPackage）", selectedEngine))
            }
        }
    }
    var ttsVoiceChoices by remember { mutableStateOf<List<PopupChoice<String>>>(emptyList()) }
    var ttsVoiceLoading by remember { mutableStateOf(false) }
    var ttsVoiceError by remember { mutableStateOf("") }
    var ttsFallbackEnginePackage by remember { mutableStateOf("") }
    val ttsFallbackEngineLabel = remember(installedTtsEngines, ttsFallbackEnginePackage) {
        installedTtsEngines.firstOrNull { it.packageName == ttsFallbackEnginePackage }
            ?.label
            ?: ttsFallbackEnginePackage
    }
    DisposableEffect(context, selectedEngine) {
        val useTts = TextVoiceSettings.isTtsEngine(selectedEngine)
        ttsVoiceLoading = useTts
        ttsVoiceError = ""
        ttsFallbackEnginePackage = ""
        ttsVoiceChoices = emptyList()
        val handle = if (useTts) {
            TextSpeechVoiceCatalog.load(
                context,
                TextVoiceSettings.ttsPackage(selectedEngine)
            ) { result ->
                ttsVoiceChoices = result.options.map { PopupChoice(it.label, it.name) }
                ttsVoiceError = result.error
                ttsFallbackEnginePackage = if (result.usedFallback) {
                    result.activeEnginePackage.ifBlank { "其它可用引擎" }
                } else {
                    ""
                }
                ttsVoiceLoading = false
            }
        } else {
            null
        }
        onDispose { handle?.cancel() }
    }
    val ttsVoiceOptions = remember(ttsVoiceChoices, selectedTtsVoice) {
        buildList {
            add(PopupChoice("跟随引擎默认", TextVoiceSettings.DEFAULT_TTS_VOICE))
            addAll(ttsVoiceChoices)
            if (selectedTtsVoice.isNotBlank() && ttsVoiceChoices.none { it.value == selectedTtsVoice }) {
                add(PopupChoice("已不可用（$selectedTtsVoice）", selectedTtsVoice))
            }
        }
    }
    var sendEnabled by remember {
        mutableStateOf(
            sp.getBoolean(TextVoiceSettings.KEY_SEND_ENABLE, TextVoiceSettings.DEFAULT_SEND_ENABLE)
        )
    }
    var playEnabled by remember {
        mutableStateOf(
            sp.getBoolean(TextVoiceSettings.KEY_PLAY_ENABLE, TextVoiceSettings.DEFAULT_PLAY_ENABLE)
        )
    }
    var speechRate by remember {
        mutableStateOf(
            TextVoiceSettings.normalizeSpeechRate(
                sp.getFloat(TextVoiceSettings.KEY_SPEECH_RATE, TextVoiceSettings.DEFAULT_SPEECH_RATE)
            )
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
            item { SmallTitle(text = "文本转语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = sendEnabled,
                        title = "文本转换语音模式（发送文字转语音）",
                        summary = "聊天输入 #tts 开启或关闭当前会话，输入 #tts e 使用英文模式"
                    ) {
                        sendEnabled = it
                        sp.edit().putBoolean(TextVoiceSettings.KEY_SEND_ENABLE, it).apply()
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = playEnabled,
                        title = "文字转换语音播放",
                        summary = "长按文字消息后选择“转语音播放[H]”"
                    ) {
                        playEnabled = it
                        sp.edit().putBoolean(TextVoiceSettings.KEY_PLAY_ENABLE, it).apply()
                    }
                    if (sendEnabled || playEnabled) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "转换引擎",
                            summary = "选择在线语音或设备上已安装的 TTS 引擎",
                            options = engineOptions,
                            currentValue = selectedEngine,
                            onValueChanged = onEngineChanged
                        )
                        InsetDivider()
                        if (TextVoiceSettings.isTtsEngine(selectedEngine)) {
                            PopupChoiceRow(
                                title = "TTS 角色",
                                summary = when {
                                    ttsVoiceLoading -> "正在读取所选引擎的角色"
                                    ttsVoiceError.isNotBlank() -> "读取失败：$ttsVoiceError"
                                    ttsFallbackEnginePackage.isNotBlank() ->
                                        "系统默认不可用，已临时使用 $ttsFallbackEngineLabel"
                                    ttsVoiceChoices.isEmpty() -> "所选引擎未提供可选角色，将跟随引擎默认"
                                    else -> "选择所选 TTS 引擎提供的角色"
                                },
                                options = ttsVoiceOptions,
                                currentValue = selectedTtsVoice,
                                enabled = !ttsVoiceLoading,
                                onValueChanged = onTtsVoiceChanged
                            )
                        } else {
                            ActionRow(
                                title = "在线语音",
                                summary = TextVoiceSettings.voiceLabel(selectedVoice),
                                onClick = onPickVoice
                            )
                        }
                        InsetDivider()
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                text = "语速 ${String.format(Locale.ROOT, "%.1f", speechRate)}x",
                                color = MiuixTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            Slider(
                                value = speechRate,
                                onValueChange = {
                                    speechRate = TextVoiceSettings.normalizeSpeechRate(it)
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                valueRange = TextVoiceSettings.MIN_SPEECH_RATE..TextVoiceSettings.MAX_SPEECH_RATE,
                                steps = 28,
                                onValueChangeFinished = {
                                    sp.edit()
                                        .putFloat(TextVoiceSettings.KEY_SPEECH_RATE, speechRate)
                                        .apply()
                                },
                                showKeyPoints = true,
                                keyPoints = listOf(0.1f, 1.0f, 2.0f, 3.0f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun VoiceForwardMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, VoiceForwardSettings.PREFS_NAME) }
    val legacyDefault = remember { sp.getBoolean(VoiceForwardSettings.KEY_ENABLE, VoiceForwardSettings.DEFAULT_ENABLE) }
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
            item { SmallTitle(text = "聊天语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_FORWARD_ENABLE,
                        "聊天语音转发",
                        "长按聊天语音后显示转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_SAVE_ENABLE,
                        "聊天语音保存",
                        "长按聊天语音后显示保存入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_MULTI_FORWARD_ENABLE,
                        "多选语音转发",
                        "多选语音后显示逐条转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_CHAT_MULTI_MERGE_ENABLE,
                        "多选语音合并",
                        "多选语音后合成为一条语音，可转发或保存为 MP3",
                        false
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "收藏语音") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_FAVORITE_FORWARD_ENABLE,
                        "收藏语音转发",
                        "长按收藏语音后显示转发入口",
                        legacyDefault
                    )
                    InsetDivider()
                    SwitchRow(
                        sp,
                        VoiceForwardSettings.KEY_FAVORITE_SAVE_ENABLE,
                        "收藏语音保存",
                        "长按收藏语音后显示保存入口",
                        legacyDefault
                    )
                }
            }
        }
    }
}

internal object AudioTransformSettingsPage {
    @Composable
    operator fun invoke(
        context: Context,
        provider: FeatureSettingsProvider,
        onBack: () -> Unit
    ) {
    val sp = remember { HchatStorage.preferences(context, AudioTransformSettings.PREFS_NAME) }
    val bridge = remember { AudioTransformBridge() }
    val handler = remember { Handler(Looper.getMainLooper()) }
    val mainListState = rememberLazyListState()
    var route by remember { mutableStateOf<AudioTransformRoute>(AudioTransformRoute.Main) }
    var mode by remember {
        mutableStateOf(
            sp.getInt(
                AudioTransformSettings.KEY_MODE,
                AudioTransformSettings.DEFAULT_MODE
            )
        )
    }
    var inputPath by rememberSaveable { mutableStateOf("") }
    var inputName by rememberSaveable { mutableStateOf("") }
    var selectedTalker by rememberSaveable {
        mutableStateOf(
            sp.getString(AudioTransformSettings.KEY_LAST_TALKER, "").orEmpty()
        )
    }
    var splitEnabled by remember {
        mutableStateOf(
            sp.getBoolean(
                AudioTransformSettings.KEY_SPLIT_ENABLED,
                AudioTransformSettings.DEFAULT_SPLIT_ENABLED
            )
        )
    }
    var splitDurationSeconds by rememberSaveable {
        mutableStateOf(
            sp.getLong(
                AudioTransformSettings.KEY_SPLIT_DURATION_SECONDS,
                AudioTransformSettings.DEFAULT_SPLIT_DURATION_SECONDS
            ).coerceAtLeast(AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS).toString()
        )
    }
    var clipStartMillis by rememberSaveable { mutableStateOf(0L) }
    var clipEndMillis by rememberSaveable { mutableStateOf(0L) }
    val splitCancellation = remember { AtomicBoolean(false) }
    val splitWorker = remember { AtomicReference<Thread?>(null) }
    var running by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            splitCancellation.set(true)
            splitWorker.getAndSet(null)?.interrupt()
        }
    }

    fun updateResult(text: String, toast: Boolean = false) {
        resultText = text
        if (toast && text.isNotBlank()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun startSend() {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val talker = selectedTalker.trim()
        if (talker.isEmpty()) {
            updateResult("请先选择聊天", toast = true)
            return
        }
        val validate = audioTransformValidateInput(mode, sourcePath, bridge)
        if (validate != null) {
            updateResult(validate, toast = true)
            return
        }
        val shouldSplit = splitEnabled
        val segmentDuration = if (shouldSplit) splitDurationSeconds.toLongOrNull() else null
        val rangeStart = clipStartMillis.coerceAtLeast(0L)
        val rangeEnd = clipEndMillis.takeIf { it > 0L }
        if (shouldSplit) {
            if (segmentDuration == null) {
                updateResult("请输入有效的每段时长", toast = true)
                return
            }
            if (rangeEnd != null && rangeEnd <= rangeStart) {
                updateResult("结束时间必须晚于开始时间", toast = true)
                return
            }
            if (segmentDuration < AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS) {
                updateResult(
                    "每段时长最少为 ${AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS} 秒",
                    toast = true
                )
                return
            }
        }
        val talkerLabel = audioTransformTalkerSummary(talker)
        splitCancellation.set(false)
        running = true
        resultText = if (shouldSplit) "正在准备切割" else "正在发送到 $talkerLabel"
        val worker = Thread({
            var sentCount = 0
            var totalCount = 0
            val message = try {
                if (!shouldSplit) {
                    val ok = runCatching {
                        WeChatApis.media()?.voices()?.send(talker, sourcePath) == true
                    }.getOrDefault(false)
                    if (ok) "已发送到 $talkerLabel" else "发送失败，请稍后重试"
                } else {
                    AudioTransformSegmenter.prepare(
                        bridge = bridge,
                        sourcePath = sourcePath,
                        cacheDir = context.cacheDir,
                        durationSeconds = segmentDuration!!,
                        startMillis = rangeStart,
                        endMillis = rangeEnd,
                        cancelled = splitCancellation,
                        onProgress = { progress ->
                            handler.post {
                                if (!splitCancellation.get()) resultText = progress
                            }
                        }
                    ).use { batch ->
                        totalCount = batch.segments.size
                        batch.segments.forEachIndexed { index, segment ->
                            if (splitCancellation.get()) throw AudioTransformCancelledException()
                            handler.post {
                                if (!splitCancellation.get()) {
                                    resultText = "正在发送 ${index + 1}/$totalCount"
                                }
                            }
                            val sent = WeChatApis.media()?.voices()?.send(
                                talker,
                                segment.file.absolutePath,
                                segment.durationMillis
                            ) == true
                            if (sent) sentCount++
                            if (splitCancellation.get()) throw AudioTransformCancelledException()
                            if (!sent) {
                                throw IllegalStateException("第 ${index + 1} 段发送失败")
                            }
                            if (sentCount < totalCount) {
                                repeat(5) {
                                    if (splitCancellation.get()) throw AudioTransformCancelledException()
                                    Thread.sleep(100L)
                                }
                            }
                        }
                    }
                    "已发送 $totalCount 段到 $talkerLabel"
                }
            } catch (_: AudioTransformCancelledException) {
                if (totalCount > 0) "已取消，已发送 $sentCount/$totalCount 段" else "已取消"
            } catch (throwable: Throwable) {
                if (splitCancellation.get() || throwable is InterruptedException) {
                    if (totalCount > 0) "已取消，已发送 $sentCount/$totalCount 段" else "已取消"
                } else {
                    val detail = throwable.message ?: "未知错误"
                    if (totalCount > 0) {
                        "发送失败，已发送 $sentCount/$totalCount 段：$detail"
                    } else {
                        "切割发送失败：$detail"
                    }
                }
            }
            if (shouldSplit) splitWorker.compareAndSet(Thread.currentThread(), null)
            handler.post {
                running = false
                updateResult(message, toast = true)
            }
        }, "Hchat-AudioTransform-Send")
        if (shouldSplit) splitWorker.set(worker)
        worker.start()
    }

    fun startSave(outputUri: Uri, outputName: String) {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val validate = audioTransformValidateInput(mode, sourcePath, bridge)
        if (validate != null) {
            updateResult(validate, toast = true)
            return
        }
        running = true
        resultText = "正在转换并保存"
        Thread({
            val sourceFile = File(sourcePath)
            val tempDir = File(context.cacheDir, "audio_transform_output").apply { mkdirs() }
            val tempFile = File(
                tempDir,
                "audio_${System.currentTimeMillis()}${audioTransformOutputExtension(mode)}"
            )
            val result = runCatching {
                val code = when (mode) {
                    AudioTransformSettings.MODE_AUDIO_TO_SILK_SAVE -> {
                        if (bridge.getFileType(sourcePath) == 1) {
                            sourceFile.copyTo(tempFile, overwrite = true)
                            0
                        } else {
                            bridge.autoToSilk(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                        }
                    }
                    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> {
                        bridge.silkToMp3(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                    }
                    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> {
                        bridge.silkToM4a(sourcePath, tempFile.absolutePath, AudioTransformBridge.DEFAULT_HZ)
                    }
                    else -> -2
                }
                if (code != 0) {
                    false to "转换失败：${bridge.getErrorMessage(code)}"
                } else if (!tempFile.isFile || tempFile.length() <= 0L) {
                    false to "转换失败：输出文件为空"
                } else if (!copyAudioTransformFileToUri(context, tempFile, outputUri)) {
                    false to "保存失败：无法写入目标文件"
                } else {
                    true to "已保存为 ${outputName.ifBlank { audioTransformSuggestedFileName(inputName, mode) }}"
                }
            }.getOrElse {
                false to "转换异常：${it.message ?: "未知错误"}"
            }
            if (tempFile.exists()) {
                runCatching { tempFile.delete() }
            }
            handler.post {
                running = false
                updateResult(result.second, toast = true)
            }
        }, "Hchat-AudioTransform-Save").start()
    }

    fun launchSavePicker() {
        if (running) return
        val sourcePath = inputPath.takeIf { it.isNotBlank() && File(it).isFile }
        if (sourcePath == null) {
            updateResult("请先选择输入文件", toast = true)
            return
        }
        val activity = context as? Activity
        if (activity == null) {
            updateResult("当前页面无法打开保存选择器", toast = true)
            return
        }
        AudioTransformDocumentBridge.launchOutput(
            activity = activity,
            suggestedFileName = audioTransformSuggestedFileName(inputName, mode),
            mimeType = audioTransformOutputMime(mode)
        ) { uri, name ->
            startSave(uri, name)
        }
    }

    SettingsRouteTransition(
        targetState = route,
        label = "AudioTransformRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            AudioTransformRoute.Main -> {
                AudioTransformMainPage(
                    provider = provider,
                    mode = mode,
                    inputPath = inputPath,
                    inputName = inputName,
                    talker = selectedTalker,
                    splitEnabled = splitEnabled,
                    splitDurationSeconds = splitDurationSeconds,
                    clipStartMillis = clipStartMillis,
                    clipEndMillis = clipEndMillis,
                    running = running,
                    resultText = resultText,
                    listState = mainListState,
                    onBack = onBack,
                    onModeChange = { next ->
                        mode = next
                        sp.edit().putInt(AudioTransformSettings.KEY_MODE, next).apply()
                    },
                    onPickInput = {
                        val activity = context as? Activity
                        if (activity == null) {
                            updateResult("当前页面无法打开文件选择器", toast = true)
                        } else {
                            AudioTransformDocumentBridge.launchInput(activity) { path, name ->
                                inputPath = path
                                inputName = name
                                clipStartMillis = 0L
                                clipEndMillis = 0L
                                resultText = ""
                            }
                        }
                    },
                    onPickTalker = { route = AudioTransformRoute.ContactPicker },
                    onSplitEnabledChange = { enabled ->
                        splitEnabled = enabled
                        sp.edit().putBoolean(AudioTransformSettings.KEY_SPLIT_ENABLED, enabled).apply()
                    },
                    onSplitDurationChange = { next ->
                        splitDurationSeconds = next
                        next.toLongOrNull()
                            ?.takeIf { it >= AudioTransformSettings.MIN_SPLIT_DURATION_SECONDS }
                            ?.let { duration ->
                                sp.edit().putLong(
                                    AudioTransformSettings.KEY_SPLIT_DURATION_SECONDS,
                                    duration
                                ).apply()
                            }
                    },
                    onClipRangeChange = { source, start, end ->
                        if (inputPath == source) {
                            clipStartMillis = start.coerceAtLeast(0L)
                            clipEndMillis = end.coerceAtLeast(0L)
                        }
                    },
                    onClearInput = {
                        inputPath = ""
                        inputName = ""
                        clipStartMillis = 0L
                        clipEndMillis = 0L
                    },
                    onCancel = {
                        splitCancellation.set(true)
                        splitWorker.get()?.interrupt()
                        resultText = "正在取消"
                    },
                    onStart = {
                        if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                            startSend()
                        } else {
                            launchSavePicker()
                        }
                    }
                )
            }
            AudioTransformRoute.ContactPicker -> {
                ContactPickerPage(
                    context = context,
                    request = ContactPickerRequest(
                        title = "选择发送聊天",
                        mode = ContactPickerMode.ALL_CHATS,
                        multiSelect = false,
                        existingValue = formatIds(listOfNotNull(selectedTalker.takeIf { it.isNotBlank() })),
                        onValue = {}
                    ),
                    onBack = { route = AudioTransformRoute.Main },
                    onConfirm = { selected ->
                        selectedTalker = selected.firstOrNull()?.id.orEmpty()
                        sp.edit().putString(AudioTransformSettings.KEY_LAST_TALKER, selectedTalker).apply()
                        route = AudioTransformRoute.Main
                    }
                )
            }
        }
    }
}

@Composable
private fun AudioTransformMainPage(
    provider: FeatureSettingsProvider,
    mode: Int,
    inputPath: String,
    inputName: String,
    talker: String,
    splitEnabled: Boolean,
    splitDurationSeconds: String,
    clipStartMillis: Long,
    clipEndMillis: Long,
    running: Boolean,
    resultText: String,
    listState: LazyListState,
    onBack: () -> Unit,
    onModeChange: (Int) -> Unit,
    onPickInput: () -> Unit,
    onPickTalker: () -> Unit,
    onSplitEnabledChange: (Boolean) -> Unit,
    onSplitDurationChange: (String) -> Unit,
    onClipRangeChange: (String, Long, Long) -> Unit,
    onClearInput: () -> Unit,
    onCancel: () -> Unit,
    onStart: () -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    val actionText = if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) "开始发送" else "开始转换"
    val cancellable = running && mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND && splitEnabled
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = actionText,
                onPrimaryClick = onStart,
                secondaryText = if (cancellable) "取消" else "返回",
                onSecondaryClick = if (cancellable) onCancel else onBack
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
            item { SmallTitle(text = "转换方式") }
            item {
                SettingsCard {
                    PopupChoiceRow(
                        title = "操作",
                        summary = audioTransformModeSummary(mode),
                        options = audioTransformModeOptions(),
                        currentValue = mode.toString(),
                        onValueChanged = { next -> onModeChange(next.toIntOrNull() ?: mode) },
                        enabled = !running
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "文件") }
            item {
                SettingsCard {
                    ActionRow("选择输入文件", audioTransformInputSummary(inputPath, inputName)) {
                        if (!running) onPickInput()
                    }
                    if (inputPath.isNotBlank()) {
                        InsetDivider()
                        AudioTransformPreviewEditor(
                            inputPath = inputPath,
                            inputName = inputName,
                            clipEnabled = mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND &&
                                splitEnabled,
                            clipStartMillis = clipStartMillis,
                            clipEndMillis = clipEndMillis,
                            enabled = !running,
                            onRangeChange = onClipRangeChange
                        )
                        InsetDivider()
                        ActionRow("清空输入文件", "移除当前已选文件") {
                            if (!running) onClearInput()
                        }
                    }
                    if (mode != AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                        InsetDivider()
                        InfoRow("输出文件", audioTransformSuggestedFileName(inputName, mode))
                    }
                }
            }
            if (mode == AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "发送") }
                item {
                    SettingsCard {
                        ActionRow("选择聊天", audioTransformTalkerSummary(talker)) {
                            if (!running) onPickTalker()
                        }
                        InsetDivider()
                        SwitchRow(
                            checked = splitEnabled,
                            title = "裁剪与自动分割",
                            summary = "按选定起止区间裁剪，并按时长依次发送",
                            enabled = !running,
                            onCheckedChange = onSplitEnabledChange
                        )
                        if (splitEnabled) {
                            InsetDivider()
                            NumberInputRow(
                                title = "自动分割（秒）",
                                summary = "单位秒，最少 1 秒",
                                value = splitDurationSeconds,
                                onValueChange = onSplitDurationChange
                            )
                        }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "状态") }
            item {
                SettingsCard {
                    InfoRow("当前状态", if (running) "处理中" else "待开始")
                    if (resultText.isNotBlank()) {
                        InsetDivider()
                        InfoRow("最近结果", resultText)
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioTransformPreviewEditor(
    inputPath: String,
    inputName: String,
    clipEnabled: Boolean,
    clipStartMillis: Long,
    clipEndMillis: Long,
    enabled: Boolean,
    onRangeChange: (String, Long, Long) -> Unit
) {
    val context = LocalContext.current
    val currentRangeChange by rememberUpdatedState(onRangeChange)
    val currentClipStart by rememberUpdatedState(clipStartMillis)
    val currentClipEnd by rememberUpdatedState(clipEndMillis)
    val currentClipEnabled by rememberUpdatedState(clipEnabled)
    var playablePath by remember(inputPath) { mutableStateOf("") }
    var previewPreparing by remember(inputPath) { mutableStateOf(true) }
    var previewError by remember(inputPath) { mutableStateOf("") }
    var player by remember(inputPath) { mutableStateOf<MediaPlayer?>(null) }
    var prepared by remember(inputPath) { mutableStateOf(false) }
    var playing by remember(inputPath) { mutableStateOf(false) }
    var durationMillis by remember(inputPath) { mutableStateOf(0) }
    var positionMillis by remember(inputPath) { mutableStateOf(0) }
    var playerError by remember(inputPath) { mutableStateOf("") }

    LaunchedEffect(inputPath, enabled) {
        playablePath = ""
        previewError = ""
        playerError = ""
        if (!enabled) {
            previewPreparing = false
            return@LaunchedEffect
        }
        previewPreparing = true
        var generatedPreview: File? = null
        try {
            val resolved = withContext(Dispatchers.IO) {
                val source = File(inputPath)
                check(source.isFile) { "输入文件不存在" }
                val previewBridge = AudioTransformBridge()
                if (previewBridge.getFileType(inputPath) != 1) {
                    inputPath
                } else {
                    val previewDir = File(context.cacheDir, "audio_transform_preview").apply { mkdirs() }
                    val output = File(
                        previewDir,
                        "preview_${System.currentTimeMillis()}_${System.nanoTime()}.m4a"
                    )
                    generatedPreview = output
                    val code = previewBridge.silkToM4a(
                        inputPath,
                        output.absolutePath,
                        AudioTransformBridge.DEFAULT_HZ
                    )
                    check(code == 0 && output.isFile && output.length() > 0L) {
                        "试听准备失败：${previewBridge.getErrorMessage(code)}"
                    }
                    output.absolutePath
                }
            }
            playablePath = resolved
            previewPreparing = false
            awaitCancellation()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (throwable: Throwable) {
            previewPreparing = false
            previewError = throwable.message ?: "试听准备失败"
        } finally {
            generatedPreview?.let { runCatching { it.delete() } }
        }
    }

    DisposableEffect(playablePath, enabled) {
        val path = playablePath
        var effectPlayer: MediaPlayer? = null
        prepared = false
        playing = false
        if (path.isNotBlank() && enabled) {
            runCatching {
                MediaPlayer().also { media ->
                    effectPlayer = media
                    media.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    media.setDataSource(path)
                    media.setOnPreparedListener { preparedPlayer ->
                        if (player !== preparedPlayer) return@setOnPreparedListener
                        val duration = preparedPlayer.duration.coerceAtLeast(0)
                        durationMillis = duration
                        prepared = duration > 0
                        playing = false
                        val maxStart = (duration - 1).coerceAtLeast(0).toLong()
                        val start = currentClipStart.coerceIn(0L, maxStart)
                        val end = currentClipEnd
                            .takeIf { it > start }
                            ?.coerceAtMost(duration.toLong())
                            ?: duration.toLong()
                        positionMillis = start.toInt()
                        runCatching { preparedPlayer.seekTo(positionMillis) }
                        currentRangeChange(inputPath, start, end)
                    }
                    media.setOnCompletionListener { completed ->
                        if (player !== completed) return@setOnCompletionListener
                        playing = false
                        val reset = if (currentClipEnabled) {
                            currentClipStart.coerceIn(
                                0L,
                                completed.duration.coerceAtLeast(0).toLong()
                            ).toInt()
                        } else {
                            0
                        }
                        runCatching { completed.seekTo(reset) }
                        positionMillis = reset
                    }
                    media.setOnErrorListener { failed, _, _ ->
                        if (player !== failed) return@setOnErrorListener true
                        prepared = false
                        playing = false
                        playerError = "当前音频无法试听"
                        true
                    }
                    player = media
                    media.prepareAsync()
                }
            }.onFailure {
                val failedPlayer = effectPlayer
                effectPlayer = null
                if (player === failedPlayer) player = null
                failedPlayer?.let { media ->
                    media.setOnPreparedListener(null)
                    media.setOnCompletionListener(null)
                    media.setOnErrorListener(null)
                    runCatching { media.release() }
                }
                prepared = false
                playing = false
                playerError = "当前音频无法试听"
            }
        }
        onDispose {
            val disposingPlayer = effectPlayer
            effectPlayer = null
            disposingPlayer?.let { media ->
                media.setOnPreparedListener(null)
                media.setOnCompletionListener(null)
                media.setOnErrorListener(null)
                runCatching { if (media.isPlaying) media.stop() }
                runCatching { media.release() }
            }
            if (player === disposingPlayer) player = null
            prepared = false
            playing = false
        }
    }

    LaunchedEffect(enabled, player, playing, prepared, clipEnabled, clipStartMillis, clipEndMillis) {
        val media = player
        if (!enabled && media != null && playing) {
            runCatching { media.pause() }
            playing = false
        }
        while (enabled && prepared && playing && media != null) {
            val current = runCatching { media.currentPosition.coerceAtLeast(0) }
                .getOrDefault(positionMillis)
            val end = if (clipEnabled) {
                clipEndMillis.takeIf { it > clipStartMillis }
                    ?.coerceAtMost(durationMillis.toLong())
                    ?.toInt()
                    ?: durationMillis
            } else {
                durationMillis
            }
            if (end > 0 && current >= end) {
                runCatching { media.pause() }
                val reset = if (clipEnabled) {
                    clipStartMillis.coerceIn(0L, end.toLong()).toInt()
                } else {
                    0
                }
                runCatching { media.seekTo(reset) }
                positionMillis = reset
                playing = false
                break
            }
            positionMillis = current
            delay(80L)
        }
    }

    fun seekTo(target: Int) {
        val media = player ?: return
        if (!prepared || durationMillis <= 0) return
        val maxStart = (durationMillis - 1).coerceAtLeast(0).toLong()
        val rangeStart = clipStartMillis.coerceIn(0L, maxStart).toInt()
        val rangeEnd = clipEndMillis
            .takeIf { it > rangeStart }
            ?.coerceAtMost(durationMillis.toLong())
            ?.toInt()
            ?: durationMillis
        val lowerBound = if (playing && clipEnabled) rangeStart else 0
        val upperBound = if (playing && clipEnabled) rangeEnd else durationMillis
        val next = target.coerceIn(lowerBound, upperBound.coerceAtLeast(lowerBound))
        runCatching { media.seekTo(next) }
        positionMillis = next
    }

    val effectiveClipStart = clipStartMillis.coerceIn(0L, durationMillis.coerceAtLeast(0).toLong())
    val effectiveClipEnd = clipEndMillis
        .takeIf { it > effectiveClipStart }
        ?.coerceAtMost(durationMillis.toLong())
        ?: durationMillis.toLong()
    val statusText = when {
        previewPreparing -> "正在准备试听"
        previewError.isNotBlank() -> previewError
        playerError.isNotBlank() -> playerError
        !enabled -> "处理中"
        !prepared -> "正在加载"
        playing -> "播放中"
        else -> "可试听"
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(
                text = inputName.ifBlank { File(inputPath).name.ifBlank { "已选音频" } },
                color = MiuixTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val playEnabled = enabled && prepared
                Box(
                    modifier = Modifier.size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (playEnabled) {
                                MiuixTheme.colorScheme.primary
                            } else {
                                MiuixTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            }
                        )
                        .clickable(enabled = playEnabled) {
                            val media = player ?: return@clickable
                            if (playing) {
                                runCatching { media.pause() }
                                playing = false
                            } else {
                                val start = if (clipEnabled) {
                                    effectiveClipStart.toInt()
                                } else {
                                    0
                                }
                                val end = if (clipEnabled) {
                                    effectiveClipEnd.toInt()
                                } else {
                                    durationMillis
                                }
                                if (positionMillis < start || positionMillis >= end) {
                                    seekTo(start)
                                }
                                runCatching {
                                    media.start()
                                    playing = true
                                }.onFailure {
                                    playerError = "当前音频无法试听"
                                    playing = false
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = if (playing) NavIcons.Pause else NavIcons.Play,
                        contentDescription = if (playing) "暂停" else "播放",
                        colorFilter = ColorFilter.tint(
                            if (playEnabled) Color.White else MiuixTheme.colorScheme.onSurfaceVariantSummary
                        ),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        text = "${formatAudioTransformTime(positionMillis.toLong())} / " +
                            formatAudioTransformTime(durationMillis.toLong()),
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = statusText,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Slider(
                value = positionMillis.coerceIn(0, durationMillis.coerceAtLeast(0)).toFloat(),
                onValueChange = { next ->
                    if (enabled && prepared) seekTo(next.toInt())
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    .graphicsLayer(alpha = if (enabled && prepared) 1f else 0.45f),
                valueRange = 0f..durationMillis.coerceAtLeast(1).toFloat(),
                steps = 0,
                showKeyPoints = false
            )
            if (clipEnabled && durationMillis > 0) {
                Text(
                    text = "选定区间  ${formatAudioTransformTime(effectiveClipStart)} - " +
                        formatAudioTransformTime(effectiveClipEnd),
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        if (clipEnabled) {
            InsetDivider()
            ActionRow(
                title = "开始时间",
                summary = if (prepared) {
                    formatAudioTransformTime(effectiveClipStart)
                } else {
                    "等待音频加载"
                }
            ) {
                if (!enabled || !prepared) return@ActionRow
                if (positionMillis.toLong() >= effectiveClipEnd) {
                    Toast.makeText(context, "开始时间必须早于结束时间", Toast.LENGTH_SHORT).show()
                } else {
                    currentRangeChange(inputPath, positionMillis.toLong(), effectiveClipEnd)
                }
            }
            InsetDivider()
            ActionRow(
                title = "结束时间",
                summary = if (prepared) {
                    formatAudioTransformTime(effectiveClipEnd)
                } else {
                    "等待音频加载"
                }
            ) {
                if (!enabled || !prepared) return@ActionRow
                if (positionMillis.toLong() <= effectiveClipStart) {
                    Toast.makeText(context, "结束时间必须晚于开始时间", Toast.LENGTH_SHORT).show()
                } else {
                    currentRangeChange(inputPath, effectiveClipStart, positionMillis.toLong())
                }
            }
            InsetDivider()
            ActionRow("重新设置", "恢复完整音频区间") {
                if (!enabled || !prepared) return@ActionRow
                currentRangeChange(inputPath, 0L, durationMillis.toLong())
                seekTo(0)
            }
        }
    }
}

private fun formatAudioTransformTime(millis: Long): String {
    val totalSeconds = millis.coerceAtLeast(0L) / 1000L
    val seconds = totalSeconds % 60L
    val minutes = totalSeconds / 60L % 60L
    val hours = totalSeconds / 3600L
    return if (hours > 0L) {
        String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    }
}

private fun audioTransformModeOptions(): List<PopupChoice<String>> = listOf(
    PopupChoice("任意音频转silk保存", AudioTransformSettings.MODE_AUDIO_TO_SILK_SAVE.toString()),
    PopupChoice("任意音频转silk发送", AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND.toString()),
    PopupChoice("Silk 转 MP3 保存", AudioTransformSettings.MODE_SILK_TO_MP3_SAVE.toString()),
    PopupChoice("Silk 转 M4A 保存", AudioTransformSettings.MODE_SILK_TO_M4A_SAVE.toString())
)

private fun audioTransformModeSummary(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_AUDIO_TO_SILK_SEND -> "把任意音频转成微信语音并直接发送"
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> "把 Silk 语音导出为 MP3 文件"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> "把 Silk 语音导出为 M4A 文件"
    else -> "把任意音频转成 Silk 并保存到本地"
}

private fun audioTransformValidateInput(
    mode: Int,
    inputPath: String,
    bridge: AudioTransformBridge
): String? {
    if (!File(inputPath).isFile) return "输入文件不存在"
    if (mode == AudioTransformSettings.MODE_SILK_TO_MP3_SAVE ||
        mode == AudioTransformSettings.MODE_SILK_TO_M4A_SAVE
    ) {
        val fileType = bridge.getFileType(inputPath)
        if (fileType != 1) return "当前模式只支持 Silk 输入"
    }
    return null
}

private fun audioTransformSuggestedFileName(inputName: String, mode: Int): String {
    val rawName = inputName.trim().ifBlank { "audio_output" }
    val base = rawName.substringBeforeLast('.').ifBlank { rawName }
    return base + audioTransformOutputExtension(mode)
}

private fun audioTransformOutputExtension(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> ".mp3"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> ".m4a"
    else -> ".silk"
}

private fun audioTransformOutputMime(mode: Int): String = when (mode) {
    AudioTransformSettings.MODE_SILK_TO_MP3_SAVE -> "audio/mpeg"
    AudioTransformSettings.MODE_SILK_TO_M4A_SAVE -> "audio/mp4"
    else -> "application/octet-stream"
}

private fun audioTransformInputSummary(inputPath: String, inputName: String): String {
    if (inputPath.isBlank()) return "未选择"
    return inputName.ifBlank { File(inputPath).name.ifBlank { "已选择文件" } }
}

private fun audioTransformTalkerSummary(talker: String): String {
    if (talker.isBlank()) return "未选择"
    val display = runCatching { WeChatApis.contacts()?.getDisplayName(talker) }.getOrNull().orEmpty()
    return if (display.isBlank() || display == talker) talker else "$display · $talker"
}

private fun copyAudioTransformFileToUri(context: Context, source: File, uri: Uri): Boolean {
    return runCatching {
        context.contentResolver.openOutputStream(uri, "w")?.use { output ->
            source.inputStream().use { input -> input.copyTo(output) }
        } != null
    }.getOrDefault(false)
}
}

@Composable
internal fun FeatureListCard(
    providers: List<FeatureSettingsProvider>,
    emptyText: String,
    onOpenProvider: (FeatureSettingsProvider) -> Unit
) {
    SettingsCard {
        if (providers.isEmpty()) {
            EmptyText(emptyText)
        } else {
            providers.forEachIndexed { index, provider ->
                FeatureRow(
                    provider = provider,
                    onClick = { onOpenProvider(provider) }
                )
                if (index < providers.lastIndex) {
                    InsetDivider()
                }
            }
        }
    }
}

@Composable
internal fun FeatureGroupRow(group: FeatureGroupEntry, onClick: () -> Unit) {
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
        Column(modifier = Modifier.weight(1f)) {
            Text(text = group.title, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(
                text = "${group.providers.size} 项功能",
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                fontSize = 12.sp
            )
        }
        Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
    }
}

@Composable
internal fun FeatureRow(provider: FeatureSettingsProvider, onClick: () -> Unit) {
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
        Column(modifier = Modifier.weight(1f)) {
            Text(text = provider.title(), color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(text = provider.subtitle(), color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
        }
        Text(text = "›", color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 22.sp)
    }
}

@Composable
internal fun ScheduledTaskMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val settings = remember { ScheduledTaskSettings(context) }
    val sp = remember { HchatStorage.preferences(context, ScheduledTaskSettings.PREFS_NAME) }
    var route by remember { mutableStateOf<ScheduledTaskRoute>(ScheduledTaskRoute.Main) }
    var enabled by remember {
        mutableStateOf(
            sp.getBoolean(
                ScheduledTaskSettings.KEY_ENABLE,
                ScheduledTaskSettings.DEFAULT_ENABLE
            )
        )
    }
    var tasks by remember { mutableStateOf(settings.tasks()) }
    var draft by remember { mutableStateOf(ScheduledTaskSettings.newDraft()) }
    var editingTaskId by remember { mutableStateOf<String?>(null) }
    val mainListState = rememberLazyListState()
    val editorListState = rememberLazyListState()

    fun reloadTasks() {
        tasks = settings.tasks()
    }

    fun persistTasks(next: List<ScheduledTaskItem>) {
        settings.saveTasks(next)
        reloadTasks()
        ScheduledTaskRuntimeCoordinator.reload()
    }

    fun saveDraft(closeAfterSave: Boolean = true): Boolean {
        var target = ScheduledTaskSettings.normalizeForSave(draft)
        val now = System.currentTimeMillis()
        val momentsError = ScheduledTaskSettings.momentsValidationError(target)
        val normalizedItems = ScheduledTaskSettings.normalizedItems(target)
        val channelError = if (
            target.targetType == ScheduledTaskSettings.TARGET_CHAT &&
            target.sendChannel == ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL
        ) {
            if (normalizedItems.all { it.type == ScheduledTaskSettings.TYPE_SELECTED_MESSAGE }) {
                val snapshots = normalizedItems.mapNotNull { SelectedMessageSnapshot.decode(it.value) }
                if (snapshots.size != normalizedItems.size) {
                    "定时转发消息快照无效"
                } else {
                    SelectedMessagesRuntimeCoordinator.snapshotValidationError(
                        target.sendChannel,
                        snapshots
                    )
                }
            } else {
                SelectedMessagesRuntimeCoordinator.validationError(target.sendChannel, normalizedItems)
            }
        } else {
            null
        }
        val planTimes = ScheduledTaskSettings.normalizedPlanTimes(target)
        val error = when {
            target.targetType == ScheduledTaskSettings.TARGET_CHAT && target.targetIds.isEmpty() -> "请选择发送对象"
            momentsError != null -> momentsError
            normalizedItems.isEmpty() -> "请配置发送内容"
            channelError != null -> channelError
            planTimes.isEmpty() -> "请添加计划时间"
            target.repeatType == ScheduledTaskSettings.REPEAT_WEEKLY && target.repeatDays.isEmpty() -> "请选择每周重复日期"
            target.repeatType == ScheduledTaskSettings.REPEAT_NONE && planTimes.any { it <= now } -> "计划时间需要晚于当前时间"
            else -> null
        }
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return false
        }
        if (target.repeatType != ScheduledTaskSettings.REPEAT_NONE) {
            val nextPlanTimes = planTimes.map { planTime ->
                ScheduledTaskSettings.resolveNextPlanTime(
                    planTime,
                    target.repeatType,
                    target.repeatDays,
                    now
                )
            }.filter { it > 0L }
            if (nextPlanTimes.isEmpty()) {
                Toast.makeText(context, "无法计算下次执行时间", Toast.LENGTH_SHORT).show()
                return false
            }
            target = updateScheduledTaskPlanTimes(target, nextPlanTimes)
        }
        val nextTasks = tasks.filterNot { it.id == target.id } + target
        persistTasks(nextTasks)
        draft = target
        if (closeAfterSave) {
            route = ScheduledTaskRoute.Main
        }
        Toast.makeText(context, "任务已保存", Toast.LENGTH_SHORT).show()
        return true
    }

    SettingsRouteTransition(
        targetState = route,
        label = "ScheduledTaskRoute",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            ScheduledTaskRoute.Main -> {
                ScheduledTaskMainPage(
                    provider = provider,
                    enabled = enabled,
                    tasks = tasks,
                    listState = mainListState,
                    onBack = onBack,
                    onEnabledChange = { checked ->
                        enabled = checked
                        sp.edit().putBoolean(ScheduledTaskSettings.KEY_ENABLE, checked).apply()
                        ScheduledTaskRuntimeCoordinator.reload()
                    },
                    onAdd = {
                        editingTaskId = null
                        draft = ScheduledTaskSettings.newDraft()
                        route = ScheduledTaskRoute.Editor
                    },
                    onEdit = { task ->
                        editingTaskId = task.id
                        draft = task
                        route = ScheduledTaskRoute.Editor
                    }
                )
            }
            ScheduledTaskRoute.Editor -> {
                ScheduledTaskEditorPage(
                    context = context,
                    task = draft,
                    isEditing = editingTaskId != null,
                    listState = editorListState,
                    onBack = { route = ScheduledTaskRoute.Main },
                    onTaskChange = { draft = it },
                    onPickTargets = { route = ScheduledTaskRoute.ContactPicker },
                    onPickFiles = { itemIndex, itemType ->
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                        } else {
                            ScheduledTaskFilePickerBridge.launch(activity, itemType) { paths ->
                                draft = updateScheduledTaskMediaItem(draft, itemIndex, itemType, paths)
                            }
                        }
                    },
                    onPickMomentsMedia = { itemType ->
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开文件选择器", Toast.LENGTH_SHORT).show()
                        } else {
                            ScheduledTaskFilePickerBridge.launch(activity, itemType) { paths ->
                                if (itemType == ScheduledTaskSettings.TYPE_IMAGE && paths.size > 9) {
                                    Toast.makeText(context, "朋友圈最多保留前 9 张图片", Toast.LENGTH_SHORT).show()
                                } else if (itemType == ScheduledTaskSettings.TYPE_VIDEO && paths.size > 1) {
                                    Toast.makeText(context, "朋友圈只保留第 1 个视频", Toast.LENGTH_SHORT).show()
                                }
                                draft = updateScheduledTaskMomentsMedia(draft, itemType, paths)
                            }
                        }
                    },
                    onPickFavorite = { itemIndex ->
                        scheduledTaskEditableItems(draft).getOrNull(itemIndex)?.let { item ->
                            route = ScheduledTaskRoute.FavoritePicker(
                                itemIndex,
                                FavoritePickerRequest(
                                    title = scheduledTaskPickerTitle(item.type),
                                    existingValue = item.value,
                                    onValue = { value ->
                                        draft = updateScheduledTaskContentItemValue(draft, itemIndex, value)
                                    },
                                    multiSelect = false,
                                    delimiter = "|"
                                )
                            )
                        }
                    },
                    onSave = { saveDraft() },
                    onDelete = if (editingTaskId == null) {
                        null
                    } else {
                        {
                            persistTasks(tasks.filterNot { it.id == draft.id })
                            route = ScheduledTaskRoute.Main
                            Toast.makeText(context, "任务已删除", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onExecuteNow = if (editingTaskId == null) {
                        null
                    } else {
                        {
                            if (saveDraft(closeAfterSave = false)) {
                                if (ScheduledTaskRuntimeCoordinator.executeNow(draft.id)) {
                                    Toast.makeText(context, "任务已加入立即执行队列", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "立即执行失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                )
            }
            ScheduledTaskRoute.ContactPicker -> {
                val official = draft.sendChannel == ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL
                ContactPickerPage(
                    context = context,
                    request = ContactPickerRequest(
                        title = if (official) "选择原生群发好友" else "选择发送对象",
                        mode = if (official) ContactPickerMode.FRIENDS else ContactPickerMode.ALL_CHATS,
                        multiSelect = true,
                        existingValue = formatIds(draft.targetIds),
                        onValue = {},
                        enableLabels = true
                    ),
                    onBack = { route = ScheduledTaskRoute.Editor },
                    onConfirm = { selected ->
                        draft = draft.copy(targetIds = selected.map { it.id })
                        route = ScheduledTaskRoute.Editor
                    }
                )
            }
            is ScheduledTaskRoute.FavoritePicker -> {
                FavoritePickerPage(
                    request = currentRoute.request,
                    onBack = { route = ScheduledTaskRoute.Editor }
                )
            }
        }
    }
}

@Composable
internal fun ScheduledTaskMainPage(
    provider: FeatureSettingsProvider,
    enabled: Boolean,
    tasks: List<ScheduledTaskItem>,
    listState: LazyListState,
    onBack: () -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onAdd: () -> Unit,
    onEdit: (ScheduledTaskItem) -> Unit
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = provider.title(),
        largeTitle = provider.title(),
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "新增任务",
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
            item { SmallTitle(text = "发送计划") }
            item {
                SettingsCard {
                    SwitchRow(
                        checked = enabled,
                        title = "启用定时任务",
                        summary = "开启后按计划时间发送聊天消息或发布朋友圈"
                    ) { onEnabledChange(it) }
                    InsetDivider()
                    InfoRow("当前任务", if (tasks.isEmpty()) "暂无任务" else "${tasks.size} 个")
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "任务列表") }
            when {
                tasks.isEmpty() -> item {
                    SettingsCard {
                        EmptyText("还没有定时任务，点击底部“新增任务”。")
                    }
                }
                else -> tasks.forEach { task ->
                    item {
                        SettingsCard {
                            ActionRow(
                                scheduledTaskTitle(task),
                                scheduledTaskSummary(task)
                            ) {
                                onEdit(task)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScheduledTaskEditorPage(
    context: Context,
    task: ScheduledTaskItem,
    isEditing: Boolean,
    listState: LazyListState,
    onBack: () -> Unit,
    onTaskChange: (ScheduledTaskItem) -> Unit,
    onPickTargets: () -> Unit,
    onPickFiles: (Int, Int) -> Unit,
    onPickMomentsMedia: (Int) -> Unit,
    onPickFavorite: (Int) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    onExecuteNow: (() -> Unit)?
) {
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = if (isEditing) "编辑任务" else "新增任务",
        largeTitle = if (isEditing) "编辑任务" else "新增任务",
        scrollBehavior = scrollBehavior,
        bottomBar = {
            BottomActionBar(
                primaryText = "保存任务",
                onPrimaryClick = onSave,
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
            item { SmallTitle(text = "备注") }
            item {
                SettingsCard {
                    InputRow(
                        title = "任务备注",
                        summary = "用于在任务列表区分任务，可留空",
                        value = task.remark,
                        onValueChange = { onTaskChange(task.copy(remark = it)) }
                    )
                }
            }
            item { SmallTitle(text = "任务目标") }
            item {
                SettingsCard {
                    if (scheduledTaskEditableItems(task).any { it.type == ScheduledTaskSettings.TYPE_SELECTED_MESSAGE }) {
                        InfoRow(label = "发送到", value = "聊天")
                    } else {
                        PopupChoiceRow(
                            title = "发送到",
                            summary = scheduledTaskTargetTypeLabel(task.targetType),
                            options = scheduledTaskTargetTypeChoices(),
                            currentValue = task.targetType.toString(),
                            onValueChanged = { value ->
                                val targetType = value.toIntOrNull() ?: ScheduledTaskSettings.TARGET_CHAT
                                onTaskChange(updateScheduledTaskTargetType(task, targetType))
                            }
                        )
                    }
                    if (task.targetType == ScheduledTaskSettings.TARGET_CHAT) {
                        InsetDivider()
                        PopupChoiceRow(
                            title = "发送通道",
                            summary = scheduledTaskSendChannelLabel(task.sendChannel),
                            options = scheduledTaskSendChannelChoices(),
                            currentValue = task.sendChannel.toString(),
                            onValueChanged = { value ->
                                val channel = value.toIntOrNull()
                                    ?.takeIf { ScheduledTaskSettings.sendChannelIsValid(it) }
                                    ?: ScheduledTaskSettings.SEND_CHANNEL_MODULE
                                onTaskChange(
                                    task.copy(
                                        sendChannel = channel,
                                        targetIds = if (channel == task.sendChannel) {
                                            task.targetIds
                                        } else {
                                            emptyList()
                                        }
                                    )
                                )
                            }
                        )
                        InsetDivider()
                        ActionRow(
                            if (task.sendChannel == ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL) {
                                "选择好友"
                            } else {
                                "选择聊天"
                            },
                            scheduledTaskTargetSummary(task.targetIds)
                        ) {
                            onPickTargets()
                        }
                    }
                }
            }
            item {
                SmallTitle(
                    modifier = Modifier.padding(top = 10.dp),
                    text = if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) "朋友圈内容" else "发送内容"
                )
            }
            item {
                SettingsCard {
                    if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) {
                        PopupChoiceRow(
                            title = "朋友圈类型",
                            summary = scheduledTaskMomentsTypeLabel(task.momentsType),
                            options = scheduledTaskMomentsTypeChoices(),
                            currentValue = task.momentsType.toString(),
                            onValueChanged = { value ->
                                val type = value.toIntOrNull() ?: ScheduledTaskSettings.MOMENTS_TEXT
                                onTaskChange(updateScheduledTaskMomentsType(task, type))
                            }
                        )
                        if (scheduledTaskMomentsUsesText(task.momentsType)) {
                            InsetDivider()
                            VariableInputRow(
                                title = "朋友圈文字",
                                summary = "输入要发布的朋友圈文案",
                                value = scheduledTaskMomentsText(task),
                                variables = emptyList(),
                                minLines = 3
                            ) {
                                onTaskChange(updateScheduledTaskMomentsText(task, it))
                            }
                        }
                        val mediaType = scheduledTaskMomentsMediaType(task.momentsType)
                        if (mediaType != null) {
                            val paths = scheduledTaskMomentsMediaPaths(task, mediaType)
                            InsetDivider()
                            ActionRow(
                                if (mediaType == ScheduledTaskSettings.TYPE_IMAGE) "选择图片" else "选择视频",
                                scheduledTaskMediaSummary(mediaType, paths)
                            ) {
                                onPickMomentsMedia(mediaType)
                            }
                            if (paths.isNotEmpty()) {
                                InsetDivider()
                                ActionRow("清空媒体", "移除已选择的朋友圈媒体") {
                                    onTaskChange(updateScheduledTaskMomentsMedia(task, mediaType, emptyList()))
                                }
                            }
                        }
                    } else {
                        ScheduledTaskChatContentRows(
                            task = task,
                            onTaskChange = onTaskChange,
                            onPickFiles = onPickFiles,
                            onPickFavorite = onPickFavorite,
                            massSendOnly = task.sendChannel == ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "执行计划") }
            item {
                SettingsCard {
                    val planTimes = ScheduledTaskSettings.normalizedPlanTimes(task)
                    planTimes.forEachIndexed { index, planTime ->
                        if (index > 0) InsetDivider()
                        ActionRow("计划时间 ${index + 1}", scheduledTaskTimeText(planTime)) {
                            val activity = context as? Activity
                            if (activity == null) {
                                Toast.makeText(context, "当前页面无法打开时间选择器", Toast.LENGTH_SHORT).show()
                            } else {
                                showScheduledTaskTimeActionPicker(
                                    activity = activity,
                                    canDelete = planTimes.size > 1,
                                    onEdit = {
                                        showScheduledTaskDateTimePicker(activity, planTime) { picked ->
                                            val next = planTimes.toMutableList().apply { set(index, picked) }
                                            onTaskChange(updateScheduledTaskPlanTimes(task, next))
                                        }
                                    },
                                    onDelete = {
                                        onTaskChange(
                                            updateScheduledTaskPlanTimes(
                                                task,
                                                planTimes.filterIndexed { currentIndex, _ -> currentIndex != index }
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                    InsetDivider()
                    ActionRow("新增计划时间", "同一任务可在多个时间分别执行") {
                        val activity = context as? Activity
                        if (activity == null) {
                            Toast.makeText(context, "当前页面无法打开时间选择器", Toast.LENGTH_SHORT).show()
                        } else {
                            val initialTime = planTimes.lastOrNull()
                                ?.let { it + 5 * 60 * 1000L }
                                ?: (System.currentTimeMillis() + 5 * 60 * 1000L)
                            showScheduledTaskDateTimePicker(activity, initialTime) { picked ->
                                onTaskChange(updateScheduledTaskPlanTimes(task, planTimes + picked))
                            }
                        }
                    }
                    InsetDivider()
                    PopupChoiceRow(
                        title = "重复方式",
                        summary = scheduledTaskRepeatLabel(task.repeatType),
                        options = scheduledTaskRepeatChoices(),
                        currentValue = task.repeatType.toString(),
                        onValueChanged = { value ->
                            val repeatType = value.toIntOrNull() ?: ScheduledTaskSettings.REPEAT_NONE
                            onTaskChange(
                                task.copy(
                                    repeatType = repeatType,
                                    repeatDays = if (repeatType == ScheduledTaskSettings.REPEAT_WEEKLY) {
                                        task.repeatDays.ifEmpty { setOf(Calendar.MONDAY) }
                                    } else {
                                        emptySet()
                                    }
                                )
                            )
                        }
                    )
                    if (task.repeatType == ScheduledTaskSettings.REPEAT_WEEKLY) {
                        InsetDivider()
                        ScheduledTaskWeekdayRow(task.repeatDays) { day ->
                            val next = if (task.repeatDays.contains(day)) {
                                task.repeatDays - day
                            } else {
                                task.repeatDays + day
                            }
                            onTaskChange(task.copy(repeatDays = next))
                        }
                    }
                    InsetDivider()
                    SwitchRow(
                        checked = task.sendOnTimeout,
                        title = "超时补发",
                        summary = "微信进程错过执行时间后，恢复时自动补发"
                    ) {
                        onTaskChange(task.copy(sendOnTimeout = it))
                    }
                }
            }
            if (
                task.targetType == ScheduledTaskSettings.TARGET_CHAT &&
                task.sendChannel == ScheduledTaskSettings.SEND_CHANNEL_MODULE
            ) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "发送节奏") }
                item {
                    SettingsCard {
                        NumberInputRow("聊天间隔", "单位秒，多个聊天之间的等待时间", task.intervalSeconds.toString()) {
                            onTaskChange(task.copy(intervalSeconds = it.toIntOrNull()?.coerceIn(0, 3600) ?: 0))
                        }
                        if (scheduledTaskHasOrderedItems(task)) {
                            InsetDivider()
                            NumberInputRow(scheduledTaskItemIntervalTitle(), "单位秒，同一聊天连续发送的等待时间", task.mediaIntervalSeconds.toString()) {
                                onTaskChange(task.copy(mediaIntervalSeconds = it.toIntOrNull()?.coerceIn(0, 3600) ?: 0))
                            }
                        }
                    }
                }
            }
            if (onExecuteNow != null || onDelete != null) {
                item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "操作") }
                item {
                    SettingsCard {
                        if (onExecuteNow != null) {
                            ActionRow("立即执行", "保存当前内容后立刻发送一次") {
                                onExecuteNow()
                            }
                        }
                        if (onExecuteNow != null && onDelete != null) {
                            InsetDivider()
                        }
                        if (onDelete != null) {
                            ActionRow("删除任务", "移除当前定时任务") {
                                onDelete()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScheduledTaskWeekdayRow(
    selectedDays: Set<Int>,
    onToggle: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        scheduledTaskWeekdayOptions().forEach { (day, label) ->
            ContactFilterChip(
                text = label,
                selected = selectedDays.contains(day),
                onClick = { onToggle(day) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun ScheduledTaskChatContentRows(
    task: ScheduledTaskItem,
    onTaskChange: (ScheduledTaskItem) -> Unit,
    onPickFiles: (Int, Int) -> Unit,
    onPickFavorite: (Int) -> Unit,
    massSendOnly: Boolean = false
) {
    val items = scheduledTaskEditableItems(task)
    items.forEachIndexed { index, item ->
        if (index > 0) InsetDivider()
        if (item.type == ScheduledTaskSettings.TYPE_SELECTED_MESSAGE) {
            InfoRow(
                label = if (items.size == 1) "发送类型" else "发送类型 ${index + 1}",
                value = scheduledTaskTypeLabel(item.type)
            )
            InsetDivider()
            InfoRow(
                label = "消息",
                value = SelectedMessageSnapshot.decode(item.value)?.label() ?: "不可用"
            )
        } else {
            PopupChoiceRow(
                title = if (items.size == 1) "发送类型" else "发送类型 ${index + 1}",
                summary = if (massSendOnly) {
                    selectedMessagesContentTypeLabel(item.type)
                } else {
                    scheduledTaskTypeLabel(item.type)
                },
                options = if (massSendOnly) selectedMessagesContentTypeChoices() else scheduledTaskTypeChoices(),
                currentValue = item.type.toString(),
                onValueChanged = { value ->
                    val type = value.toIntOrNull() ?: if (massSendOnly) {
                        ScheduledTaskSettings.TYPE_TEXT
                    } else {
                        ScheduledTaskSettings.TYPE_TEXT
                    }
                    onTaskChange(updateScheduledTaskContentItemType(task, index, type))
                }
            )
            InsetDivider()
        }
        if (item.type != ScheduledTaskSettings.TYPE_SELECTED_MESSAGE && scheduledTaskUsesText(item.type)) {
            VariableInputRow(
                title = if (massSendOnly && item.type == ScheduledTaskSettings.TYPE_XML) {
                    "视频号内容"
                } else {
                    scheduledTaskContentTitle(item.type)
                },
                summary = if (item.type == ScheduledTaskSettings.TYPE_XML) {
                    if (massSendOnly) "输入视频号分享消息 XML" else "输入 <msg><appmsg>...</appmsg></msg>"
                } else {
                    "输入要发送的内容"
                },
                value = item.value,
                variables = if (massSendOnly) emptyList() else scheduledTaskTemplateVariables,
                minLines = if (item.type == ScheduledTaskSettings.TYPE_XML) 4 else 2
            ) {
                onTaskChange(updateScheduledTaskContentItemValue(task, index, it))
            }
        } else if (item.type != ScheduledTaskSettings.TYPE_SELECTED_MESSAGE &&
            item.type == ScheduledTaskSettings.TYPE_FAVORITE
        ) {
            ActionRow(scheduledTaskPickerTitle(item.type), favoriteSummary(item.value)) {
                onPickFavorite(index)
            }
            if (item.value.isNotBlank()) {
                InsetDivider()
                ActionRow("清空收藏", "移除当前收藏") {
                    onTaskChange(updateScheduledTaskContentItemValue(task, index, ""))
                }
            }
        } else if (item.type != ScheduledTaskSettings.TYPE_SELECTED_MESSAGE) {
            ActionRow("选择文件", scheduledTaskMediaSummary(item.type, listOf(item.value))) {
                onPickFiles(index, item.type)
            }
            if (item.value.isNotBlank()) {
                InsetDivider()
                ActionRow("清空文件", "移除当前文件") {
                    onTaskChange(updateScheduledTaskContentItemValue(task, index, ""))
                }
            }
        }
        if (items.size > 1) {
            InsetDivider()
            ActionRow("删除本条", "移除第 ${index + 1} 条内容") {
                onTaskChange(removeScheduledTaskContentItem(task, index))
            }
        }
    }
    if (items.none { it.type == ScheduledTaskSettings.TYPE_SELECTED_MESSAGE }) {
        InsetDivider()
        ActionRow("新增内容", "添加一条按顺序发送的内容") {
            onTaskChange(
                appendScheduledTaskContentItem(
                    task,
                    ScheduledTaskSettings.TYPE_TEXT
                )
            )
        }
    }
}

internal fun scheduledTaskUsesText(type: Int): Boolean {
    return ScheduledTaskSettings.scheduledTypeUsesText(type)
}

internal fun scheduledTaskEditableItems(task: ScheduledTaskItem): List<ScheduledTaskContentItem> {
    return task.items.ifEmpty {
        if (scheduledTaskUsesText(task.type)) {
            val contents = task.contentItems.ifEmpty { listOf(task.content) }
            contents.map { ScheduledTaskContentItem(task.type, it) }
        } else {
            task.mediaPaths.map { ScheduledTaskContentItem(task.type, it) }
        }
    }.ifEmpty {
        listOf(ScheduledTaskContentItem(ScheduledTaskSettings.TYPE_TEXT, ""))
    }.map {
        val type = it.type.takeIf { value -> ScheduledTaskSettings.scheduledTypeIsValid(value) }
            ?: ScheduledTaskSettings.TYPE_TEXT
        ScheduledTaskContentItem(type, it.value)
    }
}

internal fun updateScheduledTaskContentItemType(
    task: ScheduledTaskItem,
    index: Int,
    type: Int
): ScheduledTaskItem {
    val items = scheduledTaskEditableItems(task).toMutableList()
    if (index !in items.indices) return task
    val previous = items[index]
    val keepValue = previous.type == type ||
        (
            scheduledTaskUsesText(previous.type) == scheduledTaskUsesText(type) &&
                previous.type != ScheduledTaskSettings.TYPE_FAVORITE &&
                type != ScheduledTaskSettings.TYPE_FAVORITE
            )
    items[index] = ScheduledTaskContentItem(type, if (keepValue) previous.value else "")
    return task.copy(items = items)
}

internal fun updateScheduledTaskContentItemValue(
    task: ScheduledTaskItem,
    index: Int,
    value: String
): ScheduledTaskItem {
    val items = scheduledTaskEditableItems(task).toMutableList()
    if (index !in items.indices) return task
    items[index] = items[index].copy(value = value)
    return task.copy(items = items)
}

internal fun updateScheduledTaskMediaItem(
    task: ScheduledTaskItem,
    index: Int,
    type: Int,
    paths: List<String>
): ScheduledTaskItem {
    val nextPaths = paths.map { it.trim() }.filter { it.isNotBlank() }.distinct()
    if (nextPaths.isEmpty()) return task
    val items = scheduledTaskEditableItems(task).toMutableList()
    if (index !in items.indices) return task
    items[index] = ScheduledTaskContentItem(type, nextPaths.first())
    nextPaths.drop(1).forEachIndexed { offset, path ->
        items.add(index + 1 + offset, ScheduledTaskContentItem(type, path))
    }
    return task.copy(items = items)
}

internal fun appendScheduledTaskContentItem(
    task: ScheduledTaskItem,
    defaultType: Int = ScheduledTaskSettings.TYPE_TEXT
): ScheduledTaskItem {
    val items = scheduledTaskEditableItems(task) + ScheduledTaskContentItem(defaultType, "")
    return task.copy(items = items)
}

internal fun removeScheduledTaskContentItem(task: ScheduledTaskItem, index: Int): ScheduledTaskItem {
    val items = scheduledTaskEditableItems(task).toMutableList()
    if (items.size <= 1 || index !in items.indices) return task
    items.removeAt(index)
    return task.copy(items = items)
}

internal fun scheduledTaskContentTitle(type: Int): String {
    return if (type == ScheduledTaskSettings.TYPE_XML) "XML 内容" else "发送内容"
}

internal fun scheduledTaskHasOrderedItems(task: ScheduledTaskItem): Boolean {
    return scheduledTaskEditableItems(task).size > 1
}

internal fun scheduledTaskItemIntervalTitle(): String {
    return "多条间隔"
}

internal fun scheduledTaskTargetTypeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("聊天", ScheduledTaskSettings.TARGET_CHAT.toString()),
    PopupChoice("朋友圈", ScheduledTaskSettings.TARGET_MOMENTS.toString())
)

internal fun scheduledTaskSendChannelChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("模块通道", ScheduledTaskSettings.SEND_CHANNEL_MODULE.toString()),
    PopupChoice("微信原生群发助手", ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL.toString())
)

internal fun scheduledTaskSendChannelLabel(channel: Int): String {
    return if (channel == ScheduledTaskSettings.SEND_CHANNEL_OFFICIAL) {
        "微信原生群发助手"
    } else {
        "模块通道"
    }
}

internal fun scheduledTaskTargetTypeLabel(type: Int): String = when (type) {
    ScheduledTaskSettings.TARGET_MOMENTS -> "朋友圈"
    else -> "聊天"
}

internal fun scheduledTaskMomentsTypeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("文字", ScheduledTaskSettings.MOMENTS_TEXT.toString()),
    PopupChoice("图文", ScheduledTaskSettings.MOMENTS_TEXT_IMAGE.toString()),
    PopupChoice("视文", ScheduledTaskSettings.MOMENTS_TEXT_VIDEO.toString()),
    PopupChoice("图片", ScheduledTaskSettings.MOMENTS_IMAGE.toString()),
    PopupChoice("视频", ScheduledTaskSettings.MOMENTS_VIDEO.toString())
)

internal fun scheduledTaskMomentsTypeLabel(type: Int): String = when (type) {
    ScheduledTaskSettings.MOMENTS_TEXT_IMAGE -> "图文"
    ScheduledTaskSettings.MOMENTS_TEXT_VIDEO -> "视文"
    ScheduledTaskSettings.MOMENTS_IMAGE -> "图片"
    ScheduledTaskSettings.MOMENTS_VIDEO -> "视频"
    else -> "文字"
}

internal fun scheduledTaskMomentsUsesText(type: Int): Boolean {
    return type == ScheduledTaskSettings.MOMENTS_TEXT ||
        type == ScheduledTaskSettings.MOMENTS_TEXT_IMAGE ||
        type == ScheduledTaskSettings.MOMENTS_TEXT_VIDEO
}

internal fun scheduledTaskMomentsMediaType(type: Int): Int? = when (type) {
    ScheduledTaskSettings.MOMENTS_TEXT_IMAGE,
    ScheduledTaskSettings.MOMENTS_IMAGE -> ScheduledTaskSettings.TYPE_IMAGE
    ScheduledTaskSettings.MOMENTS_TEXT_VIDEO,
    ScheduledTaskSettings.MOMENTS_VIDEO -> ScheduledTaskSettings.TYPE_VIDEO
    else -> null
}

internal fun scheduledTaskMomentsText(task: ScheduledTaskItem): String {
    return task.items.firstOrNull { it.type == ScheduledTaskSettings.TYPE_TEXT }?.value
        ?: task.content.takeIf { task.type == ScheduledTaskSettings.TYPE_TEXT }.orEmpty()
}

internal fun scheduledTaskMomentsMediaPaths(task: ScheduledTaskItem, mediaType: Int): List<String> {
    val itemPaths = task.items.asSequence()
        .filter { it.type == mediaType }
        .map { it.value.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .toList()
    if (itemPaths.isNotEmpty()) return itemPaths
    return if (task.type == mediaType) task.mediaPaths else emptyList()
}

internal fun updateScheduledTaskTargetType(task: ScheduledTaskItem, targetType: Int): ScheduledTaskItem {
    val safeTarget = targetType.takeIf { ScheduledTaskSettings.targetTypeIsValid(it) }
        ?: ScheduledTaskSettings.TARGET_CHAT
    if (safeTarget == task.targetType) return task
    if (safeTarget == ScheduledTaskSettings.TARGET_CHAT) {
        return task.copy(targetType = safeTarget)
    }
    val hasText = scheduledTaskMomentsText(task).isNotBlank()
    val hasImages = scheduledTaskMomentsMediaPaths(task, ScheduledTaskSettings.TYPE_IMAGE).isNotEmpty()
    val hasVideo = scheduledTaskMomentsMediaPaths(task, ScheduledTaskSettings.TYPE_VIDEO).isNotEmpty()
    val momentsType = when {
        hasText && hasImages -> ScheduledTaskSettings.MOMENTS_TEXT_IMAGE
        hasText && hasVideo -> ScheduledTaskSettings.MOMENTS_TEXT_VIDEO
        hasImages -> ScheduledTaskSettings.MOMENTS_IMAGE
        hasVideo -> ScheduledTaskSettings.MOMENTS_VIDEO
        else -> ScheduledTaskSettings.MOMENTS_TEXT
    }
    return task.copy(
        targetType = safeTarget,
        momentsType = momentsType,
        sendChannel = ScheduledTaskSettings.SEND_CHANNEL_MODULE,
        targetIds = emptyList()
    )
}

internal fun updateScheduledTaskMomentsType(task: ScheduledTaskItem, momentsType: Int): ScheduledTaskItem {
    val safeType = momentsType.takeIf { ScheduledTaskSettings.momentsTypeIsValid(it) }
        ?: ScheduledTaskSettings.MOMENTS_TEXT
    return task.copy(momentsType = safeType)
}

internal fun updateScheduledTaskMomentsText(task: ScheduledTaskItem, value: String): ScheduledTaskItem {
    val items = task.items.filterNot { it.type == ScheduledTaskSettings.TYPE_TEXT }.toMutableList()
    items.add(0, ScheduledTaskContentItem(ScheduledTaskSettings.TYPE_TEXT, value))
    return task.copy(items = items)
}

internal fun updateScheduledTaskMomentsMedia(
    task: ScheduledTaskItem,
    mediaType: Int,
    paths: List<String>
): ScheduledTaskItem {
    val nextPaths = paths.asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .let { values ->
            if (mediaType == ScheduledTaskSettings.TYPE_IMAGE) values.take(9) else values.take(1)
        }
        .toList()
    val items = task.items.filterNot { it.type == mediaType }.toMutableList()
    items += nextPaths.map { ScheduledTaskContentItem(mediaType, it) }
    return task.copy(items = items)
}

internal fun scheduledTaskTypeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("文本", ScheduledTaskSettings.TYPE_TEXT.toString()),
    PopupChoice("图片", ScheduledTaskSettings.TYPE_IMAGE.toString()),
    PopupChoice("视频", ScheduledTaskSettings.TYPE_VIDEO.toString()),
    PopupChoice("文件", ScheduledTaskSettings.TYPE_FILE.toString()),
    PopupChoice("表情", ScheduledTaskSettings.TYPE_EMOJI.toString()),
    PopupChoice("语音", ScheduledTaskSettings.TYPE_VOICE.toString()),
    PopupChoice("XML", ScheduledTaskSettings.TYPE_XML.toString()),
    PopupChoice("收藏", ScheduledTaskSettings.TYPE_FAVORITE.toString())
)

internal fun selectedMessagesContentTypeChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("文字", ScheduledTaskSettings.TYPE_TEXT.toString()),
    PopupChoice("图片", ScheduledTaskSettings.TYPE_IMAGE.toString()),
    PopupChoice("视频", ScheduledTaskSettings.TYPE_VIDEO.toString()),
    PopupChoice("语音", ScheduledTaskSettings.TYPE_VOICE.toString()),
    PopupChoice("表情", ScheduledTaskSettings.TYPE_EMOJI.toString()),
    PopupChoice("视频号", ScheduledTaskSettings.TYPE_XML.toString())
)

internal fun selectedMessagesContentTypeLabel(type: Int): String = when (type) {
    ScheduledTaskSettings.TYPE_TEXT -> "文字"
    ScheduledTaskSettings.TYPE_IMAGE -> "图片"
    ScheduledTaskSettings.TYPE_VIDEO -> "视频"
    ScheduledTaskSettings.TYPE_VOICE -> "语音"
    ScheduledTaskSettings.TYPE_EMOJI -> "表情"
    ScheduledTaskSettings.TYPE_XML -> "视频号"
    else -> scheduledTaskTypeLabel(type)
}

internal fun scheduledTaskRepeatChoices(): List<PopupChoice<String>> = listOf(
    PopupChoice("单次", ScheduledTaskSettings.REPEAT_NONE.toString()),
    PopupChoice("每天", ScheduledTaskSettings.REPEAT_DAILY.toString()),
    PopupChoice("每周", ScheduledTaskSettings.REPEAT_WEEKLY.toString())
)

internal fun scheduledTaskTypeLabel(type: Int): String = when (type) {
    ScheduledTaskSettings.TYPE_TEXT -> "文本"
    ScheduledTaskSettings.TYPE_IMAGE -> "图片"
    ScheduledTaskSettings.TYPE_VIDEO -> "视频"
    ScheduledTaskSettings.TYPE_FILE -> "文件"
    ScheduledTaskSettings.TYPE_EMOJI -> "表情"
    ScheduledTaskSettings.TYPE_VOICE -> "语音"
    ScheduledTaskSettings.TYPE_XML -> "XML"
    ScheduledTaskSettings.TYPE_FAVORITE -> "收藏"
    ScheduledTaskSettings.TYPE_SELECTED_MESSAGE -> "聊天记录"
    else -> "未知"
}

internal fun scheduledTaskRepeatLabel(repeatType: Int): String = when (repeatType) {
    ScheduledTaskSettings.REPEAT_DAILY -> "每天"
    ScheduledTaskSettings.REPEAT_WEEKLY -> "每周"
    else -> "单次"
}

internal fun scheduledTaskTargetSummary(targetIds: List<String>): String {
    return if (targetIds.isEmpty()) "未选择" else "已选 ${targetIds.distinct().size} 个聊天"
}

internal fun scheduledTaskTargetSummary(task: ScheduledTaskItem): String {
    return if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) {
        "朋友圈"
    } else {
        scheduledTaskTargetSummary(task.targetIds)
    }
}

internal fun scheduledTaskMediaSummary(type: Int, paths: List<String>): String {
    if (type == ScheduledTaskSettings.TYPE_FAVORITE) {
        return favoriteSummary(paths.joinToString("|"))
    }
    val values = paths.map { it.trim() }.filter { it.isNotBlank() }
    if (values.isEmpty()) return "未选择"
    val first = File(values.first()).name.ifBlank { "文件" }
    return if (values.size == 1) first else "$first 等 ${values.size} 个文件"
}

internal fun scheduledTaskTitle(task: ScheduledTaskItem): String {
    task.remark.trim().takeIf { it.isNotBlank() }?.let { return it }
    if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) {
        return "朋友圈 · ${scheduledTaskMomentsTypeLabel(task.momentsType)}"
    }
    val countText = scheduledTaskItemCountText(task)
    return "${scheduledTaskTypeSummary(task)}$countText · ${scheduledTaskTargetSummary(task)}"
}

internal fun scheduledTaskItemCountText(task: ScheduledTaskItem): String {
    if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) return ""
    val count = ScheduledTaskSettings.normalizedItems(task).size
    if (count <= 1) return ""
    return "(${count}条)"
}

internal fun scheduledTaskSummary(task: ScheduledTaskItem): String {
    val repeat = when (task.repeatType) {
        ScheduledTaskSettings.REPEAT_DAILY -> "每天"
        ScheduledTaskSettings.REPEAT_WEEKLY -> scheduledTaskRepeatDaysText(task.repeatDays)
        else -> "单次"
    }
    val result = if (task.lastExecutedTime > 0L) {
        " · 上次 ${task.lastSuccessCount}/${task.lastFailCount}"
    } else {
        ""
    }
    val channel = if (task.targetType == ScheduledTaskSettings.TARGET_CHAT) {
        scheduledTaskSendChannelLabel(task.sendChannel) + " / "
    } else {
        ""
    }
    val planTimes = ScheduledTaskSettings.normalizedPlanTimes(task)
    val timeSummary = if (planTimes.size <= 1) {
        scheduledTaskTimeText(task.planTime)
    } else {
        "${scheduledTaskTimeText(task.planTime)} 等 ${planTimes.size} 个时间"
    }
    val schedule = "$channel$timeSummary / $repeat$result"
    return if (task.remark.isBlank()) {
        schedule
    } else {
        val countText = scheduledTaskItemCountText(task)
        "${scheduledTaskTypeSummary(task)}$countText · ${scheduledTaskTargetSummary(task)} / $schedule"
    }
}

internal fun scheduledTaskTypeSummary(task: ScheduledTaskItem): String {
    if (task.targetType == ScheduledTaskSettings.TARGET_MOMENTS) {
        return scheduledTaskMomentsTypeLabel(task.momentsType)
    }
    val items = ScheduledTaskSettings.normalizedItems(task)
    if (items.isEmpty()) return scheduledTaskTypeLabel(task.type)
    val types = items.map { it.type }.distinct()
    return if (types.size == 1) scheduledTaskTypeLabel(types.first()) else "混合"
}

internal fun scheduledTaskWeekdayOptions(): List<Pair<Int, String>> = listOf(
    Calendar.MONDAY to "一",
    Calendar.TUESDAY to "二",
    Calendar.WEDNESDAY to "三",
    Calendar.THURSDAY to "四",
    Calendar.FRIDAY to "五",
    Calendar.SATURDAY to "六",
    Calendar.SUNDAY to "日"
)

internal fun scheduledTaskRepeatDaysText(days: Set<Int>): String {
    if (days.isEmpty()) return "每周"
    val labels = scheduledTaskWeekdayOptions()
        .filter { days.contains(it.first) }
        .joinToString("") { "周${it.second}" }
    return labels.ifBlank { "每周" }
}

internal fun scheduledTaskTimeText(timeMillis: Long): String {
    if (timeMillis <= 0L) return "未设置"
    return runCatching {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(timeMillis))
    }.getOrDefault("未设置")
}

internal fun showScheduledTaskDateTimePicker(
    activity: Activity,
    currentTimeMillis: Long,
    onPicked: (Long) -> Unit
) {
    val base = Calendar.getInstance().apply {
        timeInMillis = if (currentTimeMillis > 0L) currentTimeMillis else System.currentTimeMillis()
    }
    DatePickerDialog(
        activity,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            TimePickerDialog(
                activity,
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    showScheduledTaskSecondPicker(activity, base.get(Calendar.SECOND)) { second ->
                        val next = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, second)
                            set(Calendar.MILLISECOND, 0)
                        }
                        onPicked(next.timeInMillis)
                    }
                },
                base.get(Calendar.HOUR_OF_DAY),
                base.get(Calendar.MINUTE),
                true
            ).show()
        },
        base.get(Calendar.YEAR),
        base.get(Calendar.MONTH),
        base.get(Calendar.DAY_OF_MONTH)
    ).show()
}

internal fun showScheduledTaskTimeActionPicker(
    activity: Activity,
    canDelete: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val choices = buildList {
        add("修改时间" to "重新选择该计划时间")
        if (canDelete) add("删除时间" to "从当前任务移除该计划时间")
    }
    VoiceForwardMiuixDialog.showChoices(
        activity = activity,
        title = "计划时间",
        summary = "",
        choices = choices,
        onSelected = { index ->
            if (index == 0) onEdit() else onDelete()
        },
        onDismiss = {}
    )
}

internal fun updateScheduledTaskPlanTimes(
    task: ScheduledTaskItem,
    values: List<Long>
): ScheduledTaskItem {
    val planTimes = values.filter { it > 0L }.distinct().sorted()
    return task.copy(
        planTime = planTimes.firstOrNull() ?: 0L,
        planTimes = planTimes
    )
}

internal fun showScheduledTaskSecondPicker(
    activity: Activity,
    initialSecond: Int,
    onPicked: (Int) -> Unit
) {
    val showDialog: () -> Unit = {
        VoiceForwardMiuixDialog.showNumberInput(
            activity = activity,
            title = "设置秒数",
            initialValue = initialSecond,
            minValue = 0,
            maxValue = 59,
            onConfirm = onPicked,
            onDismiss = {}
        )
    }
    activity.window?.decorView?.postOnAnimation {
        if (!activity.isFinishing && !activity.isDestroyed) showDialog()
    } ?: showDialog()
}

@Composable
internal fun FakeScanCameraMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, FakeScanCameraSettings.PREFS_NAME) }
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
            item { SmallTitle(text = "扫码") }
            item {
                SettingsCard {
                    SwitchRow(
                        sp,
                        FakeScanCameraSettings.KEY_ENABLE,
                        "模拟相机扫码",
                        "让相册识别二维码按相机扫码来源处理",
                        FakeScanCameraSettings.DEFAULT_ENABLE
                    )
                }
            }
        }
    }
}

@Composable
internal fun RedPacketMiuixPage(
    context: Context,
    provider: FeatureSettingsProvider,
    onBack: () -> Unit
) {
    val sp = remember { HchatStorage.preferences(context, RedPacketSettings.PREFS_NAME) }
    var templates by remember {
        mutableStateOf(
            RedPacketRuleConfig.parseTemplates(sp.getString(RedPacketRuleConfig.KEY_TEMPLATES, "") ?: "")
        )
    }
    var bindings by remember {
        mutableStateOf(
            RedPacketRuleConfig.parseBindings(sp.getString(RedPacketRuleConfig.KEY_BINDINGS, "") ?: "")
        )
    }
    var defaultTemplateId by remember {
        mutableStateOf(sp.getString(RedPacketRuleConfig.KEY_DEFAULT_TEMPLATE_ID, "") ?: "")
    }
    var keywords by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_KEYWORDS, "") ?: "") }
    var keywordMode by remember { mutableStateOf(sp.getInt(RedPacketSettings.KEY_KW_MODE, 0)) }
    var whitelist by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_WHITELIST, "") ?: "") }
    var blacklist by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_BLACKLIST, "") ?: "") }
    var listMode by remember { mutableStateOf(sp.getInt(RedPacketSettings.KEY_MODE, 0)) }
    var wishText by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_WISH_TEXT, "谢谢老板") ?: "谢谢老板") }
    var privateReplySteps by remember { mutableStateOf(loadGlobalRedPacketReplySteps(sp)) }
    var groupReplySteps by remember { mutableStateOf(loadGlobalGroupRedPacketReplySteps(sp)) }
    var notifyText by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "") }
    var notifyToastText by remember {
        mutableStateOf(
            sp.getString(
                RedPacketSettings.KEY_NOTIFY_TOAST_TEXT,
                sp.getString(RedPacketSettings.KEY_NOTIFY_TEXT, "抢到红包 {amount} 元") ?: "抢到红包 {amount} 元"
            ) ?: ""
        )
    }
    var failText by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "") }
    var failToastText by remember {
        mutableStateOf(
            sp.getString(
                RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_TEXT,
                sp.getString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, "未抢到红包") ?: "未抢到红包"
            ) ?: ""
        )
    }
    var announceText by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_ANNOUNCE_TEXT, "抢到红包 {amount} 元") ?: "") }
    var timeFormat by remember {
        mutableStateOf(
            PaymentTemplateTimeFormatter.normalizePattern(
                sp.getString(RedPacketSettings.KEY_TIME_FORMAT, RedPacketSettings.DEFAULT_TIME_FORMAT)
            )
        )
    }
    var wishEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_WISH_ENABLE, false)) }
    var replyEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_REPLY_ENABLE, false)) }
    var notifySystemEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SYSTEM_ENABLE, false)) }
    var notifyToastEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_NOTIFY_TOAST_ENABLE, false)) }
    var notifySoundEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_NOTIFY_SOUND_ENABLE, false)) }
    var announceEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_ANNOUNCE_ENABLE, false)) }
    var notifyFailedSystemEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_SYSTEM_ENABLE, false)) }
    var notifyFailedToastEnabled by remember { mutableStateOf(sp.getBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_ENABLE, false)) }
    var notifySoundUri by remember { mutableStateOf(sp.getString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, "") ?: "") }
    var notifySoundMode by remember {
        mutableStateOf(sp.getInt(
            RedPacketSettings.KEY_NOTIFY_SOUND_MODE,
            RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM
        ))
    }
    var grabDelay by remember { mutableStateOf(sp.getInt(RedPacketSettings.KEY_DELAY_VALUE, 0).coerceAtLeast(0).toString()) }
    var grabRandomMinDelay by remember {
        mutableStateOf(sp.getInt(RedPacketSettings.KEY_DELAY_RANDOM_MIN, 500).coerceAtLeast(0).toString())
    }
    var grabRandomMaxDelay by remember {
        mutableStateOf(sp.getInt(RedPacketSettings.KEY_DELAY_RANDOM_MAX, 3000).coerceAtLeast(0).toString())
    }
    var grabDelayMode by remember {
        mutableStateOf(redPacketDelayModeFromPrefs(sp))
    }
    var picker by remember { mutableStateOf<ContactPickerRequest?>(null) }
    var bindingPicker by remember { mutableStateOf<MessageBlockContactPickerRequest?>(null) }
    var optionPicker by remember { mutableStateOf<OptionPickerRequest?>(null) }
    var templateEditor by remember { mutableStateOf<RedPacketTemplateEditorRequest?>(null) }
    var bindingEditor by remember { mutableStateOf<RedPacketBindingEditorRequest?>(null) }
    var showTemplateManager by remember { mutableStateOf(false) }
    var showListManager by remember { mutableStateOf(false) }
    var editingReplyStepsForGroup by remember { mutableStateOf<Boolean?>(null) }
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    fun persistRules(
        nextTemplates: List<RedPacketRuleTemplate> = templates,
        nextBindings: List<RedPacketRuleBinding> = bindings,
        nextDefaultTemplateId: String = defaultTemplateId
    ) {
        sp.edit()
            .putString(RedPacketRuleConfig.KEY_TEMPLATES, RedPacketRuleConfig.encodeTemplates(nextTemplates))
            .putString(RedPacketRuleConfig.KEY_BINDINGS, RedPacketRuleConfig.encodeBindings(nextBindings))
            .putString(RedPacketRuleConfig.KEY_DEFAULT_TEMPLATE_ID, nextDefaultTemplateId)
            .commit()
    }

    val route = when {
        templateEditor != null -> RedPacketRoute.TemplateEditor(templateEditor!!)
        bindingEditor != null -> RedPacketRoute.BindingEditor(bindingEditor!!)
        bindingPicker != null -> RedPacketRoute.RuleContactPicker(bindingPicker!!)
        picker != null -> RedPacketRoute.ContactPicker(picker!!)
        optionPicker != null -> RedPacketRoute.OptionPicker(optionPicker!!)
        editingReplyStepsForGroup != null -> RedPacketRoute.ReplySteps
        showTemplateManager -> RedPacketRoute.TemplateManager
        showListManager -> RedPacketRoute.ListManager
        else -> RedPacketRoute.Main
    }

    SettingsRouteTransition(
        targetState = route,
        label = "RedPacketRouteTransition",
        depthOf = { it.depth() }
    ) { currentRoute ->
        when (currentRoute) {
            is RedPacketRoute.TemplateEditor -> {
                val request = currentRoute.request
                RedPacketTemplatePage(
                    context = context,
                    request = request,
                    onBack = { templateEditor = null },
                    onSave = { updated ->
                        val next = if (request.index in templates.indices) {
                            templates.toMutableList().also { it[request.index] = updated }
                        } else {
                            templates + updated
                        }
                        val nextDefault = defaultTemplateId
                            .takeIf { id -> next.any { it.id == id } }
                            ?: next.firstOrNull()?.id.orEmpty()
                        templates = next
                        defaultTemplateId = nextDefault
                        persistRules(nextTemplates = next, nextDefaultTemplateId = nextDefault)
                        templateEditor = null
                    },
                    onDelete = {
                        if (request.index in templates.indices) {
                            val deletedId = templates[request.index].id
                            val next = templates.toMutableList().also { it.removeAt(request.index) }
                            val nextBindings = bindings.map { binding ->
                                if (binding.templateId == deletedId) binding.copy(templateId = "") else binding
                            }
                            val nextDefault = defaultTemplateId
                                .takeIf { id -> id != deletedId && next.any { it.id == id } }
                                ?: next.firstOrNull()?.id.orEmpty()
                            templates = next
                            bindings = nextBindings
                            defaultTemplateId = nextDefault
                            persistRules(nextTemplates = next, nextBindings = nextBindings, nextDefaultTemplateId = nextDefault)
                        }
                        templateEditor = null
                    }
                )
            }
            is RedPacketRoute.BindingEditor -> {
                val request = currentRoute.request
                RedPacketBindingPage(
                    context = context,
                    request = request,
                    templates = templates,
                    onBack = { bindingEditor = null },
                    onSave = { updated ->
                        val normalized = normalizedRedPacketBinding(updated)
                        val base = if (request.index in bindings.indices) {
                            bindings.toMutableList().also { it.removeAt(request.index) }
                        } else {
                            bindings
                        }
                        val next = upsertRedPacketBindings(base, listOf(normalized))
                        bindings = next
                        persistRules(nextBindings = next)
                        bindingEditor = null
                    },
                    onDelete = {
                        if (request.index in bindings.indices) {
                            val next = bindings.toMutableList().also { it.removeAt(request.index) }
                            bindings = next
                            persistRules(nextBindings = next)
                        }
                        bindingEditor = null
                    }
                )
            }
            is RedPacketRoute.RuleContactPicker -> {
                val request = currentRoute.request
                MessageBlockContactPickerPage(
                    context = context,
                    request = request,
                    onBack = { bindingPicker = null },
                    onConfirm = { selected ->
                        val additions = selected.map {
                            redPacketBindingFromContact(
                                row = it,
                                templates = templates,
                                existing = findRedPacketBinding(bindings, it.contact.id)
                            )
                        }
                        if (additions.size == 1) {
                            val binding = additions.first()
                            val existingIndex = bindings.indexOfFirst { it.targetId == binding.targetId }
                            bindingEditor = RedPacketBindingEditorRequest(
                                index = existingIndex.takeIf { it >= 0 } ?: bindings.size,
                                binding = binding,
                                canDelete = existingIndex >= 0
                            )
                        } else if (additions.isNotEmpty()) {
                            val next = upsertRedPacketBindings(bindings, additions)
                            bindings = next
                            persistRules(nextBindings = next)
                            Toast.makeText(context, "已添加 ${additions.size} 个适用聊天", Toast.LENGTH_SHORT).show()
                        }
                        bindingPicker = null
                    }
                )
            }
            RedPacketRoute.TemplateManager -> {
                RedPacketTemplateListPage(
                    templates = templates,
                    onBack = { showTemplateManager = false },
                    onOpenTemplate = { index, template ->
                        templateEditor = RedPacketTemplateEditorRequest(index, template, canDelete = true)
                    },
                    onAddTemplate = {
                        templateEditor = RedPacketTemplateEditorRequest(
                            templates.size,
                            newRedPacketTemplate(templates.size + 1, sp),
                            canDelete = false
                        )
                    }
                )
            }
            RedPacketRoute.ListManager -> {
                RedPacketBindingListPage(
                    bindings = bindings,
                    templates = templates,
                    onBack = { showListManager = false },
                    onOpenBinding = { index, binding ->
                        bindingEditor = RedPacketBindingEditorRequest(index, binding, canDelete = true)
                    },
                    onAddBinding = {
                        bindingPicker = MessageBlockContactPickerRequest(
                            title = "选择适用聊天",
                            existingValue = "",
                            onValue = {},
                            allowOfficialAccounts = false
                        )
                    },
                    onDeleteBindings = { targets ->
                        val removeIds = targets.mapTo(HashSet()) { it.id }
                        val next = bindings.filterNot { it.id in removeIds }
                        bindings = next
                        persistRules(nextBindings = next)
                        Toast.makeText(context, "已删除 ${targets.size} 个适用聊天", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            is RedPacketRoute.ContactPicker -> {
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
            is RedPacketRoute.OptionPicker -> {
                val request = currentRoute.request
                OptionPickerPage(
                    request = request,
                    onBack = { optionPicker = null },
                    onSelected = { selected ->
                        request.onSelected(selected)
                    }
                )
            }
            RedPacketRoute.ReplySteps -> {
                val group = editingReplyStepsForGroup == true
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
            }
            RedPacketRoute.Main -> PageScaffold(
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
                    val cleanPrivateReplySteps = cleanRedPacketReplySteps(privateReplySteps)
                    val cleanGroupReplySteps = cleanRedPacketReplySteps(groupReplySteps)
                    val firstReply = cleanPrivateReplySteps.firstOrNull()
                    val firstReplyText = firstReply?.content.orEmpty()
                    val firstReplyMode = firstReply?.mode ?: RedPacketRuleConfig.REPLY_OFF
                    val firstReplyDelayMs = firstReply?.delayMs?.coerceAtLeast(0L) ?: 0L
                    val randomMinDelay = grabRandomMinDelay.toIntOrNull()?.coerceIn(0, 600000) ?: 0
                    val randomMaxDelay = grabRandomMaxDelay.toIntOrNull()?.coerceIn(randomMinDelay, 600000) ?: randomMinDelay
                    sp.edit()
                        .putString(RedPacketSettings.KEY_KEYWORDS, if (keywordMode == 0) "" else keywords)
                        .putString(RedPacketSettings.KEY_WHITELIST, whitelist)
                        .putString(RedPacketSettings.KEY_BLACKLIST, blacklist)
                        .putString(RedPacketSettings.KEY_WISH_TEXT, wishText)
                        .putInt(RedPacketSettings.KEY_REPLY_TYPE, firstReplyMode)
                        .putString(RedPacketSettings.KEY_REPLY_TEMPLATES, if (redPacketReplyUsesText(firstReplyMode)) firstReplyText else "")
                        .putString(RedPacketSettings.KEY_REPLY_TEXT, if (redPacketReplyUsesText(firstReplyMode)) firstTemplate(firstReplyText) else "")
                        .putString(
                            RedPacketSettings.KEY_REPLY_MEDIA_PATHS,
                            if (firstReplyMode != RedPacketRuleConfig.REPLY_OFF && !redPacketReplyUsesText(firstReplyMode)) {
                                firstReplyText.trim()
                            } else {
                                ""
                            }
                        )
                        .putString(RedPacketSettings.KEY_REPLY_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanPrivateReplySteps))
                        .putString(RedPacketSettings.KEY_REPLY_GROUP_ITEMS, RedPacketRuleConfig.encodeReplySteps(cleanGroupReplySteps))
                        .putString(RedPacketSettings.KEY_NOTIFY_TEXT, notifyText)
                        .putString(RedPacketSettings.KEY_NOTIFY_TOAST_TEXT, notifyToastText)
                        .putString(RedPacketSettings.KEY_NOTIFY_FAILED_TEXT, failText)
                        .putString(RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_TEXT, failToastText)
                        .putString(RedPacketSettings.KEY_ANNOUNCE_TEXT, announceText)
                        .putString(RedPacketSettings.KEY_TIME_FORMAT, normalizedTimeFormat)
                        .putString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, notifySoundUri.trim())
                        .putInt(RedPacketSettings.KEY_DELAY_MODE, grabDelayMode)
                        .putInt(
                            RedPacketSettings.KEY_DELAY_VALUE,
                            if (grabDelayMode == RedPacketRuleConfig.DELAY_CUSTOM) {
                                grabDelay.toIntOrNull()?.coerceIn(0, 10000) ?: 0
                            } else {
                                0
                            }
                        )
                        .putInt(RedPacketSettings.KEY_DELAY_RANDOM_MIN, randomMinDelay)
                        .putInt(RedPacketSettings.KEY_DELAY_RANDOM_MAX, randomMaxDelay)
                        .putInt(RedPacketSettings.KEY_REPLY_DELAY_VALUE, (firstReplyDelayMs / 1000L).coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
                        .putInt(RedPacketSettings.KEY_REPLY_DELAY_UNIT, 1)
                        .putBoolean(RedPacketSettings.KEY_REPLY_CUSTOM_ENABLE, firstReplyDelayMs > 0L)
                        .putBoolean(RedPacketSettings.KEY_REPLY_RANDOM, firstReply?.random ?: false)
                        .apply()
                    privateReplySteps = cleanPrivateReplySteps
                    groupReplySteps = cleanGroupReplySteps
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
            item { SmallTitle(text = "核心功能") }
            item {
                SettingsCard {
                    SwitchRow(sp, RedPacketSettings.KEY_ENABLE, "自动抢红包", "开启后自动识别并抢红包", false)
                    InsetDivider()
                    OptionRow(
                        sp = sp,
                        key = RedPacketSettings.KEY_GRAB_MODE,
                        title = "抢包模式",
                        options = optionItems("打开红包页面" to 0, "静默抢包（后台）" to 1),
                        defaultValue = RedPacketSettings.DEFAULT_GRAB_MODE
                    )
                    InsetDivider()
                    SwitchRow(sp, RedPacketSettings.KEY_SKIP_SELF, "跳过自己的红包", "不会抢自己发出的红包", false)
                    InsetDivider()
                    SwitchRow(
                        sp,
                        RedPacketSettings.KEY_BLOCK_NEW_GROUP_ENABLE,
                        "自动屏蔽新进群",
                        "新群自动加入适用聊天，并默认关闭该群抢红包",
                        false
                    )
                    InsetDivider()
                    SwitchRow(sp, RedPacketSettings.KEY_AUTO_CLOSE, "自动关闭页面", "抢完或失败后自动收起红包页", false)
                    InsetDivider()
                    PopupOptionRow(
                        title = "抢包延迟",
                        summary = redPacketDelayModeLabel(grabDelayMode),
                        options = redPacketDelayModeOptions(includeRandom = true),
                        currentValue = grabDelayMode,
                        onValueChanged = { mode ->
                            grabDelayMode = mode
                            if (mode == RedPacketRuleConfig.DELAY_FIXED) grabDelay = "0"
                        }
                    )
                    if (grabDelayMode == RedPacketRuleConfig.DELAY_CUSTOM) {
                        InsetDivider()
                        NumberInputRow("自定义延迟", "单位 ms，保存后生效", grabDelay) { grabDelay = it }
                    } else if (grabDelayMode == RedPacketRuleConfig.DELAY_RANDOM) {
                        InsetDivider()
                        NumberInputRow("最小延迟", "单位 ms，保存后生效", grabRandomMinDelay) { grabRandomMinDelay = it }
                        InsetDivider()
                        NumberInputRow("最大延迟", "单位 ms，不能小于最小延迟", grabRandomMaxDelay) { grabRandomMaxDelay = it }
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "规则管理") }
            item {
                SettingsCard {
                    SelectRow(
                        title = "红包模板",
                        summary = if (templates.isEmpty()) "暂无模板，进入后添加不同群的抢包策略" else "${templates.size} 个模板，进入后新增或修改",
                        onClick = { showTemplateManager = true }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "适用聊天",
                        summary = if (bindings.isEmpty()) "暂无适用聊天，未命中时走默认规则" else "${bindings.size} 个聊天，进入后分配模板",
                        onClick = { showListManager = true }
                    )
                    InsetDivider()
                    SelectRow(
                        title = "默认规则",
                        summary = describeRedPacketDefaultTemplate(defaultTemplateId, templates),
                        onClick = {
                            val options = listOf(
                                OptionItem(
                                    label = "旧版全局设置",
                                    value = -1,
                                    summary = "未命中适用聊天时继续使用下方全局设置"
                                )
                            ) + templates.mapIndexed { index, template ->
                                OptionItem(
                                    label = template.name.ifBlank { "模板 ${index + 1}" },
                                    value = index,
                                    summary = describeRedPacketTemplate(template)
                                )
                            }
                            val current = templates.indexOfFirst { it.id == defaultTemplateId }
                            optionPicker = OptionPickerRequest(
                                title = "默认规则",
                                options = options,
                                currentValue = current.takeIf { it >= 0 } ?: -1,
                                onSelected = { selected ->
                                    val nextDefault = if (selected.value in templates.indices) {
                                        templates[selected.value].id
                                    } else {
                                        ""
                                    }
                                    defaultTemplateId = nextDefault
                                    persistRules(nextDefaultTemplateId = nextDefault)
                                    optionPicker = null
                                }
                            )
                        }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "过滤规则") }
            item {
                SettingsCard {
                    OptionRow(
                        sp = sp,
                        key = RedPacketSettings.KEY_MODE,
                        title = "过滤模式",
                        options = optionItems("全部抢" to 0, "白名单（只抢指定人）" to 1, "黑名单（不抢指定人）" to 2),
                        defaultValue = 0,
                        onValueChanged = { listMode = it }
                    )
                    InsetDivider()
                    OptionRow(
                        sp = sp,
                        key = RedPacketSettings.KEY_KW_MODE,
                        title = "关键词过滤",
                        options = optionItems("无限制" to 0, "只抢含关键词的" to 1, "屏蔽含关键词的" to 2),
                        defaultValue = 0,
                        onValueChanged = {
                            keywordMode = it
                            if (it == 0) keywords = ""
                        }
                    )
                    if (keywordMode != 0) {
                        InsetDivider()
                        InputRow("关键词列表", "多个关键词用 | 分隔", keywords) { keywords = it }
                    }
                    if (listMode == 1) {
                        InsetDivider()
                        ActionRow("白名单", autoReplySelectedIdSummary(whitelist)) {
                            picker = ContactPickerRequest(
                                title = "选择白名单",
                                mode = ContactPickerMode.BOTH,
                                multiSelect = true,
                                existingValue = whitelist,
                                onValue = { whitelist = it },
                                enableLabels = true
                            )
                        }
                    } else if (listMode == 2) {
                        InsetDivider()
                        ActionRow("黑名单", autoReplySelectedIdSummary(blacklist)) {
                            picker = ContactPickerRequest(
                                title = "选择黑名单",
                                mode = ContactPickerMode.BOTH,
                                multiSelect = true,
                                existingValue = blacklist,
                                onValue = { blacklist = it },
                                enableLabels = true
                            )
                        }
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
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "自动祝福") }
            item {
                SettingsCard {
                    SwitchRow(wishEnabled, "自动发送祝福语", "抢到红包后发送祝福语") {
                        wishEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_WISH_ENABLE, it).apply()
                    }
                    if (wishEnabled) {
                        InsetDivider()
                        InputRow("祝福语内容", "抢到红包后发送的文字", wishText) { wishText = it }
                        InsetDivider()
                        SwitchRow(sp, RedPacketSettings.KEY_WISH_RANDOM, "随机祝福语", "从模板中随机选择", false)
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "自动回复") }
            item {
                SettingsCard {
                    SwitchRow(replyEnabled, "抢到后自动回复", "发送到红包所在会话") {
                        replyEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_REPLY_ENABLE, it).apply()
                    }
                    if (replyEnabled) {
                        InsetDivider()
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
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "通知提醒") }
            item {
                SettingsCard {
                    SwitchRow(notifySystemEnabled, "通知栏提醒", "抢到红包时提醒") {
                        notifySystemEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_NOTIFY_SYSTEM_ENABLE, it).apply()
                    }
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
                    SwitchRow(notifyToastEnabled, "浮窗提醒", "抢到红包时短暂提示") {
                        notifyToastEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_NOTIFY_TOAST_ENABLE, it).apply()
                    }
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
                    SwitchRow(notifySoundEnabled, "通知铃声", "开启后播放通知铃声") {
                        notifySoundEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_NOTIFY_SOUND_ENABLE, it).apply()
                    }
                    if (notifySoundEnabled) {
                        InsetDivider()
                        OptionRow(
                            sp = sp,
                            key = RedPacketSettings.KEY_NOTIFY_SOUND_MODE,
                            title = "铃声模式",
                            options = optionItems(
                                "选择系统铃声" to RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM,
                                "从文件选择铃声" to RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM
                            ),
                            defaultValue = RedPacketSettings.NOTIFY_SOUND_MODE_SYSTEM,
                            onValueChanged = { mode ->
                                notifySoundMode = mode
                                notifySoundUri = ""
                                sp.edit()
                                    .putInt(RedPacketSettings.KEY_NOTIFY_SOUND_MODE, mode)
                                    .putString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, "")
                                    .apply()
                                optionPicker = null
                                val activity = context as? Activity
                                if (activity == null) {
                                    Toast.makeText(context, "当前页面无法打开铃声选择器", Toast.LENGTH_SHORT).show()
                                } else {
                                    val onPicked: (String) -> Unit = { uri ->
                                        if (uri.isNotBlank()) {
                                            val storedUri = if (mode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                                CustomNotificationRuntime.freezeRingtoneUri(context, uri)
                                            } else {
                                                uri
                                            }
                                            notifySoundUri = storedUri
                                            sp.edit()
                                                .putInt(RedPacketSettings.KEY_NOTIFY_SOUND_MODE, mode)
                                                .putString(RedPacketSettings.KEY_NOTIFY_SOUND_URI, storedUri)
                                                .apply()
                                        }
                                    }
                                    if (mode == RedPacketSettings.NOTIFY_SOUND_MODE_CUSTOM) {
                                        RingtonePickerBridge.launchFile(activity, onPicked)
                                    } else {
                                        RingtonePickerBridge.launchSystem(activity, notifySoundUri, onPicked)
                                    }
                                }
                            }
                        )
                        InsetDivider()
                        InfoRow("当前铃声", ringtoneDisplayName(context, notifySoundUri, notifySoundMode))
                    }
                    InsetDivider()
                    SwitchRow(sp, RedPacketSettings.KEY_NOTIFY_VIBRATE_ENABLE, "通知震动", "开启后触发通知震动", false)
                    InsetDivider()
                    SwitchRow(announceEnabled, "抢到红包播报", "抢到后用系统语音播报") {
                        announceEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_ANNOUNCE_ENABLE, it).apply()
                    }
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
                    SwitchRow(notifyFailedSystemEnabled, "未抢到提醒", "未抢到红包时通知") {
                        notifyFailedSystemEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_SYSTEM_ENABLE, it).apply()
                    }
                    if (notifyFailedSystemEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "未抢到通知栏文案",
                            summary = "支持下方变量",
                            value = failText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { failText = it }
                        )
                    }
                    InsetDivider()
                    SwitchRow(notifyFailedToastEnabled, "未抢到浮窗", "未抢到红包时短暂提示") {
                        notifyFailedToastEnabled = it
                        sp.edit().putBoolean(RedPacketSettings.KEY_NOTIFY_FAILED_TOAST_ENABLE, it).apply()
                    }
                    if (notifyFailedToastEnabled) {
                        InsetDivider()
                        VariableInputRow(
                            title = "未抢到浮窗文案",
                            summary = "支持下方变量",
                            value = failToastText,
                            variables = redPacketTemplateVariables,
                            onValueChange = { failToastText = it }
                        )
                    }
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = "假红包兼容") }
            item {
                SettingsCard {
                    SwitchRow(sp, RedPacketSettings.KEY_FAKE_PACKET_ENABLE, "开启假红包", "发送假红包时修正请求和响应", false)
                    InsetDivider()
                    SwitchRow(sp, RedPacketSettings.KEY_FAKE_PACKET_RECEIVE_ENABLE, "领取假红包", "收到异常群 ID 红包时尝试修正", false)
                }
            }
            item { StatsCard(sp) }
        }
    }
        }
    }
}

@Composable
internal fun RedPacketTemplateListPage(
    templates: List<RedPacketRuleTemplate>,
    onBack: () -> Unit,
    onOpenTemplate: (Int, RedPacketRuleTemplate) -> Unit,
    onAddTemplate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = MiuixScrollBehavior()
    PageScaffold(
        title = "红包模板",
        largeTitle = "红包模板",
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
                        EmptyText("暂无模板。模板只配置抢包策略，适用聊天在“适用聊天”里分配。")
                    } else {
                        templates.forEachIndexed { index, template ->
                            SelectRow(
                                title = template.name.ifBlank { "模板 ${index + 1}" },
                                summary = describeRedPacketTemplate(template),
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
internal fun RedPacketBindingListPage(
    bindings: List<RedPacketRuleBinding>,
    templates: List<RedPacketRuleTemplate>,
    onBack: () -> Unit,
    onOpenBinding: (Int, RedPacketRuleBinding) -> Unit,
    onAddBinding: () -> Unit,
    onDeleteBindings: (List<RedPacketRuleBinding>) -> Unit
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
                (lower.isEmpty() || redPacketBindingMatchesQuery(binding, templates, lower))
        }
    val listTitle = when {
        bindings.isEmpty() -> "适用聊天"
        lower.isEmpty() -> "适用聊天 · ${bindings.size} 项"
        else -> "适用聊天 · ${visibleBindings.size}/${bindings.size} 项"
    }
    val visibleIds = visibleBindings.mapTo(LinkedHashSet()) { it.second.id }
    val allVisibleSelected = visibleIds.isNotEmpty() && visibleIds.all { it in selectedIds }
    val selectedBindings = bindings.filter { it.id in selectedIds }
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
                    onPrimaryClick = onAddBinding,
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
                        onSelected = { category = it },
                        includeOfficial = false
                    )
                }
            }
            item {
                SettingsCard {
                    InputRow(
                        title = "搜索聊天",
                        summary = "昵称 / wxid / 群号 / 模板名",
                        value = query,
                        onValueChange = { query = it }
                    )
                }
            }
            item { SmallTitle(modifier = Modifier.padding(top = 10.dp), text = listTitle) }
            item {
                SettingsCard {
                    if (bindings.isEmpty()) {
                        EmptyText("暂无适用聊天。添加群聊或好友后，再给它分配红包模板。")
                    } else if (visibleBindings.isEmpty()) {
                        EmptyText("没有匹配结果。可按昵称、ID 或模板名搜索。")
                    } else {
                        visibleBindings.forEachIndexed { rowIndex, (index, binding) ->
                            if (batchDeleteMode) {
                                OptionChoiceRow(
                                    item = OptionItem(
                                        label = binding.label.ifBlank { binding.targetId },
                                        value = index,
                                        summary = describeRedPacketBinding(binding, templates)
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
                                    summary = describeRedPacketBinding(binding, templates),
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
    if (showDeleteConfirm) {
        WindowDialog(
            show = true,
            title = "确认批量删除",
            onDismissRequest = { showDeleteConfirm = false },
            content = {
                Column {
                    Text(
                        text = buildString {
                            append("将删除已选的 ${selectedBindings.size} 个适用聊天，此操作不可撤销。")
                            val names = selectedBindings.take(6).joinToString("、") {
                                it.label.ifBlank { it.targetId }
                            }
                            if (names.isNotBlank()) append("\n\n$names")
                            if (selectedBindings.size > 6) append(" 等")
                        },
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
                                val targets = selectedBindings
                                showDeleteConfirm = false
                                batchDeleteMode = false
                                selectedIds = emptySet()
                                onDeleteBindings(targets)
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

