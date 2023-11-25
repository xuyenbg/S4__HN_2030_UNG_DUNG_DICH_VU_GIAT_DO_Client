package datn.fpoly.myapplication.ui.myshop

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.StoreModel
import okhttp3.ResponseBody
import retrofit2.Response

data class MyShopViewState(
    var statStoreDetail: Async<StoreModel> = Uninitialized,
    var statUpdateStore: Async<Response<ResponseBody>> = Uninitialized
) : MvRxState