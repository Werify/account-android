package net.werify.id.retrofit

import net.werify.official.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


// Builder Pattern To Set More Option For NetWork .
class WerifyConfigure private constructor(
    val appKey: String,
    val url: String,
    val readTimeout: Long,
    val writeTimeout: Long,
    val connectTimeout: Long
) {


    data class Builder(
        private var appKey: String,
        private var url: String? = null,
        private var readTimeout: Long = TimeUnit.SECONDS.toSeconds(60),
        private var writeTimeout: Long = TimeUnit.SECONDS.toSeconds(60),
        private var connectTimeout: Long = TimeUnit.SECONDS.toSeconds(60)
    ) {
        fun setApplicationKey(key: String) = apply { this.appKey = key }
        fun setUrl(url: String) = apply { this.url = url }
        fun readTimeout(timeout: Long, unit: TimeUnit) =
            apply { this.readTimeout = unit.toSeconds(timeout) }

        fun writeTimeout(timeout: Long, unit: TimeUnit) =
            apply { this.writeTimeout = unit.toSeconds(timeout) }

        fun connectTimeout(timeout: Long, unit: TimeUnit) =
            apply { this.connectTimeout = unit.toSeconds(timeout) }

        fun build() = WerifyConfigure(
            appKey,url ?: BuildConfig.BACKEND_URL,
            readTimeout, writeTimeout, connectTimeout
        )
    }
}