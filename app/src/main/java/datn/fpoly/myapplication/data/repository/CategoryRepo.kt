package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.network.APICategory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryRepo @Inject constructor(
    private val api: APICategory
) {
    fun getDataCategory(): Observable<MutableList<CategoryModel>> = api.getListCategory().subscribeOn(
        Schedulers.io())
}