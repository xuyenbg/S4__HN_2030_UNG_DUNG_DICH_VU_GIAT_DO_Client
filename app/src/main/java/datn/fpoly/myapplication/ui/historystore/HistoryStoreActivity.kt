package datn.fpoly.myapplication.ui.historystore

import android.os.Bundle
import android.util.Log
import android.view.View
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ActivityHistoryStoreBinding

@Suppress("DEPRECATION")
class HistoryStoreActivity : BaseActivity<ActivityHistoryStoreBinding>() {
    private val historyAdapter = HistoryAdapter()
    override fun getBinding() = ActivityHistoryStoreBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views.recycleViewHistory.adapter = historyAdapter

    }

    override fun initUiAndData() {
        super.initUiAndData()
        val listHist = intent.getSerializableExtra("KEY_HIS") as List<OrderResponse>?
        if (listHist != null) {
            historyAdapter.updateData(listHist.filter { it.status == 3 })
            views.tvNoneHis.visibility = View.GONE
        } else {
            views.tvNoneHis.visibility = View.VISIBLE
        }

        views.apply {
            toobar.title.text = "Lịch Sử Đơn Hàng"
            toobar.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            toobar.btnNotification.visibility = View.GONE
        }
    }
}