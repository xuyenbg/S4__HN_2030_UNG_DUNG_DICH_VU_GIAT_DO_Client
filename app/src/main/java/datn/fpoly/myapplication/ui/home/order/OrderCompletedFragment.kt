package datn.fpoly.myapplication.ui.home.order

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.DialogRateBinding
import datn.fpoly.myapplication.databinding.FragmentOrderCompletedBinding
import datn.fpoly.myapplication.ui.home.order.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DialogLoading
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var orderAdapter: OrderAdapter
    lateinit var dialog: Dialog

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderCompletedBinding = FragmentOrderCompletedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = Hawk.get<AccountModel>("Account", null)
        viewModel.handle(HomeViewAction.OrderActionGetList(account.id.toString()))
        orderAdapter = OrderAdapter()
        val itemDecoration = ItemSpacingDecoration(16)
        views.rcvItemOrderComplete.addItemDecoration(itemDecoration)
        orderAdapter.setListener(object : OrderAdapter.OrderListener {
            override fun onClickOrder(order: OrderExtend) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra("completed", true)
                intent.putExtra(Common.KEY_ID_ORDER, order.id)
                startActivity(intent)
            }

            override fun onRateingOrder(orderModel: OrderExtend) {
                dialogRating(requireContext(), orderModel)
            }
        })
        views.swipeToRefresh.setOnRefreshListener {
            if(views.swipeToRefresh.isRefreshing){
                viewModel.handle(HomeViewAction.OrderActionGetList(account.id.toString()))
            }
        }
    }

    private fun dialogRating(context: Context, orderExtend: OrderExtend) {
        dialog = Dialog(context)
        val bindingDialog = DialogRateBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.tran)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        bindingDialog.btnRating.setOnClickListener {
            orderExtend.idStore?.id?.let { it1 ->
                orderExtend.idUser?.id?.let { it2 ->
                    orderExtend.id?.let { it3 ->
                        HomeViewAction.AddRate(
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
        }
        bindingDialog.cancle.setOnClickListener {
            dialog.dismiss()
        }
        if (activity?.isFinishing == false) {
            dialog.show()
        }
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getListOrder(it)
        updateStateAddRate(it)

    }

    override fun onResume(): Unit = withState(viewModel) {
        super.onResume()
        getListOrder(it)
        updateStateAddRate(it)
    }

    private fun getListOrder(it: HomeViewState) {
        when (it.stateOrder) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateOrder.invoke()?.let { orders ->
                            val sortedOrders = orders.sortedByDescending { order -> order.createAt }
                            views.shimmerLoading.visibility=View.GONE
                            views.rcvItemOrderComplete.visibility=View.VISIBLE
                            views.swipeToRefresh.isRefreshing= false
                            Timber.tag("OrderCompleteFragment")
                                .d("orderCompleteInvalidate: ${sortedOrders.size}")
                            orderAdapter.updateDataByStatus(
                                sortedOrders,
                                listOf(2,3)
                            ) // Cập nhật danh sách đơn hàng đã hủy
                            views.rcvItemOrderComplete.adapter = orderAdapter
                            orderAdapter.notifyDataSetChanged()
                            Log.d("OrderCompleteFragment", "getListOrderComplete: ${sortedOrders.size}")
                        }
                    }
                }
            }

            is Loading -> {
                views.shimmerLoading.visibility=View.VISIBLE
                views.rcvItemOrderComplete.visibility=View.GONE
                views.shimmerLoading.startShimmer()
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: loading")
            }

            is Fail -> {
                views.shimmerLoading.visibility=View.GONE
                views.rcvItemOrderComplete.visibility=View.VISIBLE
                Timber.tag("AAAAAAAAAAAAAAA").e("getOrderComplete: Fail")
            }

            else -> {

            }
        }
    }

    private fun updateStateAddRate(state: HomeViewState){

        when(state.stateRate){
            is Loading-> {
                    DialogLoading.showDialog(requireContext())
                Timber.tag("AAAAAAAAAAAA").e("updateStateAddRate:loading ")
            }
            is Success->{
                runBlocking {
                    launch {
                        DialogLoading.hideDialog()
                        dialog.dismiss()
                        Toast.makeText(requireContext(), "Đánh giá thành công", Toast.LENGTH_SHORT).show()
                    }
                }


            }
            is Fail->{
                DialogLoading.hideDialog()
                dialog.dismiss()
                Toast.makeText(requireContext(), "Đánh giá thất bại", Toast.LENGTH_SHORT).show()
                Timber.tag("AAAAAAAAAAAA").e("updateStateAddRate:fail ")
            }
            else->{}
        }
    }
}