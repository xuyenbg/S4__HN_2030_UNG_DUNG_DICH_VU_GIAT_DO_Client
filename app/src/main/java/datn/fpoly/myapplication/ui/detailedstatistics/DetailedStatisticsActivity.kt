package datn.fpoly.myapplication.ui.detailedstatistics

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.StatisticsModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.ActivityDetailStoreBinding
import datn.fpoly.myapplication.databinding.ActivityDetailedStatisticsBinding
import datn.fpoly.myapplication.ui.historyOrderUser.HistoryOrderViewAction
import datn.fpoly.myapplication.ui.historyOrderUser.HistoryOrderViewState
import datn.fpoly.myapplication.ui.home.order.adapter.OrderAdapter
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DialogLoading
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DetailedStatisticsActivity : BaseActivity<ActivityDetailedStatisticsBinding>(),
    DetailedStatisticsViewModel.Factory {
    @Inject
    lateinit var detailedStatisticsViewStateFactory: DetailedStatisticsViewModel.Factory
    private val viewModel: DetailedStatisticsViewModel by viewModel()
    private var dialogLoading: Dialog_Loading? = null

    override fun getBinding(): ActivityDetailedStatisticsBinding {
        return ActivityDetailedStatisticsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)

        val idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id

        idStore?.let { DetailedStatisticsViewAction.GetDetailedStatistics(it) }
            ?.let {
                viewModel.handle(it)

                views.imgBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

            }
        viewModel.subscribe(this) {
            getDetailedStatistics(it)
        }
    }

    private fun getDetailedStatistics(it: DetailedStatisticsViewState) {
        when (it.stateDetailedStatistics) {
            is Success -> {
                runBlocking {
                    launch {
                        dialogLoading?.dismiss()
                        it.stateDetailedStatistics.invoke()?.let { statifical ->
                            if (statifical.isNotEmpty())
                            barChartData(statifical)
                            Log.d(
                                "DetailedStatisticsvv",
                                "getListOrderComplete: ${statifical}"
                            )
                        }
                    }
                }
            }

            is Loading -> {
                dialogLoading?.show(supportFragmentManager, "loading11")
                Timber.tag("DetailedStatisticsvv").e("getOrderComplete: loading")
            }

            is Fail -> {
                dialogLoading?.dismiss()
                Timber.tag("DetailedStatisticsvv").e("getOrderComplete: Fail")
            }

            else -> {

            }
        }
    }

    private fun barChartData(list: List<StatisticsModel>) {
        views.tvDate.text = "Từ ${list.first().date} Đến ${list.last().date}"
        val listBarChart = arrayListOf<BarEntry>()
        list.forEachIndexed { index, statisticsModel ->
            listBarChart.add(BarEntry(index.toFloat(), statisticsModel.total.toFloat()))
        }

        val label : List<String> = list.map { it.day }
        Log.d("DetailedStatisticsvv", "barChartData: $listBarChart")

        val barDataSet = BarDataSet(listBarChart, label.toString())
        barDataSet.formLineWidth = 0.5f
        barDataSet.setColors(getColor(R.color.colorPriamry))
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)
        views.barchart.data = barData
        views.barchart.description.isEnabled = false
        views.barchart.invalidate()
        views.barchart.axisLeft.axisMinimum = 0f

        views.apply {
            barchart.axisRight.isEnabled = false
            barchart.setDrawValueAboveBar(true)
            barchart.setMaxVisibleValueCount(7)
            barchart.setPinchZoom(false)
            barchart.legend.isEnabled = false
//
//            barchart.xAxis.labelRotationAngle = -45f
            barchart.xAxis.setDrawLabels(true)
            barchart.xAxis.setDrawGridLines(false)
            barchart.xAxis.setDrawAxisLine(true)
            barchart.xAxis.labelCount = 7

            barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barchart.xAxis.typeface = Typeface.MONOSPACE
            barchart.xAxis.valueFormatter = IndexAxisValueFormatter(label)
            barchart.extraBottomOffset = 10f


            barchart.xAxis.granularity = 1f
            barchart.xAxis.isGranularityEnabled = false

            barchart.setFitBars(true)
            barchart.animateY(1500)
        }

    }

    override fun create(initialState: DetailedStatisticsViewState): DetailedStatisticsViewModel {
        return detailedStatisticsViewStateFactory.create(initialState)
    }

}