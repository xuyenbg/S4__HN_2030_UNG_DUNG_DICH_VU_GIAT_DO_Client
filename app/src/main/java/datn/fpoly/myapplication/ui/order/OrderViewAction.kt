package datn.fpoly.myapplication.ui.order

import datn.fpoly.myapplication.core.ViewAction

sealed class OrderViewAction : ViewAction {
    data class GetOrderDetail(var idOrder: String) : OrderViewAction()
}