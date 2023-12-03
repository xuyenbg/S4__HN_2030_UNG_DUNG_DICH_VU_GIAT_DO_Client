package datn.fpoly.myapplication.ui.splashScreen

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.*
import datn.fpoly.myapplication.data.model.account.AccountExtend
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.data.model.post.PostModel
import okhttp3.ResponseBody
import retrofit2.Response

data class SplashState(
   var stateStore: Async<StoreModel> = Uninitialized,
   var stateUser: Async<AccountExtend> = Uninitialized

) : MvRxState

