package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIOrder {
    @GET("api/order/list")
    fun getListOrder(): Observable<MutableList<Order>>

    @POST("api/order/insert")
    fun insertOrder(@Body order: Order): Observable<Unit>
}