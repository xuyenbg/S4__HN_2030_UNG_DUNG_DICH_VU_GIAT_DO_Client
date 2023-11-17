package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName

data class AccountModel(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("passwd")
    var passwd: String?= null,
    @SerializedName("fullname")
    var fullname: String? = null,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("idRole")
    var idRole: String? = null,
    @SerializedName("favoriteStores")
    var favoriteStores : List<String>? = null,
    @SerializedName("avatar")
    var avatar : String? = null,
) {
    override fun toString(): String {
        return "AccountModel(id=$id, phone=$phone, passwd=$passwd, fullname=$fullname, username=$username, idRole=$idRole, favoriteStores=$favoriteStores, avatar=$avatar)"
    }
}