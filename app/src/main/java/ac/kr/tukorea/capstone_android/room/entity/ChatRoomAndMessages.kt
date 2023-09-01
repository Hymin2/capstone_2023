package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatRoomAndMessages(
    @Embedded var room : ChatRoomEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "roomId"
    ) var messages : List<ChatMessageEntity>
)
