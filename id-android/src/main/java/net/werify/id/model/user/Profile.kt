package net.werify.id.model.user

import com.google.gson.annotations.SerializedName

data class Profile (

    @SerializedName("first_name"    ) var firstName    : String? = null,
    @SerializedName("middle_name"   ) var middleName   : String? = null,
    @SerializedName("last_name"     ) var lastName     : String? = null,
    @SerializedName("mobile_number" ) var mobileNumber : String? = null,
    @SerializedName("avatar"        ) var avatar       : String? = null,
    @SerializedName("cover"         ) var cover        : String? = null,
    @SerializedName("is_private"    ) var isPrivate    : String? = null,
    @SerializedName("language"      ) var language     : String? = null,
    @SerializedName("currency"      ) var currency     : String? = null,
    @SerializedName("timezone"      ) var timezone     : String? = null,
    @SerializedName("calendar"      ) var calendar     : String? = null,
    @SerializedName("shortcuts"     ) var shortcuts    : String? = null,
    @SerializedName("layout"        ) var layout       : String? = null,
    @SerializedName("latitude"      ) var latitude     : String? = null,
    @SerializedName("longitude"     ) var longitude    : String? = null,
    @SerializedName("last_online"   ) var lastOnline   : String? = null,
    @SerializedName("updated_at"    ) var updatedAt    : String? = null

)