package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.StoreModel
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
    fun getDataOrder(): Observable<MutableList<Order>> = api.getListOrder().subscribeOn(Schedulers.io())

    fun insertOrder(order: Order) : Observable<Unit> = api.insertOrder(order).subscribeOn(Schedulers.io())
}