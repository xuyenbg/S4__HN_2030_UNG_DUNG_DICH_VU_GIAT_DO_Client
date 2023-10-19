package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class StoreModel(
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null,
    @SerializedName("idUser")
    var iduser: String?=null,
    @SerializedName("idAddress")
    var idAddress :AdressModel ?=null,
    @SerializedName("status")
    var status : Int=-1,
    @SerializedName("rate")
    var rate: Double=0.0,
    @SerializedName("transportTypeList")
    var transportTypeList: MutableList<String>?=null,
    @SerializedName("imageQRCode")
    var imageQACode : String?=null






)