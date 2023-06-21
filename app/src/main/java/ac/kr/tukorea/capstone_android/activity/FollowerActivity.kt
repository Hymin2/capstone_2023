package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.adapter.FollowAdapter
import ac.kr.tukorea.capstone_android.data.Follow
import ac.kr.tukorea.capstone_android.data.FollowResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityFollowerBinding
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Response

class FollowerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowerBinding

    private val userService = RetrofitAPI.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.followerBtnBack.setOnClickListener {
            onBackPressed()
        }

        val username = intent.getStringExtra("username")

        userService.getFollowerList(App.prefs.getString("access_token", ""), username!!).enqueue(object : retrofit2.Callback<FollowResponseBody>{
            override fun onResponse(
                call: Call<FollowResponseBody>,
                response: Response<FollowResponseBody>,
            ) {
                if(response.isSuccessful){
                    val follows = response.body()!!.message.follows
                    val followAdapter = FollowAdapter(follows as ArrayList<Follow>, this@FollowerActivity)
                    val recyclerLayoutManager = LinearLayoutManager(this@FollowerActivity)

                    binding.apply {
                        followerRecyclerView.layoutManager = recyclerLayoutManager
                        followerRecyclerView.setHasFixedSize(true)

                        followAdapter.setOnItemClickListener(object : FollowAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                if(follows[position].username != App.prefs.getString("username", "")) {
                                    var intent = Intent(this@FollowerActivity,
                                        OthersProfileActivity::class.java)
                                    intent.putExtra("username", follows[position].username)
                                    intent.putExtra("nickname", follows[position].nickname)
                                    startActivity(intent)
                                } else{
                                    val intent = Intent(this@FollowerActivity, MainActivity::class.java)
                                    intent.putExtra("isProfile", true)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        })
                        followerRecyclerView.adapter = followAdapter
                    }

                }else{

                }
            }

            override fun onFailure(call: Call<FollowResponseBody>, t: Throwable) {

            }

        })

    }
}
