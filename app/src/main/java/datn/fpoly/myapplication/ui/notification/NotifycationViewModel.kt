package datn.fpoly.myapplication.ui.notification

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.NotifyRepo
import datn.fpoly.myapplication.data.repository.StoreRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody


class NotifycationViewModel @AssistedInject constructor(
    @Assisted state: NotifycationViewState,
    private val notify: NotifyRepo
) : BaseViewModel<NotifycationViewState, NotifycationViewAction, NotifycationViewEvent>(state) {

    override fun handle(action: NotifycationViewAction) {
        when (action) {
            is NotifycationViewAction.GetListNotifyById->{
                getListNotify(action.idUser)
            }

            else -> {}
        }
    }
    private fun getListNotify(idUser: String){
        setState { copy(stateNotify =Loading()) }
        notify.getListNotifyById(idUser).execute { copy(stateNotify=it) }
    }



    @AssistedFactory
    interface Factory {
        fun create(initialState: NotifycationViewState): NotifycationViewModel
    }

    companion object : MvRxViewModelFactory<NotifycationViewModel, NotifycationViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: NotifycationViewState
        ): NotifycationViewModel {
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