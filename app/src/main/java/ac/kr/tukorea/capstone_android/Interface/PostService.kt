package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.PostResponseBody
import ac.kr.tukorea.capstone_android.data.ProductDetailsResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @Multipart
    @POST("/api/v1/post")
    fun registerPost(
        @Part ("username") username : String,
        @Part ("userImage") userImage : Int,
        // @Part ("")
    )

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
}