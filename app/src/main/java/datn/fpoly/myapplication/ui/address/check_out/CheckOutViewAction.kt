package datn.fpoly.myapplication.ui.address.check_out

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.OrderBase

sealed class CheckOutViewAction : ViewAction {
    data class GetStoreById(val idStore : String): CheckOutViewAction()
    data class InsertOrder(val orderBase : OrderBase): CheckOutViewAction()
    object GetListAddress: CheckOutViewAction()
}