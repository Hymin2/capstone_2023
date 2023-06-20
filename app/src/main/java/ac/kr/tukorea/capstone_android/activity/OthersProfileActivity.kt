package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.databinding.ActivityOthersProfileBinding
import ac.kr.tukorea.capstone_android.fragment.Like
import ac.kr.tukorea.capstone_android.fragment.SoldOut
import ac.kr.tukorea.capstone_android.fragment.onSale
import ac.kr.tukorea.capstone_android.retrofit.RetrofitUser
import ac.kr.tukorea.capstone_android.util.App
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OthersProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOthersProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOthersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // val retrofit = RetrofitUser()

        // retrofit.getUserInfo(App.prefs.getString("username", ""), binding)

        // 탭 레이아웃과 뷰페이저 설정
        val pagerAdapter = OtherPagerAdapter()
        binding.othersProfileViewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2 간의 상호작용 설정
        TabLayoutMediator(binding.othersProfileTabLayout, binding.othersProfileViewPager) { tab, position ->
            tab.text = getPageTitle(position)
        }.attach()


        binding.othersBackButton.setOnClickListener {
            onBackPressed()
        }

    }

    private inner class OtherPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            // 탭 개수 반환
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            // 해당 탭에 대한 프래그먼트 반환
            return when (position) {
                0 -> onSale(App.prefs.getString("username", ""))
                1 -> SoldOut(App.prefs.getString("username", ""))
                2 -> Like(App.prefs.getString("username", ""))
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }

    private fun getPageTitle(position: Int): String? {
        // 탭의 타이틀 반환
        return when (position) {
            0 -> "판매 중"
            1 -> "판매 완료"
            2 -> "좋아요"
            else -> null
        }
    }
}