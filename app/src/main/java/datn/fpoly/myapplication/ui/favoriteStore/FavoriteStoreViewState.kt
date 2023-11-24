package datn.fpoly.myapplication.ui.favoriteStore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.account.AccountExtend
import datn.fpoly.myapplication.data.model.account.AccountModel

data class FavoriteStoreViewState(
    var stateFavoriteStore: Async<AccountExtend> = Uninitialized,
    var stateFavoriteStoreRe: Async<AccountModel> = Uninitialized
): MvRxState
