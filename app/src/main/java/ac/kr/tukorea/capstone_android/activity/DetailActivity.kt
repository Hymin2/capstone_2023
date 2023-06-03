package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.ViewPagerAdapter
import ac.kr.tukorea.capstone_android.databinding.ActivityDetailBinding
import ac.kr.tukorea.capstone_android.fragment.*
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productId = intent.getLongExtra("productId", 0)
        var productName = intent.getStringExtra("productName")
        var productPath = intent.getStringExtra("productPath")

        var retrofitProduct = RetrofitProduct()

        retrofitProduct.getProductDetails(productId, binding, this)
        binding.apply {
            detailProductNameTextview.text = productName

            val glideUrl = GlideUrl(
                productPath!!.replace("localhost", "10.0.2.2"),
                LazyHeaders.Builder()
                    .addHeader("Authorization", App.prefs.getString("access_token", ""))
                    .build()
            )

            Glide.with(this.root.context).load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(productImage)
        }

        transaction = supportFragmentManager.beginTransaction()

        binding.toSaleProductListBtn.setOnClickListener{
            val intent = Intent(this,SaleProductListActivity::class.java)
            startActivity(intent)
        }
        binding.graphTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            // 탭 버튼을 선택할 때 이벤트
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                // var bundle = Bundle()
                when (tab?.text) {
                    "1-month" -> transaction.replace(R.id.graph_FrameLayout, graph1Month())
                    "3-month" -> transaction.replace(R.id.graph_FrameLayout, graph3Month())
                    "6-month" -> transaction.replace(R.id.graph_FrameLayout, graph6Month())
                }
                transaction.commit()
            }

            // 다른 탭 버튼을 눌러 선택된 탭 버튼이 해제될 때 이벤트
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            // 선택된 탭 버튼을 다시 선택할 때 이벤트
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when (tab?.text) {
                    "1-month" -> transaction.replace(R.id.graph_FrameLayout, graph1Month())
                    "3-month" -> transaction.replace(R.id.graph_FrameLayout, graph3Month())
                    "6-month" -> transaction.replace(R.id.graph_FrameLayout, graph6Month())
                }
                transaction.commit()
            }
        })
    }
}
