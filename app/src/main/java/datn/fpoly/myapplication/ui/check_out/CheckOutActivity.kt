package datn.fpoly.myapplication.ui.check_out


import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.databinding.ActivityCheckOutBinding
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class CheckOutActivity : BaseActivity<ActivityCheckOutBinding>(), CheckOutViewModel.Factory {
    @Inject
    lateinit var addPosFactory: CheckOutViewModel.Factory

    var adapterItemCart:AdapterItemCart2? = null

    var cart: Order? = null

    var address: AddressExtend? = null

    private val viewModel: CheckOutViewModel by viewModel()

    override fun getBinding(): ActivityCheckOutBinding {
        return ActivityCheckOutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        viewModel.handle(CheckOutViewAction.GetListAddress)
        viewModel.getCart().observe(this) {
            if (it != null && it.listItem.isNotEmpty()) {
                cart = it
                viewModel.handle(CheckOutViewAction.GetStoreById(it.idStore ?: "-"))
                views.total.text = it.total?.formatCurrency(null) ?: "- đ"
                adapterItemCart = AdapterItemCart2(this, it.listItem, eventClick = {})
                views.recyclerView.adapter = adapterItemCart
            }
        }
        views.toolbar.title.text = "ĐẶT ĐƠN"
        views.toolbar.btnBack.setOnClickListener {
            this.finish()
        }
        views.toolbar.btnNotification.visibility = View.INVISIBLE
        views.btnAction.text = "Xác nhận"
        views.btnAction.setOnClickListener {
            cart?.let { cart ->
                cart.idAddress = address?.id
                cart.note = views.note.text.toString()
                cart.methodPaymentType = "Thanh toán tiền mặt"
                cart.transportType = if(views.radioShip.isChecked) "Shipper" else "Mang tới cửa hàng"
                cart.feeDelivery = if(views.radioShip.isChecked) 30000.0 else 0.0
                cart.isPaid = false
                cart.status = 1
                viewModel.handle(CheckOutViewAction.InsertOrder(cart))
            }
        }
    }

    private fun updateWithState(state: CheckOutViewState) {
        when(state.stateGetStoreById){
            is Success -> {
                runBlocking {
                    launch {
                        val store = state.stateGetStoreById.invoke()
                        views.nameStore.text = store?.name ?: "-"
                        views.addressStore.text = store?.idAddress?.address ?: "-"
                        views.storePhone.text = store?.iduser?.phone ?: "-"
                        state.stateGetStoreById = Uninitialized
                    }
                }
            }
            else -> {}
        }
        when(state.stateInsertOrder){
            is Success -> {
                runBlocking {
                    launch {
                        viewModel.clearCart()
                        state.stateInsertOrder = Uninitialized
                    }
                }
                Toast.makeText(this, "Đặt dịch vụ thành công", Toast.LENGTH_SHORT).show()
            }
            is Fail ->{
                Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
        when(state.stateGetListAddress){
            is Success -> {
                runBlocking {
                    launch {
                        val list = state.stateGetListAddress.invoke()
                        val addressList = state.stateGetListAddress.invoke()?.filter { address -> address.isDefault == true }
                        Timber.tag("addressList").d(list.toString())
                        if (!addressList.isNullOrEmpty()) {
                            address = addressList.first()
                            views.address.text = address?.address ?: "-"
                            views.fullName.text = address?.idUser?.fullname ?: "-"
                            views.phone.text = address?.idUser?.phone ?: "-"
                        }
                        state.stateGetListAddress = Uninitialized
                    }
                }
            }

            else -> {}
        }
    }

    override fun create(initialState: CheckOutViewState) = addPosFactory.create(initialState)


}