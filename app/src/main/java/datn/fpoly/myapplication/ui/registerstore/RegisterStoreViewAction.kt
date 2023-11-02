package datn.fpoly.myapplication.ui.registerstore

import datn.fpoly.myapplication.core.ViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

sealed class RegisterStoreViewAction : ViewAction {
    data class AddStore(
        val name: RequestBody,
        val rate: RequestBody,
        val idUser: RequestBody,
        val status: RequestBody,
        val transportTypeList: RequestBody,
        val longitude: RequestBody,
        val latitude: RequestBody,
        val address: RequestBody,
        val isDefault: RequestBody,
        val imageQRCode: MultipartBody.Part?,
    ) : RegisterStoreViewAction()
}