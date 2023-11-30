package datn.fpoly.myapplication.ui.historyOrderUser

import datn.fpoly.myapplication.core.ViewAction

sealed class HistoryOrderViewAction: ViewAction {
    data class GetListHistoryOrder(val idUser: String): HistoryOrderViewAction()
}