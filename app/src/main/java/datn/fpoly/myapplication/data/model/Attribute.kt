package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Attribute (
    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("price" ) var price : Double?    = null
)