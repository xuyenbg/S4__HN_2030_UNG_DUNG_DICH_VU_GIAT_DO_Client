package datn.fpoly.myapplication.ui.fragment.cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.data.model.ItemService
import datn.fpoly.myapplication.databinding.ItemOrderCartBinding
import datn.fpoly.myapplication.utils.Common.formatCurrency

class AdapterItemCart(private val context: Context) : Adapter<AdapterItemCart.ViewHolderItemStore>() {
    private val list = mutableListOf<ItemService>()
    private var storeListener: ItemCartListener? = null

    fun setData(list: MutableList<ItemService>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: ItemCartListener){
        this.storeListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
       ViewHolderItemStore(ItemOrderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        if(list.isNotEmpty()){
            val item= list[position]
            holder.bind(item = item, context = context)
            holder.itemView.setOnClickListener {
                storeListener?.onClickStoreListener(item)
            }
        }
    }

    inner class ViewHolderItemStore(val binding: ItemOrderCartBinding) : ViewHolder(binding.root){
        fun bind(item: ItemService, context: Context){
            Glide.with(context).load(item.image ?: "").into(binding.imgAvatar)
            item.service?.let {
                binding.tvName.text = it.name ?: ""
                binding.tvPrice.text = it.price?.formatCurrency(unit = item.service!!.unit) ?: ""
                binding.tvUnitQuantity.text = it.unit ?: ""
                binding.tvQuantity.text = if( item.number == null) "-" else item.number!!.toInt().toString()
                binding.tvPriceTotal.text = item.total?.formatCurrency(null)
            }
        }
    }
    interface ItemCartListener{
        fun onClickStoreListener(itemService: ItemService)
    }
}