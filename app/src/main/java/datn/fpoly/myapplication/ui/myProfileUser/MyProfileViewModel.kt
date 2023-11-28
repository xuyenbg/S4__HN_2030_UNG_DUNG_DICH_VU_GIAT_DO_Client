package datn.fpoly.myapplication.ui.myProfileUser

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody


class MyProfileViewModel @AssistedInject constructor(
    @Assisted state: MyProfileViewState,
    private var repoUser: AuthRepo
) : BaseViewModel<MyProfileViewState, MyProfileViewAction, MyProfileViewEvent>(state) {

    override fun handle(action: MyProfileViewAction) {
        when (action) {
            is MyProfileViewAction.UpdateProfile -> {
                updateProfile(
                    action.idUser,
                    action.phone,
                    action.passwd,
                    action.fullName,
                    action.idRole,
                    action.favStore,
                    action.avata
                )
            }

            else -> {}
        }
    }

    private fun updateProfile(
        idUSer: String,
        phone: RequestBody,
        passwd: RequestBody,
        fullName: RequestBody,
        idRole: RequestBody?,
        favStore: Map<String, String>?,
        avata: MultipartBody.Part?
    ) {
        setState { copy(stateUpdateProfile = Loading()) }
        repoUser.updateProfile(idUSer, phone, passwd, fullName, favStore, idRole, avata)
            .execute { copy(stateUpdateProfile = it) }

    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: MyProfileViewState): MyProfileViewModel
    }

    companion object : MvRxViewModelFactory<MyProfileViewModel, MyProfileViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: MyProfileViewState
        ): MyProfileViewModel {
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