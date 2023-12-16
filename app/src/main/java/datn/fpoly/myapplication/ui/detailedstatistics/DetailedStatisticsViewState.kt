package datn.fpoly.myapplication.ui.detailedstatistics

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.StatisticsModel
import okhttp3.ResponseBody
import retrofit2.Response

data class DetailedStatisticsViewState (
    var stateDetailedStatistics: Async<MutableList<StatisticsModel>> = Uninitialized,
): MvRxState
