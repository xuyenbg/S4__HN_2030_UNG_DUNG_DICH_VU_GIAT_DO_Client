package datn.fpoly.myapplication.ui.searchService

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.ServiceRepo

class SearchServiceViewModel @AssistedInject constructor(
    @Assisted state: SearchServiceViewState,
    private var repo: ServiceRepo
) : BaseViewModel<SearchServiceViewState, SearchServiceViewAction, SearchServiceViewEvent>(state) {
    override fun handle(action: SearchServiceViewAction) {
        when(action){
            is SearchServiceViewAction.SearchService->{
                searchService(action.query)
            }
        }
    }
    private fun searchService(query: String){
        setState { copy(stateSearchService = Loading()) }
        repo.getListServiceByName(query).execute { copy(stateSearchService = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: SearchServiceViewState): SearchServiceViewModel
    }

    companion object : MvRxViewModelFactory<SearchServiceViewModel, SearchServiceViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchServiceViewState
        ): SearchServiceViewModel {
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

