package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_room_table")
data class ChatRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "room_id") var roomId : Long,
    @ColumnInfo(name = "post_id") var postId : Long,
    @ColumnInfo(name = "opponent_username") var opponentUsername : String,
    @ColumnInfo(name = "opponent_nickname") var opponentNickname : String,
    @ColumnInfo(name = "opponent_user_image") var opponentUserImage : String?,
    @ColumnInfo(name = "my_user_type") var myUserType : String,
    @ColumnInfo(name = "recent_message") var recentMessage : String?,
    @ColumnInfo(name = "recent_time") var recentTime : String?
)