package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.activity.LoginActivity
import ac.kr.tukorea.capstone_android.activity.MainActivity
import ac.kr.tukorea.capstone_android.data.RefreshResponseBody
import ac.kr.tukorea.capstone_android.util.App
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRefresh {
    private val service = RetrofitAPI.refreshService

    fun refreshToken(){
        service.refreshToken(App.prefs.getString("refresh_token", ""), App.prefs.getString("username", "")).enqueue(object :Callback<RefreshResponseBody>{
            override fun onResponse(
                call: Call<RefreshResponseBody>,
                response: Response<RefreshResponseBody>,
            ) {
                if(response.isSuccessful){
                    Log.d("Refresh Token", "재발급 성공")
                    App.prefs.setString("access_token", "Bearer " + response.body()!!.accessToken)
                }else{
                    Log.d("Refresh Token", "재발급 실패, 로그인 다시 해야함")
                }
            }

            override fun onFailure(call: Call<RefreshResponseBody>, t: Throwable) {
                Log.d("Refresh Token", "서버 오류")
            }

        })
    }
}