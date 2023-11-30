package datn.fpoly.myapplication.ui.home

import datn.fpoly.myapplication.core.ViewAction

sealed class HomeViewAction : ViewAction {
    object HomeActionCategory : HomeViewAction()
    data class HomeActionGetListStore(val latitude: Float, val longitude: Float): HomeViewAction()
    object PostClientActionList : HomeViewAction()
    data class OrderActionGetList(var idUser: String) : HomeViewAction()

    data class AddRate(val idStore: String, val idUser: String, val startNumber: Float, val content: String, val idOrder: String): HomeViewAction()
}