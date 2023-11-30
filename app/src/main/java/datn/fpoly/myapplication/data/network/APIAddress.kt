package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.AddressExtend
import datn.fpoly.myapplication.data.model.AddressModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIAddress {
    @GET("api/address/list-by-iduser/{idUser}")
    fun getListAddress(@Path("idUser") idUser: String): Observable<MutableList<AddressExtend>>
    @GET("api/address/detail-address/{idAddress}")
    fun getDetailAddress(@Path("idAddress") idAddress: String): Observable<AddressExtend>
    @PUT("api/address/update-default-address/{idAddress}")
    fun putDefaultAddress(
        @Path("idAddress") idAddress: String,
        @Query("idUser") idUser: String
    ): Observable<AddressModel>
    @PUT("api/address/update-address/{idAddress}")
    fun putAddress(
        @Path("idAddress") idAddress: String,
        @Body addressModel: AddressModel): Observable<AddressModel>
    @POST("api/address/add-address")
    fun addAddress(@Body addressModel: AddressModel): Observable<AddressModel>
    @DELETE("api/address/delete-address/{idAddress}")
    fun deleteAddress(@Path("idAddress") idAddress: String): Observable<AddressModel>
}