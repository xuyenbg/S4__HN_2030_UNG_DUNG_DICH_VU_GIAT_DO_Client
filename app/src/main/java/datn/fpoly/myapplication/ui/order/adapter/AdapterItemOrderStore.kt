package datn.fpoly.myapplication.ui.order.adapter

import android.Manifest
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.network.RemoteDataSource
import datn.fpoly.myapplication.databinding.ItemCartItemBinding
import datn.fpoly.myapplication.ui.home.cart.AdapterAddOn
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common.formatCurrency
import gun0912.tedimagepicker.builder.TedImagePicker
import timber.log.Timber


class AdapterItemOrderStore(
    private val context: Context,
    private val list: MutableList<ItemServiceBase>,
    val onClick: (ItemServiceBase) -> Unit
) : Adapter<AdapterItemOrderStore.ViewHolderItemStore>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemStore =
        ViewHolderItemStore(
            ItemCartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderItemStore, position: Int) {
        if (list.isNotEmpty()) {
            val i = position
            val item = list[position]
            holder.bind(item = item, context = context, position = position)
            holder.itemView.setOnClickListener {
                onClick(item)
            }

             val permissionListener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    TedImagePicker.with(context).start { uri ->
                        OrderDetailStoreActivity.listItem?.get(i)?.image = uri.toString()
                        notifyItemChanged(i)
                    }

                }

                override fun onPermissionDenied(deniedPermissions: List<String>) { }
            }
            holder.binding.pickImage.setOnClickListener {
                TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }
        }
    }

    inner class ViewHolderItemStore(val binding: ItemCartItemBinding) : ViewHolder(binding.root) {
        fun bind(item: ItemServiceBase, context: Context,  position: Int) {
            item.service?.let {
                binding.serviceName.text = it.name
                binding.price.text = it.price?.formatCurrency(it.unit) ?: "-"
                binding.priceService.text = it.price?.formatCurrency(null) ?: "-"
                binding.groupAddOn.visibility = if (it.attributeList?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
            if (!item.attributeListExtend.isNullOrEmpty()) {
                binding.recyclerViewAddOn.adapter = AdapterAddOn(context, item.attributeListExtend!!)
            }
            binding.total.text = item.total?.formatCurrency(null) ?: "-"
            Glide.with(context).load(RemoteDataSource.BASE_URL_IMAGE + item.service?.image).error(R.drawable.image_no_pick).into(binding.image)
            binding.number2.text = String.format("SL: %d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            binding.number.text = String.format("%d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            Timber.tag("URL Image").d(position.toString()+ ": " + list[position].image)
            Glide.with(context).load(list[position].image).error(R.drawable.img_pick_image).into(binding.pickImage)
            //Xử lý click expand
            binding.groupExpand.visibility = View.GONE
            if (!item.service!!.unit?.lowercase().equals("kg")) {
                binding.groupPickImage.visibility = View.GONE
            }
            binding.expand.setOnClickListener {
                if(binding.groupExpand.visibility == View.VISIBLE){
                    binding.expand.text = "chi tiết"
                    binding.expand.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down,0)
                    binding.groupExpand.visibility = View.GONE
                }else{
                    binding.expand.text = "thu gọn"
                    binding.expand.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_up,0)
                    binding.groupExpand.visibility = View.VISIBLE
                    if (item.service!!.unit?.lowercase().equals("kg")) {
                        binding.groupWeight.visibility = View.VISIBLE
                        binding.groupNumber.visibility = View.GONE
                    }else{
                        binding.groupWeight.visibility = View.GONE
                        binding.groupNumber.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}