package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FollowAdapter
import ac.kr.tukorea.capstone_android.data.Follow
import ac.kr.tukorea.capstone_android.databinding.ActivityFollowingBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

class FollowingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFollowingBinding

    private lateinit var followArrayList: ArrayList<Follow>
    private lateinit var followAdapter: FollowAdapter

    private lateinit var followProfileImage: Array<Int>
    private lateinit var followUserName: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.followingBtnBack.setOnClickListener{
            onBackPressed()
        }

        initializeData()

        val recyclerLayoutManager = LinearLayoutManager(this)
        followAdapter = FollowAdapter(followArrayList, object : FollowAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // TODO: 아이템 클릭 이벤트 처리
            }
        })

        binding.followingRecyclerView.apply {
            layoutManager = recyclerLayoutManager
            adapter = followAdapter
            setHasFixedSize(true)
        }

        getUserData()
    }

    private fun initializeData() {
        followArrayList = arrayListOf()

        followProfileImage = arrayOf(
            R.drawable.profile_image,
            R.drawable.galaxys23,
            R.drawable.iphone14pro
        )

        followUserName = arrayOf(
            "123",
            "qwe",
            "zxc"
        )
    }

    private fun getUserData() {
        for (i in followProfileImage.indices) {
            val follow = Follow(
                followProfileImage[i],
                followUserName[i]
            )
            followArrayList.add(follow)
        }
    }
}