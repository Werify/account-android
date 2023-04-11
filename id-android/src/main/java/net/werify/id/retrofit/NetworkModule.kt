package net.werify.id.retrofit

import net.werify.id.NetworkDataSource
import net.werify.id.internal.InternalNetworking
import net.werify.official.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {
    var sRetrofit: Retrofit? = null

    fun initialize(client: OkHttpClient , url : String = BuildConfig.BACKEND_URL) {
        sRetrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .client(client)
            .build()
        val api = sRetrofit!!.create(NetworkApi::class.java)

//        sNetworkDataSource = RetrofitWerifyNetwork(api)
    }

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
//    fun provideRetrofit(configure: WerifyConfigure): Retrofit = Retrofit.Builder()
//        .addConverterFactory(GsonConverterFactory.create())
//        .baseUrl(configure.url)
//        .client(configure.client)
//        .build()

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