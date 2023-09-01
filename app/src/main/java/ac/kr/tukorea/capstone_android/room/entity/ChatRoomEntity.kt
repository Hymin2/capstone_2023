package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_room_table")
data class ChatRoomEntity(
    var roomId : Long,
    var postId : Long,
    var nickname : String,
    var recentMessage : String,
    var recentTime : String
){
    @PrimaryKey(autoGenerate = true) var id : Long = 0
}
