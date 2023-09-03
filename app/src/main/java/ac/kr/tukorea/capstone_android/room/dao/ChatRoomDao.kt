package ac.kr.tukorea.capstone_android.room.dao

import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import androidx.room.*

@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chatRoomEntity: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(chatRoomEntities: List<ChatRoomEntity>)

    @Delete
    fun delete(chatRoomEntity: ChatRoomEntity)

    @Query("UPDATE chat_room_table SET recent_message = :recentMessage, recent_time = :recentTime WHERE room_id = :roomId")
    fun updateRoomRecentMessageAndTimeByRoomId(recentMessage : String, recentTime : String, roomId : Long)

    @Query("SELECT * FROM chat_room_table WHERE post_id = :postId limit 1")
    fun getRoomByPostId(postId : Long) : ChatRoomEntity

    @Query("SELECT * FROM chat_room_table WHERE my_user_type = :myUserType ORDER BY recent_time DESC")
    fun getRoomsByMyUserType(myUserType : String) : List<ChatRoomEntity>
}