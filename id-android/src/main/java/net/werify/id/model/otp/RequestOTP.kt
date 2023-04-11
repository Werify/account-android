package net.werify.id.model.otp

import net.werify.id.model.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

data class RequestOTP(val identifier: String) : Request {
    override fun toRequestBody() =
        "$this".toRequestBody("application/json".toMediaType())
    override fun toString(): String {
        return "{\"identifier\":\"$identifier\"}"
    }
}


data class VerifyOTP(
    val type: String,
    val hash: String,
    val otp: String,
    val id: String
) : Request {

    override fun toRequestBody() =
        "$this".toRequestBody("application/json".toMediaType())

    override fun toString(): String {
        return "{\"type\":\"$type\",\"hash\":\"$hash\",\"otp\":\"$otp\",\"id\":\"$id\"}"
    }
}