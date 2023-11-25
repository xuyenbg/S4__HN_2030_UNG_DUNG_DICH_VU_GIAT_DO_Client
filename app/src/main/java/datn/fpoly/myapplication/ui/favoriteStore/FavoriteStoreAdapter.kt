package datn.fpoly.myapplication.ui.favoriteStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.ItemListFavoriteStoreBinding
import javax.inject.Inject

class FavoriteStoreAdapter@Inject constructor():
    RecyclerView.Adapter<FavoriteStoreAdapter.FavoriteStoreViewHolder>() {
    private val listFavoriteStore = mutableListOf<StoreExtend>()
    private var favoriteListener: FavoriteListener? = null

    fun updateDataFavorite(list:MutableList<StoreExtend>){
        this.listFavoriteStore.clear()
        this.listFavoriteStore.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: FavoriteListener) {
        this.favoriteListener = listener
    }

    inner class FavoriteStoreViewHolder(val binding: ItemListFavoriteStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStoreViewHolder {
        return FavoriteStoreViewHolder(ItemListFavoriteStoreBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: FavoriteStoreViewHolder, position: Int) {
        if(holder is FavoriteStoreViewHolder){
            if(listFavoriteStore.isNotEmpty()){
                val itemFavorite = listFavoriteStore[position]
                holder.binding.apply {
                    tvNameStore.text = itemFavorite.name
                    if(itemFavorite.status == 0){
                        tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.red_2))
                        tvStatus.text = "Đóng cửa"
                    }
                    if(itemFavorite.status == 1){
                        tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.green))
                        tvStatus.text = "Mở cửa"
                    }
                    btnFavorite.setOnClickListener {
                        favoriteListener?.onClickFavorite(itemFavorite.id)
                        btnFavoriteRe.visibility = View.VISIBLE
                        btnFavorite.visibility = View.INVISIBLE
                    }
                    btnFavoriteRe.setOnClickListener {
                        favoriteListener?.onClickFavoriteRe(itemFavorite.id)
                        btnFavoriteRe.visibility = View.INVISIBLE
                        btnFavorite.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return listFavoriteStore.size
    }
    interface FavoriteListener {
        fun onClickFavorite(id: String?)
        fun onClickFavoriteRe(id: String?)
    }
}