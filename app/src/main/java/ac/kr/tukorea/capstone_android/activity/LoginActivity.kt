package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.retrofit.RetrofitLogin
import ac.kr.tukorea.capstone_android.data.LoginRequestBody
import ac.kr.tukorea.capstone_android.databinding.ActivityLoginBinding
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

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

                    retrofitLogin.login(loginInfo, binding, this@LoginActivity)
                }
            }

            btnToRegister.setOnClickListener {
                val intent = Intent(this.root.context,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }


}