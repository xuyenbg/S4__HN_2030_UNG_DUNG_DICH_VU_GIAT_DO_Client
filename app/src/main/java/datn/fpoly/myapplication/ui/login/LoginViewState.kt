package datn.fpoly.myapplication.ui.login

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.model.account.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response

data class LoginViewState (
    var stateLogin: Async<LoginResponse> = Uninitialized,
    val stateStore: Async<StoreModel> = Uninitialized
): MvRxState