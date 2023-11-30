package datn.fpoly.myapplication.ui.updateAddress

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.AddressModel

sealed class UpdateAddressViewAction: ViewAction {
    data class GetDetailAddress(val idAddress: String): UpdateAddressViewAction()
    data class PutAddress(val idAddress: String, val addressModel: AddressModel): UpdateAddressViewAction()
    data class DeleteAddress(val idAddress: String): UpdateAddressViewAction()
}