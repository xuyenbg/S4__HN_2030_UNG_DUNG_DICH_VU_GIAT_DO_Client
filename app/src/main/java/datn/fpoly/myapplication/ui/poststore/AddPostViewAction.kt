package datn.fpoly.myapplication.ui.poststore

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class AddPostViewAction : ViewAction {
    data class AddPostAction(
        val idStore: RequestBody,
        val title: RequestBody,
        val content: RequestBody,
        val image: MultipartBody.Part?
    ) : AddPostViewAction()
}