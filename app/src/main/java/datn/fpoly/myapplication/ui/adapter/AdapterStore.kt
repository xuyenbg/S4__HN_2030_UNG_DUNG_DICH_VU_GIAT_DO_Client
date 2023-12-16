package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.StoreNearplaceModel
import datn.fpoly.myapplication.data.repository.StoreRepo
import datn.fpoly.myapplication.databinding.ItemStoreBinding
import datn.fpoly.myapplication.utils.Common

class AdapterStore(val limit: Int) : Adapter<AdapterStore.ViewHolderItemStore>() {
    private val listStore = mutableListOf<StoreNearplaceModel>()
    private var storeListener: StoreListener? = null
    fun setData(list: MutableList<StoreNearplaceModel>) {
        this.listStore.clear()
        this.listStore.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: StoreListener) {
        this.storeListener = listener
    }

    val getListStore: MutableList<StoreNearplaceModel> = listStore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
        ViewHolderItemStore(
            ItemStoreBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    override fun getItemCount(): Int {
        if (limit == 0) {
            return listStore.size
        } else if (limit <= listStore.size) {
            return limit
        } else {
            return listStore.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        if (!listStore.isEmpty()) {
            val index = position
            val itemStore = listStore[index]
            holder.bind(itemStore)
            Glide.with(holder.itemView).load(Common.baseUrl + itemStore.image).placeholder(R.drawable.img_demo_store)
                .error(R.drawable.img_demo_store).into(holder.binding.imgAvateStore)
            holder.itemView.setOnClickListener {
                storeListener?.onClickStoreListener(itemStore)
            }
        }

    }

    inner class ViewHolderItemStore(val binding: ItemStoreBinding) : ViewHolder(binding.root) {
        fun bind(item: StoreNearplaceModel) {
            binding.tvNameStore.text = item.name
            binding.tvRate.text = item.rate.toString()
            binding.tvLocation.text = item.idAddress?.address
            binding.tvPhoneStore.text = item.iduser?.phone

            if(item.distance!! >=1000){
                binding.tvDistance.text="Cách khoảng: "+(item.distance!! /1000)+"km"
            }else{
                binding.tvDistance.text="Cách khoảng: "+(item.distance)+"m"
            }
            binding.tvDistance.visibility= View.INVISIBLE
        }
    }

    interface StoreListener {
        fun onClickStoreListener(storeModel: StoreNearplaceModel)
    }
}