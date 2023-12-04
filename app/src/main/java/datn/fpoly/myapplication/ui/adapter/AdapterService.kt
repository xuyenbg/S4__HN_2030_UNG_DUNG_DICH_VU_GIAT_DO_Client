package datn.fpoly.myapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemServiceBinding
import datn.fpoly.myapplication.utils.Common
import java.util.Locale

class AdapterService(val isStore: Boolean) : Adapter<AdapterService.ViewholderItemService>() {
    private val listService = mutableListOf<ServiceExtend>()
    var ListFiltered = mutableListOf<ServiceExtend>()

    private var serviceListenner: ServiceListenner? = null
    private lateinit var context: Context
    fun setData(list: MutableList<ServiceExtend>) {
        this.listService.clear()
        this.listService.addAll(list)
        ListFiltered.addAll(list)
        notifyDataSetChanged()
    }

    fun setListenner(listenner: ServiceListenner) {
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
        holder.binding.btnEdit.setOnClickListener {
            serviceListenner?.EditService(itemService)
        }

    }

    inner class ViewholderItemService(var binding: ItemServiceBinding) : ViewHolder(binding.root) {
        fun bind(item: ServiceExtend) {
            binding.tvNameService.text = item.name
            binding.tvPrice.text = "" + item.price + "đ/" + item.unit
            if (isStore) {
                Glide.with(binding.btnEdit).load(R.drawable.ic_edit).error(R.drawable.img_service)
                    .into(binding.btnEdit)
                binding.btnEdit.visibility = View.VISIBLE
            } else {
                binding.btnEdit.visibility = View.INVISIBLE
                Glide.with(binding.btnEdit).load(R.drawable.img_cart).error(R.drawable.img_service)
                    .into(binding.btnEdit)
            }
            Glide.with(binding.root).load(Common.baseUrl + item.image).error(R.drawable.img_service)
                .into(binding.imageService)

        }
    }

    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        listService.clear()
        if (charText.length == 0) {
            listService.addAll(ListFiltered)
        } else {
            for (s in ListFiltered) {
                if (s.name?.lowercase(Locale.getDefault())
                        ?.contains(charText) == true
                ) {
                    listService.add(s)
                }
            }
        }
        notifyDataSetChanged()
    }

    interface ServiceListenner {
        fun ServiceOnClick(item: ServiceExtend, position: Int)
        fun EditService(serviceExtend: ServiceExtend)
    }
}