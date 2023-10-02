package datn.fpoly.myapplication.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityDashboardBinding
import javax.inject.Inject
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(), DashbroardViewModel.Factory {
    @Inject
    lateinit var dashboardViewModelFactory: DashbroardViewModel.Factory
    private val viewModel:DashbroardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        setCurrentFragment(HomeFragment())
        views.bottomNavigationView.setOnItemSelectedListener  {
            when(it.itemId){
                R.id.home->setCurrentFragment(HomeFragment())
                R.id.favorite->setCurrentFragment(FavoriteFragment())
                R.id.cart->setCurrentFragment(CartFragment())
                R.id.order->setCurrentFragment(OrderFragment())
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    override fun getBinding() = ActivityDashboardBinding.inflate(layoutInflater)
    override fun create(initialState: DashboardViewState) = dashboardViewModelFactory.create(initialState)
}