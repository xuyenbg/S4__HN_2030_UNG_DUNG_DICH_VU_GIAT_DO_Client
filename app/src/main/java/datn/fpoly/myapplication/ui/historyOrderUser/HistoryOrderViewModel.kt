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
import datn.fpoly.myapplication.data.repository.RatePepo

class HistoryOrderViewModel@AssistedInject constructor(
    @Assisted state: HistoryOrderViewState,
    private val repo: OrderRepo,
    private val respoRate: RatePepo
): BaseViewModel<HistoryOrderViewState,HistoryOrderViewAction,HistoryOrderViewEvent>(state) {
    override fun handle(action: HistoryOrderViewAction) {
        when(action){
            is HistoryOrderViewAction.GetListHistoryOrder->{
                handleGetListOrder(action.idUser)
            }
            is HistoryOrderViewAction.AddRate->{
                addRate(action.idStore, action.idUser, action.startNumber, action.content, action.idOrder)

            }
        }
    }

    private fun handleGetListOrder(idUser: String) {
        setState { copy(stateOrder = Loading()) }
        repo.getDataOrder(idUser).execute { copy(stateOrder = it) }
    }
    private fun addRate(idStore: String , idUser: String, startNumber: Float, content: String, idOrder: String){
        setState { copy(stateRate = Loading()) }
        respoRate.AddRate(idStore, idUser, startNumber, content, idOrder).execute { copy(stateRate= it) }
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