package datn.fpoly.myapplication.ui.detailedstatistics

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

class DetailedStatisticsViewModel@AssistedInject constructor(
    @Assisted state: DetailedStatisticsViewState,
    private val repo: OrderRepo,
): BaseViewModel<DetailedStatisticsViewState,DetailedStatisticsViewAction,DetailedStatisticsViewEvent>(state) {
    override fun handle(action: DetailedStatisticsViewAction) {
        when(action){
            is DetailedStatisticsViewAction.GetDetailedStatistics->{
                handleGetListOrder(action.idStore)
            }
        }
    }

    private fun handleGetListOrder(idStore: String) {
        setState { copy(stateDetailedStatistics = Loading()) }
        repo.getStatisticalDetail(idStore).execute { copy(stateDetailedStatistics = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: DetailedStatisticsViewState): DetailedStatisticsViewModel
    }
    companion object : MvRxViewModelFactory<DetailedStatisticsViewModel, DetailedStatisticsViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: DetailedStatisticsViewState
        ): DetailedStatisticsViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? DetailedStatisticsViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? DetailedStatisticsViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}