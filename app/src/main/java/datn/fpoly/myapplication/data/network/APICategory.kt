package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.CategoryModel
import io.reactivex.Observable
import retrofit2.http.GET

interface APICategory {
    @GET("api/categories/list")
    fun getListCategory(): Observable<MutableList<CategoryModel>>

}