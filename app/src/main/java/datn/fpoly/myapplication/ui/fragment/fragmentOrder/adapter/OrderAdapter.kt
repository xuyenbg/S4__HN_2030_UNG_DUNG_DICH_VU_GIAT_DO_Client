package datn.fpoly.myapplication.ui.fragment.fragmentOrder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemListOrderBinding
import datn.fpoly.myapplication.utils.DateTimeUtils
import javax.inject.Inject

class OrderAdapter @Inject constructor() :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val listOrder = mutableListOf<OrderResponse>();
    private var orderListener: OrderListener? = null

    fun updateData(list: MutableList<OrderResponse>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        notifyDataSetChanged()
    }

    fun updateDataByStatus(list: List<OrderResponse>, statuses: List<Int>) {
        val filteredList = list.filter { it.status in statuses }
        listOrder.clear()
        listOrder.addAll(filteredList)
        notifyDataSetChanged()
    }

    fun setListener(listener: OrderListener) {
        this.orderListener = listener
    }

    inner class OrderViewHolder(val binding: ItemListOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(ItemListOrderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        if (holder is OrderViewHolder) {
            if (listOrder.isNotEmpty()) {
                val itemOrder = listOrder[position]
                holder.itemView.setOnClickListener {
                    orderListener?.onClickOrder(itemOrder)
                }
                holder.binding.apply {
                    tvId.text = "#${itemOrder.id}"
                    tvNameStore.text = itemOrder.idStore
                    tvTime.text = DateTimeUtils.formatDateOrder(itemOrder.updateAt)
                    if (itemOrder.status == 1) {
                        tvStatus2.visibility = View.VISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.INVISIBLE
                    }
                    if (itemOrder.status == 2) {
                        tvStatus3.visibility = View.VISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.INVISIBLE
                    }
                    if (itemOrder.status == 3) {
                        tvStatus1.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
                    }
                    if (itemOrder.status == 4) {
                        tvStatus4.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
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