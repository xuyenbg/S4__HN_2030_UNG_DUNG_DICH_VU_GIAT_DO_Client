package datn.fpoly.myapplication.ui.listServiceByName

import datn.fpoly.myapplication.core.ViewAction

sealed class ListServiceByNameViewAction : ViewAction {
    data class GetListServiceByName(val nameService: String): ListServiceByNameViewAction()
}