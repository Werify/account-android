package net.werify.id.model.qr

import com.google.gson.annotations.SerializedName
import net.werify.id.model.user.Profile


data class QrResult(
    @SerializedName("id") var id: String = "",
    @SerializedName("hash") var hash: String = "",
    @SerializedName("expired_at") var expiredAt: String = "",
    @SerializedName("url") var url: String = ""
)

data class QRSession(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("email_verified_at") var emailVerifiedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("profile") var profile: Profile? = Profile(),
    @SerializedName("profile_badges") var profileBadges: ArrayList<String> = arrayListOf(),
    @SerializedName("profile_numbers") var profileNumbers: ArrayList<String> = arrayListOf(),
    @SerializedName("profile_education") var profileEducation: ArrayList<String> = arrayListOf(),
    @SerializedName("financial_information") var financialInformation: String? = null,
    @SerializedName("profile_metas") var profileMetas: ArrayList<String> = arrayListOf()

)