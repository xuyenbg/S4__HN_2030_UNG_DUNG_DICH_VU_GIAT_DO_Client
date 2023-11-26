package datn.fpoly.myapplication.ui.myProfileUser

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class MyProfileViewAction : ViewAction {
    data class UpdateProfile(
        val idUser: String,
        val phone: RequestBody,
        val passwd: RequestBody,
        val fullName: RequestBody,
        val idRole: RequestBody?,
        val favStore: Map<String, String>?,
        val avata: MultipartBody.Part?
    ): MyProfileViewAction()
}