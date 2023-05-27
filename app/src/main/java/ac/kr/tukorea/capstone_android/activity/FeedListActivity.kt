package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FeedListAdapter
import ac.kr.tukorea.capstone_android.data.FeedList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ac.kr.tukorea.capstone_android.databinding.ActivityFeedListBinding

class FeedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedListBinding
    private lateinit var feedArrayList: ArrayList<FeedList>
    private lateinit var filteredList: ArrayList<FeedList> // 검색 결과를 담을 리스트
    private lateinit var adapter: FeedListAdapter // 어댑터 변수 추가

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

        setSupportActionBar(binding.feedListToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        dataInitialize()
        val recyclerLayoutManager = LinearLayoutManager(this)
        binding.feedListRecyclerView.apply {
            layoutManager = recyclerLayoutManager
            setHasFixedSize(true)
        }

        adapter = FeedListAdapter(feedArrayList) // 어댑터 초기화
        binding.feedListRecyclerView.adapter = adapter

        getUserData()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."

        // SearchView 리스너 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText) // 입력된 텍스트로 검색 결과 필터링
                return true
            }
        })

        return true
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


        filteredList = ArrayList(feedArrayList) // 검색 결과 리스트 초기화
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

    private fun filter(query: String) {
        filteredList.clear() // 검색 결과 리스트 초기화

        if (query.isNotEmpty()) {
            val searchQuery = query.lowercase()

            for (item in feedArrayList) {
                // 검색어가 닉네임, 제품 모델, 제품 메인 내용 중 하나와 일치하는지 확인
                if (item.feedUserNickName.lowercase().contains(searchQuery) ||
                    item.feedProductModel.lowercase().contains(searchQuery) ||
                    item.feedProductMain.lowercase().contains(searchQuery)
                ) {
                    filteredList.add(item)
                }
            }
        } else {
            filteredList.addAll(feedArrayList) // 검색어가 없으면 모든 아이템 추가
        }

        adapter.updateList(filteredList) // 어댑터의 데이터 업데이트
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            // SearchView 확장 시 툴바 색상 변경
            val layoutParams = binding.feedListToolBar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.feedListToolBar.layoutParams = layoutParams
            binding.feedListToolBar.requestLayout()
        }
        return super.onOptionsItemSelected(item)
    }

}
