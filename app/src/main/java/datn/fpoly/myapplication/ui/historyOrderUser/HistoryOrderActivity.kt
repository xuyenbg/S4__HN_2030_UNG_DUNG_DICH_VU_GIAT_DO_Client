package datn.fpoly.myapplication.ui.historyOrderUser

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
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
//                dialogRating(this@HistoryOrderActivity, orderModel)
            }
        })
        viewModel.subscribe(this){
            getListOrder(it)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun getListOrder(it: HistoryOrderViewState) {
        when (it.stateOrder) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateOrder.invoke()?.let {
                            Timber.tag("OrderCompleteFragment")
                                .d("orderCompleteInvalidate: ${it.size}")
                            orderAdapter.updateDataByStatus(it, listOf(3,4))
                            views.rcvListOrder.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderCompleteFragment", "getListOrderComplete: ${it.size}")
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: Fail")
            }

            else -> {

            }
        }
    }
    override fun getBinding(): ActivityHistoryOrderBinding {
        return ActivityHistoryOrderBinding.inflate(layoutInflater)
    }

    override fun create(initialState: HistoryOrderViewState): HistoryOrderViewModel {
        return listOrderFactory.create(initialState)
    }
}