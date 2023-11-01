package datn.fpoly.myapplication.ui.listService

import datn.fpoly.myapplication.core.ViewAction

sealed class ListServiceViewAction : ViewAction {
    data class GetListServiceByCategory(val idCate: String): ListServiceViewAction()
}