package datn.fpoly.myapplication.ui.fragment.homeStore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemOrderHomeLaundryBinding
import datn.fpoly.myapplication.utils.Utils
import javax.inject.Inject

class OrderStoreCompleteMissionAdapter @Inject constructor(val onBtnAction: (OrderResponse) -> Unit) :
    RecyclerView.Adapter<OrderStoreCompleteMissionAdapter.OrderViewHolder>() {
    private val listOrder = mutableListOf<OrderResponse>();

    fun updateData(list: MutableList<OrderResponse>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        notifyDataSetChanged()
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
                holder.binding.apply {
                    tvOrderId.text = "#${itemOrder.id}"
                    tvFullName.text = itemOrder.idUser?.fullname
                    tvTime.text = Utils.formatDateOrder(itemOrder.updateAt)

                    tvStatusName.text = "Hoàn Thành"
                    btnAction.text = "Hoàn Thành"
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

}