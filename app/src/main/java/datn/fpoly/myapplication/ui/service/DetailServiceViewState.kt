package datn.fpoly.myapplication.ui.service

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceModel

data class DetailServiceViewState(
    var stateService: Async<MutableList<ServiceModel>> = Uninitialized
): MvRxState