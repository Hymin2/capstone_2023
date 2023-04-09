package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.RetrofitWork
import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import ac.kr.tukorea.capstone_android.databinding.ActivityRegisterBinding
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userData = RegisterRequestBody(
            binding.edtIdRegister.text.toString(),
            binding.edtPwRegister.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtPhoneNumber.text.toString(),
            binding.edtNickname.text.toString()
        )

        binding.btnRegister.setOnClickListener{
            val retrofitWork = RetrofitWork(userData)
            retrofitWork.work()
        }
    }
}