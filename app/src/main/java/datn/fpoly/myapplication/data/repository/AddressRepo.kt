package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.AddressModel
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
        api.getListAddress(idUser).subscribeOn(Schedulers.io())
    fun getDetailAddress(idAddress: String): Observable<AddressExtend> =
        api.getDetailAddress(idAddress).subscribeOn(Schedulers.io())
    fun putDefaultAddress(idAddress: String,idUser: String): Observable<AddressModel> =
        api.putDefaultAddress(idAddress,idUser).subscribeOn(Schedulers.io())
    fun putAddress(idAddress: String, addressModel: AddressModel): Observable<AddressModel> =
        api.putAddress(idAddress, addressModel).subscribeOn(Schedulers.io())
    fun addAddress(addressModel: AddressModel): Observable<AddressModel> =
        api.addAddress(addressModel).subscribeOn(Schedulers.io())
    fun deleteAddress(idAddress: String): Observable<AddressModel> =
        api.deleteAddress(idAddress).subscribeOn(Schedulers.io())
}