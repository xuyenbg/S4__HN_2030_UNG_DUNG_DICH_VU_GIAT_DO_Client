package datn.fpoly.myapplication.ui.detailstore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.repository.*

class DetailStoreViewModel @AssistedInject constructor(
    @Assisted state: DetailStoreViewState,
    private var repo: ServiceRepo,
    private var repoStore: StoreRepo,
    private var repoAuth: AuthRepo,
    private var repoRate: RatePepo
) : BaseViewModel<DetailStoreViewState, DetailStoreViewAction, DetailStoreViewEvent>(state) {
    override fun handle(action: DetailStoreViewAction) {
        when(action){
            is DetailStoreViewAction.GetListServiceByStore->{
                getListServiceByStore(action.idStore)
            }
            is DetailStoreViewAction.GetStoreById->{
                getStoreById(action.id)
            }
            is DetailStoreViewAction.AddFavoriteStore->{
                addFavoriteStore(action.idUser, action.accountModel)
            }
            is DetailStoreViewAction.GetListRateByStore->{
                litsRateByStore(action.idStore)
            }
            is DetailStoreViewAction.GetDetailUser->{
                getDetailUser(action.idUser)
            }
        }
    }
    fun getListServiceByStore(id: String){
        setState { copy(stateService= Loading()) }
        repo.getListServiceByStore(id).execute { copy(stateService = it) }
    }
    fun getStoreById(id: String){
        setState { copy(stateStore= Loading()) }
        repoStore.getStoreById(id).execute { copy(stateStore = it) }
    }
    fun addFavoriteStore(id: String, accountModel: AccountModel){
        setState { copy(stateFavoriteStore= Loading()) }
        repoAuth.addFavoriteStore(id,accountModel).execute { copy(stateFavoriteStore = it) }
    }
    fun litsRateByStore(idStore: String){
        setState { copy(stateListRateStore = Loading()) }
        repoRate.getListRateByStore(idStore).execute { copy(stateListRateStore = it) }
    }
    fun getDetailUser(id: String){
        setState { copy(stateDetailAccount= Loading()) }
        repoAuth.getDetailUser(id).execute { copy(stateDetailAccount = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: DetailStoreViewState): DetailStoreViewModel
    }

    companion object : MvRxViewModelFactory<DetailStoreViewModel, DetailStoreViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: DetailStoreViewState
        ): DetailStoreViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}

