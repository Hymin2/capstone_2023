package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message_table",
        foreignKeys = [
            ForeignKey(
                entity = ChatRoomEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("roomId"),
                onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ChatMessageEntity(
    var roomId : Long,
    var message : String,
    var time : String,
    var messageType : String
){
    @PrimaryKey(autoGenerate = true) var id : Long = 0
}

