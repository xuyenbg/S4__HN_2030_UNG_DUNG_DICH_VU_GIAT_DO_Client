package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.AddressExtend
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APIAddress {
    @GET("api/address/list-by-iduser/{idUser}")
    fun getListAddress(@Path("idUser") idUser: String): Observable<MutableList<AddressExtend>>

}