package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class HomeUserViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val responsePost: PostRepo,
    private val responseCategory: CategoryRepo,
    private val responseStore: StoreRepo,
    private val dbRepo: RoomDbRepo
) : BaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {
    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.HomeActionCategory -> {
                handleGetListCategory()
            }
            is HomeViewAction.HomeActionGetListStore->{
                handleGetListStore()
            }
            is HomeViewAction.PostClientActionList -> {
                handleGetPost()
            }
        }
    }

    private fun handleGetListCategory() {
        setState { copy(stateCategory = Loading()) }
        responseCategory.getDataCategory().execute { copy(stateCategory = it) }
    }
    private fun handleGetListStore(){
        setState { copy(stateStore = Loading()) }
        responseStore.getDataStore().execute { copy(stateStore = it) }
    }

    private fun handleGetPost() {
        setState { copy(statePost = Loading()) }
        responsePost.getListPost().execute { copy(statePost = it) }
    }

    fun getCart() = dbRepo.getCart()

    fun updateCart(order: Order) = dbRepo.updateCart(order)

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

