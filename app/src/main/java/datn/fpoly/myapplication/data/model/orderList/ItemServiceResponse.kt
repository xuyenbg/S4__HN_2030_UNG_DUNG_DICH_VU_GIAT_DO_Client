package datn.fpoly.myapplication.data.model.orderList

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.AttributeModel
import datn.fpoly.myapplication.data.model.ServiceModelOrder
import java.io.Serializable

data class ItemServiceResponse(
    @SerializedName("idService") var idService: ServiceModelOrder? = null,
    @SerializedName("number") var number: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("attributeList") var attributeList: MutableList<AttributeModel>? = arrayListOf(),
    @SerializedName("_id") var _id: String? = null,

) : Serializable {
    override fun toString(): String {
        return "ItemService(number=$number, total=$total, idService=$idService, attributeList=$attributeList)"
    }
}