package net.werify.id.retrofit

import net.werify.id.NetworkDataSource
import net.werify.official.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {

     fun provideBaseUrl() = BuildConfig.BACKEND_URL
     fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
         val loggingInterceptor = HttpLoggingInterceptor()
         loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
         OkHttpClient.Builder()
             .addInterceptor(loggingInterceptor)
             .build()
     } else {
         OkHttpClient
             .Builder()
             .build()
     }
    fun provideRetrofit(configure: Configure): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(configure.url)
        .client(configure.client)
        .build()

        /* private fun networkApi(url: String = ApiBaseUrl) = Retrofit.Builder()
        .baseUrl(url)
        .client(
            OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build()
        ).addConverterFactory(GsonConverterFactory.create())
        .build().create(NetworkApi::class.java)*/

    fun provideApiService(retrofit: Retrofit): NetworkApi = retrofit.create(NetworkApi::class.java)
    fun bindsNetwork(appNetwork: RetrofitWerifyNetwork): NetworkDataSource = appNetwork


}