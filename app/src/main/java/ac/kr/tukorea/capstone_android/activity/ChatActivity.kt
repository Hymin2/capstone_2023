package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.ChatMessageAdapter
import ac.kr.tukorea.capstone_android.data.ChatCreateRequestBody
import ac.kr.tukorea.capstone_android.data.ChatCreateResponseBody
import ac.kr.tukorea.capstone_android.data.ChatMessage
import ac.kr.tukorea.capstone_android.databinding.ActivityChatBinding
import ac.kr.tukorea.capstone_android.room.database.MyDataBase
import ac.kr.tukorea.capstone_android.room.entity.ChatMessageEntity
import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ChatMessageViewType
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    val url = ServerInfo.STOMP_URL.url
    val stomp = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    val chatRetrofit = RetrofitAPI.chatService

    var chatList = arrayListOf<ChatMessage>()

    var roomID : Long? = null
    var postID : Long? = null
    var opponentUsername : String? = null
    var opponentNickname : String? = null
    var opponentUserImage : String? = null
    var myUserType : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.chatToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val db = MyDataBase.getInstance(this@ChatActivity)
        val postId = intent.getLongExtra("postId", 0)
        val username = intent.getStringExtra("username")
        val nickname = intent.getStringExtra("nickname")
        val userImage = intent.getStringExtra("userImage")
        val userType = intent.getStringExtra("userType")

        initData(postId, username!!, nickname!!, userImage, userType!!)

        CoroutineScope(Dispatchers.IO).launch { // 다른애 한테 일 시키기
            var chatRoom = db!!.chatRoomDao().getRoomByPostId(postId)

            if(chatRoom == null){
                setListenerFirstSendMessage()
            } else{
                initRoomID(chatRoom.roomId)

                runStomp()
                readMessage()

                chatList = db!!.chatMessageDao().getAllMessageByRoomId(chatRoom.roomId)
                    .stream()
                    .map{ i -> ChatMessage(i.message, i.day, i.time, i.viewType)}
                    .collect(Collectors.toList()) as ArrayList<ChatMessage>

                setListenerSendMessage()
            }

            withContext(Dispatchers.Main) {
                binding.apply {
                    chattingRecyclerView.layoutManager = LinearLayoutManager(this@ChatActivity,
                        LinearLayoutManager.VERTICAL,
                        false)
                    chattingRecyclerView.adapter = ChatMessageAdapter(chatList, opponentNickname!!, opponentUserImage, this@ChatActivity)
                    chattingRecyclerView.scrollToPosition(chatList.size - 1)
                }
            }
        }

        binding.apply {
            chatOpponentName.text = nickname

            chatBtnBack.setOnClickListener {
                onBackPressed()
            }

            chattingRecyclerView.addOnLayoutChangeListener(object: View.OnLayoutChangeListener{
                override fun onLayoutChange(
                    v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
                ) {
                    if (bottom < oldBottom) {
                        chattingRecyclerView.postDelayed({
                            if (chatList.size > 0) {
                                chattingRecyclerView.scrollToPosition(chatList.size -1)
                            }
                        }, 100)
                    }
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stomp.disconnect()
    }

    private fun initRoomID(roomId: Long){
        Log.d("init room id", "before")
        roomID = roomId
        Log.d("init room id", "after " + roomID.toString())
    }

    private fun initData(
        postId: Long,
        username: String,
        nickname: String,
        userImage: String?,
        userType: String
    ){
        postID = postId
        opponentUsername = username
        opponentNickname = nickname
        opponentUserImage = userImage
        myUserType = userType
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListenerFirstSendMessage(){
        binding.apply {
            btnSendMessage.setOnClickListener {
                if (messageEdtText.text.isEmpty()) {
                    messageEdtText.requestFocus()
                    keyBordShow()
                    Toast.makeText(this@ChatActivity, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
                } else {
                    createChatRoomAndSendMessage(messageEdtText.text.toString())

                    messageEdtText.text.clear()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListenerSendMessage(){
        binding.apply {
            btnSendMessage.setOnClickListener {
                Log.d("chat", "click send button")
                if (messageEdtText.text.isEmpty()) {
                    messageEdtText.requestFocus()
                    keyBordShow()
                    Toast.makeText(this@ChatActivity, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()

                } else {
                    sendMessage(messageEdtText.text.toString(), ChatMessageViewType.RIGHT)
                    messageEdtText.text.clear()
                }
            }
        }
    }

    private fun keyBordShow() {
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
    }

    private fun createChatRoomAndSendMessage(message : String){
        val db = MyDataBase.getInstance(this@ChatActivity)

        var createRoomBody = ChatCreateRequestBody(postID!!, opponentUsername!!, App.prefs.getString("username", ""))

        chatRetrofit.createChatRoom(App.prefs.getString("access_token", ""), createRoomBody)
            .enqueue(object : Callback<ChatCreateResponseBody> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ChatCreateResponseBody>,
                    response: Response<ChatCreateResponseBody>,
                ) {
                    Log.d("chat", "in createChatRoom fun retrofit")
                    if (response.isSuccessful) {
                        val roomId = response.body()!!.message

                        var chatRoomEntity = ChatRoomEntity(
                            roomId,
                            postID!!,
                            opponentUsername!!,
                            opponentNickname!!,
                            opponentUserImage,
                            "buy",
                            null,
                            null)

                        CoroutineScope(Dispatchers.IO).async {
                            db!!.chatRoomDao().insert(chatRoomEntity)
                        }

                        initRoomID(roomId)
                        runStomp()
                        readMessage()
                        sendMessage(message, ChatMessageViewType.RIGHT)
                        setListenerSendMessage()
                    }
                }

                override fun onFailure(
                    call: Call<ChatCreateResponseBody>,
                    t: Throwable,
                ) {

                }

            })
    }

    @SuppressLint("CheckResult")
    fun runStomp(){
        stomp.lifecycle().subscribe{ lifecycle ->
            when(lifecycle.type){
                LifecycleEvent.Type.OPENED -> {
                    Log.i("OPEND", "!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i("CLOSED", "!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i("ERROR", "!!")
                    Log.e("CONNECT ERROR", lifecycle.exception.toString())
                }
                else ->{
                    Log.i("ELSE", lifecycle.message)
                }
            }
        }

        var headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("Authorization", App.prefs.getString("access_token", "")))

        stomp.connect(headerList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(message : String, viewType: Int){
        val data = JSONObject()

        val datetime = getDatetime()
        val day = getDate(datetime)
        val time = getTime(datetime)

        data.put("roomId", roomID!!)
        data.put("postId", postID!!)
        data.put("nickname", App.prefs.getString("nickname", ""))
        data.put("message", message)
        data.put("userType", myUserType)

        if(viewType == ChatMessageViewType.RIGHT)
            data.put("messageType", "TALK")
        else if(viewType == ChatMessageViewType.CENTER)
            data.put("messageType", "REQUEST")

        data.put("time", datetime)

        stomp.send("/pub/chat/message", data.toString()).subscribe()

        if(isInsertDate(day)){
            insertDate(day)
        }

        insertMessage(message, day, time, viewType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeDeal(){
        sendMessage(App.prefs.getString("nickname", "") + "님이 거래 완료 요청을 했습니다.", ChatMessageViewType.CENTER)
    }

    private fun insertMessage(message: String, day: String, time : String, viewType : Int) {
        val db = MyDataBase.getInstance(this@ChatActivity)

        var chatMessageEntity = ChatMessageEntity(0, roomID!!, message, day, time, viewType)
        val chatMessage = ChatMessage(message, day, time, viewType)

        chatList.add(chatMessage)
        binding.chattingRecyclerView.adapter!!.notifyItemChanged(chatList.size - 2)
        binding.chattingRecyclerView.adapter!!.notifyItemInserted(chatList.size - 1)
        binding.chattingRecyclerView.scrollToPosition(chatList.size - 1)

        CoroutineScope(Dispatchers.IO).launch {
            db!!.chatMessageDao().insert(chatMessageEntity)
            db!!.chatRoomDao().updateRoomRecentMessageAndTimeByRoomId(message, "$day $time", roomID!!)
        }
    }

    private fun insertDate(day : String){
        val db = MyDataBase.getInstance(this@ChatActivity)

        chatList.add(ChatMessage(day, day, null, ChatMessageViewType.CENTER))

        var centerMessage = ChatMessageEntity(0, roomID!!, day, day, null, ChatMessageViewType.CENTER)

        CoroutineScope(Dispatchers.IO).launch {
            db!!.chatMessageDao().insert(centerMessage)
        }
    }

    private fun isInsertDate(day : String) : Boolean{
        return chatList.size == 0 || chatList[chatList.size - 1].day != day
    }

    private fun getDatetime(): String {
        val timeFormatter = SimpleDateFormat("yyyy년 MM월 dd일 E요일 a hh:mm", Locale.KOREA)
        timeFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        return timeFormatter.format(Calendar.getInstance().time)
    }

    private fun getDate(datetime: String): String {
        val datetimeSplit = datetime.split(" ")

        return datetimeSplit[0] + " " + datetimeSplit[1] + " " + datetimeSplit[2] + " " + datetimeSplit[3]
    }

    fun getTime(datetime: String): String {
        val datetimeSplit = datetime.split(" ")

        return datetimeSplit[4] + " " + datetimeSplit[5]
    }

    @SuppressLint("CheckResult")
    fun readMessage(){
        stomp.topic("/sub/room/$roomID").subscribe { message ->
            val data = JSONObject(message.payload)

            val datetime = data.get("time") as String
            val day = getDate(datetime)
            val time = getTime(datetime)
            val message = data.get("message") as String
            val messageType = data.get("messageType") as String

            Log.d("read message", data.get("message") as String)

            runOnUiThread {
                if ((data.get("nickname") as String) != App.prefs.getString("nickname", "") || messageType == "COMPLETE") {
                    if (isInsertDate(day))
                        insertDate(getDate(datetime))

                    if (messageType == "TALK" && isFirstLeft()) {
                        insertMessage(message, day, time, ChatMessageViewType.FIRST_LEFT)
                    } else if (messageType == "TALK" && !isFirstLeft()) {
                        insertMessage(message, day, time, ChatMessageViewType.LEFT)
                    } else if (messageType == "REQUEST") {
                        insertMessage(message, day, time, ChatMessageViewType.CENTER)
                    } else if (messageType == "COMPLETE"){
                        insertMessage(message, day, time, ChatMessageViewType.CENTER)
                    }
                }
            }
        }
    }

    private fun isFirstLeft() : Boolean{
        val lastViewType = chatList[chatList.size - 1].viewType

        return lastViewType != ChatMessageViewType.LEFT && lastViewType != ChatMessageViewType.FIRST_LEFT
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    private fun showConfirmationDialog(title: String, message: String, positiveAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("예") { dialog, which ->
                positiveAction.invoke()
            }
            .setNegativeButton("아니오", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.end_trade -> {
                showConfirmationDialog("거래 완료", "거래를 완료하시겠습니까?") {
                    Log.d("complete", "예? 아니오?")
                    Log.d("complete", roomID!!.toString())

                    completeDeal()

                    // 거래 완료
                }
                return true
            }
            R.id.delete_chattingRoom -> {
                showConfirmationDialog("대화방 삭제", "대화방을 삭제하시겠습니까?") {
                    // 대화방 삭제
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
