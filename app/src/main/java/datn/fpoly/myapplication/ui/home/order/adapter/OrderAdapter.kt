package datn.fpoly.myapplication.ui.home.order.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.databinding.ItemListOrderBinding
import datn.fpoly.myapplication.utils.Common.formatDateOrder
import javax.inject.Inject

class OrderAdapter @Inject constructor() :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val listOrder = mutableListOf<OrderExtend>();
    private var orderListener: OrderListener? = null

    var holderOrder : OrderViewHolder?=null
    private lateinit var mContext: Context

    fun updateData(list: MutableList<OrderExtend>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
        notifyDataSetChanged()
    }

    fun updateDataByStatus(list: List<OrderExtend>, statuses: List<Int>) {
        val filteredList = mutableListOf<OrderExtend>()
        filteredList.clear()
        filteredList.addAll(list.filter { it.status in statuses })
        Log.e("AAAAAAAAAAA", "updateDataByStatus: "+statuses.size )
//        if(statuses[0]==4){
//          filteredList.addAll(list.filter { it.status in listOf<Int>(4) })
//        }
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
        mContext = parent.context
        return OrderViewHolder(ItemListOrderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        if (holder is OrderViewHolder) {
            holderOrder = holder
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
                    if(itemOrder.status==0){
                        tvStatus0.visibility=View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.INVISIBLE
                    }
                    else if (itemOrder.status == 1) {
                        tvStatus0.visibility=View.INVISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.VISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.INVISIBLE
                    }else if (itemOrder.status == 2) {
                        tvStatus0.visibility=View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        tvStatus2.visibility = View.VISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.INVISIBLE
                    }else if (itemOrder.status == 3) {
                        tvStatus0.visibility=View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.VISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
                        btnReOrder.setText("Đánh giá")
                    }else if (itemOrder.status == 4) {
                        tvStatus0.visibility=View.INVISIBLE
                        tvStatus4.visibility = View.INVISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.VISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
                        tvStatus3.text="Đơn đã hoàn thành"
                        tvStatus3.setTextColor(ContextCompat.getColor(mContext, R.color.gray))
                        holder.binding.btnReOrder.setText("Đã đánh giá")
                        holder.binding.btnReOrder.setBackgroundResource(R.drawable.shape_item_btn_4)
                    }else if(itemOrder.status==5){
                        tvStatus0.visibility=View.INVISIBLE
                        tvStatus4.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE
                        tvStatus3.visibility = View.INVISIBLE
                        tvStatus1.visibility = View.INVISIBLE
                        btnReOrder.visibility = View.VISIBLE
                    }
                }
                holder.binding.btnReOrder.setOnClickListener {
                    if(itemOrder.status==3){
                       holder.binding.btnReOrder.setText("Đã đánh giá")
                        holder.binding.btnReOrder.setBackgroundResource(R.drawable.shape_item_btn_4)
                        orderListener?.onRateingOrder(itemOrder)
                    }else if(itemOrder.status==5){
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