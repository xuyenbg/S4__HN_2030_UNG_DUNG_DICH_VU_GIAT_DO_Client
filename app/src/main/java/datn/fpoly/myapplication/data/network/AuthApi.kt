package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.model.account.AcountLogin
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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

}