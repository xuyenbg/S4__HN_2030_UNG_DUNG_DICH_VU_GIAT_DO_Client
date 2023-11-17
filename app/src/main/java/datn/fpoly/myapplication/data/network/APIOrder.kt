package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("api/order/get-list-order-by-idUser/{idUser}")
    fun getListOrder(@Path("idUser") idUser: String): Observable<MutableList<OrderExtend>>
}