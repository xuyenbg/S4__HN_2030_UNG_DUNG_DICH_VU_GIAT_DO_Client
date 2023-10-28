package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ItemService(
    @SerializedName("number") var number: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("idOrder") var idOrder: String? = null,
    @SerializedName("service") var service: ServiceModel? = ServiceModel(),
    @SerializedName("attributeList") var attributeList: MutableList<AttributeModel> = arrayListOf()
) {
    override fun toString(): String {
        return "ItemService(number=$number, total=$total, image=$image, idOrder=$idOrder, service=$service, attributeList=$attributeList)"
    }
}