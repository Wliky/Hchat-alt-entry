# Hchat 开发变更记录

只记改了什么、改了哪里，不记分析过程。每次开工前先读这里。

## 2026-11-04

### 第一批：模块排查准确性

- 平板模式 / 屏蔽热更新：hook 装在 `ModuleEntry` 早期入口，不在 `FeatureManager` 的 featureTag 范围内，加 `requiresRuntimeBinding() = false`，不再误报「未注册任何 hook」
  - `app/src/main/java/h/Hchat/hooks/items/tablet/WeChatTabletFeature.kt`
  - `app/src/main/java/h/Hchat/hooks/items/hotupdate/DisableHotUpdateFeature.kt`
- 定位缺失分级：`sendVideoMethod` 与 `sendVideoTaskClass` 同属可选路径（8.0.80 起静默视频改走直发回退），任一缺失不再计入必需缺失；`DexFinder` 新增 `optionalMissingLocateFields()`，排查页拆成「定位缺失（必需）」「定位缺失（可选）」两栏
  - `app/src/main/java/h/Hchat/dexkit/DexFinder.java`
  - `app/src/main/java/h/Hchat/hooks/core/ModuleStatusStore.kt`
  - `app/src/main/java/h/Hchat/hooks/core/FeatureManager.kt`
  - `app/src/main/java/h/Hchat/ui/miuix/ModuleDiagnosticsMiuixPage.kt`
- 排查页新增说明：命中记录仅本次进程有效，重启微信后清零（摘要卡片 + 复制报告标题）
- 核查结论：`audio_transform`、`group_chat_label`、`quick_terminate` 已有打点，无需补
  - `audio_transform`：转换并落盘成功后打点（`MiuixSettingsPagePart7.kt:1306`）
  - `group_chat_label`：标签写入成功后打点（`MiuixSettingsPagePart6.kt:478`）
  - `quick_terminate`：菜单点击处 `FeatureTrigger.mark(...)`（`SettingsInjector.java:910、957`）

### 第二批：功能修复

- 资料 ID：好友资料页补「渲染后直接绑 View」兜底，与群聊页共用 `bindRenderedRow()`；原先只绑 Preference 点击回调，部分版本点不动
  - `app/src/main/java/h/Hchat/hooks/items/profileid/ProfileIdFeature.kt`
- 通话消息显示时间：移除 `50/52/53/1000052/1000053` 的排除，语音/视频通话行也显示时间；锚点取不到时仍返回 null 安全跳过
  - `app/src/main/java/h/Hchat/hooks/items/hchatextra/MessageDetailsVisibility.kt`
- 间距与字号：四项间距并入「布局」卡片，范围 -6～12dp，左右默认 8、上下默认 0；字号 8–16sp；旧值不迁移（存过 0 就是 0）
  - `HchatExtraSettings.kt`：新增水平/垂直默认值与范围常量，删除原 `DEFAULT_MESSAGE_DETAILS_MARGIN`
  - `HchatExtraFeature.kt`：读取时按 -6～12 / 8–16 收敛
  - `MiuixSettingsPagePart3.kt`：UI 合并为「布局」一张卡，默认值、保存范围同步
  - `MiuixSettingsPagePart11.kt`：`NumberInputRow` 增加 `allowNegative`，支持输入负号

### 第三批：性能与卡顿

改动集中「消息显示时间」每条消息 bind 的热路径，目标是减少主线程反射、遍历和重复布局。

- 消息 bind 复用捕获结果：`bindBottomMessageDetails` 新增 `capturedRoot`，绑定前已解析的 holder / 行根容器直接传入，不再为每条消息重复 `findRootView`（原路径每条 2～4 次反射）
  - `app/src/main/java/h/Hchat/hooks/items/hchatextra/HchatExtraFeature.kt`
- 行根容器字段缓存：`findRootView` 首条消息付一次按名查字段的代价，之后直接 `Field.get`；新增 `holderRootMissing` 负缓存，不再反复扫描找不到的类
  - `HchatExtraFeature.kt`
- 清理标签不再全表扫描：`removeMessageDetailsFromRoot` 先在行根子树按 tag 找标签，找不到才回退绑定表（原实现每条消息遍历整张 WeakHashMap 并分配中间集合）
  - `HchatExtraFeature.kt`
- 复用 attach 监听器：`MessageDetailsAttachQueue.observe` 对同一行只换回调，不再每条消息 remove/add 一次监听器（View 内部会复制监听器数组）
  - `app/src/main/java/h/Hchat/hooks/items/hchatextra/MessageDetailsPreDrawQueue.kt`
- 跳过无变化的布局重设：时间标签按「锚点 + 间距 + 字号 + scaledDensity」算签名，命中时不再 `setLayoutParams` 与同步 `measure`（每次都会向整行请求重新布局，是滚动卡顿的主要来源）
  - `HchatExtraFeature.kt`
- 红包时间格式化缓存 `DateTimeFormatter`：原来每次 `ofPattern` 都重新解析 pattern，红包记录列表滚动时反复付这个成本
  - `HchatExtraFeature.kt`
- 分段计时：新增 `timed()`，热点列表以 `hchat_extra#消息详情捕获`、`#消息详情绑定`、`#红包时间格式化` 分开显示；子项不走 1/64 采样，低频项也有样本；热点条数 8 → 12
  - `HchatExtraFeature.kt`、`app/src/main/java/h/Hchat/ui/miuix/ModuleDiagnosticsMiuixPage.kt`

内存检查结论：`messageDetailsBindings`、`messageDetailsReservedPaddings`、`ForwardedRecordTypeLabels` 均已用 WeakHashMap / WeakReference，且每行 rebind 前显式清理，未发现无界增长；`timeFormatterCache` 只有 3 个 pattern，`holderRootFieldCache` / `holderRootMissing` 以 holder 类为 key，都是有界集合。

### 第四批：排查状态持久化与菜单自检

- 命中记录按微信版本落盘：`ModuleStatusStore` 新增 `attach(context, versionName)`，启动时从 `HchatStorage` 的 `Hchat_module_status` 回灌 `triggered:<版本>`，首次命中写盘，换版本删除旧 key。修复"用过的功能重启微信后又退回待验证"
  - `app/src/main/java/h/Hchat/hooks/core/ModuleStatusStore.kt`
  - `app/src/main/java/h/Hchat/hooks/core/FeatureManager.kt`
  - `app/src/main/java/h/Hchat/ui/miuix/ModuleDiagnosticsMiuixPage.kt`（摘要卡片与报告标题文案）
- 三个菜单类功能补 `healthCheck`，安装失败不再只能停在"待验证"：
  - `hide_chat_menu`：区分「创建方法未定位到」与「候选方法全部安装失败」
  - `quick_moments`：区分「创建方法未定位到」与「Hook 失败」
  - `quick_contact_edit`：会话菜单、朋友圈头像菜单分别记录，同时失败时合并展示
  - `HideChatMenuFeature.kt`、`QuickMomentsFeature.kt`、`QuickContactEditFeature.kt`
- 静默发视频定位修复：
  - `DexFinder.resolveSendVideoApi()` 改为返回 `boolean`，补进 `resolveAll()` 序列；缓存命中分支发现 `sendVideoMethod` 为空时补解析并回写，修复老缓存永远为 null
  - `WeChatVideoApi.hasDirectSendEntry()` 放宽 `int` / `Integer`，与 `isSendVideoMethod()` 一致
  - 8.0.80 已删除 `CopyVideoTask` 及其日志锚点，`sendVideoTaskClass` 保持可选缺失，走直发回退
  - `app/src/main/java/h/Hchat/dexkit/DexFinder.java`、`app/src/main/java/h/Hchat/hooks/api/media/WeChatVideoApi.java`

- 修复 `NumberInputRow` 编译失败：`allowNegative` 原本排在 `onValueChange` 之后，尾随 lambda 被当成 `Boolean` 参数，63 处 `NumberInputRow(a, b, c) { ... }` 全部报 `No value passed for parameter 'onValueChange'`。把 `allowNegative` 前移，`onValueChange` 保持末位
  - `app/src/main/java/h/Hchat/ui/miuix/MiuixSettingsPagePart11.kt`
  - 教训：带默认值的非函数参数不能放在函数参数之后，否则破坏所有尾随 lambda 调用点；这类改动本地 lint 查不出来，只能靠整包编译

### 待做

- 收成 8 组 + 规范命名与描述
