package datn.fpoly.myapplication.ui.address.check_out

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.databinding.ItemCartItemBinding
import datn.fpoly.myapplication.ui.home.cart.AdapterAddOn
import datn.fpoly.myapplication.utils.Common.formatCurrency
import java.text.DecimalFormat

class AdapterItemCart(
    private val context: Context,
    val list: MutableList<ItemServiceBase>,
    val eventClick: (ItemServiceBase) -> Unit
) : Adapter<AdapterItemCart.ViewHolderItemStore>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
        ViewHolderItemStore(ItemCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

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
        fun bind(item: ItemServiceBase, context: Context) {
            item.service?.let {
                binding.serviceName.text = it.name
                binding.price.text = it.price?.formatCurrency(it.unit) ?: "-"
                binding.priceService.text = it.price?.formatCurrency(null)
                binding.discount.text = if(it.idSale != null){
                    if (it.idSale?.unit.equals("%")) {
                        (it.price!! * it.idSale?.value!!/100).formatCurrency(null)
                    }else{
                        it.idSale?.value!!.formatCurrency(null)
                    }
                } else{
                    "0 đ"
                }
                binding.groupAddOn.visibility = if (it.attributeList?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
            if (!item.attributeListExtend.isNullOrEmpty()) {
                binding.recyclerViewAddOn.adapter = AdapterAddOn(context, item.attributeListExtend!!)
            }
            binding.total.text = item.total?.formatCurrency(null) ?: "-"
            Glide.with(context).load(item.service?.image).error(R.drawable.image_no_pick).into(binding.image)
            binding.number2.text = String.format("SL: %d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            binding.number.text = String.format("%d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            binding.numberKg.setText( String.format("%.1f",(item.number ?: 0.0).toDouble()))
            binding.numberKg.isEnabled = false
            //Xử lý click expand
            binding.groupWeight.visibility = View.GONE
            binding.groupExpand.visibility = View.GONE
            binding.groupPickImage.visibility = View.GONE
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