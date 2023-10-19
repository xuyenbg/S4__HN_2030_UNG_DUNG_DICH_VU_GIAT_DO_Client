package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.model.account.AcountLogin
import datn.fpoly.myapplication.data.network.AuthApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val api: AuthApi
) {
    fun login(phone: String, userId: String): Observable<Response<ResponseBody>> =
        api.login(AcountLogin(phone,userId)).subscribeOn(Schedulers.io())

    fun register(
        phone: String,
        passwd: String,
        fullname: String,
        idRole: String,
        favouriteStores: List<String>?
    ): Observable<Response<ResponseBody>> = api.register(
        AccountModel(
            phone,
            passwd,
            fullname,
            phone,
            idRole,
            favouriteStores ?: arrayListOf(""),
            null
        )
    ).subscribeOn(Schedulers.io())
}