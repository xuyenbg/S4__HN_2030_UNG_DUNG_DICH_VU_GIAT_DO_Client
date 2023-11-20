package datn.fpoly.myapplication.ui.listServiceByName

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceExtend

data class ListServiceByNameViewState (
    var stateService: Async<MutableList<ServiceExtend>> = Uninitialized,
): MvRxState