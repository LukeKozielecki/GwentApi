package luke.koz.gwentapi.data.remote.utils

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.request.crossfade
import java.io.File

object PersistentImageLoader {
    fun create(context: Context): ImageLoader {
        val cacheDir = File(context.filesDir, "card_artRes_low")

        val diskCache = DiskCache.Builder()
            .directory(cacheDir)
            .maxSizeBytes(100L * 1024 * 1024) // 100MB
            .build()

        return ImageLoader.Builder(context)
            .diskCache(diskCache)
            .crossfade(true)
            .build()
    }
}