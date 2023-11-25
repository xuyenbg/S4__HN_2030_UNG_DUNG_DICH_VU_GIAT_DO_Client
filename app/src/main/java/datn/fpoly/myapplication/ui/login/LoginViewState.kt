package datn.fpoly.myapplication.ui.login

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import okhttp3.ResponseBody
import retrofit2.Response

data class LoginViewState (
    var stateLogin: Async<Response<ResponseBody>> = Uninitialized
): MvRxState