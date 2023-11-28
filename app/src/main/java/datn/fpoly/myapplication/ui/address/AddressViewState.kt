package datn.fpoly.myapplication.ui.address

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.AddressModel

data class AddressViewState(
    var stateAddress: Async<MutableList<AddressExtend>> = Uninitialized,
    var stateAddressDefault: Async<AddressModel> = Uninitialized
): MvRxState