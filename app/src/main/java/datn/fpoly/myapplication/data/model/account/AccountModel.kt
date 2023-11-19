package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccountModel(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("passwd")
    var passwd: String?,
    @SerializedName("fullname")
    var fullname: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("idRole")
    var idRole: String?,
    @SerializedName("favoriteStores")
    var favoriteStores : List<String>?,
    @SerializedName("avatar")
    var avatar : String?
) : Serializable {
    override fun toString(): String {
        return "AccountModel(id=$id, phone=$phone, passwd=$passwd, fullname=$fullname, username=$username, idRole=$idRole, favoriteStores=$favoriteStores, avatar=$avatar)"
    }
}