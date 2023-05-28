package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.ProductDetailsResponseBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("api/v1/user/{username}")
    fun getUserInfo(@Header("Authorization") token: String,
                          @Path("username") username : String) : Call<UserInfoResponseBody>

    @PUT("api/v1/user/{username}")
    fun updateNickname(@Header("Authorization") token : String, @Path("username") username : String, @Body nickname : String) : Call<ResponseBody>

    @Multipart
    @POST("api/v1/user/{username}")
    fun uploadProfileImage(@Header("Authorization") token: String, @Path("username") username: String, @Part multipartFile: MultipartBody.Part) : Call<ResponseBody>
}