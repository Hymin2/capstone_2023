package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class FollowResponseBody(
    @SerializedName("status")
    val status : Int,
    @SerializedName("message")
    val message : FollowList,
    @SerializedName("result")
    val result : String
)

data class FollowList(
    @SerializedName("id")
    val id : Long,
    @SerializedName("username")
    val username :String,
    @SerializedName("follows")
    val follows : List<Follow>
)

data class Follow(
    @SerializedName("id")
    val id : Long,
    @SerializedName("username")
    val username : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("image")
    val image : String
)