package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityLoginBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var btn_login = findViewById<Button>(R.id.btn_login)
        var btn_toRegister = findViewById<Button>(R.id.btn_toRegister)

        btn_login.setOnClickListener{

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        btn_toRegister.setOnClickListener{

            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}