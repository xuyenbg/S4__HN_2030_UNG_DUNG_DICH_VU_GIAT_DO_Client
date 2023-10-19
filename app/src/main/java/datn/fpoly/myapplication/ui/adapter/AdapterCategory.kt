package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.databinding.ItemCategoryServiceBinding

class AdapterCategory(var limits: Int) : Adapter<AdapterCategory.ViewHolderItemCategory>(){

    private val listCategoryService = mutableListOf<CategoryModel>()
    private var listener: CategoryListener?=null
    fun updateData(list: MutableList<CategoryModel>){
        this.listCategoryService.clear()
        this.listCategoryService.addAll(list)
        notifyDataSetChanged()
    }
    fun setListener(listener: CategoryListener){
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemCategory {
        return ViewHolderItemCategory(ItemCategoryServiceBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = if(limits==0) listCategoryService.size else limits
    override fun onBindViewHolder(holder: ViewHolderItemCategory, position: Int) {
        val index = position
        val item = listCategoryService[index]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener?.onClickCate(item)
        }
    }
    inner class ViewHolderItemCategory(val binding: ItemCategoryServiceBinding): ViewHolder(binding.root){
        fun bind(item: CategoryModel){
            binding.tvCategoryService.text = item.name
        }
    }
    interface CategoryListener{
        fun onClickCate(categoryModel: CategoryModel)
    }
}