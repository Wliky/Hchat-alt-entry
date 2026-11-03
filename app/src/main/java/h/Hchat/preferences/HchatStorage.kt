package h.Hchat.preferences

import android.content.Context
import android.content.SharedPreferences
import h.Hchat.utils.HLog
import io.fastkv.FastKV
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object HchatStorage {
    private const val TAG = "[Hchat:Storage]"
    private const val DIR_NAME = "Hchat"
    private const val FILES_DIR_NAME = "files"
    private const val PREFS_DIR_NAME = "shared_prefs"
    private val cache = ConcurrentHashMap<String, SharedPreferences>()
    private val registeredNames = java.util.Collections.newSetFromMap(ConcurrentHashMap<String, Boolean>())
    private val legacyMigrated = java.util.concurrent.atomic.AtomicBoolean(false)

    @JvmStatic
    fun preferences(context: Context, name: String): SharedPreferences {
        val appContext = context.applicationContext ?: context
        registeredNames.add(name)
        val cacheKey = cacheKey(appContext, name)
        cache[cacheKey]?.let { return it }

        val created = create(appContext, name)
        val existing = cache.putIfAbsent(cacheKey, created)
        return existing ?: created
    }

    @JvmStatic
    fun reopenIfFilesMissing(context: Context, name: String): SharedPreferences {
        val appContext = context.applicationContext ?: context
        val dir = storageDir(appContext)
        if (hasBackingFile(dir, name)) return preferences(appContext, name)

        val cacheKey = cacheKey(appContext, name)
        synchronized(cache) {
            if (hasBackingFile(dir, name)) return preferences(appContext, name)
            val stale = cache.remove(cacheKey)
            if (stale is FastKV) {
                runCatching { stale.close() }
                    .onFailure { HLog.e("$TAG 关闭失效配置失败: $name ${it.message}", it) }
            }
            return create(appContext, name).also { cache[cacheKey] = it }
        }
    }

    private fun create(context: Context, name: String): SharedPreferences {
        val dir = storageDir(context)
        return FastKV.Builder(dir.absolutePath, name).build()
    }

    private fun cacheKey(context: Context, name: String): String = "${context.packageName}:$name"

    private fun hasBackingFile(dir: File, name: String): Boolean {
        return FAST_KV_SUFFIXES.any { suffix -> File(dir, name + suffix).isFile }
    }

    @JvmStatic
    fun storageDir(context: Context): File {
        val base = try {
            context.dataDir
        } catch (_: Throwable) {
            context.filesDir.parentFile ?: context.filesDir
        }
        // 放进 shared_prefs：微信"清理缓存-其他数据"只扫 files/、cache/ 等目录，
        // shared_prefs 是系统 SharedPreferences 目录，清理不会碰这里。
        val dir = File(File(base, PREFS_DIR_NAME), DIR_NAME)
        migrateLegacyDirs(base, dir)
        if (!dir.isDirectory && !dir.mkdirs()) {
            HLog.e("$TAG 创建目录失败: ${dir.absolutePath}")
        }
        return dir
    }

    /** 兼容两代旧路径：<dataDir>/files/Hchat、<dataDir>/Hchat，首次启动搬到 shared_prefs。 */
    private fun migrateLegacyDirs(base: File, target: File) {
        if (!legacyMigrated.compareAndSet(false, true)) return
        migrateLegacyDir(File(File(base, FILES_DIR_NAME), DIR_NAME), target)
        migrateLegacyDir(File(base, DIR_NAME), target)
    }

    private fun migrateLegacyDir(legacy: File, target: File) {
        if (!legacy.isDirectory) return
        runCatching {
            if (!target.isDirectory && !target.mkdirs()) return
            legacy.listFiles()?.forEach { file ->
                if (!file.isFile) return@forEach
                val dest = File(target, file.name)
                if (!dest.exists()) file.copyTo(dest, overwrite = false)
            }
            // 复制完就把旧目录删掉：配置只留 shared_prefs/Hchat 一份，避免清理残留。
            val removed = legacy.deleteRecursively()
            HLog.i("$TAG 配置目录已迁移到 ${target.absolutePath}，旧目录清理=$removed")
        }.onFailure { HLog.e("$TAG 迁移配置目录失败: ${it.message}", it) }
    }

    /** 本次进程访问过的配置名。 */
    @JvmStatic
    fun knownNames(): List<String> = registeredNames.toList()

    /**
     * 存储目录里实际存在的配置名。
     *
     * 备份不能只依赖硬编码清单：功能一直在加，漏一个就等于用户的配置没被带走。
     * 这里按 FastKV 的数据文件反推名字，未在本次启动访问过的功能也能覆盖。
     */
    @JvmStatic
    fun storageNames(context: Context): List<String> {
        val files = runCatching { storageDir(context).listFiles() }.getOrNull() ?: return emptyList()
        return files.mapNotNull { file ->
            val suffix = FAST_KV_SUFFIXES.firstOrNull { file.name.endsWith(it) } ?: return@mapNotNull null
            file.name.removeSuffix(suffix).takeIf { it.isNotBlank() }
        }.distinct()
    }

    private val FAST_KV_SUFFIXES = arrayOf(".kva", ".kvb", ".kvc")
}
