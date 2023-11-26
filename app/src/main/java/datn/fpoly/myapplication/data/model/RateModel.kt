package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountExtend
import datn.fpoly.myapplication.data.model.account.AccountModel

class RateModel(
    @SerializedName("_id")
    val id: String,
    @SerializedName("idUser")
    val idUser: AccountExtend?,
    @SerializedName("rateNumber")
    val rateNumber : Float,
    @SerializedName("content")
    val content: String,
//    @SerializedName("idOrder")
//    val idOrder: String
): java.io.Serializable