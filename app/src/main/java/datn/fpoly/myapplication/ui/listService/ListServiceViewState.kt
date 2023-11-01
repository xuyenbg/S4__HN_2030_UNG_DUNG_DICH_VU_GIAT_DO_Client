package datn.fpoly.myapplication.ui.listService

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel

data class ListServiceViewState (
    var stateService: Async<MutableList<ServiceModel>> = Uninitialized,
): MvRxState