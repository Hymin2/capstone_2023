package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Body

interface RegisterService {

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun addUser (@Body userInfo: RegisterRequestBody): Response<RegisterRequestBody> // Call 은 흐름처리 기능을 제공해줌

}