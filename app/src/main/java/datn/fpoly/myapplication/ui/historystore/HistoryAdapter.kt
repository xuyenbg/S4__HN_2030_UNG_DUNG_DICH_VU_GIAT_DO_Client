package datn.fpoly.myapplication.ui.historystore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ItemListHistoryStoreBinding
import datn.fpoly.myapplication.utils.Utils
import javax.inject.Inject

class HistoryAdapter @Inject constructor() :
    RecyclerView.Adapter<HistoryAdapter.ViewHolderItemPost>() {
    private val listHistory = mutableListOf<OrderResponse>()
    var onClicked : ((OrderResponse) -> Unit)?=null

    fun updateData(list: List<OrderResponse>) {
        this.listHistory.clear()
        this.listHistory.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolderItemPost(val binding: ItemListHistoryStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemPost {
        return ViewHolderItemPost(ItemListHistoryStoreBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ViewHolderItemPost, position: Int) {
        if (holder is ViewHolderItemPost) {
            if (listHistory.isNotEmpty()) {
                val itemHistory = listHistory[position]
                holder.binding.apply {
                    tvId.text = "#${itemHistory.id}"
                    tvNameStore.text = itemHistory.idUser?.fullname
                    tvTime.text = Utils.formatDateOrder(itemHistory.updateAt)

                    btnDetail.setOnClickListener {
                        onClicked?.invoke(itemHistory)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

}