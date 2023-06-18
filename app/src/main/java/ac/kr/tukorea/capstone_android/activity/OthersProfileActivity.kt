package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OthersProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOthersProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOthersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // val retrofit = RetrofitUser()

        // retrofit.getUserInfo(App.prefs.getString("username", ""), binding)


        binding.othersBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}