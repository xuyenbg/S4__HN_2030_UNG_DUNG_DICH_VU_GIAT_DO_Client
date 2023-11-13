package datn.fpoly.myapplication.ui.editpost

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class EditPostViewAction : ViewAction {
    data class EditPostAction(
        val idPost : String,
        val title: RequestBody,
        val content: RequestBody,
        val image: MultipartBody.Part?
    ) : EditPostViewAction()
}