package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountModel
import java.io.Serializable

class StoreExtend(
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null,
    @SerializedName("idUser")
    var iduser: String?= null,
    @SerializedName("idAddress")
    var idAddress :String?= null,
    @SerializedName("status")
    var status : Int? =null,
    @SerializedName("rate")
    var rate: Double? =null,
    @SerializedName("transportTypeList")
    var transportTypeList: MutableList<String>?=null,
    @SerializedName("imageQRCode")
    var imageQACode : String?=null
) : Serializable