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
import datn.fpoly.myapplication.databinding.FragmentCompleteBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.OrderStoreCompleteAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class CompleteFragment : BaseFragment<FragmentCompleteBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private var idStore: String? = null
    private lateinit var orderStoreAdapter: OrderStoreCompleteAdapter
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCompleteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE)?.id
        if (idStore != null) {
            viewModel.handle(
                HomeStoreViewAction.GetDataOrderStoreDateComplete(
                    idStore!!,
                    2,
                    "desc"
                )
            )
        }


        orderStoreAdapter = OrderStoreCompleteAdapter(onBtnAction = {
            viewModel.handle(HomeStoreViewAction.UpdateStatusComplete(it.id, 3))
            viewModel.handle(
                HomeStoreViewAction.GetDataOrderStoreDateComplete(
                    idStore!!,
                    2,
                    "desc"
                )
            )
            viewModel.handle(
                HomeStoreViewAction.GetDataOrderStoreDateCompleteMission(
                    idStore!!,
                    3,
                    "desc"
                )
            )

        }, itemOnclick = {
            val intent = Intent(requireContext(), OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            intent.putExtra("store", true)
            requireContext().startActivity(intent)
        })
        views.recycleviewComplete.adapter = orderStoreAdapter
        views.recycleviewComplete.addItemDecoration(ItemSpacingDecoration(46))
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getOrderDateComplete(it)
        getOrderUpdateComplete(it)
    }

    private fun getOrderDateComplete(it: HomeStoreState) {
        when (it.stateGetOrderDateStoreComplete) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderDateStoreComplete.invoke()?.let {
                            orderStoreAdapter.updateData(it)
                            orderStoreAdapter.notifyDataSetChanged()
                            Log.d("recycleviewComplete", "getOrderDateWait: ${it.size}")

                            if (it.isEmpty()) {
                                views.recycleviewComplete.visibility = View.GONE
//                                views.labelListEmpty.visibility = View.VISIBLE
                            } else {
                                views.recycleviewComplete.visibility = View.VISIBLE
//                                views.labelListEmpty.visibility = View.GONE
                            }
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getComplete: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getComplete: Fail")
            }

            else -> {

            }
        }
    }

    private fun getOrderUpdateComplete(it: HomeStoreState) {
        when (it.stateUpdateStatusComplete) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateUpdateStatusComplete.invoke()?.let {
                            Log.d("stateUpdateStatusWashing", "Chạy qua đây${it.code()}")

                            if (it.code() == 200) {

                                val viewPager: ViewPager2? =
                                    requireActivity().findViewById(R.id.list_order)
                                viewPager?.currentItem = 3
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