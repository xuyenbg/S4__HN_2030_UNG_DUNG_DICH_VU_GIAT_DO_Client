package datn.fpoly.myapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.RateModel
import datn.fpoly.myapplication.databinding.ItemRateBinding

class AdapterRate : Adapter<AdapterRate.ViewHolderItemRate>() {

    private val listRate = mutableListOf<RateModel>()
    private lateinit var mContext: Context
    fun initData(list: MutableList<RateModel>) {
        this.listRate.clear()
        this.listRate.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolderItemRate(val binding: ItemRateBinding) : ViewHolder(binding.root) {
        fun bind(item: RateModel) {
            binding.tvNameUser.text = item.idUser?.fullname
            binding.tvContent.text = item.content
            binding.rate.rating = item.rateNumber
            Glide.with(binding.imgAvatar).load(item.idUser?.avatar)
                .error(ContextCompat.getDrawable(mContext, R.drawable.avatar_profile))
                .into(binding.imgAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemRate {
        mContext =parent.context
        return ViewHolderItemRate(ItemRateBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = listRate.size

    override fun onBindViewHolder(holder: ViewHolderItemRate, position: Int) {
        val index = position
        val itemRate = listRate[index]
        holder.bind(itemRate)
        if(index==listRate.size-1){
            holder.binding.line.visibility= View.INVISIBLE
        }else{
            holder.binding.line.visibility= View.VISIBLE
        }
    }
}