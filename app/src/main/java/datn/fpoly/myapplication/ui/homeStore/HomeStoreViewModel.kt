package datn.fpoly.myapplication.ui.homeStore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.ServiceRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class HomeStoreViewModel @AssistedInject constructor(
    @Assisted state: HomeStoreState,
    private val responsePost: PostRepo,
    private val resposeCate: CategoryRepo,
    private val resposeStore: StoreRepo,
    private val orderRepo: OrderRepo,
    private val respoService: ServiceRepo
) : BaseViewModel<HomeStoreState, HomeStoreViewAction, HomeStoreViewEvent>(state) {
    override fun handle(action: HomeStoreViewAction) {
        when (action) {
            is HomeStoreViewAction.PostStoreActionList -> {
                getPostStore(action.idStore)
            }

            is HomeStoreViewAction.GetListCategory -> {
                getListCate()
            }

            is HomeStoreViewAction.GetStoreByIdUser -> {
                getStoreByIdUser(action.idUser)
            }

            is HomeStoreViewAction.deletePost -> {
                deletePost(action.idPost)
            }

            is HomeStoreViewAction.GetDataOrderStore -> {
                getDataOrder(action.idStore,action.sortOrder)
            }

            is HomeStoreViewAction.getListServiceByStore->{
                getListServiceByStore(action.idStore)
            }
        }
    }

    private fun getPostStore(idStore: String) {
        setState { copy(statePostStore = Loading()) }
        responsePost.getListPostStore(idStore).execute { copy(statePostStore = it) }
    }

    private fun getListCate() {
        setState { copy(stateCate = Loading()) }
        resposeCate.getDataCategory().execute { copy(stateCate = it) }
    }

    private fun getStoreByIdUser(idUser: String) {
        setState { copy(stateGetStore = Loading()) }
        resposeStore.getStoreByIdUser(idUser).execute { copy(stateGetStore = it) }
    }

    private fun deletePost(idPost: String) {
        setState { copy(stateDelete = Loading()) }
        responsePost.deletePost(idPost).execute { copy(stateDelete = it) }
    }
    private fun getListServiceByStore(idStore: String){
        setState { copy(stateGetListService= Loading()) }
        respoService.getListServiceByStore(idStore).execute { copy(stateGetListService = it) }
    }

    private fun getDataOrder(idStore: String, sortOrder : String) {
        setState { copy(stateGetOrderStore = Loading()) }
        orderRepo.getDataOrderStore(idStore,sortOrder).execute { copy(stateGetOrderStore = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: HomeStoreState): HomeStoreViewModel
    }

    companion object : MvRxViewModelFactory<HomeStoreViewModel, HomeStoreState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomeStoreState
        ): HomeStoreViewModel {
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

