package datn.fpoly.myapplication.data.model.post

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.StoreModel

data class PostModel(
    @SerializedName("_id")
    val id: String,
    @SerializedName("idStore")
    val idStore: StorePostModel,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("image")
    val image: String?
) {
}