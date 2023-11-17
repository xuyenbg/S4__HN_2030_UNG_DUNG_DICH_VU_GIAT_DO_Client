package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.OrderBase
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
    ): Observable<MutableList<OrderBase>>

    @GET("api/order/get-list-order-by-idStore/{idStore}")
    fun getListOrderStore(
        @Path("idStore") idStore: String,
        @Query("sortOrder") sortOrder: String
    ): Observable<MutableList<OrderResponse>>

    @POST("api/order/insert")
    fun insertOrder(@Body orderBase: OrderBase): Observable<Unit>

    @GET("api/order/get-list-order-by-idUser/{idUser}")
    fun getListOrder(@Path("idUser") idUser: String): Observable<MutableList<OrderExtend>>

    @GET("api/order/order-detail/{idOrder}")
    fun getOrderDetail(@Path("idOrder") idOrder: String): Observable<OrderExtend>
}