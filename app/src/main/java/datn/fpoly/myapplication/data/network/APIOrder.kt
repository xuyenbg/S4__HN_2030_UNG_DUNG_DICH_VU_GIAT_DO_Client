package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.OrderExtendHistory
import datn.fpoly.myapplication.data.model.StatisticalModel
import datn.fpoly.myapplication.data.model.StatisticsModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIOrder {
    @GET("api/order/get-list-order-by-status-user/{idUser}/{status}")
    fun getListOrder(
        @Path("idUser") idUser: String,
        @Path("status") status: Int
    ): Observable<MutableList<OrderBase>>

    @GET("api/order/get-list-order-by-idStore/{idStore}")
    fun getListOrderStore(
        @Path("idStore") idStore: String,
        @Query("sortOrder") sortOrder: String
    ): Observable<MutableList<OrderResponse>>

    @POST("api/order/insert")
    fun insertOrder(@Body orderBase: OrderBase): Observable<Unit>

    @PUT("api/order/update-order/{idOrder}")
    fun updateOrder(@Body orderBase: OrderBase, @Path("idOrder") idOrder: String): Observable<Unit>

    @FormUrlEncoded
    @PUT("api/order/update-status/{idOrder}")
    fun updateOrder(
        @Path("idOrder") idOrder: String,
        @Field("status") status: Int
    ): Observable<Response<ResponseBody>>

    @GET("api/order/get-list-order-by-idUser/{idUser}")
    fun getListOrder(@Path("idUser") idUser: String): Observable<MutableList<OrderExtend>>

    @GET("api/order/order-detail/{idOrder}")
    fun getOrderDetail(@Path("idOrder") idOrder: String): Observable<OrderExtendHistory>

    @GET("api/order/list-order-today-by-idstore-status/{idStore}")
    fun getListOrderDateStore(
        @Path("idStore") idStore: String,
        @Query("status") status: Int,
        @Query("sortOrder") sortOrder: String
    ): Observable<MutableList<OrderResponse>>

    @GET("api/order/list-order-by-date-status/{idStore}")
    fun getFilterOrder(
        @Path("idStore") idStore: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("status") status: Int
    ): Observable<MutableList<OrderResponse>>

    @GET("api/order/total-order-by-currentdate/{idStore}")
    fun getStatisticalByToday(
        @Path("idStore") idStore: String,
    ): Observable<StatisticalModel>

    @GET("api/order/total-order-by-week-month/{idStore}")
    fun getStatisticalByMonth(
        @Path("idStore") idStore: String,
        @Query("month") month: Int
    ): Observable<StatisticalModel>

    @GET("api/order/total-order-by-week-month/{idStore}")
    fun getStatisticalByWeek(
        @Path("idStore") idStore: String,
        @Query("week") week: Int
    ): Observable<StatisticalModel>

    @GET("api/order/thong-ke-theo-tuan/{idStore}")
    fun getDetailStatistical(
        @Path("idStore") idStore: String,
    ) : Observable<MutableList<StatisticsModel>>
}