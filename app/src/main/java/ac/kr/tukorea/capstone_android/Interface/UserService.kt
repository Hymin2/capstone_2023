package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.ProductDetailsResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserService {
    @GET("api/v1/user/{username}")
    fun getUserInfo(@Header("Authorization") token: String,
                          @Path("username") username : String) : Call<UserInfoResponseBody>
}