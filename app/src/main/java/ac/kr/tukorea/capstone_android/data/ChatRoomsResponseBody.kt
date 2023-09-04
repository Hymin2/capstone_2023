package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class ChatRoomsResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : List<ChatRoom>,
)

data class ChatRoom(
    @SerializedName("roomId")
    val roomId : Long,
    @SerializedName("postId")
    val postId : Long,
    @SerializedName("opponentUsername")
    val opponentUsername : String,
    @SerializedName("opponentNickname")
    val opponentNickname : String,
    @SerializedName("opponentUserImage")
    val opponentUserImage : String?,
)
