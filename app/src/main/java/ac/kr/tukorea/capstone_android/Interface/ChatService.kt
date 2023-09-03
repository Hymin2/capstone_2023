package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.ChatCreateRequestBody
import ac.kr.tukorea.capstone_android.data.ChatCreateResponseBody
import ac.kr.tukorea.capstone_android.data.ChatRoomMessageResponseBody
import ac.kr.tukorea.capstone_android.data.ChatRoomResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ChatService {
    @POST("/api/v1/chat/room")
    fun createChatRoom(@Header("Authorization") token: String,
                       @Body chatCreateRequestBody : ChatCreateRequestBody
    ) : Call<ChatCreateResponseBody>

    @GET("/api/v1/chat/room/buy")
    fun getBuyChatRoom(@Header("Authorization") token: String,
                       @Query("username") username : String) : Call<ChatRoomResponseBody>

    @GET("/api/v1/chat/room/sell")
    fun getSellChatRoom(@Header("Authorization") token: String,
                        @Query("username") username: String) : Call<ChatRoomResponseBody>

    @GET("/api/v1/chat/room/{roomId}")
    fun getChatMessages(@Header("Authorization") token : String,
                        @Path("roomId") roomId : Long) : Call<ChatRoomMessageResponseBody>
}