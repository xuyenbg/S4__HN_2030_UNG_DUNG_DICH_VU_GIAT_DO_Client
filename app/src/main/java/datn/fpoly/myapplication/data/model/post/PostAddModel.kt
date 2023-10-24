package datn.fpoly.myapplication.data.model.post

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.StoreModel

data class PostAddModel(
    @SerializedName("idStore")
    val idStore: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("image")
    val image: String
) {
}