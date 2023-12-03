package datn.fpoly.myapplication.ui.address.check_out

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.repository.AddressRepo
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class CheckOutViewModel @AssistedInject constructor(
    @Assisted state: CheckOutViewState,
    private val orderRepo: OrderRepo,
    private val storeRepo: StoreRepo,
    private val addressRepo: AddressRepo,
    private val authRepo: AuthRepo,
    private val dbRepo: RoomDbRepo
) : BaseViewModel<CheckOutViewState, CheckOutViewAction, CheckOutViewEvent>(state) {
    override fun handle(action: CheckOutViewAction) {
        when (action) {
            is CheckOutViewAction.GetStoreById -> handleGetStoreById(action.idStore)

            is CheckOutViewAction.InsertOrder -> handleInsertOrder(action.orderBase)

            is CheckOutViewAction.GetListAddress -> handleGetListAddress()
        }

    }

    private fun handleGetListAddress() {
        setState { copy(stateGetListAddress = Loading()) }
        val idUser = authRepo.getUser()?.id ?: "-"
        addressRepo.getListAddress(idUser).execute { copy(stateGetListAddress = it) }
    }

    private fun handleInsertOrder(orderBase: OrderBase) {
        setState { copy(stateInsertOrder = Loading()) }
        orderRepo.insertOrder(orderBase).execute { copy(stateInsertOrder = it) }
    }

    private fun handleGetStoreById(idStore: String) {
        setState { copy(stateGetStoreById = Loading()) }
        storeRepo.getStoreById(idStore).execute { copy(stateGetStoreById = it) }
    }

    fun getCart() = dbRepo.getCart()

    fun clearCart() = dbRepo.clearCart()

    @AssistedFactory
    interface Factory {
        fun create(initialState: CheckOutViewState): CheckOutViewModel
    }

    companion object : MvRxViewModelFactory<CheckOutViewModel, CheckOutViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: CheckOutViewState
        ): CheckOutViewModel {
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

