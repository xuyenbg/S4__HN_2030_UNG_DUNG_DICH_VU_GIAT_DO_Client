package datn.fpoly.myapplication.ui.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.User

data class HomeViewState (
    var stateCategory: Async<MutableList<CategoryModel>> = Uninitialized
): MvRxState