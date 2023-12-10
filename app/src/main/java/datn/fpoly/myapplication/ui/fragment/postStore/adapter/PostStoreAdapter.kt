package datn.fpoly.myapplication.ui.fragment.postclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.databinding.ItemPostBinding
import datn.fpoly.myapplication.databinding.ItemPostStoreBinding
import datn.fpoly.myapplication.utils.Common
import javax.inject.Inject

class PostStoreAdapter @Inject constructor() :
    RecyclerView.Adapter<PostStoreAdapter.ViewHolderItemPost>() {
    private val listPost = mutableListOf<PostModel>()
    private var postlistener: PostListener? = null

    fun updateData(list: MutableList<PostModel>) {
        this.listPost.clear()
        this.listPost.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: PostListener) {
        this.postlistener = listener
    }

    inner class ViewHolderItemPost(val binding: ItemPostStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemPost {
        return ViewHolderItemPost(ItemPostStoreBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ViewHolderItemPost, position: Int) {
        if (holder is ViewHolderItemPost) {
            if (listPost.isNotEmpty()) {
                val itemPost = listPost[position]

                holder.binding.imageAvt.setOnClickListener {
                    postlistener?.onClickPost(itemPost)
                }
                holder.binding.apply {
                    if (itemPost.image == null) {
                        imagePostClient.visibility = View.GONE
                    }
                    tvName.text = itemPost.idStore.name
                    tvTitle.text = itemPost.title
                    tvContent.text = itemPost.content
                    tvDate.text = Common.convertISO8601ToCustomFormat(itemPost.date)
                    Glide.with(holder.itemView.context)
                        .load(Common.baseUrl + itemPost.image)
                        .into(imagePostClient)
                    Glide.with(holder.itemView.context)
                        .load(Common.baseUrl + itemPost.idStore.imageQACode).placeholder(R.drawable.avatar_profile).
                        error(R.drawable.avatar_profile)
                        .into(imageAvt)
                }
                holder.binding.icEdit.setOnClickListener {
                    postlistener?.onClickEdit(itemPost)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    interface PostListener {
        fun onClickPost(postModel: PostModel)
        fun onClickEdit(postModel: PostModel)
    }
}