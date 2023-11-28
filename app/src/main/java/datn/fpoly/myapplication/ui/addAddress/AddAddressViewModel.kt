package datn.fpoly.myapplication.ui.addAddress

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.AddressModel
import datn.fpoly.myapplication.data.repository.AddressRepo

class AddAddressViewModel@AssistedInject constructor(
    @Assisted state: AddAddressViewState,
    private val repo: AddressRepo
):BaseViewModel<AddAddressViewState,AddAddressViewAction,AddAddressEvent>(state) {
    override fun handle(action: AddAddressViewAction) {
        when(action){
            is AddAddressViewAction.AddAddress->{
                addAddress(action.addressModel)
            }
        }
    }

    fun addAddress(addressModel: AddressModel){
        setState { copy(stateAddAddress= Loading()) }
        repo.addAddress(addressModel).execute { copy(stateAddAddress = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: AddAddressViewState): AddAddressViewModel
    }
    companion object : MvRxViewModelFactory<AddAddressViewModel, AddAddressViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AddAddressViewState
        ): AddAddressViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? AddAddressViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? AddAddressViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}