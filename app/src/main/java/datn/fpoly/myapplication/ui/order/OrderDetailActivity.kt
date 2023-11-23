package datn.fpoly.myapplication.ui.order


import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import datn.fpoly.myapplication.ui.check_out.AdapterItemCart
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var orderViewModelFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()

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
                runBlocking {
                    launch {
                        val order = state.stateOrderDetail.invoke()
                        views.nameStore.text = order?.idStore?.name ?: "-"
                        views.addressStore.text = order?.idStore?.idAddress?.address ?: "-"
                        views.storePhone.text = order?.idStore?.iduser?.phone ?: "-"
                        views.address.text = order?.idAddress?.address ?: "-"
                        views.fullName.text = order?.idUser?.fullname ?: "-"
                        views.phone.text = order?.idUser?.phone ?: "-"
                        views.radioShip.text = order?.transportType ?: "-"
                        views.note.setText(order?.note ?: "")
                        if(order?.listItem != null && order.listItem.isNotEmpty()){
                            val listItem = order.listItem.map {
                                ItemServiceBase(
                                    it.number,
                                    it.total,
                                    it.image,
                                    it.idOrder,
                                    ServiceExtend(name = it.idService?.name, unit = it.idService?.unit, price = it.idService?.price, image = it.idService?.image),
                                    it.attributeList,
                                    null
                                )
                            }.toMutableList()
                            views.recyclerView.adapter = AdapterItemCart(this@OrderDetailActivity,listItem) {}
                            views.tvServiceNumber.text = String.format("%d Dịch vụ", order.listItem.size)
                            views.total.text = order.total?.formatCurrency(null) ?: "- đ"
                        }


                    }
                }
            }
            else -> {}
        }
    }

    override fun create(initialState: OrderViewState) = orderViewModelFactory.create(initialState)


}