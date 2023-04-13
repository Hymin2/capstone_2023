package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class RefreshResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("access_token")
    val accessToken : String
)