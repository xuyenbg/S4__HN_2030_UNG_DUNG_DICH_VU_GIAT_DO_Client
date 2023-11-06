package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APIStore {
    @GET("api/stores/list")
    fun getListCategory(): Observable<MutableList<StoreModel>>
    @GET("api/stores/store-by-iduse/{idUser}")
    fun getStoreByIdUser(@Path("idUser") idUser: String): Observable<StoreModel>

}