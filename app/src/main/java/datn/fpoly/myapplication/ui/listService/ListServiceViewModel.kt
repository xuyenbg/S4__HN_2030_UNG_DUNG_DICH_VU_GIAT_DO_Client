package datn.fpoly.myapplication.ui.listService

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.*

class ListServiceViewModel @AssistedInject constructor(
    @Assisted state: ListServiceViewState,
   private val repo: ServiceRepo
) : BaseViewModel<ListServiceViewState, ListServiceViewAction, ListServiceViewEvent>(state) {
    override fun handle(action: ListServiceViewAction) {
        when (action) {
            is ListServiceViewAction.GetListServiceByCategory->{
                getListServiceByCategory(action.idCate)
            }
        }
    }
    private fun getListServiceByCategory(idCate: String){
        setState { copy(stateService = Loading()) }
        repo.getListByCate(idCate).execute { copy(stateService = it) }
    }


    @AssistedFactory
    interface Factory {
        fun create(initialState: ListServiceViewState): ListServiceViewModel
    }

    companion object : MvRxViewModelFactory<ListServiceViewModel, ListServiceViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: ListServiceViewState
        ): ListServiceViewModel {
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

