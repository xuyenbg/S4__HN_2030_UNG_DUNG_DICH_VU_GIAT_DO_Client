package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.User
import datn.fpoly.myapplication.data.network.AuthApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val api: AuthApi
) {
    fun login(username: String, password: String): Observable<List<User>> =
        api.login(username, password).subscribeOn(Schedulers.io())

    fun register(
        phone: String,
        passwd: String,
        fullname: String,
        idRole: String,
        favouriteStores: List<String>?
    ): Observable<String>? = favouriteStores?.let {
        api.register(phone, passwd, fullname, idRole, it).subscribeOn(Schedulers.io())
    }
}