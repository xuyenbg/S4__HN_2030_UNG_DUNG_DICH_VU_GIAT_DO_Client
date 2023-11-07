package datn.fpoly.myapplication.ui.check_out

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel

data class CheckOutViewState (
    var stateCategory: Async<MutableList<CategoryModel>> = Uninitialized,
    var stateStore: Async<MutableList<StoreModel>> = Uninitialized,
    var statePost : Async<MutableList<PostModel>> = Uninitialized
): MvRxState