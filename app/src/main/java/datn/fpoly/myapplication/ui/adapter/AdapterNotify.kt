package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.NotifycationModel
import datn.fpoly.myapplication.databinding.ItemNotifycationBinding
import datn.fpoly.myapplication.utils.Utils

class AdapterNotify : Adapter<AdapterNotify.ViewHolderItemNotify>() {
    private val listNotify = mutableListOf<NotifycationModel>()
    fun initData(list: MutableList<NotifycationModel>) {
        this.listNotify.clear()
        this.listNotify.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolderItemNotify(val bining: ItemNotifycationBinding) :
        ViewHolder(bining.root) {
            fun bind(item: NotifycationModel){
                bining.tvTitle.text = item.title
                bining.tvBody.text= item.body
                bining.tvDate.text= Utils.formatDateOrder(item.createAt)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemNotify {
        return ViewHolderItemNotify(ItemNotifycationBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = listNotify.size

    override fun onBindViewHolder(holder: ViewHolderItemNotify, position: Int) {
        val index = position
        val itemNoti = listNotify[index]
        holder.bind(itemNoti)
        if(index==listNotify.size-1){
            holder.bining.line.visibility= View.INVISIBLE
        }else{
            holder.bining.line.visibility= View.VISIBLE
        }
    }

}