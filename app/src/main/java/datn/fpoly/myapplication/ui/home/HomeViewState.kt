package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel

data class HomeViewState (
    var stateCategory: Async<MutableList<CategoryModel>> = Uninitialized,
    var stateStore: Async<MutableList<StoreModel>> = Uninitialized,
    var statePost : Async<MutableList<PostModel>> = Uninitialized,
    var stateOrder: Async<MutableList<OrderExtend>> = Uninitialized
): MvRxState{
    fun isLoading(): Boolean {
        return stateCategory is Loading
                || stateStore is Loading
                || statePost is Loading
                || stateOrder is Loading
    }
}