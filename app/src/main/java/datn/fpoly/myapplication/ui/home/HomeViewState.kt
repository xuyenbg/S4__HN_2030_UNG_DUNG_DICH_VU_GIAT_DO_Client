package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.StoreNearplaceModel
import datn.fpoly.myapplication.data.model.post.PostModel
import okhttp3.ResponseBody
import retrofit2.Response

data class HomeViewState (
    var stateCategory: Async<MutableList<CategoryModel>> = Uninitialized,
    var stateStore: Async<MutableList<StoreNearplaceModel>> = Uninitialized,
    var statePost : Async<MutableList<PostModel>> = Uninitialized,
    var stateOrder: Async<MutableList<OrderExtend>> = Uninitialized,
    var stateRate: Async<Response<ResponseBody>>  = Uninitialized
): MvRxState{
    fun isLoading(): Boolean {
        return stateCategory is Loading
                || stateStore is Loading
                || statePost is Loading
                || stateOrder is Loading
                || stateRate is Loading
    }
}

