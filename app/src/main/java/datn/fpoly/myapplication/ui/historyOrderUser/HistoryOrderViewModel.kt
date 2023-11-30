package datn.fpoly.myapplication.ui.historyOrderUser

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

class HistoryOrderViewModel@AssistedInject constructor(
    @Assisted state: HistoryOrderViewState,
    private val repo: OrderRepo
): BaseViewModel<HistoryOrderViewState,HistoryOrderViewAction,HistoryOrderViewEvent>(state) {
    override fun handle(action: HistoryOrderViewAction) {
        when(action){
            is HistoryOrderViewAction.GetListHistoryOrder->{
                handleGetListOrder(action.idUser)
            }
        }
    }

    private fun handleGetListOrder(idUser: String) {
        setState { copy(stateOrder = Loading()) }
        repo.getDataOrder(idUser).execute { copy(stateOrder = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: HistoryOrderViewState): HistoryOrderViewModel
    }
    companion object : MvRxViewModelFactory<HistoryOrderViewModel, HistoryOrderViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HistoryOrderViewState
        ): HistoryOrderViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? HistoryOrderViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? HistoryOrderViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}