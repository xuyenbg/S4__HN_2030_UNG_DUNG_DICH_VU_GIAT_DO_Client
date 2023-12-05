package datn.fpoly.myapplication.ui.signup

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody


class SignUpViewModel @AssistedInject constructor(
    @Assisted state: SignUpViewState,
    private val repository: AuthRepo,
): BaseViewModel<SignUpViewState,SignUpViewAction,SignUpViewEvent>(state) {

    override fun handle(action: SignUpViewAction) {
        when (action){
            is SignUpViewAction.SignUpAction -> {
                handleSignUp(action.phone,action.passwd,action.fullname,action.idRole,null,action.avatar)
            }

            else -> {}
        }
    }

    private fun handleSignUp(phone : RequestBody, passwd : RequestBody, fullname: RequestBody, idRole: RequestBody, favouriteStores : List<String>?, avatar : MultipartBody.Part? ) {
        setState { copy(stateSignUp = Loading()) }
        repository.register(phone,passwd,fullname,idRole,null,avatar).execute { copy(stateSignUp = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: SignUpViewState): SignUpViewModel
    }

    companion object : MvRxViewModelFactory<SignUpViewModel, SignUpViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: SignUpViewState
        ): SignUpViewModel {
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