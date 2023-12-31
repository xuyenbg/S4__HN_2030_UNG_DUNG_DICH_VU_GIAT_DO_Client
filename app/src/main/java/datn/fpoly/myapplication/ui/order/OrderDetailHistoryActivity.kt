package datn.fpoly.myapplication.ui.order


import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.OrderExtendHistory
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import datn.fpoly.myapplication.ui.order.adapter.AdapterItemOrderHistory
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import javax.inject.Inject

class OrderDetailHistoryActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var orderViewModelFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()

    private var posItem: Int? = null

    var order: OrderExtendHistory? = null

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
        views.btnAction.visibility = View.GONE
        views.btnActionCancel.visibility = View.GONE

        views.btnActionCancel.setOnClickListener {
            viewModel.handle(OrderViewAction.UpdateStatus(idOrder?:"",5))
        }
    }

    private fun updateWithState(state: OrderViewState) {
        views.progressCircular.root.visibility = if(state.isLoading()) View.VISIBLE else View.GONE

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
                    views.recyclerView.adapter = AdapterItemOrderHistory(this@OrderDetailHistoryActivity, order!!.toListItemBase())
                    views.tvServiceNumber.text = String.format("%d Dịch vụ", order!!.listItem.size)
                    views.total.text = order!!.total?.formatCurrency(null) ?: "- đ"
                    state.stateOrderDetail = Uninitialized
                }
              if (order?.status==0) {
                  views.btnActionCancel.visibility = View.VISIBLE
              } else {
                  views.btnActionCancel.visibility = View.GONE
              }
            }
            else -> {}
        }

        when(state.stateUpdateOrder){
            is Success -> {
                viewModel.handle(OrderViewAction.GetOrderDetail(order?.id ?: "-"))
//                state.stateUpdateOrder = Uninitialized
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

}