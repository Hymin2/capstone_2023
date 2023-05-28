package ac.kr.tukorea.capstone_android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ac.kr.tukorea.capstone_android.activity.FollowerActivity
import ac.kr.tukorea.capstone_android.activity.FollowingActivity
import ac.kr.tukorea.capstone_android.activity.ProfileEditActivity
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding

class myProfile : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 탭 레이아웃과 뷰페이저 설정
        val pagerAdapter = MyPagerAdapter()
        binding.myProfileViewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2 간의 상호작용 설정
        TabLayoutMediator(binding.myProfileTabLayout, binding.myProfileViewPager) { tab, position ->
            tab.text = getPageTitle(position)
        }.attach()

        binding.myFollowerLinear.setOnClickListener {
            val intent = Intent(context, FollowerActivity::class.java)
            startActivity(intent)
        }

        binding.myFollowingLinaer.setOnClickListener {
            val intent = Intent(context, FollowingActivity::class.java)
            startActivity(intent)
        }

        binding.editProfile.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class MyPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            // 탭 개수 반환
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            // 해당 탭에 대한 프래그먼트 반환
            return when (position) {
                0 -> onSale()
                1 -> SoldOut()
                2 -> Like()
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


