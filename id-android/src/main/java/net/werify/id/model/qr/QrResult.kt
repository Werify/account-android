package net.werify.id.model.qr

import com.google.gson.annotations.SerializedName
import net.werify.id.model.user.Profile


data class QrResult(
    @SerializedName("id") var id: String = "",
    @SerializedName("hash") var hash: String = "",
    @SerializedName("expired_at") var expiredAt: String = "",
    @SerializedName("url") var url: String = ""
)

