package datn.fpoly.myapplication.ui.historyOrderUser

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import com.airbnb.mvrx.*
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityHistoryOrderBinding
import datn.fpoly.myapplication.databinding.DialogRateBinding
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.home.order.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DialogLoading
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class HistoryOrderActivity : BaseActivity<ActivityHistoryOrderBinding>(),HistoryOrderViewModel.Factory {
    @Inject
    lateinit var listOrderFactory: HistoryOrderViewModel.Factory
    private val viewModel: HistoryOrderViewModel by viewModel()
    private lateinit var orderAdapter: OrderAdapter
    lateinit var dialog: Dialog
    private val account = Hawk.get<AccountModel>("Account",null)
    private var idUser = account.id.toString()
    private var dialogLoading: Dialog_Loading?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        viewModel.handle(HistoryOrderViewAction.GetListHistoryOrder(idUser))
        orderAdapter = OrderAdapter()
        views.rcvListOrder.adapter = orderAdapter
        orderAdapter.setListener(object : OrderAdapter.OrderListener {
            override fun onClickOrder(order: OrderExtend) {
                val intent = Intent(this@HistoryOrderActivity, OrderDetailActivity::class.java)
                intent.putExtra(Common.KEY_ID_ORDER, order.id)
                startActivity(intent)
            }

            override fun onRateingOrder(orderModel: OrderExtend) {
                dialogRating(this@HistoryOrderActivity, orderModel)
            }
        })
        viewModel.subscribe(this){
            getListOrder(it)
            updateStateAddRate(it)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    private fun dialogRating(context: Context, orderExtend: OrderExtend) {
        dialog = Dialog(context)
        val bindingDialog = DialogRateBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.tran)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        bindingDialog.btnRating.setOnClickListener {
            orderExtend.idStore?.id?.let { it1 ->
                orderExtend.idUser?.id?.let { it2 ->
                    orderExtend.id?.let { it3 ->
                        HistoryOrderViewAction.AddRate(
                            it1,
                            it2,
                            bindingDialog.rate.rating,
                            bindingDialog.edContent.text.toString().trim(), it3
                        )
                    }
                }
            }?.let { it2 ->
                viewModel.handle(
                    it2
                )
            }
            dialog.dismiss()
        }
        bindingDialog.cancle.setOnClickListener {
            dialog.dismiss()
        }
        if (isFinishing == false) {
            dialog.show()
        }
    }
    private fun getListOrder(it: HistoryOrderViewState) {
        when (it.stateOrder) {
            is Success -> {
                runBlocking {
                    launch {
                        DialogLoading.hideDialog()
                        it.stateOrder.invoke()?.let { orders ->
                            val sortedOrders = orders.sortedByDescending { order -> order.createAt }
                            Timber.tag("OrderCompleteFragment")
                                .d("orderCompleteInvalidate: ${sortedOrders.size}")
                            orderAdapter.updateDataByStatus(sortedOrders, listOf(3,4))
                            views.rcvListOrder.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderCompleteFragment", "getListOrderComplete: ${sortedOrders.size}")
                        }
                    }
                }
            }

            is Loading -> {
                DialogLoading.showDialog(this)
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: loading")
            }

            is Fail -> {
                DialogLoading.hideDialog()
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: Fail")
            }

            else -> {

            }
        }
    }
    private fun updateStateAddRate(state: HistoryOrderViewState){
        when(state.stateRate){
            is Loading-> {
               DialogLoading.showDialog(this@HistoryOrderActivity)
                Timber.tag("AAAAAAAAAAAA").e("updateStateAddRate:loading ")
            }
            is Success->{
                runBlocking {
                    launch {
                        dialog.dismiss()
                        DialogLoading.hideDialog()
                        orderAdapter.holderOrder?.binding?.btnReOrder?.setText("Đã đánh giá")
                        orderAdapter.holderOrder?.binding?.btnReOrder?.setBackgroundResource(R.drawable.shape_item_btn_4)
                        Toast.makeText(this@HistoryOrderActivity, "Đánh giá thành công", Toast.LENGTH_SHORT).show()
                        state.stateRate = Uninitialized
                    }
                }
            }
            is Fail->{
                dialog.dismiss()
                DialogLoading.hideDialog()
                Toast.makeText(this@HistoryOrderActivity, "Đánh giá thất bại", Toast.LENGTH_SHORT).show()
                state.stateRate = Uninitialized
                Timber.tag("AAAAAAAAAAAA").e("updateStateAddRate:fail ")
            }
            else->{}
        }
    }
    override fun getBinding(): ActivityHistoryOrderBinding {
        return ActivityHistoryOrderBinding.inflate(layoutInflater)
    }

    override fun create(initialState: HistoryOrderViewState): HistoryOrderViewModel {
        return listOrderFactory.create(initialState)
    }
}