package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.ItemSlideshowBinding
import datn.fpoly.myapplication.utils.Common
import javax.inject.Inject

class SlideImageAdapter @Inject constructor(private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SlideImageAdapter.SlideViewHolder>() {
    private val listImage = mutableListOf<Int>()

    fun updateData(list: MutableList<Int>) {
        this.listImage.clear()
        this.listImage.addAll(list)
        notifyDataSetChanged()
    }

    inner class SlideViewHolder(val binding: ItemSlideshowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        return SlideViewHolder(
            ItemSlideshowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        if (holder is SlideViewHolder) {
            val slide = listImage[position]
            holder.binding.apply {
                imageSlide.setImageResource(slide)
            }
            if (position == listImage.size - 1) {
                viewPager2.post(runnable)
            }
        }
    }

    private val runnable = Runnable {
        listImage.addAll(listImage)
        notifyDataSetChanged()
    }
}