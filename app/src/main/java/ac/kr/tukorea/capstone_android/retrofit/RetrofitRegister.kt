package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.activity.RegisterActivity
import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityRegisterBinding
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRegister {
    private val service = RetrofitAPI.registerService

    fun register(userInfo:RegisterRequestBody, binding: ActivityRegisterBinding, activity: RegisterActivity) {
        CoroutineScope(Dispatchers.IO).launch {

            val response = service.register(userInfo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful){
                    Log.d("회원가입 성공", response.code().toString())

                    val builder = AlertDialog.Builder(binding.root.context)
                    builder.setTitle("회원가입")
                        .setMessage("회원가입 성공!")
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                            activity.finish()
                        })
                    builder.show()
                } else {
                    Log.d("회원가입 실패", response.code().toString())

                    val builder = AlertDialog.Builder(binding.root.context)
                    builder.setTitle("회원가입")
                        .setMessage("회원가입 실패\n다시 시도 해주세요.")
                        .setPositiveButton("확인", null)
                    builder.show()
                }
            }
        }
    }

    fun checkDuplicateId(id : String, binding: ActivityRegisterBinding) {
        CoroutineScope(Dispatchers.IO).launch{
            service.checkDuplicateId(id).enqueue(object: Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if(response.isSuccessful){
                        Log.d("ID 중복체크", "ID가 중복이 아닐 때")
                        binding.textIdCheck.text = "사용 가능한 아이디입니다."
                        binding.textIdCheck.setTextColor(Color.BLUE)
                        binding.textIdCheck.visibility = View.VISIBLE
                    }else{
                        Log.d("ID 중복체크", "ID가 중복일 때")
                        binding.textIdCheck.text = "사용 불가능한 아이디입니다."
                        binding.textIdCheck.setTextColor(Color.RED)
                        binding.textIdCheck.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("ID 중복체크", "서버와 연결이 원할하지 않음")
                }

            })
        }

    }

    fun checkDuplicateNickname(nickname: String, binding: ActivityRegisterBinding) {
        CoroutineScope(Dispatchers.IO).launch{
            service.checkDuplicateNickname(nickname).enqueue(object: Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if(response.isSuccessful){
                        Log.d("Nickname 중복체크", "Nickname이 중복이 아닐 때")
                        binding.textNicknameCheck.text = "사용 가능한 닉네임입니다."
                        binding.textNicknameCheck.setTextColor(Color.BLUE)
                        binding.textNicknameCheck.visibility = View.VISIBLE
                    }else{
                        Log.d("Nickname 중복체크", "Nickname이 중복일 때")
                        binding.textNicknameCheck.text = "사용 불가능한 닉네임입니다."
                        binding.textNicknameCheck.setTextColor(Color.RED)
                        binding.textNicknameCheck.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("Nickname 중복체크", "서버와 연결이 원할하지 않음")
                }
            })
        }
    }

}