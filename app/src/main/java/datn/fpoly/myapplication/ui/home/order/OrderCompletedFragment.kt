package datn.fpoly.myapplication.ui.home.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.FragmentOrderCompletedBinding
import datn.fpoly.myapplication.ui.home.order.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var orderAdapter : OrderAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderCompletedBinding = FragmentOrderCompletedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = Hawk.get<AccountModel>("Account",null)
        viewModel.handle(HomeViewAction.OrderActionGetList(account.id.toString()))
        orderAdapter = OrderAdapter()
        val itemDecoration = ItemSpacingDecoration(16)
        views.rcvItemOrderComplete.addItemDecoration(itemDecoration)
        orderAdapter.setListener(object : OrderAdapter.OrderListener{
            override fun onClickOrder(order: OrderExtend) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(Common.KEY_ID_ORDER,order.id)
                startActivity(intent)
            }

        })
    }

    override fun invalidate() : Unit = withState(viewModel){
        super.invalidate()
        getListOrder(it)
    }
    override fun onResume(): Unit = withState(viewModel) {
        super.onResume()
        getListOrder(it)
    }
    private fun getListOrder(it: HomeViewState){
        when(it.stateOrder){
            is Success -> {
                runBlocking {
                    launch {
                        it.stateOrder.invoke()?.let {
                            Timber.tag("OrderCompleteFragment").d("orderCompleteInvalidate: ${it.size}")
                            orderAdapter.updateDataByStatus(it, listOf(3)) // Cập nhật danh sách đơn hàng đã hủy
                            views.rcvItemOrderComplete.adapter = orderAdapter
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
}