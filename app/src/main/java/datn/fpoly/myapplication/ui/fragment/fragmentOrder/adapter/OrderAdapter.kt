package datn.fpoly.myapplication.ui.fragment.fragmentOrder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.databinding.ItemListOrderBinding
import javax.inject.Inject

class OrderAdapter @Inject constructor():
RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){
    private val listOrder = mutableListOf<Order>();
    private var orderListener: OrderListener? = null

    fun updateData(list: MutableList<Order>) {
        this.listOrder.clear()
        this.listOrder.addAll(list)
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
        if(holder is OrderViewHolder){
            if(listOrder.isNotEmpty()){
                val itemOrder = listOrder[position]
                holder.itemView.setOnClickListener {
                    orderListener?.onClickOrder(itemOrder)
                }
                holder.binding.apply {
                    tvNameStore.text = itemOrder.idStore
                    if(itemOrder.status==0){
                        tvStatus2.visibility = View.VISIBLE
                        tvStatus3.visibility = View.INVISIBLE;
                        tvStatus1.visibility = View.INVISIBLE;
                        tvStatus4.visibility = View.INVISIBLE;
                    }
                    if(itemOrder.status==1){
                        tvStatus3.visibility = View.VISIBLE
                        tvStatus1.visibility = View.INVISIBLE;
                        tvStatus2.visibility = View.INVISIBLE;
                        tvStatus4.visibility = View.INVISIBLE;
                    }
                    if(itemOrder.status==2){
                        tvStatus1.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE;
                        tvStatus3.visibility = View.INVISIBLE;
                        tvStatus4.visibility = View.INVISIBLE;
                    }
                    if(itemOrder.status==3){
                        tvStatus4.visibility = View.VISIBLE
                        tvStatus2.visibility = View.INVISIBLE;
                        tvStatus3.visibility = View.INVISIBLE;
                        tvStatus1.visibility = View.INVISIBLE;
                    }
                }
            }else{

            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }


    interface OrderListener {
        fun onClickOrder(orderModel: Order)
    }
}