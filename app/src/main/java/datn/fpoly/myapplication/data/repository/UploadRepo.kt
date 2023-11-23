package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.network.APIUpload
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadRepo @Inject constructor(
    private val api: APIUpload
){
    fun uploadImage(image: MultipartBody.Part) : Observable<Response<ResponseBody>> =
        api.uploadImage(image).subscribeOn(Schedulers.io())
}