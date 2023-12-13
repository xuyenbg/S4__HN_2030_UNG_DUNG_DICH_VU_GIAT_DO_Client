package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ItemServiceExtend(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("number") var number: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("idOrder") var idOrder: String? = null,
    @SerializedName("idService") var idService: ServiceBase? = null,
    @SerializedName("attributeList") var attributeList: MutableList<AttributeModel>? = arrayListOf()
): java.io.Serializable {
    override fun toString(): String {
        return "ItemService(number=$number, total=$total, image=$image, idOrder=$idOrder, attributeList=$attributeList)"
    }
}

data class ItemServiceExtendHistory(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("number") var number: Double? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("idOrder") var idOrder: String? = null,
    @SerializedName("idService") var idService: ServiceHistory? = null,
    @SerializedName("attributeList") var attributeList: MutableList<AttributeModel>? = arrayListOf()
): java.io.Serializable {
    override fun toString(): String {
        return "ItemService(number=$number, total=$total, image=$image, idOrder=$idOrder, attributeList=$attributeList)"
    }
}