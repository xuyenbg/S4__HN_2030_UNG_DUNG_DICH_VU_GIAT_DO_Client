package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.StoreNearplaceModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface APIStore {
    @GET("api/address/getListStoreNearest/{latitude}/{longitude}")
    fun getListStoreByLoaction(@Path("latitude") latitude: Float, @Path("longitude") longitude: Float): Observable<MutableList<StoreNearplaceModel>>

    @GET("api/stores/store-by-iduse/{idUser}")
    fun getStoreByIdUser(@Path("idUser") idUser: String): Observable<StoreModel>

    @Multipart
    @POST("api/stores/register-store")
    fun addStore(
        @Part("name") name: RequestBody,
        @Part("rate") rate: RequestBody,
        @Part("idUser") idUser: RequestBody,
        @Part("status") status: RequestBody,
        @PartMap transportTypeList: Map<String, String>,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("address") address: RequestBody,
        @Part("isDefault") isDefault: RequestBody,
        @Part imageQRCode: MultipartBody.Part?,
    ): Observable<Response<ResponseBody>>

    @GET("api/stores/store-by-idstore/{idStore}")
    fun getStoreById(@Path("idStore") idStore: String): Observable<StoreModel>
    @FormUrlEncoded
    @PUT("api/stores/open-close-store/{idStore}")
    fun opendAndCloseSStore(@Path("idStore") idStore: String ,@Field("status") status: Int ): Observable<Response<ResponseBody>>

    @Multipart
    @PUT("api/stores/update-store/{idStore}")
    fun getUpdateStore(
        @Path("idStore") idStore: String,
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("isDefault") isDefault : RequestBody,
        @Part("idUser") idUser : RequestBody,
        @Part image : MultipartBody.Part?
    ): Observable<StoreModel>

    @Multipart
    @PUT("api/stores/update-store/{idStore}")
    fun getUpdateStoreOne(
        @Path("idStore") idStore: String,
        @Part("name") name: RequestBody,
        @Part image : MultipartBody.Part?
    ): Observable<StoreModel>
}