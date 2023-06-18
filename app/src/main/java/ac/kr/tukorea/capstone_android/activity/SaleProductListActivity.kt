package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FeedListAdapter
import ac.kr.tukorea.capstone_android.data.FeedList
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.data.PostResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivitySaleProductListBinding
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaleProductListActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySaleProductListBinding

    private lateinit var saleProductList: ArrayList<FeedList>
    private lateinit var adapter: FeedListAdapter // 어댑터 변수 추가

    private val service = RetrofitAPI.postService

    var productId : Long = 0
    var searchString : String? = null
    var isOnSale : String = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaleProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.saleProductListToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        productId = intent.getLongExtra("productId", 0L)

        searchPost()

        val recyclerLayoutManager = LinearLayoutManager(this)
        binding.saleProductListRecyclerView.apply {
            layoutManager = recyclerLayoutManager
            setHasFixedSize(true)
        }

        binding.saleProductListBtnBack.setOnClickListener{
            onBackPressed()
        }
    }

    fun searchPost(){
        service.getPostList(App.prefs.getString("access_token", ""), productId, null, searchString, searchString, isOnSale).enqueue(object:
            Callback<PostResponseBody> {
            override fun onResponse(
                call: Call<PostResponseBody>,
                response: Response<PostResponseBody>,
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    adapter = FeedListAdapter(body!!.message as ArrayList<PostInfo>, this@SaleProductListActivity) // 어댑터 초기화
                    adapter.setOnItemClickListener(object : FeedListAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@SaleProductListActivity, SaleDetailActivity::class.java)
                            intent.putExtra("detail", body.message[position])
                            startActivity(intent)
                        }

                    })
                    binding.saleProductListRecyclerView.adapter = adapter
                }else{
                    Toast.makeText(this@SaleProductListActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponseBody>, t: Throwable) {
                Toast.makeText(this@SaleProductListActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."

        // SearchView 리스너 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchString = query
                searchPost()

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchString = newText
                searchPost()

                return true
            }
        })

        return true
    }

}