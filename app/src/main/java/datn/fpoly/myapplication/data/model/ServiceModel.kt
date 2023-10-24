package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class ServiceModel(
    @SerializedName("_id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("idStore") var isStore: String,
    @SerializedName("attributeList") var attributeList: MutableList<AttributeModel>? = null,
    @SerializedName("price") var price: Double,
    @SerializedName("image") var image: String,
    @SerializedName("isActive") var isActive: Boolean,
    @SerializedName("unit") var unit:String,
    @SerializedName("idCategory") var idCategory: CategoryModel,
//    @SerializedName("idSale") var idSale: MutableList<SaleModel>? = null
)