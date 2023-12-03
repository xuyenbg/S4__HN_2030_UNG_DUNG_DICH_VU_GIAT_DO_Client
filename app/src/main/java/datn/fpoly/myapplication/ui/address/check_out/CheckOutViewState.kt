package datn.fpoly.myapplication.ui.address.check_out

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.StoreModel

data class CheckOutViewState (
    var stateGetStoreById: Async<StoreModel> = Uninitialized,
    var stateInsertOrder: Async<Unit> = Uninitialized,
    var stateGetListAddress: Async<MutableList<AddressExtend>> = Uninitialized,
): MvRxState {
    fun isLoading(): Boolean {
        return stateGetStoreById is Loading
                || stateInsertOrder is Loading
                || stateGetListAddress is Loading
    }
}