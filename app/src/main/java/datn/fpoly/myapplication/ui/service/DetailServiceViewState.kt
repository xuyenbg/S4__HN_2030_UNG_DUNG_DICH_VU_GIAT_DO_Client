package datn.fpoly.myapplication.ui.service

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceExtend

data class DetailServiceViewState(
    var stateService: Async<MutableList<ServiceExtend>> = Uninitialized,
    var stateServiceByid: Async<ServiceExtend> = Uninitialized
): MvRxState