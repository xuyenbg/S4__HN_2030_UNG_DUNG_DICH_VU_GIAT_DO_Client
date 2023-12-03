package datn.fpoly.myapplication.ui.splashScreen

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.*

class SplashViewModel @AssistedInject constructor(
    @Assisted state: SplashState,
    private val resposeStore: StoreRepo,
    private val userRepo: AuthRepo
) : BaseViewModel<SplashState, SplashViewAction, SplashViewEvent>(state) {
    override fun handle(action: SplashViewAction) {
        when (action) {
            is SplashViewAction.getStore -> {
                getStoreByIdUser(action.idUser)
            }
            is SplashViewAction.getUser -> {
                getAccountBYId(action.idUser)
            }
            else -> {}
        }
    }

    private fun getStoreByIdUser(idUser: String) {
        setState { copy(stateStore = Loading()) }
        resposeStore.getStoreByIdUser(idUser).execute { copy(stateStore = it) }
    }

    private fun getAccountBYId(idUser: String) {
        setState { copy(stateUser = Loading()) }
        userRepo.getDetailUser(idUser).execute { copy(stateUser = it) }
    }


    @AssistedFactory
    interface Factory {
        fun create(initialState: SplashState): SplashViewModel
    }

    companion object : MvRxViewModelFactory<SplashViewModel, SplashState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: SplashState
        ): SplashViewModel {
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

