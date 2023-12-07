package datn.fpoly.myapplication.ui.homeStore

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityHomeStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterViewPage
import datn.fpoly.myapplication.ui.fragment.homeStore.FragmentHomeStore
import datn.fpoly.myapplication.ui.fragment.settingStore.FragmentSettingStore
import datn.fpoly.myapplication.ui.fragment.postStore.FragmentPostStore
import datn.fpoly.myapplication.ui.fragment.settingStore.setting.FragmentSetting
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.databinding.FragmentOrderStoreBinding
import datn.fpoly.myapplication.ui.fragment.homeUser.HomeUserFragment
import datn.fpoly.myapplication.ui.fragment.orderStore.OrderStoreFragment
import datn.fpoly.myapplication.ui.fragment.postclient.PostClientFragment
import datn.fpoly.myapplication.ui.fragment.serviceStore.ServicesStoreFragment
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.home.cart.CartFragment
import datn.fpoly.myapplication.ui.home.order.FragmentOrder
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import datn.fpoly.myapplication.utils.ZoomOutPageTransformer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class HomeStoreActivity : BaseActivity<ActivityHomeStoreBinding>(), HomeStoreViewModel.Factory {
    @Inject
    lateinit var homeStoreFatory: HomeStoreViewModel.Factory
    private val viewModel: HomeStoreViewModel by viewModel()
    private lateinit var adapterVp: AdapterViewPage
    private val listFragment = mutableListOf<Fragment>()
    private var inforUser: AccountModel? = null

    @Inject
    lateinit var authRepo: AuthRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        authRepo.getUser()?.let {
            inforUser = it
        }
        viewModel.handle(HomeStoreViewAction.GetListCategory)
        viewModel.subscribe(this) {
            getDataCate(it)
        }
        if (!Common.checkPermissionNotify(this)) {
            Common.requestPermissionNotify(this)
        }
        initPage()
        views.bottomnav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_store -> views.vp2Home.setCurrentItem(0, true)
                R.id.service_store -> views.vp2Home.setCurrentItem(1, true)
                R.id.order_store -> views.vp2Home.setCurrentItem(2, true)
                R.id.post_store -> views.vp2Home.setCurrentItem(3, true)
                R.id.setting_store -> views.vp2Home.setCurrentItem(4, true)
            }
            true
        }
    }

    private fun initPage() {
        views.vp2Home.adapter = HomeStorePagerAdapter(supportFragmentManager, lifecycle)
        views.vp2Home.setPageTransformer(ZoomOutPageTransformer())
        views.vp2Home.isUserInputEnabled = false
    }

    private class HomeStorePagerAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentHomeStore()
                1 -> ServicesStoreFragment()
                2 -> OrderStoreFragment()
                3 -> FragmentPostStore()
                else -> FragmentSettingStore()
            }
        }
    }

    private fun getDataCate(state: HomeStoreState) {
        when (state.stateCate) {
            is Loading -> {
                Timber.tag("Loading").e("getDataCate: Loading")
            }

            is Success -> {

                state.stateCate.invoke()?.let {
                    Hawk.put(Common.KEY_LIST_CATE, it)
                }
            }

            is Fail -> {
                Timber.tag("ERROR").e("getDataCate: Fail")
            }

            else -> {}
        }
    }

    override fun getBinding(): ActivityHomeStoreBinding =
        ActivityHomeStoreBinding.inflate(layoutInflater)

    override fun create(initialState: HomeStoreState): HomeStoreViewModel =
        homeStoreFatory.create(initialState)

    override fun onResume() {
        super.onResume()
        val store =  Hawk.get<StoreModel>(Common.KEY_STORE)
        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDate(store.id!!, 0, "desc"))
        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateWashing(store.id!!, 1, "desc"))
        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateComplete(store.id!!, 2, "desc"))
        viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateCompleteMission(store.id!!, 3, "desc"))
    }
}