package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FeedListAdapter
import ac.kr.tukorea.capstone_android.data.FeedList
import ac.kr.tukorea.capstone_android.databinding.ActivityFeedListBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

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


        val recyclerLayoutManager = LinearLayoutManager(this)
        binding.feedListRecyclerView.apply {
            layoutManager = recyclerLayoutManager
            setHasFixedSize(true)
        }


        binding.feedListBtnBack.setOnClickListener {
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
                return true
            }
        })

        return true
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
