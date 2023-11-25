package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.network.APIStore
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class StoreRepo @Inject constructor(
    private val api: APIStore
) {
    fun getDataStore(): Observable<MutableList<StoreModel>> =
        api.getListCategory().subscribeOn(Schedulers.io())

    fun registerStore(
        name: RequestBody,
        rate: RequestBody,
        idUser: RequestBody,
        status: RequestBody,
        transportTypeList: Map<String, String>,
        imageQRCode: MultipartBody.Part?,
        longitude: RequestBody,
        latitude: RequestBody,
        address: RequestBody,
        isDefault: RequestBody,
    ): Observable<Response<ResponseBody>> = api.addStore(
        name,
        rate,
        idUser,
        status,
        transportTypeList,
        longitude,
        latitude,
        address,
        isDefault,
        imageQRCode,
    ).subscribeOn(Schedulers.io())

    fun getStoreByIdUser(idUSer: String): Observable<StoreModel> =
        api.getStoreByIdUser(idUSer).subscribeOn(Schedulers.io())

    fun getStoreById(id: String): Observable<StoreModel> =
        api.getStoreById(id).subscribeOn(Schedulers.io())

    fun opendCloseStore(idStoer: String, status: Int): Observable<Response<ResponseBody>> =
        api.opendAndCloseSStore(idStoer, status).subscribeOn(Schedulers.io())
}