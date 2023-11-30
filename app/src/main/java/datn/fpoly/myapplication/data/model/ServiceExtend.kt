package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class ServiceExtend(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: StoreModel? = null,
    @SerializedName("attributeList")
    var attributeList: MutableList<AttributeModel>? = null,
    @SerializedName("idCategory")
    var idCategory: CategoryModel? = null,
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("unit")
    var unit: String? = null,
    @SerializedName("idSale")
    var idSale: Sale? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("image")
    var image: String? = null
) : java.io.Serializable{
    override fun toString(): String {
        return "ServiceModel(id=$id, name=$name, idStore=$idStore, attributeList=$attributeList, idCategory=$idCategory, isActive=$isActive, unit=$unit, idSale=$idSale, price=$price, image=$image)"
    }
}