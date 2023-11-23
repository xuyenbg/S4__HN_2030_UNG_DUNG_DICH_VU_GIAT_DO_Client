package datn.fpoly.myapplication.data.repository

import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.account.AccountExtend
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
            null,
            phone,
            passwd,
            fullname,
            phone,
            idRole,
            favouriteStores ?: arrayListOf(""),
            null
        )
    ).subscribeOn(Schedulers.io())

    fun addFavoriteStore(
        idUser: String,
        accountModel: AccountModel
    ): Observable<AccountModel> = api.addFavoriteStore(
        idUser,
        accountModel
    ).subscribeOn(Schedulers.io())
    fun removeFavoriteStore(
        idUser: String,
        accountModel: AccountModel
    ): Observable<AccountModel> = api.removeFavoriteStore(
        idUser,
        accountModel
    ).subscribeOn(Schedulers.io())
    fun getDetailUser(idUser: String):Observable<AccountExtend> = api.getDetailUser(idUser).subscribeOn(Schedulers.io())
    fun saveUser(accountResponse: AccountModel) = Hawk.put("Account",accountResponse)

    fun getUser():AccountModel? = Hawk.get<AccountModel?>("Account",null)

    fun isLogging():Boolean = Hawk.get("CheckLogin",false)

    fun setLogin(isLogin:Boolean) = Hawk.put("CheckLogin",isLogin)

}