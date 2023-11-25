package datn.fpoly.myapplication.ui.fragment.homeStore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.activityViewModel
import com.example.ql_ban_hang.core.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentHomeLaundryBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.TabLayoutAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.utils.Common

class FragmentHomeStore : BaseFragment<FragmentHomeLaundryBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeLaundryBinding.inflate(layoutInflater)

    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayoutAdapter = TabLayoutAdapter(childFragmentManager, lifecycle)
        views.tabView.addTab(views.tabView.newTab().setText("Chờ nhận đồ"))
        views.tabView.addTab(views.tabView.newTab().setText("Đang giặt"))
        views.tabView.addTab(views.tabView.newTab().setText("Giặt xong"))
        views.tabView.addTab(views.tabView.newTab().setText("Hoàn Thành"))

        views.listOrder.adapter = tabLayoutAdapter
        views.tabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    views.listOrder.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        views.listOrder.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                views.tabView.selectTab(views.tabView.getTabAt(position))
            }
        })
        setView()
    }

    private fun setView() {
        views.apply {
            tvNameLaundry.text = Hawk.get<StoreModel>(Common.KEY_STORE,null).name
        }
    }


}