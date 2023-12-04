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
                getDataOrder(action.idStore, action.sortOrder)
            }

            is HomeStoreViewAction.getListServiceByStore -> {
                getListServiceByStore(action.idStore)
            }

            is HomeStoreViewAction.GetDataOrderStoreDate -> {
                getOrderDate(action.idStore, action.status, action.sortOrder)

            }

            is HomeStoreViewAction.GetDataOrderStoreDateWashing -> {
                getOrderDateWashing(action.idStore, action.status, action.sortOrder)
            }

            is HomeStoreViewAction.GetDataOrderStoreDateComplete -> {
                getOrderDateComplete(action.idStore, action.status, action.sortOrder)
            }

            is HomeStoreViewAction.GetDataOrderStoreDateCompleteMission -> {
                getOrderDateCompleteMission(action.idStore, action.status, action.sortOrder)
            }

            is HomeStoreViewAction.UpdateStatus -> {
                updateStatus(action.idOrder, action.status)
            }

            is HomeStoreViewAction.UpdateStatusWashing -> {
                updateStatusWashing(action.idOrder, action.status)
            }

            is HomeStoreViewAction.UpdateStatusComplete -> {
                updateStatusComplete(action.idOrder, action.status)
            }

            is HomeStoreViewAction.OpendCloseStore -> {
                opendClose(action.idStore, action.status)
            }

            is HomeStoreViewAction.FilterOrder -> {
                filterOrder(action.idStore, action.startDate, action.endDate, action.status)
            }

            is HomeStoreViewAction.GetStatisticalByToday -> {
                getStatisticalByToday(action.idStore)
            }

            is HomeStoreViewAction.GetStatisticalByMonth -> {
                getStatisticalByMonth(action.idStore, action.month)
            }
            is HomeStoreViewAction.GetStatisticalByWeek -> {
                getStatisticalByWeek(action.idStore,action.week)
            }

            else -> {}
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

    private fun getListServiceByStore(idStore: String) {
        setState { copy(stateGetListService = Loading()) }
        respoService.getListServiceByStore(idStore).execute { copy(stateGetListService = it) }
    }

    private fun getDataOrder(idStore: String, sortOrder: String) {
        setState { copy(stateGetOrderStore = Loading()) }
        orderRepo.getDataOrderStore(idStore, sortOrder).execute { copy(stateGetOrderStore = it) }
    }

    private fun getOrderDate(idStore: String, status: Int, sortOrder: String) {
        setState { copy(stateGetOrderDateStore = Loading()) }
        orderRepo.getOrderDateStoreWait(idStore, status, sortOrder)
            .execute { copy(stateGetOrderDateStore = it) }
    }

    private fun getOrderDateWashing(idStore: String, status: Int, sortOrder: String) {
        setState { copy(stateGetOrderDateStoreWashing = Loading()) }
        orderRepo.getOrderDateStoreWait(idStore, status, sortOrder)
            .execute { copy(stateGetOrderDateStoreWashing = it) }
    }

    private fun getOrderDateComplete(idStore: String, status: Int, sortOrder: String) {
        setState { copy(stateGetOrderDateStoreComplete = Loading()) }
        orderRepo.getOrderDateStoreWait(idStore, status, sortOrder)
            .execute { copy(stateGetOrderDateStoreComplete = it) }
    }

    private fun getOrderDateCompleteMission(idStore: String, status: Int, sortOrder: String) {
        setState { copy(stateGetOrderDateStoreCompleteMission = Loading()) }
        orderRepo.getOrderDateStoreWait(idStore, status, sortOrder)
            .execute { copy(stateGetOrderDateStoreCompleteMission = it) }
    }

    private fun updateStatus(idOrder: String, status: Int) {
        setState { copy(stateUpdateStatus = Loading()) }
        orderRepo.updateOrder(idOrder, status)
            .execute { copy(stateUpdateStatus = it) }
    }

    private fun updateStatusWashing(idOrder: String, status: Int) {
        setState { copy(stateUpdateStatusWashing = Loading()) }
        orderRepo.updateOrderWashing(idOrder, status)
            .execute { copy(stateUpdateStatusWashing = it) }
    }

    private fun updateStatusComplete(idOrder: String, status: Int) {
        setState { copy(stateUpdateStatusComplete = Loading()) }
        orderRepo.updateOrderComplete(idOrder, status)
            .execute { copy(stateUpdateStatusComplete = it) }
    }

    private fun opendClose(idStore: String, status: Int) {
        setState { copy(stateOpendCloseStore = Loading()) }
        resposeStore.opendCloseStore(idStore, status).execute { copy(stateOpendCloseStore = it) }
    }

    private fun filterOrder(idStore: String, startDate: String, endDate: String, status: Int) {
        setState { copy(stateFilterOrder = Loading()) }
        orderRepo.filterOrder(idStore, startDate, endDate, status)
            .execute { copy(stateFilterOrder = it) }
    }

    private fun getStatisticalByToday(idStore: String) {
        setState { copy(stateStatisticalByToday = Loading()) }
        orderRepo.getStatisticalByToday(idStore)
            .execute { copy(stateStatisticalByToday = it) }
    }

    private fun getStatisticalByMonth(idStore: String, month: Int) {
        setState { copy(stateStatisticalByMonth = Loading()) }
        orderRepo.getStatisticalByMonth(idStore, month)
            .execute { copy(stateStatisticalByMonth = it) }
    }

    private fun getStatisticalByWeek(idStore: String, week: Int) {
        setState { copy(stateStatisticalByWeek = Loading()) }
        orderRepo.getStatisticalByWeek(idStore, week)
            .execute { copy(stateStatisticalByWeek = it) }
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

