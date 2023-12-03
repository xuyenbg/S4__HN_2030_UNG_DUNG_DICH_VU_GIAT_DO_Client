package datn.fpoly.myapplication.ui.notification

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class NotifycationViewAction : ViewAction {
data class GetListNotifyById( val idUser: String): NotifycationViewAction()
}