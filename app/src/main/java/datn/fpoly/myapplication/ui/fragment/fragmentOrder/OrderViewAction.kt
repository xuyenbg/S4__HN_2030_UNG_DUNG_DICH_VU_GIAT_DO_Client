package datn.fpoly.myapplication.ui.fragment.fragmentOrder

import datn.fpoly.myapplication.core.ViewAction

sealed class OrderViewAction : ViewAction {
    object OrderActionGetList : OrderViewAction()
}