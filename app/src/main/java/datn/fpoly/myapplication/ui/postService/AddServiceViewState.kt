package datn.fpoly.myapplication.ui.postService

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import okhttp3.ResponseBody
import retrofit2.Response

data class AddServiceViewState(
    var stateService: Async<Response<ResponseBody>>  = Uninitialized
): MvRxState