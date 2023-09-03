package ac.kr.tukorea.capstone_android.room.dao

import ac.kr.tukorea.capstone_android.room.entity.ChatMessageEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatMessageDao {
    @Insert
    fun insert(chatMessageEntity: ChatMessageEntity)

    @Query("SELECT * FROM chat_message_table WHERE room_id = :roomId")
    fun getAllMessageByRoomId(roomId : Long) : List<ChatMessageEntity>
}