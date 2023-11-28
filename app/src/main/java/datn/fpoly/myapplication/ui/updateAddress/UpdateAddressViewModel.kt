package datn.fpoly.myapplication.ui.updateAddress

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

class UpdateAddressViewModel@AssistedInject constructor(
    @Assisted state: UpdateAddressViewState,
    private val repo: AddressRepo
):BaseViewModel<UpdateAddressViewState,UpdateAddressViewAction,UpdateAddressViewEvent>(state) {
    override fun handle(action: UpdateAddressViewAction) {
        when(action){
            is UpdateAddressViewAction.PutAddress->{
                putAddress(action.idAddress,action.addressModel)
            }
            is UpdateAddressViewAction.GetDetailAddress->{
                getDetailAddress(action.idAddress)
            }
        }
    }

    fun getDetailAddress(idAddress: String){
        setState { copy(stateDetailAddress=Loading()) }
        repo.getDetailAddress(idAddress).execute { copy(stateDetailAddress=it) }
    }
    fun putAddress(idAddress: String,addressModel: AddressModel){
        setState { copy(stateAddress=Loading()) }
        repo.putAddress(idAddress,addressModel).execute { copy(stateAddress=it) }
    }
    @AssistedFactory
    interface Factory {
        fun create(initialState: UpdateAddressViewState): UpdateAddressViewModel
    }
    companion object : MvRxViewModelFactory<UpdateAddressViewModel, UpdateAddressViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: UpdateAddressViewState
        ): UpdateAddressViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? UpdateAddressViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? UpdateAddressViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}