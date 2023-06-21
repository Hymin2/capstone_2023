package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.FollowAdapter
import ac.kr.tukorea.capstone_android.adapter.RecommendProductAdapter
import ac.kr.tukorea.capstone_android.adapter.ViewPagerAdapter
import ac.kr.tukorea.capstone_android.databinding.ActivityDetailBinding
import ac.kr.tukorea.capstone_android.fragment.graph1Month
import ac.kr.tukorea.capstone_android.fragment.graph3Month
import ac.kr.tukorea.capstone_android.fragment.graph6Month
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailBinding

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ViewPagerAdapter

    private lateinit var graphTab : TabLayout
    private lateinit var graphViewPager : ViewPager2
    val transaction = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init()
        // setUpTransformer()

        var productId = intent.getLongExtra("productId", 0)
        var productName = intent.getStringExtra("productName")
        var productPath = intent.getStringExtra("productPath")

        var retrofitProduct = RetrofitProduct()

        retrofitProduct.getProductDetails(productId, binding, this)
        binding.apply {
            detailProductNameTextview.text = productName

            val glideUrl = GlideUrl(
                ServerInfo.SERVER_URL.url + ServerInfo.PRODUCT_IMAGE_URI.url + productPath!!,
                LazyHeaders.Builder()
                    .addHeader("Authorization", App.prefs.getString("access_token", ""))
                    .build()
            )

            Glide.with(this.root.context).load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(productImage)

            transaction.replace(R.id.graph_FrameLayout, graph1Month(productId)).commit()

            val recyclerLayoutManager = LinearLayoutManager(this@DetailActivity)
            recommendListRecyclerView.layoutManager = recyclerLayoutManager
            recommendListRecyclerView.setHasFixedSize(true)

        }


        binding.toSaleProductListBtn.setOnClickListener{
            val intent = Intent(this,SaleProductListActivity::class.java)
            intent.putExtra("productId", productId)
            intent.putExtra("productName", productName)

            startActivity(intent)
        }

        binding.detailBtnBack.setOnClickListener{
            onBackPressed()
        }
        binding.graphTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // 탭 버튼을 선택할 때 이벤트
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                // var bundle = Bundle()
                when(tab?.text) {
                    "1-month" -> transaction.replace(R.id.graph_FrameLayout, graph1Month(productId)).commit()
                    "3-month" -> transaction.replace(R.id.graph_FrameLayout, graph3Month(productId)).commit()
                    "6-month" -> transaction.replace(R.id.graph_FrameLayout, graph6Month(productId)).commit()
                }
            }

            // 다른 탭 버튼을 눌러 선택된 탭 버튼이 해제될 때 이벤트
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            // 선택된 탭 버튼을 다시 선택할 때 이벤
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}