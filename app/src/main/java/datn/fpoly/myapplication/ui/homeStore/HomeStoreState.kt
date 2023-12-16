package datn.fpoly.myapplication.ui.homeStore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StatisticalModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.data.model.post.PostModel
import okhttp3.ResponseBody
import retrofit2.Response

data class HomeStoreState(
    var statePostStore: Async<MutableList<PostModel>> = Uninitialized,
    var stateCate: Async<MutableList<CategoryModel>> = Uninitialized,
    var stateGetStore: Async<StoreModel> = Uninitialized,
    var stateDelete: Async<Response<ResponseBody>> = Uninitialized,
    val stateGetOrderStore: Async<MutableList<OrderResponse>> = Uninitialized,
    val stateGetListService: Async<MutableList<ServiceExtend>> = Uninitialized,
    var stateGetOrderDateStore: Async<MutableList<OrderResponse>> = Uninitialized,
    var stateGetOrderDateStoreWashing: Async<MutableList<OrderResponse>> = Uninitialized,
    val stateGetOrderDateStoreComplete: Async<MutableList<OrderResponse>> = Uninitialized,
    var stateGetOrderDateStoreCompleteMission: Async<MutableList<OrderResponse>> = Uninitialized,
    val stateUpdateStatus: Async<Response<ResponseBody>> = Uninitialized,
    val stateUpdateStatusWashing: Async<Response<ResponseBody>> = Uninitialized,
    var stateUpdateStatusComplete: Async<Response<ResponseBody>> = Uninitialized,
    val stateOpendCloseStore : Async<Response<ResponseBody>> = Uninitialized,
    val stateFilterOrder : Async<MutableList<OrderResponse>> = Uninitialized,
    val stateStatisticalByToday : Async<StatisticalModel> = Uninitialized,
    val stateStatisticalByMonth : Async<StatisticalModel> = Uninitialized,
    val stateStatisticalByWeek : Async<StatisticalModel> = Uninitialized

) : MvRxState {
    fun isLoading(): Boolean {
        return statePostStore is Loading
                || stateCate is Loading
                || stateGetStore is Loading
                || stateDelete is Loading
                || stateGetOrderStore is Loading
                || stateGetListService is Loading
                || stateGetOrderDateStore is Loading
                || stateGetOrderDateStoreWashing is Loading
                || stateGetOrderDateStoreComplete is Loading
                || stateGetOrderDateStoreCompleteMission is Loading
                || stateUpdateStatus is Loading
                || stateUpdateStatusWashing is Loading
                || stateUpdateStatusComplete is Loading
                || stateOpendCloseStore is Loading
                || stateFilterOrder is Loading
                || stateStatisticalByToday is Loading
                || stateStatisticalByMonth is Loading
                || stateStatisticalByWeek is Loading
     }
}

