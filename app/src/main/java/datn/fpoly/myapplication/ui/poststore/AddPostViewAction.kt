package datn.fpoly.myapplication.ui.poststore

import datn.fpoly.myapplication.core.ViewAction

sealed class AddPostViewAction : ViewAction {
    data class AddPostAction(
        val idStore: String,
        val title: String,
        val content: String,
        val image: String?
    ) : AddPostViewAction()
}