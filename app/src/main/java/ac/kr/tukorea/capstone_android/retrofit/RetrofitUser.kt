package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.adapter.MyShopAdapter
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding
import ac.kr.tukorea.capstone_android.util.App
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitUser {
    private val service = RetrofitAPI.userService

    fun getUserInfo(username : String, binding : FragmentMyProfileBinding){
        service.getUserInfo(token = App.prefs.getString("access_token", ""), username).enqueue(object : Callback<UserInfoResponseBody>{
            override fun onResponse(
                call: Call<UserInfoResponseBody>,
                response: Response<UserInfoResponseBody>,
            ) {
                Log.d("user", response.isSuccessful.toString())
                if(response.isSuccessful){
                    val body = response.body()!!

                    binding.apply {
                        Log.d("user", body.message.username)

                        this.myProfileUserName.text = body.message.username
                        myOnSale.text = body.message.onSale.toString()
                        mySoldOut.text = body.message.soldOut.toString()
                        myFollowerNum.text = body.message.followNum.toString()

                        if(body.message.image != null) {
                            val glideUrl = GlideUrl(
                                body.message.image.replace("localhost", "10.0.2.2")
                            )

                            Glide.with(binding.root.context).load(glideUrl)
                                .override(Target.SIZE_ORIGINAL)
                                .into(myProfileProfileImage)
                        }

                        if(body.message.posts != null){
                            var listManager = GridLayoutManager(binding.root.context, 3)
                            var listAdapter = MyShopAdapter(body.message.posts as ArrayList<PostInfo>, binding.root.context)

                            binding.myProfileRecyclerView.apply {
                                setHasFixedSize(true)
                                layoutManager = listManager
                                adapter = listAdapter
                            }
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
}