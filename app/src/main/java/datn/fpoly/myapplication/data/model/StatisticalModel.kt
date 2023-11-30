package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class StatisticalModel(
    @SerializedName("total")
    val total: Double,
    @SerializedName("totalOrder")
    val totalOrder: Double
)