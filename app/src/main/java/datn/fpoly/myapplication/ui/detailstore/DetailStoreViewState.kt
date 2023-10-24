package datn.fpoly.myapplication.ui.detailstore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceModel

data class DetailStoreViewState(
    var stateService: Async<MutableList<ServiceModel>> = Uninitialized
): MvRxState