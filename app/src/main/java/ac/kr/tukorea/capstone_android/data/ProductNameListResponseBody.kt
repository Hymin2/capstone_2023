package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class ProductNameListResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : List<String>
)
