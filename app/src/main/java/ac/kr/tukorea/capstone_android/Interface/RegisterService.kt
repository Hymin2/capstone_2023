package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RegisterService {

    @Headers("Content-Type: application/json")
    @POST("api/v1/user/register")
    suspend fun register (@Body userInfo: RegisterRequestBody): Response<RegisterRequestBody> // Call 은 흐름처리 기능을 제공해줌

    @GET("api/v1/user/register/check/username")
    fun checkDuplicateId(@Query("username") id : String) : Call<ResponseBody>

    @GET("api/v1/user/register/check/nickname")
    fun checkDuplicateNickname(@Query("nickname") nickname : String) : Call<ResponseBody>

}