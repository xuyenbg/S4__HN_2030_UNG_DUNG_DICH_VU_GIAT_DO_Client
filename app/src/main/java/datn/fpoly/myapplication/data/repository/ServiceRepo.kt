package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.data.network.APIService
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.Observable

class ServiceRepo @Inject constructor(
    private val api: APIService
) {
    fun getListServiceByStore(id: String): Observable<MutableList<ServiceModel>> = api.getListSeviceByStore(id).subscribeOn(
        Schedulers.io())
}