package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.databinding.ItemCategoryLlinearBinding
import datn.fpoly.myapplication.databinding.ItemCategoryServiceBinding
import datn.fpoly.myapplication.utils.Common

class AdapterCategory(var limits: Int, val linearEnable: Boolean) :
    Adapter<ViewHolder>() {

    private val listCategoryService = mutableListOf<CategoryModel>()
    private var listener: CategoryListener? = null
    fun updateData(list: MutableList<CategoryModel>) {
        this.listCategoryService.clear()
        this.listCategoryService.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: CategoryListener) {
        this.listener = listener
    }

    var getListCate = listCategoryService


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (linearEnable) ViewHolderItemCategoryLinear(
            ItemCategoryLlinearBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        ) else ViewHolderItemCategory(
            ItemCategoryServiceBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        if (limits == 0) {
            return listCategoryService.size
        } else if (limits <= listCategoryService.size) {
            return limits
        } else {
            return listCategoryService.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listCategoryService.size != 0) {
            val index = position
            val item = listCategoryService[index]
            if (linearEnable) {
                val holderItemCateLinear = holder as ViewHolderItemCategoryLinear
                holderItemCateLinear.bind(item)
                holderItemCateLinear.itemView.setOnClickListener {
                    listener?.onClickCate(item)
                }
            } else {
                val holderItemCate = holder as ViewHolderItemCategory
                holder.bind(item)
                holderItemCate.itemView.setOnClickListener {
                    listener?.onClickCate(item)
                }
            }
        }
    }

    inner class ViewHolderItemCategory(val binding: ItemCategoryServiceBinding) :
        ViewHolder(binding.root) {
        fun bind(item: CategoryModel) {
            binding.tvCategoryService.text = item.name
            Glide.with(itemView).load(Common.baseUrl+item.image).placeholder(R.drawable.image_category).error(R.drawable.image_category).into(binding.imgCategory)

        }
    }

    inner class ViewHolderItemCategoryLinear(val binding: ItemCategoryLlinearBinding) :
        ViewHolder(binding.root) {
        fun bind(item: CategoryModel) {
            binding.tvCategoryService.text = item.name
            Glide.with(itemView).load(Common.baseUrl+item.image).placeholder(R.drawable.image_category).error(R.drawable.image_category).into(binding.imgCategory)

        }
    }

    interface CategoryListener {
        fun onClickCate(categoryModel: CategoryModel)
    }
}