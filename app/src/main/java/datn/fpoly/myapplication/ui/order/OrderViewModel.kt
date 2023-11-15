package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AddressRepo
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class OrderViewModel @AssistedInject constructor(
    @Assisted state: OrderViewState,
    private val orderRepo: OrderRepo,
    private val storeRepo: StoreRepo,
    private val dbRepo: RoomDbRepo
) : BaseViewModel<OrderViewState, OrderViewAction, OrderViewEvent>(state) {
    override fun handle(action: OrderViewAction) {

    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: OrderViewState): OrderViewModel
    }

    companion object : MvRxViewModelFactory<OrderViewModel, OrderViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: OrderViewState
        ): OrderViewModel {
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

