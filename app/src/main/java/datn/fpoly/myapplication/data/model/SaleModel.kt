package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class SaleModel(
    @SerializedName("_id") var id: String?,
    @SerializedName("unit") var unit: String?,
    @SerializedName("value") var value: Int?
)