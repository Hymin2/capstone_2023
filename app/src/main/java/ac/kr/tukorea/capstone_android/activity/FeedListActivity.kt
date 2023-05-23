package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FeedListAdapter
import ac.kr.tukorea.capstone_android.data.ChatList
import ac.kr.tukorea.capstone_android.data.FeedList
import ac.kr.tukorea.capstone_android.databinding.ActivityFeedListBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_feed_list.*

class FeedListActivity : AppCompatActivity() {

    lateinit var binding : ActivityFeedListBinding
    private lateinit var feedArrayList : ArrayList<FeedList>

    lateinit var feedProfileImage : Array<Int>
    lateinit var feedUserNickName : Array<String>
    lateinit var feedProductImage : Array<Int>
    lateinit var feedProductModel : Array<String>
    lateinit var feedProductPrice : Array<Int>
    lateinit var feedProductMain : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataInitialize()
        val recyclerlayoutManager = LinearLayoutManager(this)
        binding.feedListRecyclerView.apply {
            layoutManager = recyclerlayoutManager
            setHasFixedSize(true)
        }

        val adapter = FeedListAdapter(feedArrayList)
        binding.feedListRecyclerView.adapter= adapter

        getUserData()
    }
    private fun dataInitialize() {

        feedArrayList = arrayListOf<FeedList>()

        feedProfileImage = arrayOf(
            R.drawable.profile_image,
            R.drawable.profile_image,
            R.drawable.profile_image,
        )

        feedUserNickName = arrayOf(
            "asd",
            "123",
            "qwe",
        )

        feedProductImage = arrayOf(
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
            R.drawable.galaxys23
        )

        feedProductModel = arrayOf(
            "갤럭시 S23",
            "아이폰 14 Pro",
            "갤럭시 S23"
        )

        feedProductPrice = arrayOf(
            123,
            456,
            789,
        )
        feedProductMain = arrayOf(
            "판매글 내용1",
            "판매글 내용2",
            "판매글 내용3"
        )

    }
    private fun getUserData() {
        for(i in feedProfileImage.indices){
            val feed = FeedList(
                feedProfileImage[i],
                feedUserNickName[i],
                feedProductImage[i],
                feedProductModel[i],
                feedProductPrice[i],
                feedProductMain[i]
            )
            feedArrayList.add(feed)
        }
    }
}