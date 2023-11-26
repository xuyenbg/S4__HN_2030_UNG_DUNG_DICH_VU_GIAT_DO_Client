package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.RateModel
import datn.fpoly.myapplication.data.network.APIRate
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jetbrains.annotations.Async.Schedule
import retrofit2.Response
import javax.inject.Inject

class RatePepo @Inject constructor(
    private val api: APIRate
) {
    fun AddRate(
        idStore: String,
        idUser: String,
        rateNumber: Float,
        content: String,
        idOrder: String
    ): Observable<Response<ResponseBody>> =
        api.addRate(idStore, idUser, rateNumber, content, idOrder).subscribeOn(
            Schedulers.io()
        )

    fun getListRateByStore(idStore: String): Observable<MutableList<RateModel>> =
        api.getListRateByStore(idStore).subscribeOn(Schedulers.io())

}