package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "chat_room_table")
data class ChatRoomEntity (
    @PrimaryKey
    @ColumnInfo(name = "room_id")
    var roomId: Long,
    @ColumnInfo(name = "post_id")
    var postId: Long,
    @ColumnInfo(name = "opponent_username")
    var opponentUsername: String,
    @ColumnInfo(name = "opponent_nickname")
    var opponentNickname: String,
    @ColumnInfo(name = "opponent_user_image")
    var opponentUserImage: String?,
    @ColumnInfo(name = "my_user_type")
    var myUserType: String,
    @ColumnInfo(name = "recent_message")
    var recentMessage: String?,
    @ColumnInfo(name = "recent_time")
    var recentTime: String?,
    @ColumnInfo(name = "unread_message_number", defaultValue = "0")
    var unReadMessageNumber: Int? = 0
){
    @Ignore
    constructor(roomId : Long, postId: Long, opponentUsername: String, opponentNickname: String, opponentUserImage: String?, myUserType: String, recentMessage: String?, recentTime: String?)
            : this(roomId = roomId,
        postId = postId,
        opponentUsername = opponentUsername,
        opponentNickname = opponentNickname,
        opponentUserImage = opponentUserImage,
        myUserType = myUserType,
        recentMessage = recentMessage,
        recentTime = recentTime,
        unReadMessageNumber = 0)
}