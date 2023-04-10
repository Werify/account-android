package net.werify.id

import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults

// @ref [https://github.com/Werify/id-ts/blob/main/README.md]
interface PublicDataSource {
    //region  ( Doesn't need any credentials or authorization )
    suspend fun login(request: Request): Response<Any>
    suspend fun loginOTP(request: Request): Response<Any>
    suspend fun requestOTP(request: Request): Response<OTPRequestResults>
    suspend fun verifyOTP(request: Request): Response<OTPVerifyResults>

    suspend fun getQRSession(): Response<Any>
    suspend fun checkSession(): Response<Any>
    //endregion
}
interface PrivateDataSource {
    //region  ( Needs token in request header )
    suspend fun getUserProfile(): Response<Any>
    suspend fun getUserNumbers(): Response<Any>
    suspend fun getFinancialInfo(): Response<Any>
    suspend fun getNewModalSession(): Response<Any>
    suspend fun checkUsername(): Response<Any>

    suspend fun claimModalSession(request: Request): Response<Any>
    suspend fun claimQRSession(request: Request): Response<Any>
    suspend fun updateUserProfile(request: Request): Response<Any>
    suspend fun addMobileNumber(request: Request): Response<Any>
    //endregion
}

interface NetworkDataSource : PublicDataSource, PrivateDataSource