package datn.fpoly.myapplication.ui.home

import datn.fpoly.myapplication.core.ViewAction

sealed class HomeViewAction : ViewAction {
    object HomeActionCategory : HomeViewAction()
    object HomeActionGetListStore: HomeViewAction()
}