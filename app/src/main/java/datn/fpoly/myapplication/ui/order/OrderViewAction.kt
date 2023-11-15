package datn.fpoly.myapplication.ui.order

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.Order

sealed class OrderViewAction : ViewAction {
    data class GetStoreById(val idStore : String): OrderViewAction()
    data class InsertOrder(val order : Order): OrderViewAction()
    object GetListAddress: OrderViewAction()
}