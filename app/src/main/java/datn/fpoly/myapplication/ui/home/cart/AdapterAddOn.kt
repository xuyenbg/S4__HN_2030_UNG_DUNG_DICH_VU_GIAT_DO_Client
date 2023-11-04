package datn.fpoly.myapplication.ui.home.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.AttributeModel
import datn.fpoly.myapplication.databinding.ItemAddOnInCartBinding
import datn.fpoly.myapplication.utils.Common.formatCurrency

class AdapterAddOn(private val context: Context, private val list: List<AttributeModel>) : Adapter<AdapterAddOn.ViewHolderItemStore>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
       ViewHolderItemStore(ItemAddOnInCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        val item = list[position]
        holder.bind(item = item, context = context)

    }

    inner class ViewHolderItemStore(val binding: ItemAddOnInCartBinding) : ViewHolder(binding.root){
        fun bind(item: AttributeModel, context: Context){
            binding.addOnName.text = item.name
            binding.addOnPrice.text = item.price.formatCurrency(null)
        }
    }
}