package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.activity.LoginActivity
import ac.kr.tukorea.capstone_android.activity.MainActivity
import ac.kr.tukorea.capstone_android.data.LoginRequestBody
import ac.kr.tukorea.capstone_android.data.LoginResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityLoginBinding
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitLogin {
    private val service = RetrofitAPI.loginService

    fun login(loginInfo : LoginRequestBody, binding: ActivityLoginBinding, activity: LoginActivity){
        CoroutineScope(Dispatchers.IO).launch {
            service.login(loginInfo).enqueue(object : Callback<LoginResponseBody> {
                override fun onResponse(
                    call: Call<LoginResponseBody>,
                    response: Response<LoginResponseBody>,
                ) {
                    if(response.isSuccessful){
                        Log.d("로그인 시도", "로그인 성공")
                        val responseBody = response.body()

                        App.prefs.setString("username", loginInfo.username)
                        App.prefs.setString("access_token", "Bearer " + responseBody!!.accessToken)
                        App.prefs.setString("refresh_token", "Bearer " + responseBody!!.refreshToken)

                        val intent = Intent(binding.root.context, MainActivity::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    } else{
                        binding.textLoginCheck.text = "아이디 또는 비밀번호가 일치하지 않습니다."
                        binding.textLoginCheck.setTextColor(Color.RED)
                        binding.textLoginCheck.visibility = View.VISIBLE

                        Log.d("로그인 시도", "로그인 실패")
                    }
                }

                override fun onFailure(call: Call<LoginResponseBody>, t: Throwable) {
                    Log.d("로그인 시도", "로그인 실패(서버 에러)")

                    binding.textLoginCheck.text = "잠시후에 다시 시도해주세요."
                    binding.textLoginCheck.setTextColor(Color.RED)
                    binding.textLoginCheck.visibility = View.VISIBLE
                }

            })
        }
    }

}