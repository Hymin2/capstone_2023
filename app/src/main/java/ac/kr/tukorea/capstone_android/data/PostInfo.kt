package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostInfo(
    @SerializedName("id")
    val postId : Long,
    @SerializedName("username")
    val username : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("userImage")
    val userImage : String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("postTitle")
    val postTitle : String,
    @SerializedName("postContent")
    val postContent : String,
    @SerializedName("isOnSale")
    val isOnSale : String,
    @SerializedName("createdTime")
    val createTime : String,
    @SerializedName("price")
    val price : Int,
    @SerializedName("postImages")
    val postImages : List<String>,
    @SerializedName("isLike")
    val isLike : Boolean
) : Serializable