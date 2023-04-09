package net.werify.official.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import net.werify.official.BuildConfig
import net.werify.official.NetworkDataSource
import net.werify.official.model.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Werify Network API RetrofitVerifyNetwork
 */
private interface NetworkApi {

    //region  Public Routes ( Doesn't need any credentials or authorization )

       //region request user login otp
    @POST(value = "api/login")// params : identifier
    suspend fun login(@Body request: RequestBody): Response<Any>

    @POST(value = "api/otp")// params : id, hash, otp
    suspend fun loginOTP(request: Request): Response<Any>

    @GET(value = "api/qr")// params : NOT
    suspend fun getQRSession(): Response<Any>

    @GET(value = "api/session-check/modal/{hash}/{id}")// params : NOT
    suspend fun checkSession(): Response<Any>

    //endregion

    //endregion

    //region  Private Routes ( Needs token in request header )
    @GET(value = "api/user/profile")// params : NOT
    suspend fun getUserProfile(): Response<Any>

    @GET(value = "api/user/profile/mobile-numbers")// params : NOT
    suspend fun getUserNumbers(): Response<Any>

    @GET(value = "api/user/financial-information")// params : NOT
    suspend fun getFinancialInfo(): Response<Any>

    @GET(value = "api/user/modal")// params : NOT
    suspend fun getNewModalSession(): Response<Any>

    @GET(value = "api/user/modal/{hash}/{id}")// params : NOT
    suspend fun claimModalSession(): Response<Any>

    @GET(value = "api/qr/{hash}/{id}")// params : NOT
    suspend fun claimQRSession(): Response<Any>

    @PUT(value = "api/user/profile")// params : form data
    suspend fun updateUserProfile(@Body request: RequestBody): Response<Any>

    @POST(value = "api/user/mobile-numbers")// params : mobile_number
    suspend fun addMobileNumber(@Body request: RequestBody): Response<Any>

    @POST(value = "api/check-username")// params : NOT
    suspend fun checkUsername(): Response<Any>
    //endregion

}

///api/qr
private const val ApiBaseUrl = BuildConfig.BACKEND_URL


/**
 * [Retrofit] backed [NetworkDataSource]
 */
@Singleton
class RetrofitWerifyNetwork @Inject constructor(netJson: Json) : NetworkDataSource {

    private val contentType = "application/json".toMediaType()

    @OptIn(ExperimentalSerializationApi::class)
    private val converterFactory = netJson.asConverterFactory(contentType)

    private fun networkApi(url: String = ApiBaseUrl) = Retrofit.Builder()
        .baseUrl(url)
        .client(
            OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build()
        ).addConverterFactory(converterFactory)
        .build().create(NetworkApi::class.java)

    override suspend fun login(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun loginOTP(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun getQRSession(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun checkSession(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun getUserProfile(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun getUserNumbers(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun getFinancialInfo(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun getNewModalSession(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun checkUsername(): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun claimModalSession(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun claimQRSession(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

    override suspend fun addMobileNumber(request: Request): net.werify.official.model.Response {
        TODO("Not yet implemented")
    }

}