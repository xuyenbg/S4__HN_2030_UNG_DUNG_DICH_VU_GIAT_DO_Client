package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import io.reactivex.Observable
import retrofit2.http.GET

interface APIStore {
    @GET("api/stores/list")
    fun getListCategory(): Observable<MutableList<StoreModel>>

}