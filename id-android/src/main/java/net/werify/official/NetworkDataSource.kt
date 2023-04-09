package net.werify.official

import net.werify.official.model.Request
import net.werify.official.model.Response

interface NetworkDataSource {

    // @ref [https://github.com/Werify/id-ts/blob/main/README.md]

    //region  Public Routes ( Doesn't need any credentials or authorization )

    suspend  fun login(request : Request) : Response
    suspend  fun loginOTP(request : Request) : Response

    suspend  fun getQRSession() : Response
    suspend  fun checkSession() : Response
    //endregion

    //region  Private Routes ( Needs token in request header )
    suspend fun getUserProfile(): Response
    suspend fun getUserNumbers(): Response
    suspend fun getFinancialInfo(): Response
    suspend fun getNewModalSession(): Response
    suspend fun checkUsername(): Response

    suspend fun claimModalSession(request : Request): Response
    suspend fun claimQRSession(request : Request): Response
    suspend fun updateUserProfile(request : Request): Response
    suspend fun addMobileNumber(request : Request): Response
    //endregion
}