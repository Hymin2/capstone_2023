package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class PostInfo(
    @SerializedName("id")
    val postId : Long,
    @SerializedName("username")
    val username : String,
    @SerializedName("userImage")
    val userImage : String,
    @SerializedName("postTitle")
    val postTitle : String,
    @SerializedName("postContent")
    val postContent : String,
    @SerializedName("isOnSale")
    val isOnSale : String,
    @SerializedName("createdTime")
    val createTime : String,
    @SerializedName("postImages")
    val postImages : List<String>
)