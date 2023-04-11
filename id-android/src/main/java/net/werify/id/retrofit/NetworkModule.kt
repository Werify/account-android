package net.werify.id.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.werify.id.NetworkDataSource
import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.qr.QrResult
import net.werify.official.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object NetworkModule {
    //private var sRetrofit: Retrofit? = null
    private var sHttpClient: OkHttpClient? = null
    private var sUserAgent: String? = null
    private lateinit var sNetworkDataSource: NetworkDataSource

    fun initialize(client: OkHttpClient, url: String = BuildConfig.BACKEND_URL) {
        val api = provideRetrofit(client, url).create(NetworkApi::class.java)
        sNetworkDataSource = RetrofitWerifyNetwork(api)
    }

    fun initializeWithDefaultClient() {
        val api = provideRetrofit().create(NetworkApi::class.java)
        sNetworkDataSource = RetrofitWerifyNetwork(api)
    }

    fun getDefaultClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun getClient(): OkHttpClient {
        return sHttpClient ?: getDefaultClient()
    }

    fun setUserAgent(userAgent: String) {
        sUserAgent = userAgent
    }

    fun setClient(okHttpClient: OkHttpClient) {
        sHttpClient = okHttpClient
    }

    fun enableLogging(level: HttpLoggingInterceptor.Level) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(level)
        sHttpClient = getClient().newBuilder()
            .addInterceptor(logging)
            .build()


    }

    private fun provideRetrofit(
        client: OkHttpClient = getDefaultClient(),
        url: String = BuildConfig.BACKEND_URL
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .client(client)
        .build()

    //region  ( Doesn't need any credentials or authorization )
    fun login(request: Request): Flow<Response<Any>> = flow { emit(sNetworkDataSource.login(request)) }
    fun loginOTP(request: Request): Flow<Response<Any>> = flow { emit(sNetworkDataSource.loginOTP(request)) }

    fun requestOTP(request: Request): Flow<Response<OTPRequestResults>> = flow { emit(sNetworkDataSource.requestOTP(request)) }
    fun verifyOTP(request: Request): Flow<Response<OTPVerifyResults>> = flow { emit(sNetworkDataSource.verifyOTP(request)) }
    fun getQRSession(): Flow<Response<QrResult>> = flow { emit(sNetworkDataSource.getQRSession()) }
    fun checkSession(hash: String, id: String): Flow<Response<Any>> = flow { emit(sNetworkDataSource.checkSession(hash, id)) }
    //endregion

    //region  ( Needs token in request header )

    fun getUserProfile(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.getUserProfile()) }
    fun getUserNumbers(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.getUserNumbers()) }
    fun getFinancialInfo(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.getFinancialInfo()) }
    fun getNewModalSession(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.getNewModalSession()) }
    fun checkUsername(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.checkUsername()) }

    fun claimModalSession(hash: String, id: String): Flow<Response<Any>> = flow { emit(sNetworkDataSource.claimModalSession(hash, id)) }
    fun claimQRSession(hash: String, id: String): Flow<Response<Any>> = flow { emit(sNetworkDataSource.claimQRSession(hash, id)) }

    fun updateUserProfile(request: Request): Flow<Response<Any>> = flow { emit(sNetworkDataSource.updateUserProfile(request)) }
    fun addMobileNumber(request: Request): Flow<Response<Any>> = flow { emit(sNetworkDataSource.addMobileNumber(request)) }


    //endregion
}