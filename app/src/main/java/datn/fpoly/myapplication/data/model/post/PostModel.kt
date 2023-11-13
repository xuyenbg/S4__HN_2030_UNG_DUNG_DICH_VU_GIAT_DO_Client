package datn.fpoly.myapplication.data.model.post

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
    val image: String?,
    @SerializedName("createAt")
    val date : String
) : Serializable