package datn.fpoly.myapplication.ui.fragment.orderStore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemOrderHomeLaundryBinding
import datn.fpoly.myapplication.utils.Utils
import java.util.Locale

class OrderStoreAdapter(
    val onBtnAction: (OrderResponse) -> Unit,
    val itemOnclick: (OrderResponse) -> Unit
) : RecyclerView.Adapter<OrderStoreAdapter.OrderViewHolder>() {
    private var listOrder = mutableListOf<OrderResponse>();
    var ListFiltered = mutableListOf<OrderResponse>()

    fun updateData(list: MutableList<OrderResponse>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        ListFiltered.addAll(listOrder)
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
                holder.itemView.setOnClickListener {
                    itemOnclick(itemOrder)
                }
                holder.binding.apply {
                    tvOrderId.text = "#${itemOrder.id}"
                    tvFullName.text = itemOrder.idUser?.fullname
                    tvTime.text = Utils.formatDateOrder(itemOrder.updateAt)
                    tvStatusName.text = "Đã xong"
                    btnAction.text = "Xong"
                    btnAction.setOnClickListener {
                        onBtnAction(itemOrder)
                    }
                    btnAction.visibility = View.GONE
                }
            } else {

            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }


    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        listOrder.clear()
        if (charText.length == 0) {
            listOrder.addAll(ListFiltered)
        } else {
            for (s in ListFiltered) {
                if (s.idUser?.fullname?.lowercase(Locale.getDefault())
                        ?.contains(charText) == true
                ) {
                    listOrder.add(s)
                }
            }
        }
        notifyDataSetChanged()
    }
}