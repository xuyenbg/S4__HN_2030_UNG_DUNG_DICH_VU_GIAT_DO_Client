package datn.fpoly.myapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.databinding.ItemServiceBinding

class AdapterService : Adapter<AdapterService.ViewholderItemService>(){
    private val listService= mutableListOf<ServiceModel>()
    private var serviceListenner: ServiceListenner?=null
    private lateinit var context: Context
    fun setData(list: MutableList<ServiceModel>){
        this.listService.clear()
        this.listService.addAll(list)
        notifyDataSetChanged()
    }
    fun setListenner(listenner: ServiceListenner){
        this.serviceListenner = listenner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderItemService {
        context = parent.context
        return ViewholderItemService(ItemServiceBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = listService.size

    override fun onBindViewHolder(holder: ViewholderItemService, position: Int) {
        val index = position
        val itemService = listService[index]
        holder.bind(itemService)
        holder.itemView.setOnClickListener {
            serviceListenner?.ServiceOnClick(itemService, index)
        }

    }
    inner class ViewholderItemService(var binding: ItemServiceBinding): ViewHolder(binding.root){
        fun bind(item: ServiceModel){
            binding.tvNameService.text = item.name
            binding.tvPrice.text =""+ item.price+"đ/"+item.unit

        }
    }
    interface ServiceListenner{
        fun ServiceOnClick(item: ServiceModel, position: Int)
    }
}