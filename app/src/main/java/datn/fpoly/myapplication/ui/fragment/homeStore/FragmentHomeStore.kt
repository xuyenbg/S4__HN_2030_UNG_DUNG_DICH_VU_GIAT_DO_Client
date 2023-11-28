package datn.fpoly.myapplication.ui.fragment.homeStore

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.*
import datn.fpoly.myapplication.core.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentHomeLaundryBinding
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.TabLayoutAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.utils.Common

import timber.log.Timber

class FragmentHomeStore : BaseFragment<FragmentHomeLaundryBinding>() {

    private val viewModel: HomeStoreViewModel by activityViewModel()
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeLaundryBinding.inflate(layoutInflater)


    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private var storeModel : StoreModel?=null
    private var isOpend = false;
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
        views.swOpendClose.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                storeModel?.id?.let { HomeStoreViewAction.OpendCloseStore(it, 1) }
                    ?.let { viewModel.handle(it) }
            }else{
                storeModel?.id?.let { HomeStoreViewAction.OpendCloseStore(it, 0) }
                    ?.let { viewModel.handle(it) }
            }
            isOpend = b
        }
        if(storeModel?.status==0){
            dialogMessage(requireContext())
        }

    }
    private fun dialogMessage(context: Context){
        val build = AlertDialog.Builder(context)
        build.setMessage("Bạn chưa mở cửa hàng. Hãy vui lòng mở cửa hàng trở lại")
        build.setTitle("Thông báo")
        build.setCancelable(false)
        build.setPositiveButton("Đóng", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface, p1: Int) {
                p0.dismiss()
            }
        })
        val alertDialog = build.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        storeModel = Hawk.get(Common.KEY_STORE)
        views.swOpendClose.isSelected = storeModel?.status==1
    }

    override fun invalidate() : Unit = withState(viewModel) {
        updateStateOpendClose(it)
    }
    private fun updateStateOpendClose(state: HomeStoreState){
        when(state.stateOpendCloseStore){
            is Loading-> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: loading ")
            }
            is Success->{
                state.stateOpendCloseStore.invoke()?.let {
                    if(isOpend){
                        storeModel?.status = 1
                    }else{
                        storeModel?.status = 0
                    }
                    Hawk.put(Common.KEY_STORE, storeModel)
                    views.swOpendClose.isSelected = isOpend
                    Timber.tag("AAAAAAAAA").e("updateStateOpendClose: Success ")
                }
            }
            is Fail->{
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: fail ")
            }
            else ->{}
        }

        setView()

    }

    private fun setView() {
        views.apply {
//            tvNameLaundry.text = Hawk.get<StoreModel>(Common.KEY_STORE,null).name
        }
    }


}