package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountModel

class StoreNearplaceModel(
    @SerializedName("_id")
    var id: String?=null,
    @SerializedName("name")
    var name : String?=null,
    @SerializedName("idUser")
    var iduser: AccountModel?= AccountModel(),
    @SerializedName("idAddress")
    var idAddress :AddressModel ?= AddressModel(),
    @SerializedName("status")
    var status : Int? =null,
    @SerializedName("rate")
    var rate: Double? =null,
    @SerializedName("transportTypeList")
    var transportTypeList: MutableList<String>?=null,
    @SerializedName("image")
    var image : String?=null,
    @SerializedName("distance")
    var distance: Double?=null,
    @SerializedName("distanceUnit")
    var distanceUnit: String?=null
): java.io.Serializable