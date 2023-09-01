package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.data.FollowRegisterRequestBody
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import ac.kr.tukorea.capstone_android.fragment.Like
import ac.kr.tukorea.capstone_android.fragment.SoldOut
import ac.kr.tukorea.capstone_android.fragment.onSale
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOthersProfileBinding
    val retrofit = RetrofitAPI.userService
    var isFollow : Boolean = false
    var username : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOthersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // val retrofit = RetrofitUser()

        // retrofit.getUserInfo(App.prefs.getString("username", ""), binding)

        username = intent.getStringExtra("username")
        val nickname = intent.getStringExtra("nickname")
        var isFollow = intent.getBooleanExtra("isFollow", false)

        binding.apply {
            othersFollowerLinear.setOnClickListener {
                val intent = Intent(this@OthersProfileActivity, FollowerActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }

            othersFollowingLinear.setOnClickListener {
                val intent = Intent(this@OthersProfileActivity, FollowingActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
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
                                    othersFollowerNum.text = (othersFollowerNum.text.toString().toInt() + 1).toString()
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
                                othersFollowerNum.text = (othersFollowerNum.text.toString().toInt() - 1).toString()
                                isFollow = false
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(this@OthersProfileActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

            retrofit.getUserInfo(App.prefs.getString("access_token", ""), App.prefs.getString("username", ""), username!!).enqueue(object : Callback<UserInfoResponseBody>{
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

                        if(message.isFollow){
                            followButton.text = "팔로우 해제"
                            isFollow = true
                        } else{
                            followButton.text = "팔로우"
                            isFollow = false
                        }

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
        // 탭 레이아웃과 뷰페이저 설정
        val pagerAdapter = OtherPagerAdapter()
        binding.othersProfileViewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2 간의 상호작용 설정
        TabLayoutMediator(binding.othersProfileTabLayout, binding.othersProfileViewPager) { tab, position ->
            tab.text = getPageTitle(position)
        }.attach()


        binding.othersBackButton.setOnClickListener {
            onBackPressed()
        }

    }

    private inner class OtherPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            // 탭 개수 반환
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            // 해당 탭에 대한 프래그먼트 반환
            return when (position) {
                0 -> onSale(username!!)
                1 -> SoldOut(username!!)
                2 -> Like(username!!)
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }

    private fun getPageTitle(position: Int): String? {
        // 탭의 타이틀 반환
        return when (position) {
            0 -> "판매 중"
            1 -> "판매 완료"
            2 -> "좋아요"
            else -> null
        }
    }
}