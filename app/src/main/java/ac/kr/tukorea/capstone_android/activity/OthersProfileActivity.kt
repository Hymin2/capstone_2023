package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.data.FollowRegisterRequestBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitUser
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import kotlinx.android.synthetic.main.activity_others_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOthersProfileBinding
    val retrofit = RetrofitAPI.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOthersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrofit.getUserInfo(App.prefs.getString("username", ""), binding)

        val username = intent.getStringExtra("username")
        val nickname = intent.getStringExtra("nickname")
        var isFollow = intent.getBooleanExtra("isFollow", false)

        binding.apply {
            if(!isFollow){
                followButton.text = "팔로우"
            } else{
                followButton.text = "팔로우 해제"
            }

            followButton.setOnClickListener {
                if(!isFollow) {
                    val follow = FollowRegisterRequestBody(App.prefs.getString("username", ""), username!!)
                    retrofit.registerFollow(App.prefs.getString("access_token", ""), follow)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>,
                            ) {
                                if (response.isSuccessful) {
                                    followButton.text = "팔로우 해제"
                                    isFollow = true
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(this@OthersProfileActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else{
                    retrofit.deleteFollow(App.prefs.getString("access_token", ""), App.prefs.getString("username", ""), username!!).enqueue(object : Callback<Unit>{
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            if(response.isSuccessful){
                                followButton.text = "팔로우"
                                isFollow = false
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(this@OthersProfileActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

            retrofit.getUserInfo(App.prefs.getString("access_token", ""), username!!).enqueue(object : Callback<UserInfoResponseBody>{
                override fun onResponse(
                    call: Call<UserInfoResponseBody>,
                    response: Response<UserInfoResponseBody>,
                ) {
                    if(response.isSuccessful){
                        val message = response.body()!!.message

                        othersUserName.text = message.nickname
                        othersFollowerNum.text = message.followNum.toString()
                        othersFollowingNum.text = message.followingNum.toString()
                        othersOnSale.text = message.onSale.toString()
                        othersSoldOut.text = message.soldOut.toString()

                        if(message.image != null) {
                            val glideUrl = GlideUrl(
                                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + message.image
                            )

                            Glide.with(this@OthersProfileActivity).load(glideUrl)
                                .override(150)
                                .into(othersProfileImage)
                        }
                    }
                }

                override fun onFailure(call: Call<UserInfoResponseBody>, t: Throwable) {
                    Toast.makeText(this@OthersProfileActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.othersBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}