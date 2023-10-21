package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: String? = null,
    @SerializedName("attributeList")
    var attributeList: MutableList<Attribute> = arrayListOf(),
    @SerializedName("idCategory")
    var idCategory: String? = null,
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("unit")
    var unit: String? = null,
    @SerializedName("idSale")
    var idSale: String? = null,
    @SerializedName("sale")
    var sale: Sale? = null
)