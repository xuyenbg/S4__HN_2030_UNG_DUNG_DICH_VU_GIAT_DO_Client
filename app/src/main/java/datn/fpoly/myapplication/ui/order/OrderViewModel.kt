package datn.fpoly.myapplication.ui.order

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.repository.OrderRepo
import datn.fpoly.myapplication.data.repository.UploadRepo
import okhttp3.MultipartBody

class OrderViewModel @AssistedInject constructor(
    @Assisted state: OrderViewState,
    private val orderRepo: OrderRepo,
    private val uploadRepo: UploadRepo
) : BaseViewModel<OrderViewState, OrderViewAction, OrderViewEvent>(state) {
    override fun handle(action: OrderViewAction) {
        when(action){
            is OrderViewAction.GetOrderDetail -> {
                handleGetOrderDetail(action.idOrder)
            }
            is OrderViewAction.UpdateOrder -> {
                handleUpdateOrder(action.orderBase, action.idOrder)
            }
            is OrderViewAction.UploadImage -> {
                handleUploadImage(action.image)
            }
            is OrderViewAction.UpdateStatus->{
                updateStatus(action.idOrder, action.status)
            }
        }
    }

    private fun handleUploadImage(image: MultipartBody.Part) {
        setState { copy(stateUploadImage = Loading()) }
        uploadRepo.uploadImage(image).execute { copy(stateUploadImage = it) }
    }

    private fun handleUpdateOrder(orderBase: OrderBase, idOrder: String) {
        setState { copy(stateUpdateOrder = Loading()) }
        orderRepo.updateOrder(orderBase, idOrder).execute { copy(stateUpdateOrder = it) }
    }

    private fun handleGetOrderDetail(idOrder: String) {
        setState { copy(stateOrderDetail = Loading()) }
        orderRepo.getOrderDetail(idOrder).execute { copy(stateOrderDetail = it) }
    }
    private fun updateStatus(idOrder: String, status: Int){
        setState { copy(stateUpdateStatus= Loading()) }
        orderRepo.updateOrder(idOrder, status).execute { copy(stateUpdateStatus = it)}
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: OrderViewState): OrderViewModel
    }

    companion object : MvRxViewModelFactory<OrderViewModel, OrderViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: OrderViewState
        ): OrderViewModel {
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

