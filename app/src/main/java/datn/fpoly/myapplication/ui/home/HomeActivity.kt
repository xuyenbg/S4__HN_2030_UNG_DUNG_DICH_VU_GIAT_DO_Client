package datn.fpoly.myapplication.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityHomeBinding
import datn.fpoly.myapplication.ui.adapter.AdapterViewPage
import datn.fpoly.myapplication.ui.home.order.FragmentOrder
import datn.fpoly.myapplication.ui.home.cart.CartFragment
import datn.fpoly.myapplication.ui.fragment.homeUser.HomeUserFragment
import datn.fpoly.myapplication.ui.fragment.postclient.PostClientFragment
import datn.fpoly.myapplication.ui.fragment.settingStore.setting.FragmentSetting
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ZoomOutPageTransformer
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding>(), HomeUserViewModel.Factory {
    @Inject
    lateinit var homeUserFatory: HomeUserViewModel.Factory
    private val viewModel: HomeUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        viewModel.observeViewEvents {

        }
        if (!Common.checkPermissionNotify(this)) {
            Common.requestPermissionNotify(this)
        }
        initPage()
        views.bottomnav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> views.vp2Home.setCurrentItem(0, true)
                R.id.cart -> views.vp2Home.setCurrentItem(1, true)
                R.id.order -> views.vp2Home.setCurrentItem(2, true)
                R.id.post -> views.vp2Home.setCurrentItem(3, true)
                R.id.setting -> views.vp2Home.setCurrentItem(4, true)
            }
            true
        }

    }

    private fun initPage() {
        views.vp2Home.adapter = HomeUserPagerAdapter(supportFragmentManager, lifecycle)
        views.vp2Home.setPageTransformer(ZoomOutPageTransformer())
        views.vp2Home.isUserInputEnabled = false
    }

    private class HomeUserPagerAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeUserFragment()
                1 -> CartFragment()
                2 -> FragmentOrder()
                3 -> PostClientFragment()
                else -> FragmentSetting()
            }
        }
    }

    fun animStart(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item_bot_na_custom))
    }

    override fun getBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
    override fun create(initialState: HomeViewState): HomeUserViewModel =
        homeUserFatory.create(initialState)

}