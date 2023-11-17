package datn.fpoly.myapplication.data.model

import com.google.gson.annotations.SerializedName
import datn.fpoly.myapplication.data.model.account.AccountModel
import java.util.Date

data class OrderExtend(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("idUser") var idUser: AccountModel? = AccountModel(),
    @SerializedName("idStore") var idStore: StoreModel? = StoreModel(),
    @SerializedName("total") var total: Double? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("transportType") var transportType: String? = null,
    @SerializedName("methodPaymentType") var methodPaymentType: String? = null,
    @SerializedName("feeDelivery") var feeDelivery: Double? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("idAddress") var idAddress: AddressModel? = AddressModel(),
    @SerializedName("isPaid") var isPaid: Boolean? = null,
    @SerializedName("createAt") var createAt: Date? = null,
    @SerializedName("updateAt") var updateAt: Date? = null,
    @SerializedName("listItem") var listItem: ArrayList<ItemServiceExtend> = arrayListOf(),
)