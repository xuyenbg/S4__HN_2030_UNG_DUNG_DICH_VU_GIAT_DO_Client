package datn.fpoly.myapplication.data.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIRate {
    @PUT("api/rate/addRate")
    @FormUrlEncoded
    fun addRate(
        @Field("idStore") idStore: String,
        @Field("idUser") idUser: String,
        @Field("rateNumber") rateNumber: Float,
        @Field("content") content: String,
        @Field("idOrder") idOrder: String
    ): Observable<Response<ResponseBody>>
}