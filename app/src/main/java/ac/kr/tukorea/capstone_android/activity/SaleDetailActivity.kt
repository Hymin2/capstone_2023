package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.SaleDetailViewPagerAdapter
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.databinding.ActivitySaleDetailBinding
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat

class SaleDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivitySaleDetailBinding
    private var isHearting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val detail = intent.getSerializableExtra("detail") as PostInfo

        binding.apply {
            saleDetailUserNickName.text = detail.nickname
            saleDetailTitle.text = detail.postTitle
            saleDetailContent.text = detail.postContent
            saleDetailProductPrice.text = toLongFormat(detail.price)
            saleDetailProductName.text = detail.productName

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