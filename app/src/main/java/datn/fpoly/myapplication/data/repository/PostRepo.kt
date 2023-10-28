package datn.fpoly.myapplication.data.repository

import datn.fpoly.myapplication.data.model.post.PostAddModel
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.data.network.APIPost
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostRepo @Inject constructor(
    private val api: APIPost
) {
    fun getListPost(): Observable<MutableList<PostModel>> =
        api.getPost().subscribeOn(Schedulers.io())

    fun addListPost(
        idStore: String,
        title: String,
        content: String,
        image: String?
    ): Observable<Response<ResponseBody>> = api.addPost(
        PostAddModel(
            idStore,
            title,
            content,
            image ?: ""
        )
    ).subscribeOn(Schedulers.io())

}