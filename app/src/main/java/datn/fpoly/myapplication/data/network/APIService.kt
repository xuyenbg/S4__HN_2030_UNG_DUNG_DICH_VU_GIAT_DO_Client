package datn.fpoly.myapplication.data.network


import datn.fpoly.myapplication.data.model.ServiceModel
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("api/services/list-service-by-store/{idStore}")
    fun getListSeviceByStore(@Path("idStore") id: String): io.reactivex.Observable<MutableList<ServiceModel>>
}