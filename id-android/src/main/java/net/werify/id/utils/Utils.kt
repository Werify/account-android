package net.werify.id.utils

import android.content.Context
import okhttp3.Cache
import java.io.File


object Utils {
    private fun getDiskCacheDir(context: Context, uniqueName: String?): File{
        return File(context.cacheDir, uniqueName)
    }


    fun getCache(context: Context, maxCacheSize: Int, uniqueName: String?): Cache {
        return Cache(getDiskCacheDir(context, uniqueName), maxCacheSize.toLong())
    }
}