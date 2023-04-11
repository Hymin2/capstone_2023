package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class LoginResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : String,
    @SerializedName("access_token")
    val accessToken : String,
    @SerializedName("refresh_token")
    val refreshToken : String
)
