package datn.fpoly.myapplication.ui.service

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.ServiceRepo

class DetailServiceViewModel @AssistedInject constructor(
    @Assisted state: DetailServiceViewState,
    private var repo: ServiceRepo,
    private val dbRepo: RoomDbRepo
) : BaseViewModel<DetailServiceViewState, DetailServiceViewAction, DetailServiceViewEvent>(state) {
    override fun handle(action: DetailServiceViewAction) {
        when(action){
            is DetailServiceViewAction.GetListServiceByStore->{
                getListServiceByStore(action.idStore)
            }
        }
    }
    fun getListServiceByStore(id: String){
        setState { copy(stateService= Loading()) }
        repo.getListServiceByStore(id).execute { copy(stateService = it) }
    }

    fun getCart() = dbRepo.getCart()

    fun updateCart(order: Order) = dbRepo.updateCart(order)

    @AssistedFactory
    interface Factory {
        fun create(initialState: DetailServiceViewState): DetailServiceViewModel
    }

    companion object : MvRxViewModelFactory<DetailServiceViewModel, DetailServiceViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: DetailServiceViewState
        ): DetailServiceViewModel {
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

