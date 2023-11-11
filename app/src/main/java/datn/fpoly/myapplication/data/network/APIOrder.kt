package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIOrder {
    @GET("api/order/get-list-order-by-status-user/{idUser}/{status}")
    fun getListOrder(@Path("idUser") idUser: String,@Path("status") status: Int):Observable<MutableList<Order>>

    @GET("api/order/list")
    fun getListOrder(): Observable<MutableList<Order>>

    @POST("api/order/insert")
    fun insertOrder(@Body order: Order): Observable<Unit>
}