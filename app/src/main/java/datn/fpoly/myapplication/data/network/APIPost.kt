package datn.fpoly.myapplication.data.network

import datn.fpoly.myapplication.data.model.post.PostAddModel
import datn.fpoly.myapplication.data.model.post.PostModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface APIPost {
    @GET("api/posts/list")
    fun getPost(): Observable<MutableList<PostModel>>

    @GET("api/posts/list-post-by-store/{idStore}")
    fun getPostStore(
        @Path("idStore") idStore: String
    ): Observable<MutableList<PostModel>>

    @POST("api/posts/insert")
    fun addPost(
        @Body body: PostAddModel
    ): Observable<Response<ResponseBody>>

    @Multipart
    @PUT("api/posts/update/{idPost}")
    fun editPostImage(
        @Path("idPost") idPost: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): Observable<Response<ResponseBody>>

    @Multipart
    @POST("api/posts/insert")
    fun addPostImage(
        @Part("idStore") idStore: RequestBody,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): Observable<Response<ResponseBody>>

    @DELETE("api/posts/delete/{idPost}")
    fun deletePost(
        @Path("idPost") idPost: String
    ): Observable<Response<ResponseBody>>
}