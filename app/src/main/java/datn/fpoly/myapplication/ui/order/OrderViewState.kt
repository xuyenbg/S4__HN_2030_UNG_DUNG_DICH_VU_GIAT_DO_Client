package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.OrderExtendHistory
import okhttp3.ResponseBody
import retrofit2.Response

data class OrderViewState (
    var stateOrderDetail: Async<OrderExtendHistory> = Uninitialized,
    var stateUpdateOrder: Async<Unit> = Uninitialized,
    var stateUploadImage: Async<Response<ResponseBody>>  = Uninitialized,
    var stateUpdateStatus: Async<Response<ResponseBody>> = Uninitialized
): MvRxState{
    fun isLoading(): Boolean {
        return stateOrderDetail is Loading
                || stateUpdateOrder is Loading
                || stateUploadImage is Loading
                || stateUpdateStatus is Loading
    }
}