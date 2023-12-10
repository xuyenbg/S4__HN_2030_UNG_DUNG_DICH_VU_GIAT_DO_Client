package datn.fpoly.myapplication.ui.postService

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.model.post.PostService
import datn.fpoly.myapplication.data.repository.ServiceRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddServiceViewModel @AssistedInject constructor(
    @Assisted state: AddServiceViewState,
    private var repo: ServiceRepo
) : BaseViewModel<AddServiceViewState, AddServiceViewAction, AddServiceViewEvent>(state) {
    override fun handle(action: AddServiceViewAction) {
        when (action) {
            is AddServiceViewAction.AddService -> {
                postService(
                    action.image,
                    action.name,
                    action.price,
                    action.attributeList,
                    action.isActive,
                    action.unit,
                    action.idCategory, action.idStore, action.unitSale, action.valueSale
                )
            }
            is AddServiceViewAction.UpdateService -> {
                updateService(
                    action.idService,
                    action.image,
                    action.name,
                    action.price,
                    action.attributeList,
                    action.isActive,
                    action.unit,
                    action.idCategory, action.idStore, action.unitSale, action.valueSale
                )
            }
        }
    }

    private fun postService(
        image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        name: RequestBody,  // Tên sản phẩm
        price: RequestBody,  // Giá sản phẩm
        attributeList: Map<String, PostService.PostAttribute>?,  // Danh sách thuộc tính sản phẩm
        isActive: RequestBody,  // Trạng thái kích hoạt
        unit: RequestBody,  // Đơn vị sản phẩm
        idCategory: RequestBody,  // ID danh mục
        idStore: RequestBody,  // ID cửa hàng
        unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        valueSale: RequestBody?
    ) {
        setState { copy(stateService = Loading()) }
        repo.addService(
            image,
            name,
            price,
            attributeList,
            isActive,
            unit,
            idCategory,
            idStore,
            unitSale,
            valueSale
        ).execute { copy(stateService = it) }
    }

    private fun updateService(
        idService: String,
        image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        name: RequestBody,  // Tên sản phẩm
        price: RequestBody,  // Giá sản phẩm
        attributeList: Map<String, PostService.PostAttribute>?,  // Danh sách thuộc tính sản phẩm
        isActive: RequestBody,  // Trạng thái kích hoạt
        unit: RequestBody,  // Đơn vị sản phẩm
        idCategory: RequestBody,  // ID danh mục
        idStore: RequestBody,  // ID cửa hàng
        unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        valueSale: RequestBody?
    ) {
        setState { copy(stateServiceUpdate = Loading()) }
        repo.updateService(
            idService,
            image,
            name,
            price,
            attributeList,
            isActive,
            unit,
            idCategory,
            idStore,
            unitSale,
            valueSale
        ).execute { copy(stateServiceUpdate = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: AddServiceViewState): AddServiceViewModel
    }

    companion object : MvRxViewModelFactory<AddServiceViewModel, AddServiceViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AddServiceViewState
        ): AddServiceViewModel {
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

