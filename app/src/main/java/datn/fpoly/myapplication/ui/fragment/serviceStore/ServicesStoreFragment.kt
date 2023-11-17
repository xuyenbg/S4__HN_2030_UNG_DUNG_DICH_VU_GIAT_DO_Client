package datn.fpoly.myapplication.ui.fragment.serviceStore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.mvrx.*
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentServicesStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.postService.AddServiceActivity
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import timber.log.Timber


class ServicesStoreFragment : BaseFragment<FragmentServicesStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private lateinit var adapter: AdapterService
    private val startActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Common.CODE_LOAD_DATA) {
                val store: StoreModel = Hawk.get(Common.KEY_STORE)
                store.id?.let { HomeStoreViewAction.getListServiceByStore(it) }
                    ?.let { viewModel.handle(it) }
            }
        }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentServicesStoreBinding {
        return FragmentServicesStoreBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.btnAddService.setOnClickListener {
            startActivityResult.launch(
                Intent(
                    requireContext(),
                    AddServiceActivity::class.java
                ).putExtra(Common.KEY_UPDATE_SERVICE, false)
            )
        }
        adapter = AdapterService(true)
        views.rcvListService.adapter = adapter
        adapter.setListenner(object : AdapterService.ServiceListenner {
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(requireContext(), DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                intent.putExtra(Common.KEY_ID_STORE, item.idStore?.id)
                startActivity(intent)
            }

            override fun EditService(serviceExtend: ServiceExtend) {
                DataRaw.setModelUpdateService(serviceExtend)
                startActivityResult.launch(
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
                state.stateGetListService.invoke().let {
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