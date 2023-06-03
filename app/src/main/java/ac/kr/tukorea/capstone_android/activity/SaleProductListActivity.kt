package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FeedListAdapter
import ac.kr.tukorea.capstone_android.adapter.SaleProductAdapter
import ac.kr.tukorea.capstone_android.data.FeedList
import ac.kr.tukorea.capstone_android.databinding.ActivityFeedListBinding
import ac.kr.tukorea.capstone_android.databinding.ActivitySaleProductListBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

class SaleProductListActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySaleProductListBinding

    private lateinit var saleProductList: ArrayList<FeedList>
    private lateinit var filteredList: ArrayList<FeedList> // 검색 결과를 담을 리스트
    private lateinit var adapter: SaleProductAdapter // 어댑터 변수 추가

    lateinit var saleProductProfileImage : Array<Int>
    lateinit var saleProductUserNickName : Array<String>
    lateinit var saleProductImage : Array<Int>
    lateinit var saleProductModel : Array<String>
    lateinit var saleProductPrice : Array<Int>
    lateinit var saleProductMain : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaleProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.saleProductListToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        dataInitialize()
        val recyclerLayoutManager = LinearLayoutManager(this)
        binding.saleProductListRecyclerView.apply {
            layoutManager = recyclerLayoutManager
            setHasFixedSize(true)
        }

        adapter = SaleProductAdapter(saleProductList) // 어댑터 초기화
        binding.saleProductListRecyclerView.adapter = adapter

        getUserData()

        binding.saleProductListBtnBack.setOnClickListener{
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
        saleProductList = arrayListOf<FeedList>()

        saleProductProfileImage = arrayOf(
            R.drawable.profile_image,
            R.drawable.profile_image,
            R.drawable.profile_image,
        )

        saleProductUserNickName = arrayOf(
            "귀여운 햄스터",
            "123",
            "qwe",
        )

        saleProductImage = arrayOf(
            R.drawable.testimage1,
            R.drawable.iphone14pro,
            R.drawable.galaxys23
        )

        saleProductModel = arrayOf(
            "햄스터",
            "아이폰 14 Pro",
            "갤럭시 S23"
        )

        saleProductPrice = arrayOf(
            123,
            456,
            789,
        )
        saleProductMain = arrayOf(
            "햄스터햄스터햄스터햄스터햄스터\n햄스터햄스터햄스터햄스터햄스터햄스터햄스터",
            "판매글 내용2",
            "판매글 내용3"
        )


        filteredList = ArrayList(saleProductList) // 검색 결과 리스트 초기화
    }
    private fun getUserData() {
        for(i in saleProductProfileImage.indices){
            val saleProduct = FeedList(
                saleProductProfileImage[i],
                saleProductUserNickName[i],
                saleProductImage[i],
                saleProductModel[i],
                saleProductPrice[i],
                saleProductMain[i]
            )
            saleProductList.add(saleProduct)
        }
    }


    private fun filter(query: String) {
        filteredList.clear() // 검색 결과 리스트 초기화

        if (query.isNotEmpty()) {
            val searchQuery = query.lowercase()

            for (item in saleProductList) {
                // 검색어가 닉네임, 제품 모델, 제품 메인 내용 중 하나와 일치하는지 확인
                if (item.feedUserNickName.lowercase().contains(searchQuery) ||
                    item.feedProductModel.lowercase().contains(searchQuery) ||
                    item.feedProductMain.lowercase().contains(searchQuery)
                ) {
                    filteredList.add(item)
                }
            }
        } else {
            filteredList.addAll(saleProductList) // 검색어가 없으면 모든 아이템 추가
        }

        adapter.updateList(filteredList) // 어댑터의 데이터 업데이트
    }
}