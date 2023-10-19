package datn.fpoly.myapplication.ui.signup

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import okhttp3.ResponseBody
import retrofit2.Response

data class SignUpViewState (
    var stateSignUp: Async<Response<ResponseBody>> = Uninitialized
): MvRxState