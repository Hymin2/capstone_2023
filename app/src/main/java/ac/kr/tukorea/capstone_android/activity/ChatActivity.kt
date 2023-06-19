package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.ChattingAdapter
import ac.kr.tukorea.capstone_android.data.Chatting
import ac.kr.tukorea.capstone_android.databinding.ActivityChatBinding
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.internal.cache.DiskLruCache
import java.time.LocalTime

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding

    var chattingList = ArrayList<Chatting>()

    var senderId : String = "sender"
    var receiverId : String = "reciever"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val oppoentName = bundle!!.getString("chatOpponentName")
        binding.chatOpponentName.text = oppoentName

        binding.chattingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.chatBtnBack.setOnClickListener{
            onBackPressed()
        }

        binding.btnSendMessage.setOnClickListener{
            if(binding.messageEdtText.text.isEmpty()){
                Toast.makeText(this,"메세지를 입력하세요",Toast.LENGTH_SHORT).show()

            } else {
                sendMessage(senderId,receiverId ,binding.messageEdtText.text.toString(), LocalTime.now())
                binding.messageEdtText.setText("")
            }
        }


    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, time: LocalTime) {
        // DB에서 senderId, receiverId를 받아 와서 전송
    }

    fun readMessage(senderId: String, receiverId: String) {
        // 채팅내역을 가져옴
        // DB 변화를 감지해서 새로고침함


        //리사이클러뷰 어댑터 설정
        val chatAdapter = ChattingAdapter(this@ChatActivity, chattingList)

        binding.chattingRecyclerView.adapter = chatAdapter
    }

}