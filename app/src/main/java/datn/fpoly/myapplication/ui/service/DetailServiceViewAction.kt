package datn.fpoly.myapplication.ui.service

import datn.fpoly.myapplication.core.ViewAction

sealed class DetailServiceViewAction : ViewAction {
    data class GetListServiceByStore(val idStore: String, val idService:String): DetailServiceViewAction()
}