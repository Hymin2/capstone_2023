package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @Multipart
    @POST("/api/v1/post")
    fun registerPost(
        @Header("Authorization") token: String,
        @Part("productId") productId: Long,
        @Part("username") username: RequestBody,
        @Part("postTitle") postTitle: RequestBody,
        @Part("postContent") postContent : RequestBody,
        @Part("price") price : Int,
        @Part multipartFiles: List<MultipartBody.Part>,
    ) : Call<ResponseBody>

    @GET("/api/v1/post")
    fun getPostList(@Header("Authorization") token: String,
                    @Query("productId") productId : Long?,
                    @Query("username") username : String?,
                    @Query("postTitle") postTitle : String?,
                    @Query("postContent") postContent : String?,
                    @Query("isOnSale") isOnSale : String?) : Call<PostResponseBody>

    @GET("/api/v1/post/like")
    fun getLikePostList(@Header("Authorization") token: String,
                        @Query("username") username : String) : Call<PostResponseBody>

    @POST("/api/v1/post/like")
    fun registerLikePost(@Header("Authorization") token: String,
                         @Body likePostRegisterRequestBody: LikePostRegisterRequestBody) : Call<ResponseBody>

    @DELETE("/api/v1/post/like")
    fun deleteLikePost(@Header("Authorization") token: String,
                       @Query("postId") postId : Long,
                       @Query("username") username : String) : Call<Unit>
}