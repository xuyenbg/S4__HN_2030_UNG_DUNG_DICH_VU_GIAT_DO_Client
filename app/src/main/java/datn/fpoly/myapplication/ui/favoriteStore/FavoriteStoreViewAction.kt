package datn.fpoly.myapplication.ui.favoriteStore

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewAction

sealed class FavoriteStoreViewAction: ViewAction {
    data class GetDetailUser(val idUser: String):FavoriteStoreViewAction()
    data class AddFavoriteStore(val idUser: String, val accountModel: AccountModel): FavoriteStoreViewAction()
    data class RemoveFavoriteStore(val idUser: String, val accountModel: AccountModel): FavoriteStoreViewAction()
}