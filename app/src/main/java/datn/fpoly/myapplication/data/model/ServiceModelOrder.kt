package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ServiceModelOrder(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: String? = null,
    @SerializedName("attributeList")
    var attributeList: MutableList<String>? = null,
    @SerializedName("idCategory")
    var idCategory: String? = null,
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("unit")
    var unit: String? = null,
    @SerializedName("idSale")
    var idSale: String? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("image")
    var image: String? = null
) :Serializable {
    override fun toString(): String {
        return "ServiceModel(id=$id, name=$name, idStore=$idStore, attributeList=$attributeList, idCategory=$idCategory, isActive=$isActive, unit=$unit, idSale=$idSale, price=$price, image=$image)"
    }
}