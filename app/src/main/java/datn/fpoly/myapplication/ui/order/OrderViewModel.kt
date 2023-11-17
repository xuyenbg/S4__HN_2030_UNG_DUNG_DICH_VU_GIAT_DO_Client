package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.OrderRepo

class OrderViewModel @AssistedInject constructor(
    @Assisted state: OrderViewState,
    private val orderRepo: OrderRepo
) : BaseViewModel<OrderViewState, OrderViewAction, OrderViewEvent>(state) {
    override fun handle(action: OrderViewAction) {
        when(action){
            is OrderViewAction.GetOrderDetail -> {
                handleGetOrderDetail(action.idOrder)
            }
        }
    }

    private fun handleGetOrderDetail(idOrder: String) {
        setState { copy(stateOrderDetail = Loading()) }
        orderRepo.getOrderDetail(idOrder).execute { copy(stateOrderDetail = it) }
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

