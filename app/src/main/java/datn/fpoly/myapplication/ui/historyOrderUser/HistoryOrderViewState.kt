package datn.fpoly.myapplication.ui.historyOrderUser

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.OrderExtend
import okhttp3.ResponseBody
import retrofit2.Response

data class HistoryOrderViewState (
    var stateOrder: Async<MutableList<OrderExtend>> = Uninitialized,
    var stateRate: Async<Response<ResponseBody>>  = Uninitialized
): MvRxState
