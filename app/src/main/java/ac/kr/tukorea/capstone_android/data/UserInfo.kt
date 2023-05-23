package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("userId")
    val userId : Long,
    @SerializedName("username")
    val username : String,
    @SerializedName("image")
    val image : String?,
    @SerializedName("soldOut")
    val soldOut : Int,
    @SerializedName("onSale")
    val onSale : Int,
    @SerializedName("followNum")
    val followNum : Int,
    @SerializedName("posts")
    val posts : List<PostInfo>?
)