package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.network.APIOrder
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OrderRepo @Inject constructor(
    private val api: APIOrder
){
    fun getDataOrder(
        idUser: String,
        status: Int
    ): Observable<MutableList<Order>> = api.getListOrder(idUser, status).subscribeOn(
    Schedulers.io())
}