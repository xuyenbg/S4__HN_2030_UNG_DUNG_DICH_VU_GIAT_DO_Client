package datn.fpoly.myapplication.data.model.account

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.RoleModel
import datn.fpoly.myapplication.data.model.StoreExtend
import datn.fpoly.myapplication.data.model.StoreModel
import java.io.Serializable

data class AccountExtend(
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
    var idRole: RoleModel? = RoleModel(),
    @SerializedName("favoriteStores")
    var favoriteStores : MutableList<StoreExtend>? = arrayListOf(),
    @SerializedName("avatar")
    var avatar : String? = null
) : Serializable {
    override fun toString(): String {
        return "AccountModel(id=$id, phone=$phone, passwd=$passwd, fullname=$fullname, username=$username, idRole=$idRole, favoriteStores=$favoriteStores, avatar=$avatar)"
    }
}