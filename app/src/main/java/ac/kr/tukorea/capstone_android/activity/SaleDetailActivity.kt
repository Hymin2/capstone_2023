package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.SaleDetailViewPagerAdapter
import ac.kr.tukorea.capstone_android.data.LikePostRegisterRequestBody
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.data.ResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivitySaleDetailBinding
import ac.kr.tukorea.capstone_android.util.App
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_sale_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class SaleDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivitySaleDetailBinding
    private var isHearting: Boolean = false
    private val service = RetrofitAPI.postService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val detail = intent.getSerializableExtra("detail") as PostInfo
        var isLike = detail.isLike

        Log.d("좋아요", isLike.toString())
        if(isLike){
            var animator : ValueAnimator
            animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500L)

            animator.addUpdateListener { animation : ValueAnimator ->
                binding.likeBtn.progress = animation.animatedValue as Float
            }

            animator.start()
        }

        binding.apply {
            saleDetailUserNickName.text = detail.nickname
            saleDetailTitle.text = detail.postTitle
            saleDetailContent.text = detail.postContent
            saleDetailProductPrice.text = toLongFormat(detail.price)
            saleDetailProductName.text = detail.productName


            likeBtn.setOnClickListener {
                val animator : ValueAnimator
                val username = App.prefs.getString("username", "")
                val postId = detail.postId

                if(isLike){
                    service.deleteLikePost(App.prefs.getString("access_token", ""), postId, username).enqueue(object :Callback<Unit>{
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            if(!response.isSuccessful){
                                Toast.makeText(this@SaleDetailActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(this@SaleDetailActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    })

                    isLike = false
                    animator = ValueAnimator.ofFloat(1f, 0f).setDuration(500L)
                }else{
                    val requestBody = LikePostRegisterRequestBody(postId, username)

                    service.registerLikePost(App.prefs.getString("access_token", ""), requestBody).enqueue(object: Callback<ResponseBody>{
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if(!response.isSuccessful){
                                Toast.makeText(this@SaleDetailActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@SaleDetailActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    })

                    isLike = true
                    animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500L)
                }

                Log.d("좋아요 버튼", isLike.toString())
                animator.addUpdateListener { animation : ValueAnimator ->
                    likeBtn.progress = animation.animatedValue as Float
                }

                animator.start()
            }

            val glideUrl = GlideUrl(
                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + detail.userImage
            )

            Glide.with(this@SaleDetailActivity).load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(saleDetailUserProfileImage)
        }

        binding.saleDetailProductImageViewPager.apply {
            adapter = SaleDetailViewPagerAdapter(detail.postImages, this@SaleDetailActivity)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        // 뷰페이저에 TabLayout 적용
        TabLayoutMediator(binding.tabLayoutViewPager!!, binding.saleDetailProductImageViewPager!!){tab, position ->
            //something
            // tab.text = position.toString()
        }.attach()
    }

    private fun toLongFormat(price: Int): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    private fun getImage(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.profile_image,)
    }

    fun onClickButton(view: View) {
        if(!isHearting){ //기본이 false이므로 false가 아닐때 실행한다.
            //애니메이션의 커스텀
            //0f가 0퍼센트, 1F가 100퍼센트
            //ofFloat(시작지점, 종료지점).setDuration(지속시간)
            // Custom animation speed or duration.
            val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(800)
            animator.addUpdateListener {
                binding.likeBtn.progress = it.animatedValue as Float
            }
            animator.start()
            isHearting = true // 그리고 트루로 바꾼다.
        }else{ //트루일때가 실행된다.
            val animator = ValueAnimator.ofFloat(1f, 0f).setDuration(500)
            animator.addUpdateListener {
                binding.likeBtn.progress = it.animatedValue as Float
            }
            animator.start()
            isHearting = false // 다시 false로 된다.
        }
    }

}