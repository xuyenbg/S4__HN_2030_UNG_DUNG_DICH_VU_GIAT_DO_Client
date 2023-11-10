package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

class AddressModel (
    @SerializedName("_id")
    var id: String ?= null,
    @SerializedName("longitude")
    var longitude: Double=0.0,
    @SerializedName("latitude")
    var latitude: Double=0.0,
    @SerializedName("isDefalt")
    var isDefault: Boolean?= null,
    @SerializedName("idUser")
    var idUser: String ?= null,
    @SerializedName("address")
    var address: String?=null
)