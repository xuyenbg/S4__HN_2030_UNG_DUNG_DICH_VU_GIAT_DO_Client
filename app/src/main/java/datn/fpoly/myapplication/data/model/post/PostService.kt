package datn.fpoly.myapplication.data.model.post

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostService(
    @SerializedName("name")
    var name: RequestBody,
    @SerializedName("price")
    var price: RequestBody,
    @SerializedName("attributeList")
    var attributeList: RequestBody,
    @SerializedName("image")
    var image: MultipartBody.Part?,
    @SerializedName("isActive")
    var isActive: Boolean = true,
    @SerializedName("unit")
    var unit: RequestBody,
    @SerializedName("idCategory")
    var idCategory: RequestBody,
    @SerializedName("idStore")
    var idStore: RequestBody,
    @SerializedName("unitSale")
    var unitSale: RequestBody,
    @SerializedName("valueSale")
    var valueSale: RequestBody


) {
    class PostAttribute(
        @SerializedName("name") var name: String,
        @SerializedName("price") var price: Double
    )

    class PostSale(
        @SerializedName("unit") var unit: String,
        @SerializedName("value") var value: Double
    )

}