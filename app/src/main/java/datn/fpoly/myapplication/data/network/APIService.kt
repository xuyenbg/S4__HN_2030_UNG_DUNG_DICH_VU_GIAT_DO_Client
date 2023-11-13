package datn.fpoly.myapplication.data.network


import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.model.post.PostService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @GET("api/services/list-service-by-store-service/")
    fun getListSeviceByStore(@Query("idStore") id: String): io.reactivex.Observable<MutableList<ServiceModel>>

    @GET("api/services/list-service-by-store-service/")
    fun getListSeviceByStore2(
        @Query("idStore") idStore: String,
        @Query("idService") idService: String
    ): io.reactivex.Observable<MutableList<ServiceModel>>

    @GET("api/services/list-service-by-category/{idCategory}")
    fun getListServiceByCate(@Path("idCategory") idCate: String): io.reactivex.Observable<MutableList<ServiceModel>>
    @Multipart
    @POST("api/services/insert")
    fun addService(
        @Part image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        @Part("name") name: RequestBody,  // Tên sản phẩm
        @Part("price") price: RequestBody,  // Giá sản phẩm
        @PartMap attributeList:Map<String, PostService.PostAttribute>,  // Danh sách thuộc tính sản phẩm
        @Part("isActive") isActive: RequestBody,  // Trạng thái kích hoạt
        @Part("unit") unit: RequestBody,  // Đơn vị sản phẩm
        @Part("idCategory") idCategory: RequestBody,  // ID danh mục
        @Part("idStore") idStore: RequestBody,  // ID cửa hàng
        @Part("unitSale") unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        @Part("valueSale") valueSale: RequestBody?
    ): io.reactivex.Observable<Response<ResponseBody>>
    @Multipart
    @PUT("api/services/update/:{idService}")
    fun UpdateService(
        @Path("idService") idService: String,
        @Part image: MultipartBody.Part?,  // Phần dữ liệu của hình ảnh
        @Part("name") name: RequestBody,  // Tên sản phẩm
        @Part("price") price: RequestBody,  // Giá sản phẩm
        @PartMap attributeList:Map<String, PostService.PostAttribute>,  // Danh sách thuộc tính sản phẩm
        @Part("isActive") isActive: RequestBody,  // Trạng thái kích hoạt
        @Part("unit") unit: RequestBody,  // Đơn vị sản phẩm
        @Part("idCategory") idCategory: RequestBody,  // ID danh mục
        @Part("idStore") idStore: RequestBody,  // ID cửa hàng
        @Part("unitSale") unitSale: RequestBody?,  // Đơn vị giảm giá (nếu có)
        @Part("valueSale") valueSale: RequestBody?
    ): io.reactivex.Observable<Response<ResponseBody>>

    @GET("api/services/")
    fun getServiceById(@Path("") idService: String): io.reactivex.Observable<ServiceModel>
}