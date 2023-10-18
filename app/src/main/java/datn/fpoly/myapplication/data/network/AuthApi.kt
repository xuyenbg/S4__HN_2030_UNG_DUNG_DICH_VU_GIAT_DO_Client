package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET("login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<List<User>>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("phone") phone:String,
        @Field("passwd") passwd:String,
        @Field("fullname") fullname:String,
        @Field("idRole") idRole:String,
        @Field("favouriteStores") favouriteStores: List<String>,
    ): Observable<String>

}