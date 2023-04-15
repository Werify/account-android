package net.werify.id.model.user

import net.werify.id.model.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

data class UserNumberRequest(
    val mobile_number: String
) : Request {
    override fun toRequestBody() =
        "{\"mobile_number\":\"$mobile_number\"}".toRequestBody("application/json".toMediaType())
}
