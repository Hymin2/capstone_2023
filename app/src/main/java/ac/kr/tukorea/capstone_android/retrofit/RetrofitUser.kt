package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.adapter.MyShopAdapter
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.data.UserInfoResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityMyShopBinding
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

    fun getUserInfo(username : String, binding : ActivityMyShopBinding){
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

                        this.username.text = body.message.username
                        onSale.text = body.message.onSale.toString()
                        soldOut.text = body.message.soldOut.toString()
                        followNum.text = body.message.followNum.toString()

                        if(body.message.image != null) {
                            val glideUrl = GlideUrl(
                                body.message.image.replace("localhost", "10.0.2.2")
                            )

                            Glide.with(binding.root.context).load(glideUrl)
                                .override(Target.SIZE_ORIGINAL)
                                .into(myShopProfileImage)
                        }

                        if(body.message.posts != null){
                            var listManager = GridLayoutManager(binding.root.context, 3)
                            var listAdapter = MyShopAdapter(body.message.posts as ArrayList<PostInfo>, binding.root.context)

                            binding.myShopRecyclerView.apply {
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