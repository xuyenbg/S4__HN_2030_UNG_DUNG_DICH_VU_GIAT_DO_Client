package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("_id")
    var _id : String?,
    @SerializedName("phone")
    var phone : String?,
    @SerializedName("createAt")
    var createAt : String?,
    @SerializedName("updateAt")
    var updateAt : String?,
    @SerializedName("passwd")
    var passwd : String?,
    @SerializedName("fullname")
    var fullname : String?,
    @SerializedName("username")
    var idRole : String?,
    @SerializedName("favouriteStores")
    var favouriteStores : List<String>?,
) {
}