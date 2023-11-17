package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ItemServiceBase(
    @SerializedName("number") var number: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("idOrder") var idOrder: String? = null,
    @SerializedName("idService") var idService: String? = null,
    @SerializedName("service") var service: ServiceExtend? = ServiceExtend(),
    @SerializedName("attributeListExtend") var attributeListExtend: MutableList<AttributeModel>? = arrayListOf(),
    @SerializedName("attributeList") var attributeList: MutableList<String>? = arrayListOf()
) {
    override fun toString(): String {
        return "ItemService(number=$number, total=$total, image=$image, idOrder=$idOrder, service=$service, attributeList=$attributeListExtend)"
    }
}