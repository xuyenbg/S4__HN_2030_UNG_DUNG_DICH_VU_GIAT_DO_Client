package datn.fpoly.myapplication.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.network.RemoteDataSource
import datn.fpoly.myapplication.databinding.ItemCartItemBinding
import datn.fpoly.myapplication.ui.home.cart.AdapterAddOn
import datn.fpoly.myapplication.utils.Common.formatCurrency


class AdapterItemOrderHistory(
    private val context: Context,
    private val list: MutableList<ItemServiceBase>
) : Adapter<AdapterItemOrderHistory.ViewHolderItemStore>(){

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
            holder.bind(item = item, context = context, position = position)
        }
    }

    inner class ViewHolderItemStore(val binding: ItemCartItemBinding) : ViewHolder(binding.root) {
        fun bind(item: ItemServiceBase, context: Context,  position: Int) {
            item.service?.let {
                binding.serviceName.text = it.name
                binding.price.text = it.price?.formatCurrency(it.unit) ?: "-"
                binding.priceService.text = it.price?.formatCurrency(null) ?: "-"
                binding.groupAddOn.visibility = if (it.attributeList?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
            if (!item.attributeListExtend.isNullOrEmpty()) {
                binding.recyclerViewAddOn.adapter = AdapterAddOn(context, item.attributeListExtend!!)
            }
            binding.total.text = item.total?.formatCurrency(null) ?: "-"
            Glide.with(context).load(RemoteDataSource.BASE_URL_IMAGE + item.service?.image).error(R.drawable.image_no_pick).into(binding.image)
            if (item.service!!.unit?.lowercase().equals("kg")) {
                binding.number2.text = String.format("SL: %.1f %s",(item.number ?: 0.0).toDouble(), item.service!!.unit)
                binding.numberKg.isEnabled = false
                Glide.with(context)
                    .load(RemoteDataSource.BASE_URL_IMAGE + list[position].image)
                    .centerCrop()
                    .override(binding.pickImage.width, binding.pickImage.width)
                    .error(R.drawable.image_no_pick)
                    .into(binding.pickImage)
                binding.groupWeight.visibility = View.VISIBLE
                binding.groupNumber.visibility = View.GONE
            }else{
                binding.groupPickImage.visibility = View.GONE
                binding.groupWeight.visibility = View.GONE
                binding.groupNumber.visibility = View.VISIBLE
                binding.number2.text = String.format("SL: %d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            }
            binding.number.text = String.format("%d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            binding.numberKg.setText(item.number.toString())
            binding.expand.visibility = View.GONE
        }
    }
}