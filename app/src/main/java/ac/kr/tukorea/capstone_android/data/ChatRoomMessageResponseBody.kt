package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class ChatRoomMessageResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : List<Message>,
)

data class Message(
    @SerializedName("messageType")
    val messageType : String,
    @SerializedName("message")
    val message : String,
    @SerializedName("sendUsername")
    val sendUsername : String,
    @SerializedName("time")
    val time : String
)
