package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class ServiceBase(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: String? = null,
    @SerializedName("attributeList")
    var attributeList: List<String>? = null,
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
):java.io.Serializable {
    override fun toString(): String {
        return "ServiceBase(id=$id, name=$name, idStore=$idStore, attributeList=$attributeList, idCategory=$idCategory, isActive=$isActive, unit=$unit, idSale=$idSale, price=$price, image=$image)"
    }
}

data class ServiceHistory(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: String? = null,
    @SerializedName("attributeList")
    var attributeList: List<String>? = null,
    @SerializedName("idCategory")
    var idCategory: String? = null,
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
)