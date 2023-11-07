package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class HomeUserViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val responsePost: PostRepo,
    private val responseCategory: CategoryRepo,
    private val responseStore: StoreRepo,
    private val dbRepo: RoomDbRepo,
    private val responseOrder: OrderRepo
) : BaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {
    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.HomeActionCategory -> {
                hanlderGetListCategory()
            }
            is HomeViewAction.HomeActionGetListStore->{
                hanlderGetListStore()
            }
            is HomeViewAction.PostClientActionList -> {
                handlerGetPost()
            }
            is HomeViewAction.OrderActionGetList ->{
                hanlderGetListOrder(action.idUser, action.status)
            }
        }
    }

    private fun hanlderGetListCategory() {
        setState { copy(stateCategory = Loading()) }
        responseCategory.getDataCategory().execute { copy(stateCategory = it) }
    }
    private fun hanlderGetListStore(){
        setState { copy(stateStore = Loading()) }
        responseStore.getDataStore().execute { copy(stateStore = it) }
    }

    private fun handlerGetPost() {
        setState { copy(statePost = Loading()) }
        responsePost.getListPost().execute { copy(statePost = it) }
    }
    private fun hanlderGetListOrder(idUser: String, status: Int) {
        setState { copy(stateOrder = Loading()) }
        responseOrder.getDataOrder(idUser, status).execute { copy(stateOrder = it) }
    }

    fun getCart() = dbRepo.getCart()

    @AssistedFactory
    interface Factory {
        fun create(initialState: HomeViewState): HomeUserViewModel
    }

    companion object : MvRxViewModelFactory<HomeUserViewModel, HomeViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomeViewState
        ): HomeUserViewModel {
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

