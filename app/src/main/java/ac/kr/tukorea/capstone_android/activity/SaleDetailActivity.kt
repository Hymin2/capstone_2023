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
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class SaleDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivitySaleDetailBinding
    private var isHearting: Boolean = false
    private val service = RetrofitAPI.postService

    var userName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val detail = intent.getSerializableExtra("detail") as PostInfo
        var isLike = detail.isLike

        userName = detail.username

        setSupportActionBar(binding.saleDetailToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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

            if(detail.username == App.prefs.getString("username", "")) saleDetailChatBtn.visibility = View.INVISIBLE

            saleDetailChatBtn.setOnClickListener {
                if(detail.isOnSale == "N") Toast.makeText(this@SaleDetailActivity, "이미 판매된 상품입니다.", Toast.LENGTH_SHORT).show()
            }

            saleDetailUserNickName.setOnClickListener {
                if(detail.username != App.prefs.getString("username", "")) {
                    var intent = Intent(this@SaleDetailActivity, OthersProfileActivity::class.java)
                    intent.putExtra("username", detail.username)
                    intent.putExtra("nickname", detail.nickname)
                    startActivity(intent)
                } else{
                    val intent = Intent(this@SaleDetailActivity, MainActivity::class.java)
                    intent.putExtra("isProfile", true)
                    startActivity(intent)
                    finish()
                }
            }

            saleDetailUserProfileImage.setOnClickListener{
                if(detail.username != App.prefs.getString("username", "")) {
                    var intent = Intent(this@SaleDetailActivity, OthersProfileActivity::class.java)
                    intent.putExtra("username", detail.username)
                    intent.putExtra("nickname", detail.nickname)
                    startActivity(intent)
                } else{
                    val intent = Intent(this@SaleDetailActivity, MainActivity::class.java)
                    intent.putExtra("isProfile", true)
                    startActivity(intent)
                    finish()
                }
            }
            saleDetailBtnBack.setOnClickListener{
                onBackPressed()
            }

            likeBtn.setOnClickListener {
                val animator: ValueAnimator
                val username = App.prefs.getString("username", "")
                val postId = detail.postId

                if (isLike) {
                    service.deleteLikePost(App.prefs.getString("access_token", ""),
                        postId,
                        username).enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            if (!response.isSuccessful) {
                                Toast.makeText(this@SaleDetailActivity,
                                    "잠시 후에 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(this@SaleDetailActivity,
                                "잠시 후에 다시 시도해주세요.",
                                Toast.LENGTH_SHORT).show()
                        }

                    })

                    isLike = false
                    animator = ValueAnimator.ofFloat(1f, 0f).setDuration(500L)
                } else {
                    val requestBody = LikePostRegisterRequestBody(postId, username)

                    service.registerLikePost(App.prefs.getString("access_token", ""), requestBody)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>,
                            ) {
                                if (!response.isSuccessful) {
                                    Toast.makeText(this@SaleDetailActivity,
                                        "잠시 후에 다시 시도해주세요.",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(this@SaleDetailActivity,
                                    "잠시 후에 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT).show()
                            }

                        })

                    isLike = true
                    animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500L)
                }

                Log.d("좋아요 버튼", isLike.toString())
                animator.addUpdateListener { animation: ValueAnimator ->
                    likeBtn.progress = animation.animatedValue as Float
                }

                animator.start()
            }

            if (detail.userImage != null) {
                val glideUrl = GlideUrl(
                    ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + detail.userImage
                )

                Glide.with(this@SaleDetailActivity).load(glideUrl)
                    .override(Target.SIZE_ORIGINAL)
                    .into(saleDetailUserProfileImage)
            }

            // 채팅하기 버튼
            saleDetailChatBtn.setOnClickListener {
                var intent = Intent(this@SaleDetailActivity, ChatActivity::class.java)
                intent.putExtra("postId", detail.postId)
                intent.putExtra("username", detail.username)
                intent.putExtra("nickname", detail.nickname)
                intent.putExtra("userImage", detail.userImage)
                intent.putExtra("userType", "Buyer")
                intent.putExtra("isFromPost", true)
                startActivity(intent)
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sale_detail_menu, menu)

        // 글 작성자의 아이디와 사용자의 아이디 비교
        val postAuthorId = userName // 작성자의 아이디를 가져와서 설정해주세요
        val currentUserId = App.prefs.getString("username", "") // 사용자의 아이디

        if (postAuthorId == currentUserId) {
            // 글 작성자와 사용자의 아이디가 일치하는 경우에만 메뉴를 표시
            for (i in 0 until menu?.size()!!) {
                menu.getItem(i).isVisible = true
            }
        } else {
            // 일치하지 않는 경우 메뉴를 숨김
            for (i in 0 until menu?.size()!!) {
                menu.getItem(i).isVisible = false
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_post -> {
                AlertDialog.Builder(this)
                    .setTitle("판매글 삭제")
                    .setMessage("판매글을 삭제하시겠습니까?")
                    .setPositiveButton("예") { dialog, which ->
                        // 게시글 삭제 로직
                    }
                    .setNegativeButton("아니오", null)
                    .show()
                return true
            }
            R.id.modify_post -> {
                AlertDialog.Builder(this)
                    .setTitle("판매글 수정")
                    .setMessage("판매글을 수정하시겠습니까?")
                    .setPositiveButton("예") { dialog, which ->
                        // 게시글 수정 로직
                    }
                    .setNegativeButton("아니오", null)
                    .show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}