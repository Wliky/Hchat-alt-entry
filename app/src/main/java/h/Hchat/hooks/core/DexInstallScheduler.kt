package h.Hchat.hooks.core

import android.os.Handler
import android.os.Looper
import h.Hchat.utils.HLog
import java.util.concurrent.Callable
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/** 统一串行安装、有限重试；卸载后旧任务不能重新安装或保留等待中的闭包。 */
object DexInstallScheduler {
    enum class Stage(internal val level: Int) { EARLY(0), BRIDGE(1), WARMUP(2) }

    private const val TAG = "[Hchat:DexInstall]"
    private const val MAX_RETRY_ATTEMPTS = 6
    private const val MAX_RETRY_DELAY_MS = 60_000L
    private val mainHandler = Handler(Looper.getMainLooper())
    private val states = ConcurrentHashMap<String, State>()
    private val registeredLabels = ConcurrentHashMap<String, String>()
    private val summaryScheduled = AtomicBoolean(false)

    @Volatile
    private var locateStateDumper: Runnable? = null

    /** 由启动入口注入：汇总时顺带输出 DexFinder 各定位字段的就位情况。 */
    @JvmStatic
    fun setLocateStateDumper(dumper: Runnable) {
        locateStateDumper = dumper
    }
    private val readyLevel = AtomicInteger(0)
    private val stateLock = Any()
    private val dexKitExecutionLock = Any()
    private val executor = ThreadPoolExecutor(
        1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>(),
        { runnable -> Thread(runnable, "Hchat-DexInstall").apply { isDaemon = true } }
    )

    fun schedule(
        key: String,
        label: String = key,
        stage: Stage = Stage.BRIDGE,
        priority: Int = 0,
        installer: () -> Boolean
    ) = synchronized(stateLock) {
        if (key.isBlank()) return@synchronized
        registeredLabels[key] = label
        val registry = HookRegistry.get()
        val generation = registry.installationGeneration()
        if (!registry.isInstallationGenerationActive(generation)) return@synchronized
        val previous = states[key]
        val state = if (previous == null || previous.generation != generation) {
            previous?.let(::cancelState)
            State(label, generation).also { states[key] = it }
        } else previous
        if (state.installed.get()) return@synchronized
        state.label = label
        state.stage = stage
        state.priority = priority
        state.installer = installer
        if (!isReady(stage)) return@synchronized
        if (!state.running.get() && !state.retryScheduled.get() &&
            state.retryIndex.get() >= MAX_RETRY_ATTEMPTS
        ) state.retryIndex.set(0)
        runNow(key, state)
    }

    @JvmStatic
    fun scheduleTask(key: String, label: String, stage: Stage, priority: Int, installer: Callable<Boolean>) {
        schedule(key, label, stage, priority) { installer.call() == true }
    }

    @JvmStatic fun markDexBridgeReady() { advanceReady(Stage.BRIDGE) }
    @JvmStatic fun markDexReady() { markDexWarmupReady() }
    @JvmStatic fun markDexWarmupReady() {
        advanceReady(Stage.WARMUP)
        scheduleSummaryDump()
    }

    /** WARMUP 队列串行执行 DexKit 查询，全部跑完要几分钟，分两次落汇总。 */
    private fun scheduleSummaryDump() {
        if (!summaryScheduled.compareAndSet(false, true)) return
        mainHandler.postDelayed({ dumpSummaryAsync(full = true) }, 120_000L)
        mainHandler.postDelayed({ dumpSummaryAsync(full = false) }, 300_000L)
    }

    /**
     * 汇总要跑一遍字段反射并写几十行 LSPosed 日志（跨进程），不能在主线程做，
     * 否则冷启动两分钟、五分钟各卡一次 UI。
     */
    private fun dumpSummaryAsync(full: Boolean) {
        Thread({ runCatching { dumpSummary(full) } }, "Hchat-DexSummary").apply { isDaemon = true }.start()
    }

    @JvmStatic
    fun runDexKitTask(task: Runnable) {
        val registry = HookRegistry.get()
        val generation = registry.installationGeneration()
        synchronized(dexKitExecutionLock) {
            registry.withInstallationGeneration(generation) { task.run() }
        }
    }

    private fun advanceReady(stage: Stage) {
        while (true) {
            val current = readyLevel.get()
            if (current >= stage.level) break
            if (readyLevel.compareAndSet(current, stage.level)) break
        }
        runReadyStates()
    }

    fun markInstalled(key: String) = synchronized(stateLock) {
        val registry = HookRegistry.get()
        val generation = registry.installationGeneration()
        if (!registry.isInstallationGenerationActive(generation)) return@synchronized
        val previous = states[key]
        val state = if (previous == null || previous.generation != generation) {
            previous?.let(::cancelState)
            State(key, generation).also { states[key] = it }
        } else previous
        cancelState(state)
        state.installed.set(true)
    }

    /** 不等待无法中断的 DexKit 调用；它返回后由旧代次租约拒绝后续挂钩。 */
    fun cancelAll() = synchronized(stateLock) {
        val previous = states.values.toList()
        states.clear()
        previous.forEach(::cancelState)
        // 只取消请求，现有 DexKit 的就绪阶段不随请求清理而倒退。
    }

    private fun cancelState(state: State) {
        state.installer = null
        state.retryTask?.let(mainHandler::removeCallbacks)
        state.retryTask = null
        state.retryScheduled.set(false)
        state.future?.let {
            it.cancel(false)
            executor.remove(it)
        }
        state.future = null
    }

    private fun isCurrent(key: String, state: State): Boolean =
        states[key] === state && HookRegistry.get().isInstallationGenerationActive(state.generation)

    private fun runNow(key: String, state: State) = synchronized(stateLock) {
        if (!isCurrent(key, state) || !isReady(state.stage) || state.installed.get() || state.installer == null) {
            return@synchronized
        }
        if (!state.running.compareAndSet(false, true)) return@synchronized
        val future = FutureTask<Unit> { runInstaller(key, state) }
        state.future = future
        try {
            executor.execute(future)
        } catch (error: Throwable) {
            state.future = null
            state.running.set(false)
            throw error
        }
    }

    private fun runInstaller(key: String, state: State) {
        var ok = false
        try {
            synchronized(dexKitExecutionLock) {
                val installer = synchronized(stateLock) {
                    if (isCurrent(key, state) && !state.installed.get()) state.installer else null
                } ?: return
                ok = HookRegistry.get().withInstallationGeneration(state.generation) { installer() }
            }
        } catch (_: CancellationException) {
            // 卸载与正在执行的定位竞争时，旧租约失效属于正常取消。
        } catch (error: Throwable) {
            state.lastError = describe(error)
            HLog.e("$TAG ${state.label} 安装异常: ${error.message}", error)
        } finally {
            synchronized(stateLock) {
                state.future = null
                state.running.set(false)
                if (states[key] === state) {
                    if (!isCurrent(key, state)) {
                        states.remove(key, state)
                        cancelState(state)
                    } else if (ok || state.installed.get()) {
                        state.installed.set(true)
                        state.installer = null
                        state.retryTask?.let(mainHandler::removeCallbacks)
                        state.retryTask = null
                        state.retryScheduled.set(false)
                    } else scheduleRetry(key, state)
                }
            }
        }
    }

    private fun runReadyStates() = synchronized(stateLock) {
        states.entries.sortedWith(
            compareBy<Map.Entry<String, State>> { it.value.stage.level }
                .thenBy { it.value.priority }.thenBy { it.key }
        ).forEach { (key, state) -> runNow(key, state) }
    }

    private fun scheduleRetry(key: String, state: State) {
        if (!isCurrent(key, state) || state.installed.get()) return
        if (!state.retryScheduled.compareAndSet(false, true)) return
        val index = state.retryIndex.getAndIncrement()
        if (index >= MAX_RETRY_ATTEMPTS) {
            state.retryScheduled.set(false)
            state.installer = null
            if (state.lastError.isEmpty()) state.lastError = "多次重试仍未安装成功"
            HLog.e("$TAG ${state.label} 多次安装失败，停止本轮重试")
            return
        }
        val retry = Runnable {
            synchronized(stateLock) {
                if (!isCurrent(key, state)) return@synchronized
                state.retryTask = null
                state.retryScheduled.set(false)
                runNow(key, state)
            }
        }
        state.retryTask = retry
        mainHandler.postDelayed(retry, retryDelayMs(index))
    }

    private fun retryDelayMs(attempt: Int): Long {
        if (attempt <= 0) return 1_000L
        if (attempt == 1) return 3_000L
        return minOf(MAX_RETRY_DELAY_MS, 3_000L * (1L shl (attempt - 1)))
    }

    private fun isReady(stage: Stage): Boolean = readyLevel.get() >= stage.level

    /**
     * 本轮所有已注册功能点的结果落日志：抓一份日志就能看出哪几项失败，
     * 再对照各功能自己打的诊断行定位原因，不用逐项复现。
     * full=false 时只输出失败和未入队项，避免第二次汇总把几十行 OK 重复写一遍。
     */
    @JvmStatic
    @JvmOverloads
    fun dumpSummary(full: Boolean = true) {
        if (full) runCatching { locateStateDumper?.run() }
        val snap = snapshot()
        val installed = snap.filter { it.installed }
        val failed = snap.filter { !it.installed }
        val queued = snap.map { it.key }.toSet()
        val pending = registeredLabels.filterKeys { it !in queued }
        HLog.e("$TAG ===== 安装汇总 成功=${installed.size} 失败=${failed.size} 未入队=${pending.size} =====")
        failed.sortedBy { it.label }.forEach {
            HLog.e("$TAG FAIL ${it.label} retry=${it.retryIndex} err=${it.error.ifEmpty { "-" }}")
        }
        if (pending.isNotEmpty()) {
            HLog.e("$TAG 未入队: " + pending.values.sorted().joinToString("、"))
        }
        // 安装成功属于信息级：默认错误级下不再输出 60+ 行 OK，
        // 需要逐项确认时把日志级别调到"信息"即可看到。
        if (full) installed.sortedBy { it.label }.forEach { HLog.i("$TAG OK ${it.label}") }
    }

    /** 供「模块排查」页面读取的安装队列快照。 */
    fun snapshot(): List<ModuleStatusStore.InstallStatus> = synchronized(stateLock) {
        states.entries
            .sortedWith(compareBy<Map.Entry<String, State>> { it.value.stage.level }.thenBy { it.value.label })
            .map { (key, state) ->
                ModuleStatusStore.InstallStatus(
                    key = key,
                    label = state.label,
                    installed = state.installed.get(),
                    retryIndex = state.retryIndex.get(),
                    error = state.lastError
                )
            }
    }

    private fun describe(error: Throwable): String {
        val message = error.message.orEmpty().trim()
        return if (message.isEmpty()) error.javaClass.simpleName else message
    }

    private class State(@Volatile var label: String, val generation: Long) {
        @Volatile var stage: Stage = Stage.BRIDGE
        @Volatile var priority: Int = 0
        @Volatile var installer: (() -> Boolean)? = null
        @Volatile var lastError: String = ""
        var future: FutureTask<Unit>? = null
        var retryTask: Runnable? = null
        val installed = AtomicBoolean(false)
        val running = AtomicBoolean(false)
        val retryScheduled = AtomicBoolean(false)
        val retryIndex = AtomicInteger(0)
    }
}
