package datn.fpoly.myapplication.ui.historystore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.ActivityHistoryStoreBinding
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common

@Suppress("DEPRECATION")
class HistoryStoreActivity : BaseActivity<ActivityHistoryStoreBinding>() {
    private val historyAdapter = HistoryAdapter()
    override fun getBinding() = ActivityHistoryStoreBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views.recycleViewHistory.adapter = historyAdapter
        historyAdapter.onClicked = {
            val intent = Intent(this, OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            intent.putExtra("store", true)
            startActivity(intent)
        }
    }

    override fun initUiAndData() {
        super.initUiAndData()
        val listHist = intent.getSerializableExtra("KEY_HIS") as List<OrderResponse>?
        if (listHist != null) {
            historyAdapter.updateData(listHist.filter { it.status == 4 || it.status==4 })
            views.tvNoneHis.visibility = View.GONE
        } else {
            views.tvNoneHis.visibility = View.VISIBLE
        }

        views.apply {
            toobar.title.text = "Lịch Sử Đơn Hàng"
            toobar.btnBack.setOnClickListener {
                setResult(Common.CODE_LOAD_DATA)
                onBackPressedDispatcher.onBackPressed()
            }
            toobar.btnNotification.visibility = View.GONE
        }
    }
}