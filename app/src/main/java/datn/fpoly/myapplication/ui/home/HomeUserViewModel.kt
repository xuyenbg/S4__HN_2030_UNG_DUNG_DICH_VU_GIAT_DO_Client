package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.RatePepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class HomeUserViewModel @AssistedInject constructor(
    @Assisted state: HomeViewState,
    private val repoPost: PostRepo,
    private val repoCategory: CategoryRepo,
    private val repoStore: StoreRepo,
    private val repoOrder: OrderRepo,
    private val dbRepo: RoomDbRepo,
    private val respoRate: RatePepo
) : BaseViewModel<HomeViewState, HomeViewAction, HomeViewEvent>(state) {
    override fun handle(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.HomeActionCategory -> {
                handleGetListCategory()
            }
            is HomeViewAction.HomeActionGetListStore->{
                handleGetListStore(action.latitude,action.longitude)
            }
            is HomeViewAction.PostClientActionList -> {
                handleGetPost()
            }
            is HomeViewAction.OrderActionGetList ->{
                handleGetListOrder(action.idUser)
            }
            is HomeViewAction.AddRate->{
                addRate(action.idStore, action.idUser, action.startNumber, action.content, action.idOrder)
            }

            else -> {}
        }
    }

    private fun handleGetListCategory() {
        setState { copy(stateCategory = Loading()) }
        repoCategory.getDataCategory().execute { copy(stateCategory = it) }
    }
    private fun handleGetListStore(latitude: Float, longitude:Float){
        setState { copy(stateStore = Loading()) }
        repoStore.getDataStore(latitude, longitude).execute { copy(stateStore = it) }
    }

    private fun handleGetPost() {
        setState { copy(statePost = Loading()) }
        repoPost.getListPost().execute { copy(statePost = it) }
    }
    private fun handleGetListOrder(idUser: String) {
        setState { copy(stateOrder = Loading()) }
        repoOrder.getDataOrder(idUser).execute { copy(stateOrder = it) }
    }
    private fun addRate(idStore: String , idUser: String, startNumber: Float, content: String, idOrder: String){
        setState { copy(stateRate = Loading()) }
        respoRate.AddRate(idStore, idUser, startNumber, content, idOrder).execute { copy(stateRate= it) }
    }

    fun getCart() = dbRepo.getCart()

    fun updateCart(orderBase: OrderBase) = dbRepo.updateCart(orderBase)

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

