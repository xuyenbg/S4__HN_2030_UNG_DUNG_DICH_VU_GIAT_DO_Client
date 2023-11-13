package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.data.network.APIPost
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostRepo @Inject constructor(
    private val api: APIPost
) {
    fun getListPost(): Observable<MutableList<PostModel>> =
        api.getPost().subscribeOn(Schedulers.io())

    fun getListPostStore(
        idStore: String
    ): Observable<MutableList<PostModel>> =
        api.getPostStore(idStore).subscribeOn(Schedulers.io())

    fun addListPost(
        idStore: RequestBody,
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part?
    ): Observable<Response<ResponseBody>> = api.addPostImage(
        idStore, title, content, image
    ).subscribeOn(Schedulers.io())

    fun editPost(
        idPost: String,
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part?
    ): Observable<Response<ResponseBody>> = api.editPostImage(
        idPost,
        title, content, image
    ).subscribeOn(Schedulers.io())

    fun deletePost(
        idPost: String,
    ): Observable<Response<ResponseBody>> = api.deletePost(
        idPost
    ).subscribeOn(Schedulers.io())
}