package net.werify.id.retrofit

import android.util.Log
import net.werify.id.NetworkDataSource
import net.werify.id.TAG
import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.qr.QrResult
import net.werify.id.model.user.FinancialResult
import net.werify.id.model.user.Profile
import net.werify.id.model.user.UserInfo
import net.werify.id.utils.Utils
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


private const val SUFFIX = "api/v1"

/**
 * Retrofit API declaration for Werify Network API RetrofitVerifyNetwork
 */
interface NetworkApi {
    //region  Public Routes ( Doesn't need any credentials or authorization )

    //region request user login otp
    @Headers("No-Authentication: true")
    @POST(value = "$SUFFIX/login")// params : identifier
    suspend fun login(@Body request: RequestBody): Response<Any>
    @Headers("No-Authentication: true")
    @POST(value = "$SUFFIX/otp")// params : id, hash, otp
    suspend fun loginOTP(@Body request: RequestBody): Response<Any>

    @Headers("No-Authentication: true")
    @POST(value = "$SUFFIX/request-otp")// params : identifier
    suspend fun requestOTP(@Body request: RequestBody): Response<OTPRequestResults>

    @Headers("No-Authentication: true")
    @POST(value = "$SUFFIX/verify-otp")// params : id, hash, otp
    suspend fun verifyOTP(@Body request: RequestBody): Response<OTPVerifyResults>

    @Headers("No-Authentication: true")
    @GET(value = "$SUFFIX/qr")// params : NOT
    suspend fun getQRSession(): Response<QrResult>

    @Headers("No-Authentication: true")
    @GET(value = "$SUFFIX/session-check/modal/{hash}/{id}")// params : NOT
    suspend fun checkSession(@Path("hash") hash: String, @Path("id") id: String): Response<Any>

    //endregion

    //endregion

    //region  Private Routes ( Needs token in request header )
    @GET(value = "$SUFFIX/user/profile")// params : NOT
    suspend fun getUserProfile(): Response<UserInfo>

    @GET(value = "$SUFFIX/user/profile/mobile-numbers")// params : NOT
    suspend fun getUserNumbers(): Response<FinancialResult>

    @GET(value = "$SUFFIX/user/profile/financial-information")// params : NOT
    suspend fun getFinancialInfo(): Response<FinancialResult>

    @GET(value = "$SUFFIX/user/modal")// params : NOT
    suspend fun getNewModalSession(): Response<FinancialResult>

    @GET(value = "$SUFFIX/user/modal/{hash}/{id}")// params : NOT
    suspend fun claimModalSession(@Path("hash") hash: String, @Path("id") id: String): Response<Any>

    @GET(value = "$SUFFIX/qr/{hash}/{id}")// params : NOT return svg file
    @Streaming
    suspend fun claimQRSession(@Path("hash") hash: String, @Path("id") id: String): Call<Any>

    @PUT(value = "$SUFFIX/user/profile")// params : form data
    suspend fun updateUserProfile(@Body request: Profile): Response<Any>
    @PUT(value = "$SUFFIX/user/profile/financial-information")// params : form data
    suspend fun updateFinancialInfo(@Body request: Profile): Response<Any>
    @POST(value = "$SUFFIX/user/mobile-numbers")// params : mobile_number
    suspend fun addMobileNumber(@Body request: RequestBody): Response<Any>

    @POST(value = "$SUFFIX/user/check-username")// params : NOT
    suspend fun checkUsername(): Response<Any>
    //endregion

}

/**
 * [Retrofit] backed [NetworkDataSource]
 */
class RetrofitWerifyNetwork constructor(private val api: NetworkApi) : NetworkDataSource {

    override suspend fun login(request: Request) = api.login(request.toRequestBody())
    override suspend fun loginOTP(request: Request) = api.loginOTP(request.toRequestBody())
    override suspend fun requestOTP(request: Request) = api.requestOTP(request.toRequestBody())
    override suspend fun verifyOTP(request: Request) = api.verifyOTP(request.toRequestBody())
    override suspend fun getQRSession(): Response<QrResult> = api.getQRSession()
    override suspend fun checkSession(hash: String, id: String) = api.checkSession(hash, id)
    override suspend fun getUserProfile() = api.getUserProfile()
    override suspend fun getUserNumbers() = api.getUserNumbers()
    override suspend fun getFinancialInfo() = api.getFinancialInfo()
    override suspend fun getNewModalSession() = api.getNewModalSession()
    override suspend fun checkUsername() = api.checkUsername()
    override suspend fun claimModalSession(hash: String, id: String) =
        api.claimModalSession(hash, id)

    override suspend fun claimQRSession(hash: String, id: String): String {
        val request = okhttp3.Request.Builder()
            .url("${NetworkModule.url}$SUFFIX/qr/$hash/$id")
            .build()
        val response = NetworkModule.getClient().newCall(request).execute()

        var filePath = ""
        response.body?.run {
            filePath = NetworkModule.saveToCache("QR.svg", byteStream())
        }
        return filePath
    }

    override suspend fun updateUserProfile(request: Profile) =
        api.updateUserProfile(request)

    override suspend fun updateFinancialInfo(request: Profile) =
        api.updateFinancialInfo(request)

    override suspend fun addMobileNumber(request: Request) =
        api.addMobileNumber(request.toRequestBody())
}