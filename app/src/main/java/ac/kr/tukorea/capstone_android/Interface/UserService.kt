package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.FollowRegisterRequestBody
import ac.kr.tukorea.capstone_android.data.FollowResponseBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("api/v1/user/{username}")
    fun getUserInfo(@Header("Authorization") token: String,
                    @Path("username") username : String,
                    @Query("otherUsername") otherUsername : String?) : Call<UserInfoResponseBody>

    @PUT("api/v1/user/{username}")
    fun updateNickname(@Header("Authorization") token : String, @Path("username") username : String, @Body nickname : String) : Call<ResponseBody>

    @Multipart
    @POST("api/v1/user/{username}")
    fun uploadProfileImage(@Header("Authorization") token: String, @Path("username") username: String, @Part multipartFile: MultipartBody.Part) : Call<ResponseBody>

    @GET("api/v1/user/follow/following")
    fun getFollowingList(@Header("Authorization") token: String, @Query("username") username : String) : Call<FollowResponseBody>

    @GET("api/v1/user/follow/follower")
    fun getFollowerList(@Header("Authorization") token: String, @Query("username") username : String) :Call<FollowResponseBody>

    @POST("api/v1/user/follow")
    fun registerFollow(@Header("Authorization") token: String, @Body followRegisterRequestBody : FollowRegisterRequestBody) : Call<ResponseBody>

    @DELETE("api/v1/user/follow")
    fun deleteFollow(@Header("Authorization") token: String,
                     @Query("followingUsername") followingUsername : String,
                     @Query("followerUsername") followerUsername : String) : Call<Unit>

    @DELETE("api/v1/user/logout")
    fun logout(@Header("Authorization") token : String,
                @Header("Authorization-refresh") refresh : String,
                @Query("username") username : String) : Call<Unit>

    @PUT("api/v1/fcm/{username}")
    fun putFcmToken(@Header("Authorization") token: String, @Path("username") username: String, @Body fcmToken: String) : Call<Unit>
}