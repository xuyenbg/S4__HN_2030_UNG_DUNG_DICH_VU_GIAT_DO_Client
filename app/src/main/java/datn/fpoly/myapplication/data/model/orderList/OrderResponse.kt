package datn.fpoly.myapplication.data.model.orderList

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderResponse (
    @SerializedName("_id") var id : String,
    @SerializedName("idUser") var idUser: String? = null,
    @SerializedName("idStore") var idStore: String? = null,
    @SerializedName("total") var total: Double? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("transportType") var transportType: String? = null,
    @SerializedName("methodPaymentType") var methodPaymentType: String? = null,
    @SerializedName("feeDelivery") var feeDelivery: Double? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("idAddress") var idAddress: String? = null,
    @SerializedName("isPaid") var isPaid: Boolean? = null,
    @SerializedName("createAt") var createAt : String,
    @SerializedName("updateAt") var updateAt : String,
    @SerializedName("listItem") var listItem: MutableList<ItemServiceResponse> = arrayListOf()
) : Serializable {
    override fun toString(): String {
        return "Order(idUser=$idUser, idStore=$idStore, total=$total, note=$note, transportType=$transportType, methodPaymentType=$methodPaymentType, feeDelivery=$feeDelivery, status=$status, idAddress=$idAddress, isPaid=$isPaid, listItem=$listItem)"
    }
}