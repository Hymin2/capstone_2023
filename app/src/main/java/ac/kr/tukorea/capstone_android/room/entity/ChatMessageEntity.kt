package ac.kr.tukorea.capstone_android.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message_table")
data class ChatMessageEntity(
    var message : String,
    var time : String,
    var messageType : String
){
    @PrimaryKey(autoGenerate = true) var id : Long = 0
}

