package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.NotifycationModel
import datn.fpoly.myapplication.data.network.APINotifycation
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import javax.inject.Inject

class NotifyRepo @Inject constructor(
    private val api: APINotifycation
) {

    fun getListNotifyById(idUser: String): Observable<MutableList<NotifycationModel>> = api.getListNotiById(idUser).subscribeOn(
        Schedulers.io())
}