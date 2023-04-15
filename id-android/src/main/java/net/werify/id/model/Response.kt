package net.werify.id.model

import com.google.gson.annotations.SerializedName

//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

//@Serializable
data class Response<JSON_OBJECT>(
    @SerializedName("succeed") val succeed: Boolean = false,
    @SerializedName("results") val results: JSON_OBJECT? = null,
    @SerializedName("metas") val metas: List<String> = emptyList(),
    @SerializedName("message") val message: String? = null
)
//"message": "Token has expired" // When response code is not 200
//{
//    "succeed": true,
//    "results": {
//    "type": "otp",
//    "hash": "002ca00b-8ee5-462f-97a1-e275ceee89a3",
//    "otp": "829301",
//    "id": "640ebb6c-c26b-414b-b2b7-e84c025c4180",
//    "created_at": "2023-04-10T18:59:54.000000Z"
//},
//    "metas": []
//}