package ac.kr.tukorea.capstone_android

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import ac.kr.tukorea.capstone_android.data.RegisterResponseBody
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class RetrofitWork(private val userInfo:RegisterRequestBody) {

    fun work() {
        val service = RetrofitAPI.emgMedService


        CoroutineScope(Dispatchers.IO).launch {

            val response = service.addUser(userInfo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful){
                    val result = response.body()
                    Log.d("회원가입 성공","$result")
                } else {
                    Log.d("회원가입 실패", response.code().toString())
                }
            }
        }
    }
}