package datn.fpoly.myapplication.ui.favoriteStore

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.repository.AuthRepo

class FavoriteStoreViewModel@AssistedInject constructor(
    @Assisted state: FavoriteStoreViewState,
    private var repoAuth: AuthRepo
): BaseViewModel<FavoriteStoreViewState,FavoriteStoreViewAction,FavoriteStoreEvent>(state) {
    override fun handle(action: FavoriteStoreViewAction) {
        when(action){
            is FavoriteStoreViewAction.GetDetailUser->{
                getDetailUser(action.idUser)
            }
            is FavoriteStoreViewAction.AddFavoriteStore->{
                addFavoriteStore(action.idUser, action.accountModel)
            }
            is FavoriteStoreViewAction.RemoveFavoriteStore->{
                removeFavoriteStore(action.idUser,action.accountModel)
            }
        }
    }

    fun getDetailUser(id: String){
        setState { copy(stateFavoriteStore= Loading()) }
        repoAuth.getDetailUser(id).execute { copy(stateFavoriteStore = it) }
    }
    fun addFavoriteStore(id: String, accountModel: AccountModel){
        setState { copy(stateFavoriteStoreRe= Loading()) }
        repoAuth.addFavoriteStore(id,accountModel).execute { copy(stateFavoriteStoreRe = it) }
    }
    fun removeFavoriteStore(id: String, accountModel: AccountModel){
        setState { copy(stateFavoriteStoreRe = Loading()) }
        repoAuth.removeFavoriteStore(id,accountModel).execute { copy(stateFavoriteStoreRe = it) }
    }
    @AssistedFactory
    interface Factory {
        fun create(initialState: FavoriteStoreViewState):FavoriteStoreViewModel
    }
    companion object : MvRxViewModelFactory<FavoriteStoreViewModel, FavoriteStoreViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: FavoriteStoreViewState
        ): FavoriteStoreViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? FavoriteStoreViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? FavoriteStoreViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}