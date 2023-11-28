package datn.fpoly.myapplication.ui.addAddress

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.AddressModel

data class AddAddressViewState(
    var stateAddAddress: Async<AddressModel> = Uninitialized
): MvRxState