package datn.fpoly.myapplication.ui.check_out

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ItemService
import datn.fpoly.myapplication.databinding.ItemCartItemBinding
import datn.fpoly.myapplication.ui.home.cart.AdapterAddOn
import datn.fpoly.myapplication.utils.Common.formatCurrency

class AdapterItemCart2(
    private val context: Context,
    val list: List<ItemService>,
    val eventClick: (ItemService) -> Unit
) : Adapter<AdapterItemCart2.ViewHolderItemStore>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
        ViewHolderItemStore(
            ItemCartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        if (list.isNotEmpty()) {
            val item = list[position]
            holder.bind(item = item, context = context)
            holder.itemView.setOnClickListener {
                eventClick(item)
            }
        }
    }

    inner class ViewHolderItemStore(val binding: ItemCartItemBinding) : ViewHolder(binding.root) {
        fun bind(item: ItemService, context: Context) {
            item.service?.let {
                binding.serviceName.text = it.name
                binding.price.text = it.price?.formatCurrency(it.unit) ?: "-"
                binding.priceService.text =
                    (item.number?.times(it.price ?: 0.0) ?: 0.0).formatCurrency(null)
                if (it.attributeList.isNotEmpty()) {
                    binding.recyclerViewAddOn.adapter = AdapterAddOn(context, it.attributeList)
                }
                binding.groupAddOn.visibility = if (it.attributeList.isNotEmpty()) View.VISIBLE else View.GONE
            }
            binding.total.text = item.total?.formatCurrency(null) ?: "-"
            Glide.with(context).load(item.service?.image).error(R.drawable.image_no_pick).into(binding.image)
            binding.number2.text = String.format("SL: %d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            //Xử lý click expand
            binding.groupExpand.visibility = View.GONE
            binding.expand.setOnClickListener {
                if(binding.groupExpand.visibility == View.VISIBLE){
                    binding.expand.text = "chi tiết"
                    binding.expand.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down,0)
                    binding.groupExpand.visibility = View.GONE
                }else{
                    binding.expand.text = "thu gọn"
                    binding.expand.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_up,0)
                    binding.groupExpand.visibility = View.VISIBLE
                    binding.groupPickImage.visibility = View.GONE
                    if (item.service!!.unit?.lowercase().equals("kg")) {
                        binding.groupWeight.visibility = View.VISIBLE
                        binding.groupNumber.visibility = View.GONE
                    }else{
                        binding.groupWeight.visibility = View.GONE
                        binding.groupNumber.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}