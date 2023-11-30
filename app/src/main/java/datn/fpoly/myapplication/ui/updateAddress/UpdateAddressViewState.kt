package datn.fpoly.myapplication.ui.updateAddress

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.AddressModel

data class UpdateAddressViewState (
    var stateAddress: Async<AddressModel> = Uninitialized,
    var stateDetailAddress: Async<AddressExtend> = Uninitialized,
    var stateDeleteAddress: Async<AddressModel> = Uninitialized
): MvRxState