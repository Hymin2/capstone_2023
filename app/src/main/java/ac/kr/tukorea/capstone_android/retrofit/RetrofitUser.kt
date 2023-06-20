package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityProfileEditBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitUser {
    private val service = RetrofitAPI.userService

    fun getUserInfo(username : String, binding : FragmentMyProfileBinding){
        service.getUserInfo(token = App.prefs.getString("access_token", ""), username, null).enqueue(object : Callback<UserInfoResponseBody>{
            override fun onResponse(
                call: Call<UserInfoResponseBody>,
                response: Response<UserInfoResponseBody>,
            ) {
                Log.d("user", response.isSuccessful.toString())
                if(response.isSuccessful){
                    val body = response.body()!!

                    binding.apply {
                        Log.d("user", body.message.username)

                        this.myProfileUserName.text = body.message.nickname
                        myOnSale.text = body.message.onSale.toString()
                        mySoldOut.text = body.message.soldOut.toString()
                        myFollowerNum.text = body.message.followNum.toString()
                        myFollowingNum.text = body.message.followingNum.toString()

                        if(body.message.image != null) {
                            val glideUrl = GlideUrl(
                                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + body.message.image
                            )

                            Glide.with(binding.root.context).load(glideUrl)
                                .override(100)
                                .into(myProfileProfileImage)
                        }
                    }
                }else{
                    val retrofitRefresh = RetrofitRefresh()
                    retrofitRefresh.refreshToken()

                    getUserInfo(username, binding)
                }
            }

            override fun onFailure(call: Call<UserInfoResponseBody>, t: Throwable) {
                Log.d("user", t.toString())
            }

        })
    }

    fun updateNickname(username: String, nickname: String, binding: ActivityProfileEditBinding){
        service.updateNickname(token = App.prefs.getString("access_token", ""), username, nickname).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    binding.apply {
                        profileEditNicknameResult.text = "닉네임이 변경되었습니다."
                        profileEditNicknameResult.setTextColor(Color.BLUE)
                        profileEditNicknameResult.visibility = View.VISIBLE
                    }
                } else{
                    binding.apply {
                        profileEditNicknameResult.text = "이미 사용중인 닉네임입니다."
                        profileEditNicknameResult.setTextColor(Color.RED)
                        profileEditNicknameResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(binding.root.context, "잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT)
            }

        })
    }

    fun uploadProfileImage(username : String, image : MultipartBody.Part, binding: ActivityProfileEditBinding){
        Log.d("프로필 이미지 수정","1")
        service.uploadProfileImage(token = App.prefs.getString("access_token", ""), username, image).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("프로필 이미지 수정","2")
                if(response.isSuccessful){
                    Toast.makeText(binding.root.context, "프로필 이미지가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT)
                } else{
                    Toast.makeText(binding.root.context, "잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("프로필 이미지 수정",t.toString())
                Toast.makeText(binding.root.context, "잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT)
            }

        })
    }
}