package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountModel

class AddressExtend (
    @SerializedName("_id")
    var id: String ?= null,
    @SerializedName("longitude")
    var longitude: Double=0.0,
    @SerializedName("latitude")
    var latitude: Double=0.0,
    @SerializedName("isDefault")
    var isDefault: Boolean?= null,
    @SerializedName("idUser")
    var idUser: AccountModel ?= null,
    @SerializedName("address")
    var address: String?=null
): java.io.Serializable {
    override fun toString(): String {
        return "AddressExtend(id=$id, longitude=$longitude, latitude=$latitude, isDefault=$isDefault, idUser=$idUser, address=$address)"
    }
}