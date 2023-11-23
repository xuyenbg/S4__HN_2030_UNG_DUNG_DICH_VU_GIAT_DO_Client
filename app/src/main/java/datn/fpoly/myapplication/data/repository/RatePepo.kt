package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.network.APIRate
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RatePepo @Inject constructor(
    private val api: APIRate
) {
    fun AddRate(
        idStore: String,
        idUser: String,
        rateNumber: Int,
        content: String,
        idOrder: String
    ): Observable<Response<ResponseBody>> = api.addRate(idStore, idUser, rateNumber, content, idOrder)

}