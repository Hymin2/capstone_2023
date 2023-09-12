package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.FollowerActivity
import ac.kr.tukorea.capstone_android.activity.FollowingActivity
import ac.kr.tukorea.capstone_android.activity.LoginActivity
import ac.kr.tukorea.capstone_android.activity.ProfileEditActivity
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitUser
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class myProfile : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private val userService = RetrofitAPI.userService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.myProfileToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        getMyInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 탭 레이아웃과 뷰페이저 설정
    }


    fun getMyInfo(){
        val pagerAdapter = MyPagerAdapter()
        binding.myProfileViewPager.adapter = pagerAdapter


        // TabLayout과 ViewPager2 간의 상호작용 설정
        TabLayoutMediator(binding.myProfileTabLayout, binding.myProfileViewPager) { tab, position ->
            tab.text = getPageTitle(position)
        }.attach()

        binding.myFollowerLinear.setOnClickListener {
            val intent = Intent(context, FollowerActivity::class.java)
            intent.putExtra("username", App.prefs.getString("username", ""))
            startActivity(intent)
        }

        binding.myFollowingLinaer.setOnClickListener {
            val intent = Intent(context, FollowingActivity::class.java)
            intent.putExtra("username", App.prefs.getString("username", ""))
            startActivity(intent)
        }

        binding.editProfile.setOnClickListener {
            if (binding.myProfileProfileImage.drawable != null) {
                val intent = Intent(context, ProfileEditActivity::class.java)
                val bitmap: Bitmap = binding.myProfileProfileImage.drawable.toBitmap(150, 150)
                intent.putExtra("image", bitmap)
                startActivity(intent)
            } else {
                val intent = Intent(context, ProfileEditActivity::class.java)
                startActivity(intent)
            }
        }

        val retrofitUser = RetrofitUser()

        retrofitUser.getUserInfo(App.prefs.getString("username", ""), binding)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logOut -> {
                userService.logout(App.prefs.getString("access_token", ""),
                    App.prefs.getString("refresh_token", ""),
                    App.prefs.getString("username", "")).enqueue(object : Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if(response.isSuccessful){
                            App.prefs.setString("username", "")
                            App.prefs.setString("nickname", "")
                            App.prefs.setString("access_token", "")
                            App.prefs.setString("refresh_token", "")

                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(context, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    } })
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MyPagerAdapter : FragmentStateAdapter(this) {
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


