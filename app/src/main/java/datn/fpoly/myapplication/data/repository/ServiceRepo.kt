package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.model.post.PostService
import datn.fpoly.myapplication.data.network.APIService
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Part

class ServiceRepo @Inject constructor(
    private val api: APIService
) {
    fun getListServiceByStore(id: String): Observable<MutableList<ServiceModel>> =
        api.getListSeviceByStore(id).subscribeOn(
            Schedulers.io()
        )

    fun getListServiceByStore2(
        idStore: String,
        idService: String
    ): Observable<MutableList<ServiceModel>> =
        api.getListSeviceByStore2(idStore, idService).subscribeOn(
            Schedulers.io()
        )

    fun getListByCate(idCate: String): Observable<MutableList<ServiceModel>> =
        api.getListServiceByCate(idCate).subscribeOn(Schedulers.io())

    fun addService(
        image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        name: RequestBody,  // Tên sản phẩm
        price: RequestBody,  // Giá sản phẩm
        attributeList:Map<String, PostService.PostAttribute>,  // Danh sách thuộc tính sản phẩm
        isActive: RequestBody,  // Trạng thái kích hoạt
        unit: RequestBody,  // Đơn vị sản phẩm
        idCategory: RequestBody,  // ID danh mục
        idStore: RequestBody,  // ID cửa hàng
        unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        valueSale: RequestBody?
    ): Observable<Response<ResponseBody>> =
        api.addService(
            image,
            name,
            price,
            attributeList,
            isActive,
            unit,
            idCategory,
            idStore,
            unitSale,
            valueSale
        ).subscribeOn(Schedulers.io())
}