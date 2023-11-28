package datn.fpoly.myapplication.ui.address

import datn.fpoly.myapplication.core.ViewAction

sealed class AddressViewAction: ViewAction {
    data class GetListAddress(val idUser: String): AddressViewAction()
    data class PutDefaultAddress(val idAddress: String, val idUser: String): AddressViewAction()
}