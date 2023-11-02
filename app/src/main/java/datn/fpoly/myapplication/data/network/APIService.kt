package datn.fpoly.myapplication.data.network


import datn.fpoly.myapplication.data.model.ServiceModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("api/services/list-service-by-store-service/")
    fun getListSeviceByStore(@Query("idStore") id: String): io.reactivex.Observable<MutableList<ServiceModel>>

    @GET("api/services/list-service-by-store-service/")
    fun getListSeviceByStore2(@Query("idStore") idStore: String, @Query("idService") idService:String): io.reactivex.Observable<MutableList<ServiceModel>>

    @GET("api/services/list-service-by-category/{idCategory}")
    fun getListServiceByCate( @Path("idCategory") idCate: String): io.reactivex.Observable<MutableList<ServiceModel>>

}