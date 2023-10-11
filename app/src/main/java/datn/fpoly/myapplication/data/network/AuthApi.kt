package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET("login")
    fun login(
        @Query("username") username : String,
        @Query("password") password : String ) : Observable<List<User>>

}