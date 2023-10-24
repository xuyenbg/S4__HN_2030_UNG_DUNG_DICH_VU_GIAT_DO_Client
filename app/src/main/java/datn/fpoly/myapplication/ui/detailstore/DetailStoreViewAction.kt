package datn.fpoly.myapplication.ui.detailstore

import datn.fpoly.myapplication.core.ViewAction

sealed class DetailStoreViewAction : ViewAction {
    data class GetListServiceByStore(val idStore: String): DetailStoreViewAction()
}