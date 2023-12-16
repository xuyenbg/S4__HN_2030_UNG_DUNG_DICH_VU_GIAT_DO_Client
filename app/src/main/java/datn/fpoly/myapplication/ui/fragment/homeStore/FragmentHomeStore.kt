package datn.fpoly.myapplication.ui.fragment.homeStore

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.*
import datn.fpoly.myapplication.core.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentHomeLaundryBinding
import datn.fpoly.myapplication.ui.detailedstatistics.DetailedStatisticsActivity
import datn.fpoly.myapplication.ui.fragment.homeStore.adapter.TabLayoutAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.notification.NotificationActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Utils

import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FragmentHomeStore : BaseFragment<FragmentHomeLaundryBinding>() {

    private val viewModel: HomeStoreViewModel by activityViewModel()
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeLaundryBinding.inflate(layoutInflater)


    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private var storeModel: StoreModel? = null
    private var isOpend = false;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayoutAdapter = TabLayoutAdapter(childFragmentManager, lifecycle)
        views.tabView.addTab(views.tabView.newTab().setText("Chờ nhận đồ"))
        views.tabView.addTab(views.tabView.newTab().setText("Đang giặt"))
        views.tabView.addTab(views.tabView.newTab().setText("Giặt xong"))
        views.tabView.addTab(views.tabView.newTab().setText("Hoàn Thành"))
        ///
        val idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id
        val formatter = DateTimeFormatter.ofPattern("MM")
        val month = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(formatter)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val week = Utils.getWeekByMonth()


        ////
        idStore?.let { HomeStoreViewAction.GetStatisticalByToday(it) }?.let { viewModel.handle(it) }
        idStore?.let { HomeStoreViewAction.GetStatisticalByMonth(it, month.toInt()) }
            ?.let { viewModel.handle(it) }
        idStore?.let { HomeStoreViewAction.GetStatisticalByWeek(it, week) }
            ?.let { viewModel.handle(it) }
        Log.d("hhh", "onViewCreated: ${Utils.getWeekByMonth()}")

        storeModel = Hawk.get<StoreModel>(Common.KEY_STORE)
        views.swOpendClose.isChecked = storeModel?.status == 1

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
            if (b) {
                storeModel?.id?.let { HomeStoreViewAction.OpendCloseStore(it, 1) }
                    ?.let { viewModel.handle(it) }
            } else {
                storeModel?.id?.let { HomeStoreViewAction.OpendCloseStore(it, 0) }
                    ?.let { viewModel.handle(it) }
            }
            isOpend = b
        }
        if (storeModel?.status == 0) {
            dialogMessage(requireContext())
        }
        views.icNotification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            intent.putExtra(Common.KEY_ID_USER, storeModel?.iduser?.id)
            requireContext().startActivity(intent)
        }
        views.swipeToRefresh.setOnRefreshListener {
            if(views.swipeToRefresh.isRefreshing){
                idStore?.let { HomeStoreViewAction.GetStatisticalByToday(it) }?.let { viewModel.handle(it) }
                idStore?.let { HomeStoreViewAction.GetStatisticalByMonth(it, month.toInt()) }
                    ?.let { viewModel.handle(it) }
                idStore?.let { HomeStoreViewAction.GetStatisticalByWeek(it, week) }
                    ?.let { viewModel.handle(it) }
                if (idStore != null){
                    viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDate(idStore, 0, "desc"))
                    viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateWashing(idStore, 1, "desc"))
                    viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateComplete(idStore, 2, "desc"))
                    viewModel.handle(HomeStoreViewAction.GetDataOrderStoreDateCompleteMission(idStore, 3, "desc"))
                }
            }
        }
        views.tvStatisticsDetail.setOnClickListener {
            startActivity(Intent(requireContext(),DetailedStatisticsActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        storeModel = Hawk.get<StoreModel>(Common.KEY_STORE)

    }

    private fun dialogMessage(context: Context) {
        val build = AlertDialog.Builder(context)
        build.setMessage("Bạn chưa mở cửa hàng. Hãy vui lòng mở cửa hàng trở lại")
        build.setTitle("Thông báo")
        build.setCancelable(false)
        build.setPositiveButton("Đóng", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface, p1: Int) {
                p0.dismiss()
            }
        })
        val alertDialog = build.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun invalidate(): Unit = withState(viewModel) {
        updateStateOpendClose(it)
        statisticalByToday(it)
        statisticalByToMonth(it)
        statisticalByToWeek(it)
    }

    private fun updateStateOpendClose(state: HomeStoreState) {
        when (state.stateOpendCloseStore) {
            is Loading -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: loading ")
            }

            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                state.stateOpendCloseStore.invoke()?.let {
                    if (isOpend) {
                        storeModel?.status = 1
                    } else {
                        storeModel?.status = 0
                    }
                    Hawk.put(Common.KEY_STORE, storeModel)
                    views.swOpendClose.isSelected = isOpend
                    Timber.tag("AAAAAAAAA").e("updateStateOpendClose: Success ")
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: fail ")
            }

            else -> {}
        }

        setView()

    }

    private fun statisticalByToday(state: HomeStoreState) {
        when (state.stateStatisticalByToday) {
            is Loading -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: loading ")
            }

            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                state.stateStatisticalByToday.invoke()?.let {
                    views.priceToday.text = Utils.formatVND(it.total)
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: fail ")
            }

            else -> {}
        }
    }

    private fun statisticalByToMonth(state: HomeStoreState) {
        when (state.stateStatisticalByMonth) {
            is Loading -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: loading ")
            }

            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                state.stateStatisticalByMonth.invoke()?.let {
                    views.priceMonth.text = Utils.formatVND(it.total)
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: fail ")
            }

            else -> {}
        }
    }
    private fun statisticalByToWeek(state: HomeStoreState) {
        when (state.stateStatisticalByWeek) {
            is Loading -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: loading ")
            }

            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                state.stateStatisticalByWeek.invoke()?.let {
                    views.priceWeek.text = Utils.formatVND(it.total)
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateStateOpendClose: fail ")
            }

            else -> {}
        }
    }
    private fun setView() {
        views.apply {
            tvNameLaundry.text = Hawk.get<StoreModel>(Common.KEY_STORE,null).name
        }
    }


}