package datn.fpoly.myapplication.data.network


import datn.fpoly.myapplication.data.model.NotifycationModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APINotifycation {
    @GET("api/notification//list-by-iduser/{idUser}")
    fun getListNotiById(@Path("idUser") idUser: String): Observable<MutableList<NotifycationModel>>
}