package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
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
    ): Observable<MutableList<Order>>

    @GET("api/order/get-list-order-by-idStore/{idStore}")
    fun getListOrderStore(
        @Path("idStore") idStore: String,
        @Query("sortOrder") sortOrder: String
    ): Observable<MutableList<OrderResponse>>

    @POST("api/order/insert")
    fun insertOrder(@Body order: Order): Observable<Unit>

    @FormUrlEncoded
    @PUT("api/order/update-status/{idOrder}")
    fun updateOrder(
        @Path("idOrder") idOrder: String,
        @Field("status") status: Int
    ): Observable<Response<ResponseBody>>

    @GET("api/order/get-list-order-by-idUser/{idUser}")
    fun getListOrder(@Path("idUser") idUser: String): Observable<MutableList<OrderResponse>>

    @GET("api/order/list-order-today-by-idstore-status/{idStore}")
    fun getListOrderDateStore(
        @Path("idStore") idStore: String,
        @Query("status") status: Int,
        @Query("sortOrder") sortOrder: String
    ): Observable<MutableList<OrderResponse>>
}