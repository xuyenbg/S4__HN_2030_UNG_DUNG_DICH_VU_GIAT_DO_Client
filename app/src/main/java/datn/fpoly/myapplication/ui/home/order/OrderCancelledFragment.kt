package datn.fpoly.myapplication.ui.home.order

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
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.FragmentOrderCancelledBinding
import datn.fpoly.myapplication.ui.check_out.CheckOutActivity
import datn.fpoly.myapplication.ui.home.order.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class OrderCancelledFragment : BaseFragment<FragmentOrderCancelledBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var orderAdapter: OrderAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderCancelledBinding = FragmentOrderCancelledBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = Hawk.get<AccountModel>("Account", null)
        viewModel.handle(HomeViewAction.OrderActionGetList(account.id.toString()))
        orderAdapter = OrderAdapter()
        val itemDecoration = ItemSpacingDecoration(16)
        views.rcvItemOrderCancelled.addItemDecoration(itemDecoration)
        orderAdapter.setListener(object : OrderAdapter.OrderListener {
            override fun onClickOrder(order: OrderExtend) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(Common.KEY_ID_ORDER, order.id)
                startActivity(intent)
            }

            override fun onRateingOrder(orderModel: OrderExtend) {
                val orderBase = OrderBase(
                    orderModel.idUser?.id,
                    orderModel.idStore?.id,
                    orderModel.total,
                    orderModel.note,
                    orderModel.transportType,
                    orderModel.methodPaymentType,
                    orderModel.feeDelivery,
                    orderModel.status,
                    orderModel.idAddress?.id,
                    orderModel.isPaid,
                    orderModel.toListItemBase()
                )
                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        CheckOutActivity::class.java
                    ).putExtra(Common.KEY_CART, orderBase)
                )
            }

        })
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()

        getListOrder(it)
    }

    override fun onResume(): Unit = withState(viewModel) {
        super.onResume()
        getListOrder(it)
    }

    private fun getListOrder(it: HomeViewState) {
        when (it.stateOrder) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateOrder.invoke()?.let {
                          // Cập nhật danh sách đơn hàng đã hủy
                            Timber.tag("OrderCancelledFragment").d("orderCancelledInvalidate: ${it.size}")
                            orderAdapter.updateDataByStatus(it, listOf(5)) // Cập nhật danh sách đơn hàng đã hủy
                            views.rcvItemOrderCancelled.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderCancelledFragment", "getListOrderCancelled: ${it.size}")
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderCancelled: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderCancelled: Fail")
            }

            else -> {

            }
        }
    }

    class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = spacing // Đặt khoảng dưới theo giá trị spacing bạn muốn
        }

    }
}