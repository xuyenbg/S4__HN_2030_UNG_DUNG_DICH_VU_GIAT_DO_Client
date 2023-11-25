package datn.fpoly.myapplication.ui.editpost

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import okhttp3.ResponseBody
import retrofit2.Response

data class EditPostViewState (
    var stateEditPost: Async<Response<ResponseBody>> = Uninitialized,
): MvRxState{
    fun isLoading(): Boolean {
        return stateEditPost is Loading
    }
}