package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class ServiceModel(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("idStore")
    var idStore: StoreModel? = null,
    @SerializedName("attributeList")
    var attributeList: MutableList<AttributeModel>? = null,
    @SerializedName("idCategory")
//    var idCategory: String? = null,
//    @SerializedName("category")
    var category: CategoryModel? = null,
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("unit")
    var unit: String? = null,
    @SerializedName("idSale")
//    var idSale: String? = null,
//    @SerializedName("sale")
    var sale: Sale? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("image")
    var image: String? = null
)