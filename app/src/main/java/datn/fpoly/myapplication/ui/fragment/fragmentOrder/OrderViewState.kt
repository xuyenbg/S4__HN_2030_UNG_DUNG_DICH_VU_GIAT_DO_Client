package datn.fpoly.myapplication.ui.fragment.fragmentOrder

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.Order

data class OrderViewState (
    var stateOrder: Async<MutableList<Order>> = Uninitialized
): MvRxState