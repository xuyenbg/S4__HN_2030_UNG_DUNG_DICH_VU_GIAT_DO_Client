package datn.fpoly.myapplication.ui.searchService

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceExtend

data class SearchServiceViewState(
    var stateSearchService: Async<MutableList<ServiceExtend>> = Uninitialized
): MvRxState