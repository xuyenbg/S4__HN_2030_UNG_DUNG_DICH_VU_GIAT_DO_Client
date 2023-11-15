package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.StoreModel

data class OrderViewState (
    var stateGetStoreById: Async<StoreModel> = Uninitialized,
    var stateInsertOrder: Async<Unit> = Uninitialized,
    var stateGetListAddress: Async<MutableList<AddressExtend>> = Uninitialized,
): MvRxState