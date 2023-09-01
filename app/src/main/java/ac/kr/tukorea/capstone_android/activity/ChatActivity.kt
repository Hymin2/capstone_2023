package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.ChattingAdapter
import ac.kr.tukorea.capstone_android.data.Chatting
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.databinding.ActivityChatBinding
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.DecimalFormat
import java.time.LocalTime


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    var chattingList = ArrayList<Chatting>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.chatToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val fromPost = intent.getBooleanExtra("fromPost", false)

        if(fromPost){
            val postId = intent.getLongArrayExtra("postId")
            val username = intent.getStringExtra("username")
            val nickname = intent.getStringExtra("nickname")
            val userImage = intent.getStringExtra("userImage")

            binding.apply {
                chatOpponentName.text = nickname
            }
        }

        binding.apply {

        }

        binding.chattingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.chatBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSendMessage.setOnClickListener {
            if (binding.messageEdtText.text.isEmpty()) {
                Toast.makeText(this, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage2()
            }
        }

        binding.messageEdtText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                sendMessage2()
                true
            } else {
                false
            }
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.end_trade -> {
                showConfirmationDialog("거래 완료", "거래를 완료하시겠습니까?") {
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

    private fun sendMessage1(senderId: String, receiverId: String, message: String, time: LocalTime) {
        // DB에서 senderId, receiverId를 받아 와서 전송
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage2() {
        if (binding.messageEdtText.text.isEmpty()) {
            Toast.makeText(this, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
        } else {
            // senderId와 reciverId를 받아옴
            val senderId = "your_sender_id"
            val receiverId = "your_receiver_id"
            val message = binding.messageEdtText.text.toString()
            val time = LocalTime.now()
            sendMessage1(senderId, receiverId, message, time)
            binding.messageEdtText.setText("")
        }
    }

    fun readMessage(senderId: String, receiverId: String) {
        // 채팅내역을 가져옴
        // DB 변화를 감지해서 새로고침함

        // 리사이클러뷰 어댑터 설정
        val chatAdapter = ChattingAdapter(this@ChatActivity, chattingList)
        binding.chattingRecyclerView.adapter = chatAdapter
    }
}
