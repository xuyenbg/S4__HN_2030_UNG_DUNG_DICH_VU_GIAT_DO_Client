package datn.fpoly.myapplication.ui.fragment.homeStore.fragment

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
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentCompleteMissionBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.OrderStoreCompleteMissionAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class CompleteMissionFragment : BaseFragment<FragmentCompleteMissionBinding>() {

    private val viewModel: HomeStoreViewModel by activityViewModel()
    private var idStore: String? = null
    private lateinit var orderStoreAdapter: OrderStoreCompleteMissionAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE)?.id
        if (idStore != null) {
            viewModel.handle(
                HomeStoreViewAction.GetDataOrderStoreDateCompleteMission(
                    idStore!!,
                    3,
                    "desc"
                )
            )
        }


        orderStoreAdapter = OrderStoreCompleteMissionAdapter (onBtnAction = {
            val intent = Intent(requireContext(), OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            intent.putExtra("store", true)
            requireContext().startActivity(intent)
        }, onClick = {
            val intent = Intent(requireContext(),OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER,it.id)
            intent.putExtra("store",true)
            requireContext().startActivity(intent)

        })
        views.recycleviewCompleteMission.adapter = orderStoreAdapter
        views.recycleviewCompleteMission.addItemDecoration(ItemSpacingDecoration(46))
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getOrderDateComplete(it)
    }

    private fun getOrderDateComplete(it: HomeStoreState) {
        when (it.stateGetOrderDateStoreCompleteMission) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderDateStoreCompleteMission.invoke()?.let {
                            orderStoreAdapter.updateData(it)
                            Log.d("recycleviewComplete", "getOrderDateWait: ${it.size}")

                            if (it.isEmpty()) {
                                views.recycleviewCompleteMission.visibility = View.GONE
//                                views.labelListEmpty.visibility = View.VISIBLE
                            } else {
                                views.recycleviewCompleteMission.visibility = View.VISIBLE
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
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompleteMissionBinding.inflate(layoutInflater)

}