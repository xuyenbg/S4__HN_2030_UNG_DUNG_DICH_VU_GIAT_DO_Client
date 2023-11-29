package datn.fpoly.myapplication.ui.order

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.OrderBase
import okhttp3.MultipartBody

sealed class OrderViewAction : ViewAction {
    data class GetOrderDetail(var idOrder: String) : OrderViewAction()
    data class UpdateOrder(var orderBase: OrderBase, val idOrder: String) : OrderViewAction()
    data class UploadImage(var image: MultipartBody.Part) : OrderViewAction()
    data class UpdateStatus(val idOrder: String, val status: Int): OrderViewAction()
}