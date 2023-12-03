package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountExtend

class NotifycationModel(
    @SerializedName("_id")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("createAt")
    var createAt: String,
    @SerializedName("idOrder")
    var idOrder: String,
    @SerializedName("idUser")
    var idUser:AccountExtend
): java.io.Serializable