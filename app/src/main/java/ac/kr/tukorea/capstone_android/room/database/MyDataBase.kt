package ac.kr.tukorea.capstone_android.room.database

import ac.kr.tukorea.capstone_android.room.dao.ChatMessageDao
import ac.kr.tukorea.capstone_android.room.dao.ChatRoomDao
import ac.kr.tukorea.capstone_android.room.entity.ChatMessageEntity
import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatRoomEntity::class, ChatMessageEntity::class], version = 1)
abstract class MyDataBase : RoomDatabase(){
    abstract fun chatRoomDao() : ChatRoomDao
    abstract fun chatMessageDao() : ChatMessageDao

    companion object {
        private var instance: MyDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MyDataBase? {
            if (instance == null) {
                synchronized(MyDataBase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDataBase::class.java,
                        "my-database.db"
                    ).build()
                }
            }
            return instance
        }
    }
}