package datn.fpoly.myapplication.ui.fragment.homeUser


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.databinding.FragmentHomeUserBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.airbnb.mvrx.activityViewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.ui.adapter.AdapterCategory
import datn.fpoly.myapplication.ui.adapter.AdapterStore
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.utils.Common
import timber.log.Timber


class HomeUserFragment  : BaseFragment<FragmentHomeUserBinding>() {

    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var adapterCate: AdapterCategory
    private lateinit var adapterStore: AdapterStore
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeUserBinding = FragmentHomeUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCate = AdapterCategory(6)
        views.rcvListCategory.adapter = adapterCate
        adapterCate.setListener(object : AdapterCategory.CategoryListener {
            override fun onClickCate(categoryModel: CategoryModel) {

            }
        })
        adapterStore = AdapterStore(6)
        views.rcvListStore.adapter = adapterStore
        adapterStore.setListener(object : AdapterStore.StoreListener {
            override fun onClickStoreListener(storeModel: StoreModel) {
                Hawk.put(Common.KEY_STORE_DETAIL, storeModel)
                requireContext().startActivity(Intent(requireContext(), DetailStoreActivity::class.java))
            }
        })



    }

    override fun onResume() {
        super.onResume()
        viewModel.handle(HomeViewAction.HomeActionCategory)
        viewModel.handle(HomeViewAction.HomeActionGetListStore)
    }

    override fun invalidate(): Unit = withState(viewModel) {
        getListCate(it)
        getListStore(it)
    }

    fun getListCate(it: HomeViewState) {
        when (it.stateCategory) {
            is Success -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success")
                runBlocking {
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success2")
                    launch {
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success3")
                        it.stateCategory.invoke()?.let {
                            adapterCate.updateData(it)
                            Timber.tag("AAAAAAAAAAAAAA").e("invalidate: " + it.size)
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Fail")
            }

            else -> {

            }
        }
    }

    fun getListStore(state: HomeViewState) {
        when (state.stateStore) {
            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: loading")
            }

            is Success -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success")
                runBlocking {
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success2")
                    launch {
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success3")
                        state.stateStore.invoke()?.let {
                            adapterStore.setData(it)
                            Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: size" + it.size)
                        }
                    }
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: fail")
            }

            else -> {

            }
        }

    }


}


