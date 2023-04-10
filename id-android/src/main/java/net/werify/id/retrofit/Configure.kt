package net.werify.id.retrofit

import net.werify.official.BuildConfig
import okhttp3.OkHttpClient

// Builder Pattern To Set More Option For NetWork .
class Configure private constructor(var client: OkHttpClient, var url: String) {
    data class Builder(var client: OkHttpClient, var url: String?) {
        fun httpClient(client: OkHttpClient) = apply { this.client = client }
        fun url(url: String) = apply { this.url = url }
        fun build() = Configure(client, url ?: BuildConfig.BACKEND_URL)
    }
}