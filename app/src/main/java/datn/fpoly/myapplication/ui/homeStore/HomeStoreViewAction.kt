package datn.fpoly.myapplication.ui.homeStore

import datn.fpoly.myapplication.core.ViewAction

sealed class HomeStoreViewAction : ViewAction {
    object PostStoreActionList : HomeStoreViewAction()
}