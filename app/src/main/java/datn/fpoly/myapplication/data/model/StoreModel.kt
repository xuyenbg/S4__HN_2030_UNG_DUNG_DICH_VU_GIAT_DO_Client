package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountResponse

class StoreModel(
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null,
    @SerializedName("idUser")
    var iduser: AccountResponse?=null,
    @SerializedName("idAddress")
    var idAddress :AdressModel ?=null,
    @SerializedName("status")
    var status : Int?,
    @SerializedName("rate")
    var rate: Double?,
    @SerializedName("transportTypeList")
    var transportTypeList: MutableList<String>?=null,
    @SerializedName("imageQRCode")
    var imageQACode : String?=null






)