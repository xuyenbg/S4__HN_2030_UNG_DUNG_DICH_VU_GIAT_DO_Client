package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.post.PostService
import datn.fpoly.myapplication.databinding.ItemExtraServiceBinding

class AdapterPostAttribute(): Adapter<AdapterPostAttribute.ViewHolderItemAttributePost>() {
    private val litsAttribute = mutableListOf<PostService.PostAttribute>()
    private var attributeListener: PostAttributeListener?=null
    fun getList(): MutableList<PostService.PostAttribute> = litsAttribute
    fun insertItem(item: PostService.PostAttribute){
        this.litsAttribute.add(item)
        notifyDataSetChanged()
    }
    fun setListener(listener: PostAttributeListener){
        this.attributeListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemAttributePost {
       return ViewHolderItemAttributePost(ItemExtraServiceBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int =litsAttribute.size

    override fun onBindViewHolder(holder: ViewHolderItemAttributePost, position: Int) {
        val index = position
        val itemAttribute = litsAttribute[index]
        holder.bind(itemAttribute)
        holder.binding.btnDelete.setOnClickListener {
            attributeListener?.onDelete(itemAttribute, index)
        }
    }
    inner class ViewHolderItemAttributePost(val binding: ItemExtraServiceBinding): ViewHolder(binding.root){
        fun bind(item: PostService.PostAttribute){
            binding.tvName.text = item.name +" "+item.price+"VNĐ"
        }

    }
    interface PostAttributeListener{
        fun onDelete(item: PostService.PostAttribute, index: Int)
    }

}