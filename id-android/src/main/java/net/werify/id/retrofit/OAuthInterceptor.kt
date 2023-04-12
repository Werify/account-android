package net.werify.id.retrofit

import android.content.SharedPreferences
import android.util.Log
import net.werify.id.TAG
import okhttp3.Interceptor

class OAuthInterceptor(
    private val
    preferences: SharedPreferences
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        if (request.header("No-Authentication") == null) {
            val tokenType: String? = preferences.getString(TOKEN_TYPE, "")
            val accessToken: String? = preferences.getString(TOKEN, "")
            request =  request.newBuilder().header("Authorization", "$tokenType $accessToken").build()
            Log.e(TAG , "OAuthInterceptor ${request.url} ,${request.headers}")
        }
        return chain.proceed(request)
    }
}