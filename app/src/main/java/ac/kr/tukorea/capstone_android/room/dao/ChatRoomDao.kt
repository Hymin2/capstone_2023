package ac.kr.tukorea.capstone_android.room.dao

import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import androidx.room.*

@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chatRoomEntity: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(chatRoomEntities: List<ChatRoomEntity>)

    @Delete
    fun delete(chatRoomEntity: ChatRoomEntity)

    @Query("SELECT 1 FROM chat_room_table WHERE room_id = :roomId LIMIT 1")
    fun existRoom(roomId: Long) : Int?

    @Query("UPDATE chat_room_table SET recent_message = :recentMessage, recent_time = :recentTime WHERE room_id = :roomId")
    fun updateRoomRecentMessageAndTimeByRoomId(recentMessage : String, recentTime : String, roomId : Long)

    @Query("UPDATE chat_room_table SET opponent_nickname = :nickname, opponent_user_image = :userImage WHERE room_id = :roomId")
    fun updateRoomOpponentUserByRoomId(nickname : String, userImage : String?, roomId: Long)

    @Query("SELECT * FROM chat_room_table WHERE post_id = :postId limit 1")
    fun getRoomByPostId(postId : Long) : ChatRoomEntity

    @Query("SELECT * FROM chat_room_table WHERE room_id = :roomId limit 1")
    fun getRoomByRoomId(roomId : Long) : ChatRoomEntity

    @Query("SELECT * FROM chat_room_table WHERE my_user_type = :myUserType ORDER BY recent_time DESC")
    fun getRoomsByMyUserType(myUserType : String) : List<ChatRoomEntity>

    @Query("UPDATE chat_room_table SET unread_message_number = unread_message_number + 1 WHERE room_id = :roomId")
    fun increaseUnreadMessageNumber(roomId: Long)

    @Query("UPDATE chat_room_table SET unread_message_number = 0 WHERE room_id = :roomId")
    fun setZeroUnreadMessageNumber(roomId: Long)

    @Query("SELECT unread_message_number FROM chat_room_table WHERE room_id = :roomId")
    fun getUnreadMessageNumber(roomId: Long) : Int
}