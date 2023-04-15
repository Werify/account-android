package net.werify.id.retrofit

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.werify.id.NetworkDataSource
import net.werify.id.TAG
import net.werify.id.common.WConstants
import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.qr.QrResult
import net.werify.id.model.user.FinancialResult
import net.werify.id.model.user.Profile
import net.werify.id.model.user.UserInfo
import net.werify.id.utils.Utils
import net.werify.official.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

const val TOKEN = "token"
const val TOKEN_TYPE = "token_type"

object NetworkModule {
    //private var sRetrofit: Retrofit? = null
    private var sHttpClient: OkHttpClient? = null
    private var sUserAgent: String? = null
    private lateinit var sNetworkDataSource: NetworkDataSource
    private lateinit var mPreferences: SharedPreferences
    var url: String = ""
    private lateinit var cache: Cache

    private fun createDataSource(api: NetworkApi) {
        sNetworkDataSource = RetrofitWerifyNetwork(api)
    }

    fun saveToCache(name: String, inStream: InputStream) : String{
        val input = BufferedInputStream(inStream)
        val file = File(cache.directory,name)
        val output: OutputStream = FileOutputStream(file)
        val data = ByteArray(1024)
        var total: Long = 0
        var count = 0
        while (input.read(data).also { count = it } != -1) {
            total += count
            output.write(data, 0, count)
        }
        output.flush()
        Log.e(TAG, "Saved $name Is Don. ${file.absolutePath}")
        output.close()
        input.close()

        return "${cache.directory.absolutePath}/$name"
    }

    fun initialize(
        ctx: Context,
        clientBuilder: OkHttpClient.Builder,
        url: String = BuildConfig.BACKEND_URL
    ) {
        mPreferences = ctx.getSharedPreferences("W-INFO", Context.MODE_PRIVATE)
        cache = Utils.getCache(ctx, WConstants.MAX_CACHE_SIZE, WConstants.CACHE_DIR_NAME)
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

        val api =
            provideRetrofit(clientBuilder.cache(cache).addInterceptor(interceptor)
                .addInterceptor(OAuthInterceptor(mPreferences)).build(), url).create(NetworkApi::class.java)
        createDataSource(api)
    }

    fun initializeWithDefaultClient(ctx: Context) {
        mPreferences = ctx.getSharedPreferences("W-INFO", Context.MODE_PRIVATE)
        cache = Utils.getCache(ctx, WConstants.MAX_CACHE_SIZE, WConstants.CACHE_DIR_NAME)
        val api = provideRetrofit().create(NetworkApi::class.java)
        createDataSource(api)
    }

    private

    fun getDefaultClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

        return OkHttpClient
            .Builder()
            .cache(cache)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(OAuthInterceptor(mPreferences))
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
    ): Retrofit {
        NetworkModule.url = url
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .serializeNulls()
                        .setPrettyPrinting()
                        .create()
                )
            )
            .baseUrl(url)
            .client(client)
            .build()
    }

    fun cacheAccessToken(type: String?, token: String?) {
        if (::mPreferences.isInitialized) {
            val editor = mPreferences.edit()
            editor.putString(TOKEN_TYPE, type)
            editor.putString(TOKEN, token)
            editor.apply()
        }
    }

    //region  ( Doesn't need any credentials or authorization )
    fun login(request: Request): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.login(request)) }

    fun loginOTP(request: Request): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.loginOTP(request)) }

    fun requestOTP(request: Request): Flow<Response<OTPRequestResults>> =
        flow { emit(sNetworkDataSource.requestOTP(request)) }

    fun verifyOTP(request: Request): Flow<Response<OTPVerifyResults>> =
        flow { emit(sNetworkDataSource.verifyOTP(request)) }

    fun getQRSession(): Flow<Response<QrResult>> = flow { emit(sNetworkDataSource.getQRSession()) }
    fun checkSession(hash: String, id: String): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.checkSession(hash, id)) }
    //endregion

    //region  ( Needs token in request header )

    fun getUserProfile(): Flow<Response<UserInfo>> = flow { emit(sNetworkDataSource.getUserProfile()) }
    fun getUserNumbers(): Flow<Response<FinancialResult>> =
        flow { emit(sNetworkDataSource.getUserNumbers()) }

    fun getFinancialInfo(): Flow<Response<FinancialResult>> =
        flow { emit(sNetworkDataSource.getFinancialInfo()) }

    fun getNewModalSession(): Flow<Response<FinancialResult>> =
        flow { emit(sNetworkDataSource.getNewModalSession()) }

    fun checkUsername(): Flow<Response<Any>> = flow { emit(sNetworkDataSource.checkUsername()) }

    fun claimModalSession(hash: String, id: String): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.claimModalSession(hash, id)) }

    fun claimQRSession(hash: String, id: String): Flow<String> =
        flow { emit(sNetworkDataSource.claimQRSession(hash, id)) }

    fun updateUserProfile(request: Profile): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.updateUserProfile(request)) }
    fun updateFinancialInfo(request: Profile): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.updateFinancialInfo(request)) }

    fun addMobileNumber(request: Request): Flow<Response<Any>> =
        flow { emit(sNetworkDataSource.addMobileNumber(request)) }


    //endregion
}