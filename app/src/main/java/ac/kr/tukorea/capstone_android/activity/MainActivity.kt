package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityMainBinding
import ac.kr.tukorea.capstone_android.fragment.Chat
import ac.kr.tukorea.capstone_android.fragment.Main
import ac.kr.tukorea.capstone_android.fragment.myProfile
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    private var isBackPressed = false

    var profile : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        if(App.prefs.getString("refresh_token","") == ""){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profile = intent.getBooleanExtra("isProfile", false)

        if(profile){
            replaceFragment(myProfile())
            binding.bottomNavigationView.selectedItemId = R.id.my_menu
        }else {
            replaceFragment(Main())
        }

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener {

                when(it.itemId){
                    R.id.main -> replaceFragment(Main())
                    R.id.chat -> replaceFragment(Chat())
                    R.id.my_menu -> replaceFragment(myProfile())

                    else -> {

                    }
                }
                true
            }

            floatingActionBtn.setOnClickListener {
                val intent = Intent(this.root.context,SalePostActivity::class.java)
                startActivity(intent)
            }


        }

    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        if(currentFragment is myProfile) {
            if(profile == true){
                finish()
            } else {
                if (isBackPressed) {
                    finishAffinity()
                } else {
                    isBackPressed = true
                    Toast.makeText(this, "앱을 종료하시려면 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        isBackPressed = false
                    }, 2000) // 2초 동안 뒤로가기 버튼을 누르지 않으면 isBackPressed를 다시 false로 설정
                }
            }
        } else {
            if (isBackPressed) {
                finishAffinity()
            } else {
                isBackPressed = true
                Toast.makeText(this, "앱을 종료하시려면 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isBackPressed = false
                }, 2000) // 2초 동안 뒤로가기 버튼을 누르지 않으면 isBackPressed를 다시 false로 설정
            }
        }
    }
}