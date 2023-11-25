package datn.fpoly.myapplication.ui.myshop

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.StoreRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody


class MyShopViewModel @AssistedInject constructor(
    @Assisted state: MyShopViewState,
    private var repoStore: StoreRepo
) : BaseViewModel<MyShopViewState, MyShopViewAction, MyShopViewEvent>(state) {

    override fun handle(action: MyShopViewAction) {
        when (action) {
            is MyShopViewAction.GetStoreById -> {
                handleGetStore(action.id)
            }

            is MyShopViewAction.UpdateStore -> {
                handleUpdateStore(
                    action.idStore,
                    action.name,
                    action.address,
                    action.lat,
                    action.long,
                    action.isDefault,
                    action.idUser,
                    action.image
                )
            }
            is MyShopViewAction.UpdateStoreOne -> {
                handleUpdateStoreOne(action.idStore,action.name, action.image)
            }

            else -> {}
        }
    }

    private fun handleGetStore(id: String) {
        setState { copy(statStoreDetail = Loading()) }
        repoStore.getStoreById(id).execute { copy(statStoreDetail = it) }
    }

    private fun handleUpdateStore(
        idStore: String,
        name: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        isDefault: RequestBody,
        idUser: RequestBody,
        image : MultipartBody.Part?
    ) {
        setState { copy(statUpdateStore = Loading()) }
        repoStore.updateStore(idStore, name, address, lat, long, isDefault, idUser,image)
            .execute { copy(statUpdateStore = it) }
    }
    private fun handleUpdateStoreOne(
        idStore: String,
        name: RequestBody,
        image : MultipartBody.Part?
    ) {
        setState { copy(statUpdateStore = Loading()) }
        repoStore.updateStoreOne(idStore, name, image)
            .execute { copy(statUpdateStore = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: MyShopViewState): MyShopViewModel
    }

    companion object : MvRxViewModelFactory<MyShopViewModel, MyShopViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: MyShopViewState
        ): MyShopViewModel {
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