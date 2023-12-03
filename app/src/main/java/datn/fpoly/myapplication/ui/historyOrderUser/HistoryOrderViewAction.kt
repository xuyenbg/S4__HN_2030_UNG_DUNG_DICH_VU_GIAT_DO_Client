package datn.fpoly.myapplication.ui.historyOrderUser

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.ui.home.HomeViewAction

sealed class HistoryOrderViewAction: ViewAction {
    data class GetListHistoryOrder(val idUser: String): HistoryOrderViewAction()
    data class AddRate(val idStore: String, val idUser: String, val startNumber: Float, val content: String, val idOrder: String): HistoryOrderViewAction()

}