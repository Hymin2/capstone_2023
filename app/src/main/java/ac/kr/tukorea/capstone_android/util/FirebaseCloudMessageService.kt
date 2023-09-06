package ac.kr.tukorea.capstone_android.util

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.ChatActivity
import ac.kr.tukorea.capstone_android.activity.MainActivity
import ac.kr.tukorea.capstone_android.data.ChatMessage
import ac.kr.tukorea.capstone_android.data.ChatRoom
import ac.kr.tukorea.capstone_android.data.ChatRoomResponseBody
import ac.kr.tukorea.capstone_android.room.database.MyDataBase
import ac.kr.tukorea.capstone_android.room.entity.ChatMessageEntity
import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.Transaction
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseCloudMessageService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    /** Token 생성 메서드(FirebaseInstanceIdService 사라짐) */
    override fun onNewToken(token: String) {
        val access_token = App.prefs.getString("access_token", "")
        val username = App.prefs.getString("username", "")

        if(access_token != "" || username != "") {
            RetrofitAPI.userService.putFcmToken(access_token, username, token).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                }

            })

            Log.d(TAG, "new Token: $token")
        }

        App.prefs.setString("fcm_token", token)
        Log.i(TAG, "성공적으로 토큰을 저장함")
    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage!!.from)

        // Notification 메시지를 수신할 경우
        // remoteMessage.notification?.body!! 여기에 내용이 저장되있음
        // Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)

        //받은 remoteMessage의 값 출력해보기. 데이터메세지 / 알림메세지
        Log.d(TAG, "Message data : ${remoteMessage.data}")
        Log.d(TAG, "Message noti : ${remoteMessage.notification}")

        if(remoteMessage.data.isNotEmpty()){
            //알림생성
            sendNotification(remoteMessage)

//            Log.d(TAG, remoteMessage.data["title"].toString())
//            Log.d(TAG, remoteMessage.data["body"].toString())
        }else {
            Log.e(TAG, "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    /** 알림 생성 메서드 */
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = remoteMessage.data["roomId"]!!.toInt()

        // 일회용 PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, ChatActivity::class.java)
        //각 key, value 추가
        for(key in remoteMessage.data.keys){
            if (key == "postId") intent.putExtra("postId", remoteMessage.data.getValue(key).toLong())
            else intent.putExtra(key, remoteMessage.data.getValue(key))
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        //23.05.22 Android 최신버전 대응 (FLAG_MUTABLE, FLAG_IMMUTABLE)
        //PendingIntent.FLAG_MUTABLE은 PendingIntent의 내용을 변경할 수 있도록 허용, PendingIntent.FLAG_IMMUTABLE은 PendingIntent의 내용을 변경할 수 없음
        //val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        // 알림 채널 이름
        val channelId = "my_channel"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setContentTitle(remoteMessage.data["nickname"].toString()) // 제목
            .setContentText(remoteMessage.data["message"].toString()) // 메시지 내용
            .setAutoCancel(true) // 알람클릭시 삭제여부
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setSound(soundUri)  // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())

        saveRoomAndMessage(remoteMessage)
    }

    private fun saveRoomAndMessage(remoteMessage: RemoteMessage){
        val db = MyDataBase.getInstance(this)

        CoroutineScope(Dispatchers.IO).launch {
            if(db!!.chatRoomDao().existRoom(remoteMessage.data["roomId"]!!.toLong()) == null){
                getRoom(remoteMessage.data["roomId"]!!.toLong(), remoteMessage)
            }
            else saveMessage(remoteMessage)
        }

    }

    private fun getRoom(room: Long, remoteMessage: RemoteMessage){
        val chatService = RetrofitAPI.chatService

        chatService.getChatRoom(App.prefs.getString("access_token", ""), room).enqueue(object: Callback<ChatRoomResponseBody>{
            override fun onResponse(
                call: Call<ChatRoomResponseBody>,
                response: Response<ChatRoomResponseBody>,
            ) {
                if(response.isSuccessful){
                    saveRoom(response.body()!!.message, remoteMessage)
                }
            }

            override fun onFailure(call: Call<ChatRoomResponseBody>, t: Throwable) {

            }

        })
    }

    @Transaction
    private fun saveRoom(chatRoom: ChatRoom, remoteMessage: RemoteMessage){
        val db = MyDataBase.getInstance(this)

        CoroutineScope(Dispatchers.IO).launch {
            val chatRoomEntity = ChatRoomEntity(chatRoom.roomId, chatRoom.postId, chatRoom.opponentUsername, chatRoom.opponentNickname, chatRoom.opponentUserImage, "sell", null, null, 0)
            db!!.chatRoomDao().insert(chatRoomEntity)

            saveMessage(remoteMessage)
        }
    }

    @Transaction
    private fun saveMessage(remoteMessage: RemoteMessage){
        val db = MyDataBase.getInstance(this)

        val message = remoteMessage.data["message"].toString()
        val roomId = remoteMessage.data["roomId"]!!.toLong()
        val messageType = remoteMessage.data["messageType"]
        val datetime = remoteMessage.data["datetime"].toString()

        CoroutineScope(Dispatchers.IO).launch{
            var prevMessageDay : String = db!!.chatMessageDao().getLastMessageDay(roomId)
            var currentMessageDay : String = getDate(datetime)

            if(prevMessageDay != currentMessageDay){
                var centerDayMessage = ChatMessageEntity(0, roomId, currentMessageDay, currentMessageDay, null, ChatMessageViewType.CENTER)
                db!!.chatMessageDao().insert(centerDayMessage)
            }

            var viewType : Int = if(messageType == "REQUEST" || messageType == "COMPLETE") {
                ChatMessageViewType.CENTER
            } else {
                var prevViewType = db!!.chatMessageDao().getLastMessageViewType(roomId)
                if(prevViewType == ChatMessageViewType.FIRST_LEFT || prevViewType == ChatMessageViewType.LEFT){
                    ChatMessageViewType.LEFT
                } else{
                    ChatMessageViewType.FIRST_LEFT
                }
            }

            val chatMessageEntity = ChatMessageEntity(0, roomId, message, getDate(datetime), getTime(datetime), viewType)
            db!!.chatMessageDao().insert(chatMessageEntity)
            db!!.chatRoomDao().updateRoomRecentMessageAndTimeByRoomId(message, datetime, roomId)
            db!!.chatRoomDao().increaseUnreadMessageNumber(roomId)

            var intent = Intent().setAction("com.package.notification")
            sendBroadcast(intent)
        }
    }

    private fun getDate(datetime: String): String {
        val datetimeSplit = datetime.split(" ")

        return datetimeSplit[0] + " " + datetimeSplit[1] + " " + datetimeSplit[2] + " " + datetimeSplit[3]
    }

    private fun getTime(datetime: String): String {
        val datetimeSplit = datetime.split(" ")

        return datetimeSplit[4] + " " + datetimeSplit[5]
    }

    /** Token 가져오기 */
    fun getFirebaseToken() {
        //비동기 방식
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d(TAG, "token=${it}")
        }
    }
}