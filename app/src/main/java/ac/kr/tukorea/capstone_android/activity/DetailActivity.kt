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
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Math.abs

class DetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()

        setSupportActionBar(binding.detailToolBar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViewPager() {

        // ViewPager2 Adapter 세팅
        val graphTabAdapter = GraphTabAdapter(this)
        graphTabAdapter.addFragment(graphWeek())
        graphTabAdapter.addFragment(graphMonth())
        graphTabAdapter.addFragment(graphYear())

        // Adapter 연결
        binding.graphViewPager.apply {
            adapter = graphTabAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }


        TabLayoutMediator(binding.graphTab,binding.graphViewPager) { tab, position ->
            when(position){
                0 -> tab.text = "1-week"
                1 -> tab.text = "3-month"
                2 -> tab.text = "1-year"
            }
        }.attach()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 눌렀을 때
                finish()
                return true
            } else -> return super.onOptionsItemSelected(item)
        }
    }
}