package datn.fpoly.myapplication.ui.homeStore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel

data class HomeStoreState (
    var statePostStore : Async<MutableList<PostModel>> = Uninitialized
): MvRxState