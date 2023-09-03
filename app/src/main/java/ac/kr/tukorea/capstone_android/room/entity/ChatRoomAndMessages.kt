package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatRoomAndMessages(
    @Embedded var room : ChatRoomEntity,

    @Relation(
        parentColumn = "room_id",
        entityColumn = "room_id"
    ) var messages : List<ChatMessageEntity>
)
