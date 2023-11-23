package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.OrderExtend
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.data.network.APIOrder
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepo @Inject constructor(
    private val api: APIOrder
){
    fun getDataOrder(idUser: String): Observable<MutableList<OrderExtend>> =
        api.getListOrder(idUser).subscribeOn(Schedulers.io())

    fun getDataOrderStore(idStore: String, sortOrder: String): Observable<MutableList<OrderResponse>> = api.getListOrderStore(idStore, sortOrder).subscribeOn(Schedulers.io())

    fun insertOrder(orderBase: OrderBase): Observable<Unit> =
        api.insertOrder(orderBase).subscribeOn(Schedulers.io())

    fun getOrderDetail(idOrder: String): Observable<OrderExtend> =
        api.getOrderDetail(idOrder).subscribeOn(Schedulers.io())

    fun getOrderDateStoreWait(idStore: String, status: Int, sortOrder: String): Observable<MutableList<OrderResponse>> =
        api.getListOrderDateStore(idStore, status, sortOrder).subscribeOn(Schedulers.io())

    fun updateOrder(idOrder: String, status: Int): Observable<Response<ResponseBody>> =
        api.updateOrder(idOrder, status).subscribeOn(Schedulers.io())

    fun updateOrderWashing(idOrder: String, status: Int): Observable<Response<ResponseBody>> =
        api.updateOrder(idOrder, status).subscribeOn(Schedulers.io())

    fun updateOrder(orderBase: OrderBase, idOrder: String): Observable<Unit> =
        api.updateOrder(orderBase, idOrder).subscribeOn(Schedulers.io())
}