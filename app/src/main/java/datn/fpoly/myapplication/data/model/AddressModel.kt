package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressModel (
    @SerializedName("_id")
    var id: String ?= null,
    @SerializedName("longitude")
    var longitude: Double? = null,
    @SerializedName("latitude")
    var latitude: Double? = null,
    @SerializedName("isDefault")
    var isDefault: Boolean?= null,
    @SerializedName("idUser")
    var idUser: String ?= null,
    @SerializedName("address")
    var address: String?=null
) : Serializable