package datn.fpoly.myapplication.ui.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.databinding.ItemListAddressBinding
import javax.inject.Inject

class AddressAdapter@Inject constructor():
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>(){
    private val listAddress = mutableListOf<AddressExtend>()
    private var addressListener: AddressListener? =null

    fun updateDataAddress(list: MutableList<AddressExtend>){
        this.listAddress.clear()
        this.listAddress.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: AddressListener){
        this.addressListener = listener
    }
    inner class AddressViewHolder(val binding: ItemListAddressBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(ItemListAddressBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        if(holder is AddressViewHolder){
            if(listAddress.isNotEmpty()){
                val item = listAddress[position]
                holder.itemView.setOnClickListener {
                    addressListener?.onClickAddress(item.id)
                }
                holder.binding.apply {
                    tvDefaultAddress.text = item.address
                    if(item.isDefault== true){
                        tvIsDefault.visibility = View.VISIBLE

                    }
                    if(item.isDefault==false){
                        tvIsDefault.visibility = View.INVISIBLE

                    }
//                    tvIsDefaultFalse.setOnClickListener {
//                        addressListener?.onClickAddressDefault(item.id, item.idUser?.id)
//                    }
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return listAddress.size
    }
    interface AddressListener {
        fun onClickAddress(id: String?)
//        fun onClickAddressDefault(idAddress: String?,idUser: String?)
    }
}