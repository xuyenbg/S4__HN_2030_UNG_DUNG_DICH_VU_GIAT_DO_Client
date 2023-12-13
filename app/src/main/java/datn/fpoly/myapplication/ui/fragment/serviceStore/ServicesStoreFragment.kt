package datn.fpoly.myapplication.ui.fragment.serviceStore

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.mvrx.*
import datn.fpoly.myapplication.core.BaseFragment
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        views.toolBar.tvTitleTooobal.text = "Danh sách dịch vụ"
        views.toolBar.icBack.visibility = View.INVISIBLE
        views.btnAddService.setOnClickListener {
            startActivityResult.launch(
                Intent(
                    requireContext(),
                    AddServiceActivity::class.java
                ).putExtra(Common.KEY_UPDATE_SERVICE, false)
            )
        }

        views.toolBar.icSearch.setOnClickListener {
            views.toolBar.toobar.visibility = View.INVISIBLE
            views.edSearch.visibility = View.VISIBLE
            views.icClose.visibility = View.VISIBLE
        }

        views.icClose.setOnClickListener {
            views.edSearch.visibility = View.GONE
            views.edSearch.setText("")
            views.icClose.visibility = View.GONE
            views.toolBar.toobar.visibility = View.VISIBLE
        }

        adapter = AdapterService(true, false)
        views.rcvListService.adapter = adapter
        adapter.setListenner(object : AdapterService.ServiceListenner {
            override fun serviceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(requireContext(), DetailServiceActivity::class.java)
                intent.putExtra("isStore", true)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                intent.putExtra(Common.KEY_ID_STORE, item.idStore?.id)
                requireContext().startActivity(intent)
            }

            override fun editService(serviceExtend: ServiceExtend) {
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

        views.swipeToRefresh.setOnRefreshListener {
            store.id?.let { HomeStoreViewAction.getListServiceByStore(it) }
                ?.let { viewModel.handle(it) }
        }
        views.edSearch.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("onTextChanged", "onTextChanged: $p0")
//                    orderStoreAdapter.filter.filter(p0.toString())
                    adapter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            }
        )
    }

    fun getListService(state: HomeStoreState) {
        when (state.stateGetListService) {
            is Loading -> {
                views.shimmer.visibility = View.VISIBLE
                views.shimmer.startShimmer()
                views.rcvListService.visibility = View.GONE
                Timber.tag("AAAAAAAAAAAAAAA").e("getListService: Loading")
            }

            is Success -> {
                runBlocking {
                    launch {
                        state.stateGetListService.invoke().let {
                            views.swipeToRefresh.isRefreshing = false

                            views.shimmer.visibility = View.GONE
                            views.rcvListService.visibility = View.VISIBLE
                            adapter.setData(it)
                            Timber.tag("AAAAAAAAAAAAAAA").e("getListService:Success " + it.size)
                        }
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListService:Success ")
                    }

                }
            }

            is Fail -> {
                views.shimmer.visibility = View.GONE

                views.rcvListService.visibility = View.VISIBLE
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