package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.GraphTabAdapter
import ac.kr.tukorea.capstone_android.adapter.ViewPagerAdapter
import ac.kr.tukorea.capstone_android.databinding.ActivityDetailBinding
import ac.kr.tukorea.capstone_android.fragment.graphMonth
import ac.kr.tukorea.capstone_android.fragment.graphWeek
import ac.kr.tukorea.capstone_android.fragment.graphYear
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Math.abs

class DetailActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ViewPagerAdapter

    private lateinit var graphTab : TabLayout
    private lateinit var graphViewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })

        graphTab = findViewById(R.id.graphTab)
        graphViewPager = findViewById(R.id.graph_viewPager)

        initViewPager()
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

    private fun initViewPager() {

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
                0 -> tab.text = "week"
                1 -> tab.text = "month"
                2 -> tab.text = "year"
            }
        }.attach()

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 2000)
    }
    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }
    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page,position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }
    private fun init(){
        viewPager2 = findViewById(R.id.product_image_viewPager)
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.galaxys23)
        imageList.add(R.drawable.iphone14pro)
        imageList.add(R.drawable.profile_image)

        adapter = ViewPagerAdapter(imageList, viewPager2)

        viewPager2.adapter=adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

/**
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.graph_viewPager,fragment)
        fragmentTransaction.commit()
    }
    **/


}