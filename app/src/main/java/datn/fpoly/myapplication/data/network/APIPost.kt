package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.post.PostAddModel
import datn.fpoly.myapplication.data.model.post.PostModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIPost {
    @GET("api/posts/list")
    fun getPost() : Observable<MutableList<PostModel>>

    @POST("api/posts/")
    fun addPost(
        @Body body: PostAddModel
    ) : Observable<Response<ResponseBody>>
}