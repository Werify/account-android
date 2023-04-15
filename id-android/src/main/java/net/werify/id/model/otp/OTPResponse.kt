package net.werify.id.model.otp

import com.google.gson.annotations.SerializedName

data class OTPRequestResults(
    @SerializedName("type") var type: String = "",
    @SerializedName("hash") var hash: String = "",
    @SerializedName("otp") var otp: String = "",
    @SerializedName("id") var id: String = "",
    @SerializedName("created_at") var createdAt: String = ""
)
fun OTPRequestResults.toVerifyObject() = VerifyOTP(type, hash, otp, id)

data class OTPVerifyResults(
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("middle_name") var middleName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("phone_numbers") var phoneNumbers: ArrayList<String> = arrayListOf(),
    @SerializedName("language") var language: String? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("last_connection") var lastConnection: String? = null,
    @SerializedName("private") var private: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("access_token") var accessToken: String? = null,
    @SerializedName("token_type") var tokenType: String? = null
)