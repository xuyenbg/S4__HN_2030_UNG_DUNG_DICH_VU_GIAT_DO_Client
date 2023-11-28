package datn.fpoly.myapplication.ui.address

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AddressRepo

class AddressViewModel@AssistedInject constructor(
    @Assisted state: AddressViewState,
    private val repo: AddressRepo
): BaseViewModel<AddressViewState,AddressViewAction,AddressEvent>(state) {
    override fun handle(action: AddressViewAction) {
        when(action){
            is AddressViewAction.GetListAddress->{
                getListAddress(action.idUser)
            }
            is AddressViewAction.PutDefaultAddress->{
                putDefaultAddress(action.idAddress,action.idUser)
            }
        }
    }
    fun getListAddress(id: String){
        setState { copy(stateAddress= Loading()) }
        repo.getListAddress(id).execute { copy(stateAddress = it) }
    }
    fun putDefaultAddress(idAddress: String, idUser: String){
        setState { copy(stateAddressDefault= Loading()) }
        repo.putDefaultAddress(idAddress,idUser).execute { copy(stateAddressDefault = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: AddressViewState): AddressViewModel
    }
    companion object : MvRxViewModelFactory<AddressViewModel, AddressViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AddressViewState
        ): AddressViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? AddressViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? AddressViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}