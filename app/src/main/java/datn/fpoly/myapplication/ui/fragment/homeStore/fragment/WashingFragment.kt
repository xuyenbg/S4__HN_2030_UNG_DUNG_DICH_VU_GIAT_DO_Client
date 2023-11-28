package datn.fpoly.myapplication.ui.fragment.homeStore.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentWashingBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.OrderStoreWashingAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class WashingFragment : BaseFragment<FragmentWashingBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private var idStore: String? = null
    private lateinit var orderStoreAdapter: OrderStoreWashingAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWashingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE)?.id
        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateWashing(idStore!!, 1, "desc"))
        orderStoreAdapter = OrderStoreWashingAdapter(onBtnAction = {
            viewModel.handle(HomeStoreViewAction.UpdateStatusWashing(it.id, 2))
            viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateWashing(idStore!!, 1, "desc"))
            viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateComplete(idStore!!, 2, "desc"))
            viewModel.handle(
                HomeStoreViewAction.GetDataOrderStoreDateComplete(
                    idStore!!,
                    2,
                    "desc"
                )
            )
//            val viewPager: ViewPager2 = requireActivity().findViewById(R.id.list_order)
//            viewPager.currentItem = 2
        }, itemOnclick = {
            val intent = Intent(context, OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            startActivity(intent)
        })
        views.recycleviewWashing.adapter = orderStoreAdapter
        views.recycleviewWashing.addItemDecoration(ItemSpacingDecoration(46))
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getOrderDateWait(it)
        getOrderUpdateWashing(it)
    }

    private fun getOrderDateWait(it: HomeStoreState) {
        when (it.stateGetOrderDateStoreWashing) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderDateStoreWashing.invoke().let {
                            orderStoreAdapter.updateData(it)
//                            views.recycleviewWashing.adapter = postClientAdapter

                            orderStoreAdapter.notifyDataSetChanged()
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWashing: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWashing: Fail")
            }

            else -> {

            }
        }
    }

    private fun getOrderUpdateWashing(it: HomeStoreState) {
        when (it.stateUpdateStatusWashing) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateUpdateStatusWashing.invoke()?.let {
                            Log.d("stateUpdateStatusWashing", "Chạy qua đây${it.code()}")

                            if (it.code() == 200) {

                                val viewPager: ViewPager2 =
                                    requireActivity().findViewById(R.id.list_order)
                                viewPager.currentItem = 2
                            }
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWashing: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getWashing: Fail")
            }

            else -> {

            }
        }
    }
}