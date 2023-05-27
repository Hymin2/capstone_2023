package ac.kr.tukorea.capstone_android.Interface

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostService {
    @Multipart
    @POST("/api/v1/post")
    fun registerPost(
        @Part ("username") username : String,
        @Part ("userImage") userImage : Int,
        // @Part ("")
    )
}