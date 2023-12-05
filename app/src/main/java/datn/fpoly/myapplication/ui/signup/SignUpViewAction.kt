package datn.fpoly.myapplication.ui.signup

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class SignUpViewAction : ViewAction {
    data class SignUpAction(val phone: RequestBody, val passwd: RequestBody, val fullname: RequestBody,var idRole : RequestBody,val favouriteStores : List<String>?, val avatar : MultipartBody.Part?) : SignUpViewAction()
}