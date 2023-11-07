package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCompleted

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.account.AccountResponse
import datn.fpoly.myapplication.databinding.FragmentOrderCompletedBinding
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var orderAdapter : OrderAdapter
    private var order: Order? = null
    private var account: AccountResponse? = null
    private var id = "65257a540aa52df907b803cf"
    private var status = 1
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderCompletedBinding = FragmentOrderCompletedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handle(HomeViewAction.OrderActionGetList(id,status))
        orderAdapter = OrderAdapter()
        val itemDecoration = ItemSpacingDecoration(16)
        views.rcvItemOrder.addItemDecoration(itemDecoration)
        orderAdapter.setListener(object : OrderAdapter.OrderListener{
            override fun onClickOrder(orderModel: Order) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun invalidate() : Unit = withState(viewModel){
        super.invalidate()
        getListOrder(it)
    }
    private fun getListOrder(it: HomeViewState){
        when(it.stateOrder){
            is Success -> {
                runBlocking {
                    launch {
                        it.stateOrder.invoke()?.let {
                            Timber.tag("OrderUncompingFragment").d("orderinvalidate: ${it.size}")
                            orderAdapter.updateData(it)
                            views.rcvItemOrder.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderUncompingFragment", "getListOrder: ${it.size}")
                        }
                    }
                }
            }
            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrder: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrder: Fail")
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