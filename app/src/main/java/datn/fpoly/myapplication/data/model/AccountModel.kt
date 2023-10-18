package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class AccountModel(
    @SerializedName("phone")
    var phone : String?,
    @SerializedName("passwd")
    var passwd : String?,
    @SerializedName("fullname")
    var fullname : String?,
    @SerializedName("username")
    var username : String?,
    @SerializedName("idRole")
    var idRole : String?,
    @SerializedName("favouriteStores")
    var favouriteStores : List<String>?,
    @SerializedName("avatar")
    var avatar : String?
) {
}