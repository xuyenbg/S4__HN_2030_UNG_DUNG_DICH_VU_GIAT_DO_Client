package datn.fpoly.myapplication.ui.order


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import datn.fpoly.myapplication.ui.order.adapter.AdapterItemOrderStore
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class OrderDetailStoreActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var orderViewModelFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()

    private var posItem: Int? = null

    companion object{
        var listItem: MutableList<ItemServiceBase>? = null
        var order:OrderExtend? = null
    }

    override fun getBinding(): ActivityOrderDetailBinding {
        return ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        val idOrder = intent.getStringExtra(Common.KEY_ID_ORDER)
        views.toolbar.btnBack.setOnClickListener {
            finish()
        }
        views.toolbar.btnNotification.visibility = View.INVISIBLE
        viewModel.handle(OrderViewAction.GetOrderDetail(idOrder ?: "null"))
        viewModel.subscribe(this) {
            updateWithState(it)
        }
    }

    private fun updateWithState(state: OrderViewState) {
        when(state.stateOrderDetail){
            is Success -> {
                order = state.stateOrderDetail.invoke()
                views.nameStore.text = order?.idStore?.name ?: "-"
                views.addressStore.text = order?.idStore?.idAddress?.address ?: "-"
                views.storePhone.text = order?.idStore?.iduser?.phone ?: "-"
                views.address.text = order?.idAddress?.address ?: "-"
                views.fullName.text = order?.idUser?.fullname ?: "-"
                views.phone.text = order?.idUser?.phone ?: "-"
                views.radioShip.text = order?.transportType ?: "-"
                views.note.setText(order?.note ?: "")
                if(order?.listItem != null && order!!.listItem.isNotEmpty()){
                    listItem = order!!.listItem.map {
                        ItemServiceBase(
                            it.number,
                            it.total,
                            it.image,
                            it.idOrder,
                            ServiceExtend(name = it.idService?.name, unit = it.idService?.unit, price = it.idService?.price, image = it.idService?.image),
                            it.attributeList,
                            it.attributeList?.map {attr -> attr.id }?.toMutableList()
                        )
                    }.toMutableList()
                    views.recyclerView.adapter = AdapterItemOrderStore(this@OrderDetailStoreActivity,listItem!!,
                        onFillWeight = { weight,pos ->
                            listItem!![pos].number = weight
                            updateTotalOrder(pos)
                            handleUpdateOrder()
                        },
                        pickImage = { imageUri, pos ->
                            posItem = pos
                            val file = File(imageUri.path ?: "-") // Chuyển URI thành File
                            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
                            viewModel.handle(OrderViewAction.UploadImage(image))
                        }) {}
                    views.tvServiceNumber.text = String.format("%d Dịch vụ", order!!.listItem.size)
                    views.total.text = order!!.total?.formatCurrency(null) ?: "- đ"
                }
                state.stateOrderDetail = Uninitialized
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
                Timber.tag("image pick: ").d(image)
                listItem!![posItem!!].image = image
                handleUpdateOrder()
                state.stateUploadImage = Uninitialized
            }
            is Fail -> {
                Toast.makeText(this, "Cập nhập đơn hàng không thành công, có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun updateTotalOrder(pos: Int) {
        var totalItem = listItem?.get(pos)?.total ?: 0.0
        listItem?.get(pos)?.attributeListExtend?.forEach {  totalItem += it.price  }
        totalItem *= listItem?.get(pos)?.number ?: 0.0
        listItem?.get(pos)?.total = totalItem

        var total = 0.0
        listItem?.forEach { total += it.total ?: 0.0 }
        order?.total = total
    }

    private fun handleUpdateOrder() {
        Timber.tag("image pick2: ").d(listItem?.get(posItem!!)?.image ?: "-")
        val orderBase = OrderBase(
            idUser = order?.idUser?.id,
            idStore = order?.idStore?.id,
            total = order?.total,
            note = order?.note,
            transportType = order?.transportType,
            methodPaymentType = order?.methodPaymentType,
            feeDelivery = order?.feeDelivery,
            status = order?.status,
            idAddress = order?.idAddress?.id,
            isPaid = order?.isPaid,
            listItem = listItem!!
        )
        viewModel.handle(OrderViewAction.UpdateOrder(orderBase, order?.id ?: "-"))
    }

    override fun create(initialState: OrderViewState) = orderViewModelFactory.create(initialState)


}