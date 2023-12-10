package datn.fpoly.myapplication.ui.detailstore

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.ui.favoriteStore.FavoriteStoreViewAction

sealed class DetailStoreViewAction : ViewAction {
    data class GetListServiceByStore(val idStore: String): DetailStoreViewAction()
    data class GetStoreById(val id: String):DetailStoreViewAction()
    data class AddFavoriteStore(val idUser: String, val accountModel: AccountModel):DetailStoreViewAction()
    data class GetListRateByStore(val idStore: String): DetailStoreViewAction()
    data class GetDetailUser(val idUser: String): DetailStoreViewAction()
}