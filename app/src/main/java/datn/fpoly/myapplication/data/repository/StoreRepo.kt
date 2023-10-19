package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.network.APICategory
import datn.fpoly.myapplication.data.network.APIStore
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StoreRepo @Inject constructor(
    private val api: APIStore
) {
    fun getDataStore(): Observable<MutableList<StoreModel>> = api.getListCategory().subscribeOn(Schedulers.io())
}