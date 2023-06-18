package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
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
/*        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.graph_viewPager, graphWeek())
        transaction.commit()*/

//        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                handler.removeCallbacks(runnable)
//                handler.postDelayed(runnable, 2000)
//            }
//        })

/*        graphTab = findViewById(R.id.graphTab)
        graphViewPager = findViewById(R.id.graph_viewPager)

        initViewPager()*/
/**
        graphTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> replaceFragment(graphWeek())
                    //1 -> replaceFragment()
                    //2 -> replaceView(laptop_tab)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        **/

    }

/*    private fun initViewPager() {

        // ViewPager2 Adapter 세팅
        val graphTabAdapter = GraphTabAdapter(this)
        graphTabAdapter.addFragment(graphWeek())
        graphTabAdapter.addFragment(graphMonth())
        graphTabAdapter.addFragment(graphYear())

        // Adapter 연결
        graphViewPager.apply {
            adapter = graphTabAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }


        TabLayoutMediator(graphTab,graphViewPager) { tab, position ->
            when(position){
                0 -> tab.text = "1-week"
                1 -> tab.text = "3-month"
                2 -> tab.text = "1-year"
            }
        }.attach()

    }*/

//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(runnable)
//    }
//    override fun onResume() {
//        super.onResume()
//        handler.postDelayed(runnable, 2000)
//    }
//    private val runnable = Runnable {
//        viewPager2.currentItem = viewPager2.currentItem + 1
//    }
//    private fun setUpTransformer() {
//        val transformer = CompositePageTransformer()
//        transformer.addTransformer(MarginPageTransformer(40))
//        transformer.addTransformer{page,position ->
//            val r = 1 - abs(position)
//            page.scaleY = 0.85f + r + 0.14f
//        }
//
//        viewPager2.setPageTransformer(transformer)
//    }
//    private fun init(){
//        viewPager2 = findViewById(R.id.product_image_viewPager)
//        handler = Handler(Looper.myLooper()!!)
//        imageList = ArrayList()
//
//        imageList.add(R.drawable.galaxys23)
//        imageList.add(R.drawable.iphone14pro)
//        imageList.add(R.drawable.profile_image)
//
//        adapter = ViewPagerAdapter(imageList, viewPager2)
//
//        viewPager2.adapter=adapter
//        viewPager2.offscreenPageLimit = 3
//        viewPager2.clipToPadding = false
//        viewPager2.clipChildren = false
//        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//    }

/**
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.graph_viewPager,fragment)
        fragmentTransaction.commit()
    }
    **/