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
): java.io.Serializable {
    override fun toString(): String {
        return "OrderExtend(id=$id, idUser=$idUser, idStore=$idStore, total=$total, note=$note, transportType=$transportType, methodPaymentType=$methodPaymentType, feeDelivery=$feeDelivery, status=$status, idAddress=$idAddress, isPaid=$isPaid, createAt=$createAt, updateAt=$updateAt, listItem=$listItem)"
    }

    fun toListItemBase():MutableList<ItemServiceBase> =
         this.listItem.map {
            ItemServiceBase(
                it.number,
                it.total,
                it.image,
                it.idService?.id,
                ServiceExtend(name = it.idService?.name, unit = it.idService?.unit, price = it.idService?.price, image = it.idService?.image),
                it.attributeList,
                it.attributeList?.map {attr -> attr.id }?.toMutableList()
            )
        }.toMutableList()

    fun updateTotal(pos: Int) {
        var totalItem = this.listItem[pos].idService?.price ?: 0.0
        this.listItem[pos].attributeList?.forEach {  totalItem += it.price  }
        totalItem *= this.listItem[pos].number ?: 0.0
        this.listItem[pos].total = totalItem

        var total = 0.0
        this.listItem.forEach { total += it.total ?: 0.0 }
        this.total = total
    }

    fun toOrderBase() :OrderBase = OrderBase(
        idUser = this.idUser?.id,
        idStore = this.idStore?.id,
        total = this.total,
        note = this.note,
        transportType = this.transportType,
        methodPaymentType = this.methodPaymentType,
        feeDelivery = this.feeDelivery,
        status = this.status,
        idAddress = this.idAddress?.id,
        isPaid = this.isPaid,
        listItem = this.toListItemBase()
    )
}