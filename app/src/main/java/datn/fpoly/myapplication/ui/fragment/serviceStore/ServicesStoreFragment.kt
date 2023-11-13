package datn.fpoly.myapplication.ui.fragment.serviceStore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentServicesStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.postService.AddServiceActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import timber.log.Timber


class ServicesStoreFragment : BaseFragment<FragmentServicesStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private lateinit var adapter: AdapterService
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentServicesStoreBinding {
        return FragmentServicesStoreBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.btnAddService.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    AddServiceActivity::class.java
                ).putExtra(Common.KEY_UPDATE_SERVICE, false)
            )
        }
        adapter = AdapterService(true)
        views.rcvListService.adapter = adapter
        adapter.setListenner(object : AdapterService.ServiceListenner {
            override fun ServiceOnClick(item: ServiceModel, position: Int) {

            }

            override fun EditService(serviceModel: ServiceModel) {
                DataRaw.setModelUpdateService(serviceModel)
                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        AddServiceActivity::class.java
                    ).putExtra(Common.KEY_UPDATE_SERVICE, true)
                )

            }
        })
        val store: StoreModel = Hawk.get(Common.KEY_STORE)
        store.id?.let { HomeStoreViewAction.getListServiceByStore(it) }
            ?.let { viewModel.handle(it) }
    }

    fun getListService(state: HomeStoreState) {
        when (state.stateGetListService) {
            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListService: Loading")
            }
            is Success -> {
                state.stateGetListService.invoke()?.let {
                    adapter.setData(it)
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListService:Success " + it.size)
                }
                Timber.tag("AAAAAAAAAAAAAAA").e("getListService:Success ")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListService: Fail")
            }
            else -> {}
        }
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getListService(it)
    }

}