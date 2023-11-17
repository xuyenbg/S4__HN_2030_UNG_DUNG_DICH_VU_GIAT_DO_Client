package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.OrderExtend

data class OrderViewState (
    var stateOrderDetail: Async<OrderExtend> = Uninitialized
): MvRxState