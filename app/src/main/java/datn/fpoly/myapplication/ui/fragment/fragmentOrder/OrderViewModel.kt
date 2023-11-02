package datn.fpoly.myapplication.ui.fragment.fragmentOrder

import androidx.room.util.copy
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
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewState

class OrderViewModel @AssistedInject constructor(
    @Assisted state: OrderViewState,
    private val responseOrder: OrderRepo
) : BaseViewModel<OrderViewState,OrderViewAction,OrderViewEvent>(state) {
    override fun handle(action: OrderViewAction) {
        when(action){
            is OrderViewAction.OrderActionGetList ->{
                hanlderGetListOrder()
            }
        }
    }

    private fun hanlderGetListOrder() {
        setState { copy(stateOrder = Loading()) }
        responseOrder.getDataOrder().execute { copy(stateOrder = it) }
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