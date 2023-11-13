package datn.fpoly.myapplication.data.model.post

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StorePostModel(
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null,
    @SerializedName("idUser")
    var idUser: String?=null,
    @SerializedName("idAddress")
    var idAddress :String ?=null,
    @SerializedName("status")
    var status : Int,
    @SerializedName("rate")
    var rate: Double=0.0,
    @SerializedName("transportTypeList")
    var transportTypeList: MutableList<String>?=null,
    @SerializedName("imageQRCode")
    var imageQACode : String?=null
) : Serializable