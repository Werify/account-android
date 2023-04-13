package net.werify.id.model.user

import com.google.gson.annotations.SerializedName

data class FinancialResult(
    @SerializedName("current_page") var currentPage: Double = 0.0,
    @SerializedName("first_page_url") var firstPageUrl: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("last_page") var lastPage: Double = 0.0,
    @SerializedName("last_page_url") var lastPageUrl: String? = null,
    @SerializedName("data") var data: ArrayList<String> = arrayListOf(),
    @SerializedName("links") var links: ArrayList<Link> = arrayListOf(),
    @SerializedName("next_page_url") var nextPageUrl: String? = null,
    @SerializedName("path") var path: String? = null,
    @SerializedName("per_page") var perPage: Double = 0.0,
    @SerializedName("prev_page_url") var prevPageUrl: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("total") var total: Double = 0.0
)

data class Link(
    @SerializedName("url") var url: String? = null,
    @SerializedName("label") var label: String? = null,
    @SerializedName("active") var active: Boolean = false
)

data class UserInfo(
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