package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.ProductListResponseBody
import ac.kr.tukorea.capstone_android.data.RefreshResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RefreshService {
    @GET("api/v1/user/refresh")
    fun refreshToken(@Header("Authorization") token : String,
                       @Query("username") username : String) : Call<RefreshResponseBody>
}