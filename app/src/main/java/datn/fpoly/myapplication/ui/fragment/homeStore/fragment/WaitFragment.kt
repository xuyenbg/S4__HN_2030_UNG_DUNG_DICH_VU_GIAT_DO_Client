package datn.fpoly.myapplication.ui.fragment.homeStore.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.FragmentWaitBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.OrderStoreWaitAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class WaitFragment : BaseFragment<FragmentWaitBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()

    private var idStore: String? = null
    private var listOrderWait: MutableList<OrderResponse>? = null
    private lateinit var orderStoreAdapter: OrderStoreWaitAdapter

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentWaitBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE)?.id
        if (idStore != null) {
            viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDate(idStore!!, 0, "desc"))
        }

        orderStoreAdapter = OrderStoreWaitAdapter(onBtnAction = {
            Toast.makeText(requireContext(), "Hoàn Thành", Toast.LENGTH_SHORT).show()
            viewModel.handle(HomeStoreViewAction.UpdateStatus(it.id, 1))
            viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDate(idStore!!, 0, "desc"))
            viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateWashing(idStore!!, 1, "desc"))
//            val viewPager: ViewPager2 = requireActivity().findViewById(R.id.list_order)
//            viewPager.currentItem = 1
        }, itemOnclick = {
            val intent = Intent(context, OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            startActivity(intent)
        })
        views.recycleviewWating.adapter = orderStoreAdapter
        views.recycleviewWating.addItemDecoration(ItemSpacingDecoration(46))
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getOrderDateWait(it)
        getOrderUpdateWait(it)
    }

    private fun getOrderDateWait(it: HomeStoreState) {
        when (it.stateGetOrderDateStore) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderDateStore.invoke()?.let {
                            orderStoreAdapter.updateData(it)

                            orderStoreAdapter.notifyDataSetChanged()
                            listOrderWait?.addAll(it)
                            if (it.isEmpty()) {
                                views.recycleviewWating.visibility = View.GONE
//                                views.labelListEmpty.visibility = View.VISIBLE
                            } else {
                                views.recycleviewWating.visibility = View.VISIBLE
//                                views.labelListEmpty.visibility = View.GONE
                            }
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWaiting: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWaiting: Fail")
            }

            else -> {

            }
        }
    }
    private fun getOrderUpdateWait(it: HomeStoreState) {
        when (it.stateUpdateStatus) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateUpdateStatus.invoke()?.let {
                            if (it.code()==200) {
                                val viewPager: ViewPager2? = requireActivity().findViewById(R.id.list_order)
                                viewPager?.currentItem = 1
                            }
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWaiting: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWaiting: Fail")
            }

            else -> {

            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDate(idStore!!, 0, "desc"))
//
//    }
}