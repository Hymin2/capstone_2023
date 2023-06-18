package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.SearchResultAdapter
import ac.kr.tukorea.capstone_android.data.SearchResult
import ac.kr.tukorea.capstone_android.fragment.searchPhone
import ac.kr.tukorea.capstone_android.fragment.searchTablet
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class SearchResultActivity : AppCompatActivity() {

    //lateinit var main_searchView : SearchView

    private lateinit var adapter: SearchResultAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchResultArrayList: ArrayList<SearchResult>

    lateinit var searchResultImageId : Array<Int>
    lateinit var searchResultProductName : Array<String>
    lateinit var searchResultProductPrice : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val phone_tab = searchPhone()
        val tablet_tab = searchTablet()
        //val laptop_tab = searchLaptop()
        val tabs = findViewById<TabLayout>(R.id.search_device_tabs)
        // val fragmentManager = FragmentManager.beginTransaction()
        // fragmentManager.add(R.id.searchDeviceFrame, phone_tab).commit()

        init()

        // recyclerView.addItemDecoration(DividerItemDecoration(this,1))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> replaceView(phone_tab)
                    1 -> replaceView(tablet_tab)
                    //2 -> replaceView(laptop_tab)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        // super.onViewCreated(view, savedInstanceState)
    }

    private fun init(){
        recyclerView = findViewById(R.id.searchResult_RecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        searchResultArrayList = ArrayList()

        addDataToList()

        adapter = SearchResultAdapter(searchResultArrayList)
        recyclerView.adapter = adapter
    }

    private fun addDataToList(){
        searchResultArrayList.add(SearchResult(R.drawable.iphone14pro,"아이폰14 Pro",888888))
        searchResultArrayList.add(SearchResult(R.drawable.galaxys23,"갤럭시 S23",999999))
        searchResultArrayList.add(SearchResult(R.drawable.iphone14pro,"아이폰14",123456))
    }


    private fun replaceView(tab : Fragment){
        // childFragmentManager.beginTransaction().replace(R.id.searchDeviceFrame, tab).commit()
    }
}