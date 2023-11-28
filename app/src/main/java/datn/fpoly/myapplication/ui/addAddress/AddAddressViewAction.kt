package datn.fpoly.myapplication.ui.addAddress

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.AddressModel

sealed class AddAddressViewAction: ViewAction {
    data class AddAddress(val addressModel: AddressModel): AddAddressViewAction()
}