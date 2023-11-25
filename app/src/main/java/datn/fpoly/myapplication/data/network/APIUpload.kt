package datn.fpoly.myapplication.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIUpload {
    @Multipart
    @POST("api/image/upload")
    fun uploadImage(@Part image: MultipartBody.Part) : Observable<Response<ResponseBody>>
}