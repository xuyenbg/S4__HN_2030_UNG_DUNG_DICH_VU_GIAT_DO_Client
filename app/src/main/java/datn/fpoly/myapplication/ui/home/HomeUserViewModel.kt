package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.StoreRepo
import datn.fpoly.myapplication.ui.login.LoginViewModel
import datn.fpoly.myapplication.ui.login.LoginViewState

class HomeUserViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val responseCategory: CategoryRepo,
    private val responseStore: StoreRepo
) : BaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {
    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.HomeActionCategory -> {
                hanlderGetListCategory()
            }
            is HomeViewAction.HomeActionGetListStore->{
                hanlderGetListStore()
            }
        }
    }

    private fun hanlderGetListCategory() {
        setState { copy(stateCategory = Loading()) }
        responseCategory.getDataCategory().execute { copy(stateCategory = it) }
    }
    private fun hanlderGetListStore(){
        setState { copy(stateStore = Loading()) }
        responseStore.getDataStore().execute { copy(stateStore = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: HomeViewState): HomeUserViewModel
    }

    companion object : MvRxViewModelFactory<HomeUserViewModel, HomeViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomeViewState
        ): HomeUserViewModel {
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

