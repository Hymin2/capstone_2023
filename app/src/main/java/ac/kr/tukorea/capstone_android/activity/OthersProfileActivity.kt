package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.Interface.UserService
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.MyShopAdapter
import ac.kr.tukorea.capstone_android.data.MyShop
import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitUser
import ac.kr.tukorea.capstone_android.util.App
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager

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