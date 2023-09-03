package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.LoginRequestBody
import ac.kr.tukorea.capstone_android.data.LoginResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/user/login")
    fun login (@Header("FcmToken") token : String, @Body loginInfo: LoginRequestBody): Call<LoginResponseBody>
}