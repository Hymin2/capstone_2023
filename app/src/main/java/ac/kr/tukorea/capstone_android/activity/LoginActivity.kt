package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.data.LoginRequestBody
import ac.kr.tukorea.capstone_android.databinding.ActivityLoginBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitLogin
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.FirebaseCloudMessageService
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                if(edtIdLogin.text.isEmpty()){
                    textLoginCheck.text = "아이디를 입력해주세요."
                    textLoginCheck.setTextColor(Color.RED)
                    textLoginCheck.visibility = View.VISIBLE
                } else if(edtPwLogin.text.isEmpty()){
                    textLoginCheck.text = "비밀번호를 입력해주세요."
                    textLoginCheck.setTextColor(Color.RED)
                    textLoginCheck.visibility = View.VISIBLE
                } else {
                    val retrofitLogin = RetrofitLogin()
                    val loginInfo = LoginRequestBody(edtIdLogin.text.toString(), edtPwLogin.text.toString())

                    Log.d("fcm token", App.prefs.getString("fcm_token", ""))

                    retrofitLogin.login(loginInfo, binding, this@LoginActivity, App.prefs.getString("fcm_token", ""))
                }
            }

            btnToRegister.setOnClickListener {
                val intent = Intent(this.root.context,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
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