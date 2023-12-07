package datn.fpoly.myapplication.ui.order


import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import datn.fpoly.myapplication.ui.address.check_out.AdapterItemCart
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var orderViewModelFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()
    private var orderExtend: OrderExtend?=null

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
            views.progressCircular.root.visibility = if(it.isLoading()) View.VISIBLE else View.GONE
            updateWithState(it)
            updateStateWithUpdateStatus(it)
        }
        if(intent.getBooleanExtra("completed", false)){
            views.btnAction.visibility=View.GONE
            views.labelNoteTotal.visibility=View.GONE
        }else{
            views.btnAction.visibility=View.VISIBLE
            views.labelNoteTotal.visibility=View.VISIBLE
        }
        if(intent.getBooleanExtra("store", false)){
            views.btnActionCancel.visibility=View.GONE
        }else{
            views.btnActionCancel.visibility=View.VISIBLE
        }
        views.btnActionCancel.setOnClickListener {
            orderExtend?.id?.let { it1 -> OrderViewAction.UpdateStatus(it1, 5) }
                ?.let { it2 -> viewModel.handle(it2) }
        }
        views.toolbar.btnNotification.visibility = View.GONE
    }

    private fun updateWithState(state: OrderViewState) {
        when(state.stateOrderDetail){
            is Success -> {
                runBlocking {
                    launch {
                        val order = state.stateOrderDetail.invoke()
                        orderExtend= order
                        if(order?.status==0 && intent.getBooleanExtra("store", false)){
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
    private fun updateStateWithUpdateStatus(state: OrderViewState){
        when(state.stateUpdateStatus){
            is Loading->{
                views.progressCircular.root.visibility=View.VISIBLE
            }
            is Success->{
                runBlocking {
                    launch {
                        state.stateUpdateStatus.invoke()?.let{
                            views.progressCircular.root.visibility=View.GONE
                            Toast.makeText(this@OrderDetailActivity, "${it.message()}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }

            }
            is Fail->{
                views.progressCircular.root.visibility=View.GONE
                Toast.makeText(this, "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show()
            }
            else->{}
        }
    }

    override fun create(initialState: OrderViewState) = orderViewModelFactory.create(initialState)


}