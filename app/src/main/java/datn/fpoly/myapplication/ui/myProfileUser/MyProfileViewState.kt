package datn.fpoly.myapplication.ui.myProfileUser

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response

data class MyProfileViewState(
    var stateUpdateProfile: Async<LoginResponse> = Uninitialized
) : MvRxState