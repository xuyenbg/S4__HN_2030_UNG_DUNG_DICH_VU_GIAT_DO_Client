package datn.fpoly.myapplication.ui.registerstore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.StoreRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterStoreViewModel @AssistedInject constructor(
    @Assisted state: RegisterStoreViewState,
    private var storeRepo: StoreRepo
) : BaseViewModel<RegisterStoreViewState, RegisterStoreViewAction, RegisterStoreViewEvent>(state) {
    override fun handle(action: RegisterStoreViewAction) {
        when(action){
            is RegisterStoreViewAction.AddStore->{
              handleAddStore(action.name,action.rate,action.idUser,action.status,action.transportTypeList,action.imageQRCode,action.longitude,action.latitude,action.address,action.isDefault)
            }
        }
    }
    private fun handleAddStore(
        name: RequestBody,
        rate: RequestBody,
        idUser: RequestBody,
        status: RequestBody,
        transportTypeList: RequestBody,
        imageQRCode: MultipartBody.Part?,
        longitude: RequestBody,
        latitude: RequestBody,
        address: RequestBody,
        isDefault: RequestBody,
    ) {
        setState { copy(stateRegisterStore = Loading()) }
        storeRepo.registerStore(name,rate,idUser,status,transportTypeList,imageQRCode,longitude,latitude,address,isDefault
        ).execute { copy(stateRegisterStore = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: RegisterStoreViewState): RegisterStoreViewModel
    }

    companion object : MvRxViewModelFactory<RegisterStoreViewModel, RegisterStoreViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: RegisterStoreViewState
        ): RegisterStoreViewModel {
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

