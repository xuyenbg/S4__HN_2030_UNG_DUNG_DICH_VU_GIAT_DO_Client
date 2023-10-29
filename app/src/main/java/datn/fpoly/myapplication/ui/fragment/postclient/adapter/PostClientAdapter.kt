package datn.fpoly.myapplication.ui.fragment.postclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.databinding.ItemPostBinding
import datn.fpoly.myapplication.utils.Common
import javax.inject.Inject

class PostClientAdapter @Inject constructor() :
    RecyclerView.Adapter<PostClientAdapter.ViewHolderItemPost>() {
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

    inner class ViewHolderItemPost(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemPost {
        return ViewHolderItemPost(ItemPostBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ViewHolderItemPost, position: Int) {
        if (holder is ViewHolderItemPost) {
            if (listPost.isNotEmpty()) {
                val itemPost = listPost[position]

                holder.itemView.setOnClickListener {
                    postlistener?.onClickPost(itemPost)
                }
                holder.binding.apply {
                    if (itemPost.image=="") {
                        imagePostClient.visibility = View.GONE
                    }
                    tvName.text = itemPost.idStore.name
                    tvTitle.text = itemPost.title
                    tvContent.text = itemPost.content
                    tvDate.text = Common.convertISO8601ToCustomFormat(itemPost.date)
                    Glide.with(holder.itemView.context)
                        .load(itemPost.image)
                        .into(imagePostClient)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    interface PostListener {
        fun onClickPost(postModel: PostModel)
    }
}