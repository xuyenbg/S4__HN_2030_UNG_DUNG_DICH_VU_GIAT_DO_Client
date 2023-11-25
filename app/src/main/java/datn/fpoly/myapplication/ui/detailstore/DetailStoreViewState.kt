package datn.fpoly.myapplication.ui.detailstore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.AccountModel

data class DetailStoreViewState(
    var stateService: Async<MutableList<ServiceExtend>> = Uninitialized,
    var stateStore: Async<StoreModel> = Uninitialized,
    var stateFavoriteStore: Async<AccountModel> = Uninitialized
): MvRxState{
    fun isLoading(): Boolean {
        return stateService is Loading
                || stateStore is Loading
                || stateFavoriteStore is Loading
    }
}