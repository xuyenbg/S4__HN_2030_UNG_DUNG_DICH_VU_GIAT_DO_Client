package datn.fpoly.myapplication.ui.address.check_out


import android.app.Notification.CarExtender
import android.content.Intent
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
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.databinding.ActivityCheckOutBinding
import datn.fpoly.myapplication.ui.address.AddressActivity
import datn.fpoly.myapplication.ui.confirm.ConfirmActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class CheckOutActivity : BaseActivity<ActivityCheckOutBinding>(), CheckOutViewModel.Factory {
    @Inject
    lateinit var addPosFactory: CheckOutViewModel.Factory

    private var adapterItemCart: AdapterItemCart? = null

    var cart: OrderBase? = null

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

    override fun onResume() {
        super.onResume()
        viewModel.handle(CheckOutViewAction.GetListAddress)
    }
    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            views.progressCircular.root.visibility = if(it.isLoading()) View.VISIBLE else View.GONE
            updateWithState(it)
        }
        viewModel.handle(CheckOutViewAction.GetListAddress)
        cart = intent.getSerializableExtra(Common.KEY_CART) as OrderBase?
        viewModel.handle(CheckOutViewAction.GetStoreById(cart?.idStore ?: "-"))
        views.total.text = cart?.total?.formatCurrency(null) ?: "- đ"
        adapterItemCart = cart?.listItem?.let { AdapterItemCart(this, it, eventClick = {}) }
        views.recyclerView.adapter = adapterItemCart
//        viewModel.getCart().observe(this) {
//            if (it != null && it.listItem.isNotEmpty()) {
//
//            }
//        }
        views.toolbar.title.text = "ĐẶT ĐƠN"
        views.toolbar.btnBack.setOnClickListener {
            this.finish()
        }
        views.changeAddress.setOnClickListener {
            startActivity(Intent(this@CheckOutActivity, AddressActivity::class.java))
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
                cart.status = 0
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
                        startActivity(Intent(this@CheckOutActivity, ConfirmActivity::class.java))
                    }
                }
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