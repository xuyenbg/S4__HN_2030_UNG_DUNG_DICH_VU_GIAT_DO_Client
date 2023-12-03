package datn.fpoly.myapplication.ui.login

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.StoreRepo


class LoginViewModel @AssistedInject constructor(
    @Assisted state: LoginViewState,
    private val repository: AuthRepo,
    private val repoStore: StoreRepo
): BaseViewModel<LoginViewState,LoginViewAction,LoginViewEvent>(state) {

    override fun handle(action: LoginViewAction) {
        when (action){
            is LoginViewAction.LoginAction -> {
                handleLogin(action.phone, action.userId)
            }
            is LoginViewAction.GetStoreDetail->{
                getStoreById(action.idUser)
            }

            else -> {}
        }
    }

    private fun handleLogin(phone: String, userId: String) {
        setState { copy(stateLogin = Loading()) }
        repository.login(phone, userId).execute { copy(stateLogin = it) }
    }
    private fun getStoreById(idUser: String){
        setState { copy(stateStore= Loading()) }
        repoStore.getStoreByIdUser(idUser).execute { copy(stateStore= it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: LoginViewState): LoginViewModel
    }

    companion object : MvRxViewModelFactory<LoginViewModel, LoginViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: LoginViewState
        ): LoginViewModel {
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