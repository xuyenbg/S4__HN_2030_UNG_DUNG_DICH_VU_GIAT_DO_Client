package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Sale(
    @SerializedName("id") var id: String? = null,
    @SerializedName("unit") var unit: String? = null,
    @SerializedName("value") var value: Double? = null

)