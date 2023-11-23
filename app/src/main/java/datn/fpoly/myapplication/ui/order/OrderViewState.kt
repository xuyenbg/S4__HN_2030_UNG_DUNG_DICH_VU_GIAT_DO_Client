package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.OrderExtend
import okhttp3.ResponseBody
import retrofit2.Response

data class OrderViewState (
    var stateOrderDetail: Async<OrderExtend> = Uninitialized,
    var stateUpdateOrder: Async<Unit> = Uninitialized,
    var stateUploadImage: Async<Response<ResponseBody>>  = Uninitialized,
): MvRxState