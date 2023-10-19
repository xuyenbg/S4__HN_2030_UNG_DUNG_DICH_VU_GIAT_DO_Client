package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class AdressModel (
    @SerializedName("_id")
    var id: String ?= null,
    @SerializedName("longitude")
    var longitude: Double=0.0,
    @SerializedName("latitude")
    var latitude: Double=0.0,
    @SerializedName("isDefalt")
    var isDefalt: Boolean?= null,
    @SerializedName("idUser")
    var idUser: String ?= null,
    @SerializedName("adress")
    var adress: String?=null
)