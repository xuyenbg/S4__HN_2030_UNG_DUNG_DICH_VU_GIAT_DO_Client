package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.network.APIAddress
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepo @Inject constructor(
    private val api: APIAddress
) {
    fun getListAddress(idUser: String): Observable<MutableList<AddressExtend>> =
        api.getListAddress(idUser).subscribeOn(
            Schedulers.io()
        )

}