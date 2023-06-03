package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityChatBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle : Bundle? = intent.extras
        val oppoentName = bundle!!.getString("chatOpponentName")
        binding.chatOpponentName.text = oppoentName

        binding.chatBtnBack.setOnClickListener{
            onBackPressed()
        }


    }
}