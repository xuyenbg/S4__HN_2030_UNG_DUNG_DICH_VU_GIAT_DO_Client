package datn.fpoly.myapplication.ui.myshop

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewAction
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class MyShopViewAction : ViewAction {
    data class GetStoreById(val id: String) : MyShopViewAction()
    data class UpdateStore(
        val idStore: String,
        val name : RequestBody,
        val address: RequestBody,
        val lat: RequestBody,
        val long: RequestBody,
        val isDefault : RequestBody,
        val idUser : RequestBody,
        val image : MultipartBody.Part?
    ) : MyShopViewAction()

    data class UpdateStoreOne(
        val idStore: String,
        val name : RequestBody,
        val image : MultipartBody.Part?
    ) : MyShopViewAction()
}