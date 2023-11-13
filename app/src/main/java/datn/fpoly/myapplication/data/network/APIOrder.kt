package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.model.User
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APIOrder {
    @GET("api/order/get-list-order-by-idUser/{idUser}")
    fun getListOrder(@Path("idUser") idUser: String):Observable<MutableList<OrderResponse>>
}