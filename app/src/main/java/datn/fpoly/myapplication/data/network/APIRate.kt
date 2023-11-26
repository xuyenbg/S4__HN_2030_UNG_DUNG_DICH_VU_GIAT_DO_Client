package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.RateModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIRate {
    @FormUrlEncoded
    @POST("api/rates/addRate")
    fun addRate(
        @Field("idStore") idStore: String,
        @Field("idUser") idUser: String,
        @Field("rateNumber") rateNumber: Float,
        @Field("content") content: String,
        @Field("idOrder") idOrder: String
    ): Observable<Response<ResponseBody>>

    @GET("api/rates/list-by-id-store/{idStore}")
    fun getListRateByStore(@Path("idStore") idStore: String): Observable<MutableList<RateModel>>
}