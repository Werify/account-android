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