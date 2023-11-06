package datn.fpoly.myapplication.ui.postService

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.data.model.post.PostService
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class AddServiceViewAction : ViewAction {
    data class AddService(
        val image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        val name: RequestBody,  // Tên sản phẩm
        val price: RequestBody,  // Giá sản phẩm
        val attributeList: Map<String, PostService.PostAttribute>,  // Danh sách thuộc tính sản phẩm
        val isActive: RequestBody,  // Trạng thái kích hoạt
        val unit: RequestBody,  // Đơn vị sản phẩm
        val idCategory: RequestBody,  // ID danh mục
        val idStore: RequestBody,  // ID cửa hàng
        val unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        val valueSale: RequestBody?
    ) : AddServiceViewAction()

}