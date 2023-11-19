package datn.fpoly.myapplication.ui.listServiceByName

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.*

class ListServiceByNameViewModel @AssistedInject constructor(
    @Assisted state: ListServiceByNameViewState,
    private val repo: ServiceRepo
) : BaseViewModel<ListServiceByNameViewState, ListServiceByNameViewAction, ListServiceByNameViewEvent>(state) {
    override fun handle(action: ListServiceByNameViewAction) {
        when (action) {
            is ListServiceByNameViewAction.GetListServiceByName->{
                getListServiceByName(action.nameService)
            }
        }
    }
    private fun getListServiceByName(nameService: String){
        setState { copy(stateService = Loading()) }
        repo.getListServiceByName(nameService).execute { copy(stateService = it) }
    }


    @AssistedFactory
    interface Factory {
        fun create(initialState: ListServiceByNameViewState): ListServiceByNameViewModel
    }

    companion object : MvRxViewModelFactory<ListServiceByNameViewModel, ListServiceByNameViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: ListServiceByNameViewState
        ): ListServiceByNameViewModel {
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

