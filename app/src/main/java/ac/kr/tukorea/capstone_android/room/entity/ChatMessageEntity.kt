package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message_table",
        foreignKeys = [
            ForeignKey(
                entity = ChatRoomEntity::class,
                parentColumns = arrayOf("room_id"),
                childColumns = arrayOf("room_id"),
                onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) var id : Long,
    @ColumnInfo(name = "room_id") var roomId : Long,
    @ColumnInfo(name = "message") var message : String,
    @ColumnInfo(name = "day") var day : String?,
    @ColumnInfo(name = "time") var time : String?,
    @ColumnInfo(name = "view_type")  var viewType : Int
)
