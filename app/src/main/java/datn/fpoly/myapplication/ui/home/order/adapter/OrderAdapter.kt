package datn.fpoly.myapplication.ui.home.order.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.databinding.ItemListOrderBinding
import datn.fpoly.myapplication.utils.Common.formatDateOrder
import javax.inject.Inject

class OrderAdapter @Inject constructor() :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val listOrder = mutableListOf<OrderExtend>();
    private var orderListener: OrderListener? = null

    fun updateData(list: MutableList<OrderExtend>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        notifyDataSetChanged()
    }

    fun updateDataByStatus(list: List<OrderExtend>, statuses: List<Int>) {
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
                Log.d("itemOrder", itemOrder.toString())
                holder.binding.apply {
                    tvId.text = "#${itemOrder.id}"
                    tvNameStore.text = itemOrder.idStore?.name
                    tvTime.text = itemOrder.createAt?.formatDateOrder()
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
                        btnReOrder.setText("Đánh giá")
                    }
                    if (itemOrder.status == 4) {
                        tvStatus4.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
                    }
                }
                holder.binding.btnReOrder.setOnClickListener {
                    if(itemOrder.status==3){
                        orderListener?.onRateingOrder(itemOrder)
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
        fun onClickOrder(orderModel: OrderExtend)
        fun onRateingOrder(orderModel: OrderExtend)
    }
}