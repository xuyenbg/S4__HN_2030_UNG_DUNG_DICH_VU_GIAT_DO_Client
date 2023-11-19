package datn.fpoly.myapplication.ui.fragment.homeStore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemOrderHomeLaundryBinding
import datn.fpoly.myapplication.utils.DateTimeUtils
import javax.inject.Inject

class OrderStoreWaitAdapter @Inject constructor(val onBtnAction: (OrderResponse) -> Unit) :
    RecyclerView.Adapter<OrderStoreWaitAdapter.OrderViewHolder>() {
    private val listOrder = mutableListOf<OrderResponse>();
    private var orderListener: OrderListener? = null

    fun updateData(list: MutableList<OrderResponse>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        notifyDataSetChanged()
    }

    fun updateDataByStatus(list: List<OrderResponse>) {
        listOrder.clear()
        listOrder.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: OrderListener) {
        this.orderListener = listener
    }

    inner class OrderViewHolder(val binding: ItemOrderHomeLaundryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(ItemOrderHomeLaundryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        if (holder is OrderViewHolder) {
            if (listOrder.isNotEmpty()) {
                val itemOrder = listOrder[position]
                holder.itemView.setOnClickListener {
                    orderListener?.onClickOrder(itemOrder)
                }
                holder.binding.apply {
                    tvOrderId.text = "#${itemOrder.id}"
                    tvFullName.text = itemOrder.idUser?.fullname
                    tvTime.text = DateTimeUtils.formatDateOrder(itemOrder.updateAt)

                    tvStatusName.text = "Chưa xác nhận"
                    btnAction.text = "Nhận đơn"
                    btnAction.setOnClickListener {
                        onBtnAction(itemOrder)
                    }
                }
            } else {

            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }


    interface OrderListener {
        fun onClickOrder(orderModel: OrderResponse)
    }
}