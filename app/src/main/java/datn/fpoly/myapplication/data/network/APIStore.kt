package datn.fpoly.myapplication.data.network

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import datn.fpoly.myapplication.data.model.StoreModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface APIStore {
    @GET("api/stores/list")
    fun getListCategory(): Observable<MutableList<StoreModel>>

    @Multipart
    @POST("api/stores/register-store")
    fun addStore(
        @Part("name") name: RequestBody,
        @Part("rate") rate: RequestBody,
        @Part("idUser") idUser: RequestBody,
        @Part("status") status: RequestBody,
        @Part("transportTypeList") transportTypeList: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("address") address: RequestBody,
        @Part("isDefault") isDefault: RequestBody,
        @Part imageQRCode: MultipartBody.Part?,
    ): Observable<Response<ResponseBody>>

    @GET("api/stores/store-by-idstore/{id}")
    fun getStoreById(@Path("id") id:String) : Observable<StoreModel>
}