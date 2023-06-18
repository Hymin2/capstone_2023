package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.SaleDetailActivity
import ac.kr.tukorea.capstone_android.adapter.MyProfileTabAdapter
import ac.kr.tukorea.capstone_android.data.PostResponseBody
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Like(val username : String) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val service = RetrofitAPI.postService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        recyclerView = view.findViewById(R.id.like_recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 그리드 리사이클러뷰 설정
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = gridLayoutManager


        service.getLikePostList(token = App.prefs.getString("access_token", ""), username).enqueue(object :
            Callback<PostResponseBody> {
            override fun onResponse(
                call: Call<PostResponseBody>,
                response: Response<PostResponseBody>,
            ) {
                if(response.isSuccessful){
                    val posts = response.body()?.message
                    val adapter = MyProfileTabAdapter(posts!!, context!!)
                    adapter.setOnItemClickListener(object : MyProfileTabAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, SaleDetailActivity::class.java)
                            intent.putExtra("detail", posts[position])

                            activity!!.startActivity(intent)
                        }

                    })
                    recyclerView.adapter = adapter
                }else{
                    Toast.makeText(context, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponseBody>, t: Throwable) {
                Toast.makeText(context, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        })
    }
}