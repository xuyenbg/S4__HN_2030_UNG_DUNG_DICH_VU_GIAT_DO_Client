package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.repository.StoreRepo
import datn.fpoly.myapplication.databinding.ItemStoreBinding

class AdapterStore(val limit: Int) : Adapter<AdapterStore.ViewHolderItemStore>() {
    private val listStore = mutableListOf<StoreModel>()
    private var storeListener: StoreListener?=null
    fun setData(list: MutableList<StoreModel>) {
        this.listStore.clear()
        this.listStore.addAll(list)
        notifyDataSetChanged()
    }
    fun setListener(listener: StoreListener){
        this.storeListener = listener
    }
    val getListStore: MutableList<StoreModel> = listStore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
       ViewHolderItemStore(ItemStoreBinding.inflate(
           LayoutInflater.from(parent.context)
       ))

    override fun getItemCount(): Int {
        if(limit==0){
            return listStore.size
        }else if(limit<=listStore.size){
            return limit
        }else{
            return listStore.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        if(!listStore.isEmpty()){
            val index = position
            val itemStore= listStore[index]
            holder.bind(itemStore)
            holder.itemView.setOnClickListener {
                storeListener?.onClickStoreListener(itemStore)
            }
        }

    }

    inner class ViewHolderItemStore(val binding: ItemStoreBinding) : ViewHolder(binding.root){
        fun  bind(item: StoreModel){
            binding.tvNameStore.text = item.name
            binding.tvRate.text = item.rate.toString()
            binding.tvLocation.text = item.idAddress?.adress
            binding.tvPhoneStore.text = item.iduser?.phone
        }
    }
    interface StoreListener{
        fun onClickStoreListener(storeModel: StoreModel)
    }
}