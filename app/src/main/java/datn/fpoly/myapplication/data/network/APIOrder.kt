package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
import io.reactivex.Observable
import retrofit2.http.GET

interface APIOrder {
    @GET("api/order/list")
    fun getListOrder(): Observable<MutableList<Order>>
}