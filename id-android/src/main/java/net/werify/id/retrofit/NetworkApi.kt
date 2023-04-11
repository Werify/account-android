package net.werify.id.retrofit

import net.werify.id.NetworkDataSource
import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


private const val SUFFIX = "api/v1"

/**
 * Retrofit API declaration for Werify Network API RetrofitVerifyNetwork
 */
interface NetworkApi {
    //region  Public Routes ( Doesn't need any credentials or authorization )

    //region request user login otp
    @POST(value = "$SUFFIX/login")// params : identifier
    suspend fun login(@Body request: RequestBody): Response<Any>

    @POST(value = "$SUFFIX/otp")// params : id, hash, otp
    suspend fun loginOTP(request: Request): Response<Any>

    @POST(value = "$SUFFIX/request-otp")// params : identifier
    suspend fun requestOTP(request: Request): Response<OTPRequestResults>

    @POST(value = "$SUFFIX/verify-otp")// params : id, hash, otp
    suspend fun verifyOTP(request: Request): Response<OTPVerifyResults>

    @GET(value = "$SUFFIX/qr")// params : NOT
    suspend fun getQRSession(): Response<Any>

    @GET(value = "$SUFFIX/session-check/modal/{hash}/{id}")// params : NOT
    suspend fun checkSession(): Response<Any>

    //endregion

    //endregion

    //region  Private Routes ( Needs token in request header )
    @GET(value = "$SUFFIX/user/profile")// params : NOT
    suspend fun getUserProfile(): Response<Any>

    @GET(value = "$SUFFIX/user/profile/mobile-numbers")// params : NOT
    suspend fun getUserNumbers(): Response<Any>

    @GET(value = "$SUFFIX/user/financial-information")// params : NOT
    suspend fun getFinancialInfo(): Response<Any>

    @GET(value = "$SUFFIX/user/modal")// params : NOT
    suspend fun getNewModalSession(): Response<Any>

    @GET(value = "$SUFFIX/user/modal/{hash}/{id}")// params : NOT
    suspend fun claimModalSession(request: Request): Response<Any>

    @GET(value = "$SUFFIX/qr/{hash}/{id}")// params : NOT
    suspend fun claimQRSession(request: Request): Response<Any>

    @PUT(value = "$SUFFIX/user/profile")// params : form data
    suspend fun updateUserProfile(@Body request: RequestBody): Response<Any>

    @POST(value = "$SUFFIX/user/mobile-numbers")// params : mobile_number
    suspend fun addMobileNumber(@Body request: RequestBody): Response<Any>

    @POST(value = "$SUFFIX/check-username")// params : NOT
    suspend fun checkUsername(): Response<Any>
    //endregion

}

/**
 * [Retrofit] backed [NetworkDataSource]
 */
class RetrofitWerifyNetwork constructor(private val api: NetworkApi) : NetworkDataSource {

    /*  private val TIME_OUT: Long = 120
      private val gson = GsonBuilder().setLenient().create()
      private val okHttpClient = OkHttpClient.Builder()
          .readTimeout(TIME_OUT, TimeUnit.SECONDS)
          .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
          .addInterceptor { chain ->
              val resp = chain.proceed(chain.request())
              // Deal with the response code
              if (resp.code == 200) {
                  try {
                      val myJson =
                          resp.peekBody(2048).string() // peekBody() will not close the response
                      println(myJson)
                  } catch (e: Exception) {
                      println("Error parse json from intercept..............")
                  }
              } else {
                  println(resp)
              }
              resp
          }.build()*/


    override suspend fun login(request: Request) = api.login(request.toRequestBody())
    override suspend fun loginOTP(request: Request) = api.loginOTP(request)
    override suspend fun requestOTP(request: Request) = api.requestOTP(request)
    override suspend fun verifyOTP(request: Request) = api.verifyOTP(request)
    override suspend fun getQRSession(): Response<Any> = api.getQRSession()
    override suspend fun checkSession() = api.checkSession()
    override suspend fun getUserProfile() = api.getUserProfile()
    override suspend fun getUserNumbers() = api.getUserNumbers()
    override suspend fun getFinancialInfo() = api.getFinancialInfo()
    override suspend fun getNewModalSession() = api.getNewModalSession()
    override suspend fun checkUsername() = api.checkUsername()
    override suspend fun claimModalSession(request: Request) =
        api.claimModalSession(request)

    override suspend fun claimQRSession(request: Request) = api.claimQRSession(request)
    override suspend fun updateUserProfile(request: Request) =
        api.updateUserProfile(request.toRequestBody())

    override suspend fun addMobileNumber(request: Request) =
        api.addMobileNumber(request.toRequestBody())
}