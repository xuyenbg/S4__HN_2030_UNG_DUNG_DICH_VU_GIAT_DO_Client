package datn.fpoly.myapplication.ui.order


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.OrderExtendHistory
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import datn.fpoly.myapplication.ui.order.adapter.AdapterItemOrderStore
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class OrderDetailStoreActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var orderViewModelFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()

    private var posItem: Int? = null

    var order: OrderExtendHistory? = null
    private lateinit var adapter: AdapterItemOrderStore

    override fun getBinding(): ActivityOrderDetailBinding {
        return ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.toolbar.btnBack.setOnClickListener {
            finish()
        }
        views.toolbar.btnNotification.visibility = View.INVISIBLE
        val idOrder = intent.getStringExtra(Common.KEY_ID_ORDER)
        viewModel.handle(OrderViewAction.GetOrderDetail(idOrder ?: "null"))
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        views.btnAction.setOnClickListener {
            viewModel.handle(OrderViewAction.UpdateStatus(
                idOrder = order?.id ?: "-",
                status = order?.status?.plus(1) ?: -1
            ))
        }
        views.btnActionCancel.setOnClickListener {
            viewModel.handle(OrderViewAction.UpdateStatus(
                idOrder = order?.id ?: "-",
                status = 5
            ))
        }
        adapter =  AdapterItemOrderStore(this@OrderDetailStoreActivity,
            onFillWeight = { weight,pos ->
                posItem = pos
                order!!.listItem[pos].number = weight
                order!!.updateTotal(pos)
                handleUpdateOrder()
            },
            pickImage = { imageUri, pos ->
                posItem = pos
                val file = File(imageUri.path ?: "-") // Chuyển URI thành File
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
                viewModel.handle(OrderViewAction.UploadImage(image))
            }) {}
        adapter.setPickImageListenner(object : AdapterItemOrderStore.PickImageListenner{
            override fun pickImage(position: Int) {
                posItem=position
                if(Common.checkPermisstionMedia(this@OrderDetailStoreActivity)&&Common.checkPermisstionCamera(this@OrderDetailStoreActivity)){
                    ImagePicker.with(this@OrderDetailStoreActivity)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start()
                }else{
                    Common.requestPermisstionPIckImage(this@OrderDetailStoreActivity)
                }
            }
        })
    }

    private fun updateWithState(state: OrderViewState) {
        views.progressCircular.root.visibility = if(state.isLoading()) View.VISIBLE else View.GONE

        when(state.stateOrderDetail){
            is Success -> {
                order = state.stateOrderDetail.invoke()
                if(order?.status==0){
                    views.btnActionCancel.visibility=View.VISIBLE

                }else{
                    views.btnActionCancel.visibility=View.GONE
                }
                views.nameStore.text = order?.idStore?.name ?: "-"
                views.addressStore.text = order?.idStore?.idAddress?.address ?: "-"
                views.storePhone.text = order?.idStore?.iduser?.phone ?: "-"
                views.address.text = order?.idAddress?.address ?: "-"
                views.fullName.text = order?.idUser?.fullname ?: "-"
                views.phone.text = order?.idUser?.phone ?: "-"
                views.radioShip.text = order?.transportType ?: "-"
                views.note.setText(order?.note ?: "")
                if(order?.listItem != null && order!!.listItem.isNotEmpty()){
                    adapter.setListItemService(order!!.toListItemBase())
                    views.recyclerView.adapter =adapter
                    views.tvServiceNumber.text = String.format("%d Dịch vụ", order!!.listItem.size)
                    views.total.text = order!!.total?.formatCurrency(null) ?: "- đ"
                    state.stateOrderDetail = Uninitialized
                }
                when(order?.status){
                    0 -> {
                        views.btnAction.text = getString(R.string.receive)
                    }
                    1 -> {
                        views.btnAction.text = getString(R.string.washed)
                        views.btnActionCancel.visibility = View.GONE
                    }
                    2 -> {
                        views.btnAction.text = getString(R.string.delivery)
                    }
                    3 -> {
                        views.btnAction.visibility = View.GONE
                        views.btnActionCancel.visibility = View.GONE
                    }
                    4 -> {
                        views.btnAction.visibility = View.GONE
                        views.btnActionCancel.visibility = View.GONE
                    }
                }
            }
            else -> {}
        }

        when(state.stateUpdateOrder){
            is Success -> {
                viewModel.handle(OrderViewAction.GetOrderDetail(order?.id ?: "-"))
                state.stateUpdateOrder = Uninitialized
            }
            is Fail -> {
                Toast.makeText(this, "Cập nhập đơn hàng không thành công, có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }

        when(state.stateUploadImage){
            is Success -> {
                val image = state.stateUploadImage.invoke()?.body()?.string() ?: "-"
                if (image.isNotBlank()){
                    order!!.listItem[posItem!!].image = image
                    handleUpdateOrder()
                }
                state.stateUploadImage = Uninitialized
            }
            is Fail -> {
                Toast.makeText(this, "Cập nhập đơn hàng không thành công, có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }

        when(state.stateUpdateStatus){
            is Success -> {
                finish()
            }
            else -> {}
        }
    }

    private fun handleUpdateOrder() {
        viewModel.handle(OrderViewAction.UpdateOrder(order!!.toOrderBase(), order?.id ?: "-"))
    }

    override fun create(initialState: OrderViewState) = orderViewModelFactory.create(initialState)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val file = data!!.data!!.path?.let { File(it) } // Chuyển URI thành File
            val requestFile = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
            viewModel.handle(OrderViewAction.UploadImage(image))
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
        }
    }
}