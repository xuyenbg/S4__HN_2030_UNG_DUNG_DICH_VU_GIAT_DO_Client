package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AttributeModel(
    @SerializedName("_id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Double
) : Serializable