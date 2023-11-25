package datn.fpoly.myapplication.ui.order.adapter

import android.Manifest
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import datn.fpoly.myapplication.utils.Common.formatCurrency
import gun0912.tedimagepicker.builder.TedImagePicker


class AdapterItemOrderStore(
    private val context: Context,
    private val list: MutableList<ItemServiceBase>,
    val onFillWeight: (Double, Int) -> Unit,
    val pickImage: (Uri, Int) -> Unit,
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
            val item = list[position]
            holder.bind(item = item, context = context, position = position)
            holder.itemView.setOnClickListener {
                onClick(item)
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
            if (item.service!!.unit?.lowercase().equals("kg")) {
                binding.number2.text = String.format("SL: %.1f %s",(item.number ?: 0.0).toDouble(), item.service!!.unit)
            }else{
                binding.number2.text = String.format("SL: %d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            }
            binding.number.text = String.format("%d %s",(item.number ?: 0.0).toInt(), item.service!!.unit)
            binding.numberKg.setText(item.number.toString())
            binding.numberKg.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.numberKg.text?.clear()
                }
            }
            binding.numberKg.setOnEditorActionListener{ _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onFillWeight(binding.numberKg.text.toString().toDouble(), position)
                    binding.numberKg.clearFocus()
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.numberKg.windowToken, 0)
                    true
                }else{
                    false
                }
            }
            val imagePick:String? = if(list[position].image?.contains("file") == false) RemoteDataSource.BASE_URL_IMAGE + list[position].image else list[position].image
            Glide.with(context)
                .load(imagePick)
                .centerCrop()
                .override(binding.pickImage.width, binding.pickImage.width)
                .error(R.drawable.img_pick_image)
                .into(binding.pickImage)
            //Chụp ảnh khi cân đồ
            //Callback. pick image
            val permissionListener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    TedImagePicker.with(context).start { uri ->
                        pickImage(uri,position)
                    }
                }
                override fun onPermissionDenied(deniedPermissions: List<String>) { }
            }
            binding.pickImage.setOnClickListener {
                //Yều cầu cấp quyền truy cập camera, bộ nhớ máy
                TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }
            //Xử lý collapse
            binding.groupExpand.visibility = View.GONE
            if (!item.service!!.unit?.lowercase().equals("kg")) {
                binding.groupPickImage.visibility = View.GONE
                binding.groupWeight.visibility = View.GONE
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
                        binding.groupNumber.visibility = View.GONE
                    }else{
                        binding.groupNumber.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}