package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderUncomping

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.FragmentOrderUncompingBinding
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class OrderUncompingFragment : BaseFragment<FragmentOrderUncompingBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var orderAdapter : OrderAdapter
    private var uncompingOrders: List<Order>? = null
    private var order: Order? = null
    private var account: AccountModel? = null
    private var id = "65257a540aa52df907b803cf"
    private var status = 1
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderUncompingBinding = FragmentOrderUncompingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = Hawk.get<AccountModel>("Account",null)
        viewModel.handle(HomeViewAction.OrderActionGetList(account.id.toString()))
        orderAdapter = OrderAdapter()
        val itemDecoration = ItemSpacingDecoration(0)
        views.rcvItemOrderUncomping.addItemDecoration(itemDecoration)
        orderAdapter.setListener(object : OrderAdapter.OrderListener{
            override fun onClickOrder(orderModel: OrderResponse) {
                startActivity(Intent(context,OrderDetailActivity::class.java))
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
                            Timber.tag("OrderUncompingFragment").d("orderUncompingInvalidate: ${it.size}")
                            orderAdapter.updateDataByStatus(it, listOf(1,2)) // Cập nhật danh sách đơn hàng đã hủy
                            views.rcvItemOrderUncomping.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderUncompingFragment", "getListOrderUncomping: ${it.size}")
                        }
                    }
                }
            }
            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderUncomping: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderUncomping: Fail")
            }
            else -> {

            }
        }
    }
}
class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = spacing // Đặt khoảng dưới theo giá trị spacing bạn muốn
    }


}