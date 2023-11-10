package datn.fpoly.myapplication.ui.check_out

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.Order

sealed class CheckOutViewAction : ViewAction {
    data class GetStoreById(val idStore : String): CheckOutViewAction()
    data class InsertOrder(val order : Order): CheckOutViewAction()
    object GetListAddress: CheckOutViewAction()
}