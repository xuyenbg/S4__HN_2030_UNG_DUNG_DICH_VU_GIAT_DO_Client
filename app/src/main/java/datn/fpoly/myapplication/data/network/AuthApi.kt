package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.account.AccountExtend
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.model.account.AcountLogin
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @POST("api/login")
    fun login(
//        @Field("phone") phone: String,
//        @Field("userId") userId: String
        @Body body: AcountLogin
    ): Observable<Response<ResponseBody>>

    @POST("api/register")
    fun register(
        @Body body: AccountModel
    ): Observable<Response<ResponseBody>>
    @PUT("api/users/add-fav-store/{idUser}")
    fun addFavoriteStore(
        @Path("idUser") idUser: String,
        @Body accountModel: AccountModel
    ): Observable<AccountModel>

    @GET("api/users/details/{idUser}")
    fun getDetailUser(@Path("idUser") idUser: String): Observable<AccountExtend>
    @PUT("api/users/remove-fav-store/{idUser}")
    fun removeFavoriteStore(
        @Path("idUser") idUser: String,
        @Body accountModel: AccountModel
    ): Observable<AccountModel>
}