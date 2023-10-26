package datn.fpoly.myapplication.ui.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.AttributeModel
import datn.fpoly.myapplication.databinding.ItemAttributeBinding

class AdapterAttribute: Adapter<AdapterAttribute.ViewHolderItemAttribute>() {
    private val listAttribute= mutableListOf<AttributeModel>()
    private var attributeListenner: AttributeListenner?=null
    var listAttributeSelect= mutableListOf<AttributeModel>()
    var selectBollent = SparseBooleanArray()
    fun setData(list: MutableList<AttributeModel>){
        this.listAttribute.clear()
        this.listAttribute.addAll(list)
        notifyDataSetChanged()
    }
    fun setListenner(listenner: AttributeListenner){
        this.attributeListenner = listenner
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemAttribute {
     return ViewHolderItemAttribute(ItemAttributeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = listAttribute.size

    override fun onBindViewHolder(holder: ViewHolderItemAttribute, position: Int) {
        val index = position
        val itemAttribute = listAttribute[index]
        holder.itemView.setOnClickListener {
//            attributeListenner?.onClickItem(itemAttribute)
        }
        holder.bind(itemAttribute)
        holder.bingind.cbAttribute1.setOnClickListener {
            selcetcItem(itemAttribute, index, holder)
        }
    }
    fun selcetcItem(item: AttributeModel, position: Int, holder: ViewHolderItemAttribute){
        if(!listAttributeSelect.contains(item)){
            listAttributeSelect.add(item)
            selectBollent.put(position, true)
            holder.bingind.cbAttribute1.isChecked = true
        }else{
            listAttributeSelect.remove(item)
            selectBollent.delete(position)
            holder.bingind.cbAttribute1.isChecked = false
        }
    }
    inner class ViewHolderItemAttribute(val bingind: ItemAttributeBinding): ViewHolder(bingind.root){
        fun bind(item: AttributeModel){
            bingind.tvPrice.text = item.price
            bingind.cbAttribute1.text = item.name
            bingind.cbAttribute1.isChecked = false
        }
    }
    interface AttributeListenner{
        fun onClickItem(attributeModel: AttributeModel)
    }
}