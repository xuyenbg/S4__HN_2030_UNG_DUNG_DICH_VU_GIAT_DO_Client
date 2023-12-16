package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.orderList.OrderResponse

data class StatisticsModel(
    @SerializedName("date")
    val date : String?,
    @SerializedName("day")
    val day : String,
    @SerializedName("total")
    val total : Double
)