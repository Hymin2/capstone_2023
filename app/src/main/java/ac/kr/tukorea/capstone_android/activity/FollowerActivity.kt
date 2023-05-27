package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityFeedListBinding
import ac.kr.tukorea.capstone_android.databinding.ActivityFollowerBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FollowerActivity : AppCompatActivity() {

    lateinit var binding: ActivityFollowerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.followerBtnBack.setOnClickListener{
            onBackPressed()
        }
    }
}