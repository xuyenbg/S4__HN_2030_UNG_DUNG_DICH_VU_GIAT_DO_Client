package datn.fpoly.myapplication.ui.registerstore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.ServiceModel
import okhttp3.ResponseBody
import retrofit2.Response

data class RegisterStoreViewState(
    var stateRegisterStore: Async<Response<ResponseBody>> = Uninitialized
): MvRxState