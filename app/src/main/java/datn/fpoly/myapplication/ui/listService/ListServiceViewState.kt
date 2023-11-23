package datn.fpoly.myapplication.ui.listService

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceExtend

data class ListServiceViewState (
    var stateService: Async<MutableList<ServiceExtend>> = Uninitialized,
): MvRxState{
    fun isLoading(): Boolean {
        return stateService is Loading
    }
}