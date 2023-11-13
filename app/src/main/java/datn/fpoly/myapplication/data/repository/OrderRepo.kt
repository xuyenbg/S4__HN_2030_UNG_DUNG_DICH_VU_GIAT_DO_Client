package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.data.network.APIOrder
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepo @Inject constructor(
    private val api: APIOrder
){
    fun getDataOrder(idUser: String): Observable<MutableList<OrderResponse>> = api.getListOrder(idUser).subscribeOn(Schedulers.io())

    fun getDataOrder(): Observable<MutableList<Order>> = api.getListOrder().subscribeOn(Schedulers.io())

    fun insertOrder(order: Order) : Observable<Unit> = api.insertOrder(order).subscribeOn(Schedulers.io())
}