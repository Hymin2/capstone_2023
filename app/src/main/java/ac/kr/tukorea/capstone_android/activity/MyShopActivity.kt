package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.MyShopAdapter
import ac.kr.tukorea.capstone_android.data.MyShop
import ac.kr.tukorea.capstone_android.databinding.ActivityMyShopBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager

class MyShopActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMyShopBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var list = arrayListOf(
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.profile_image,
            R.drawable.galaxys23,

            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.profile_image,
            R.drawable.galaxys23,

            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.profile_image,
            R.drawable.galaxys23,

            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.profile_image,
            R.drawable.galaxys23,
        )
        var listManager = GridLayoutManager(this, 3)
        var listAdapter = MyShopAdapter(list)

        binding.myShopRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = listManager
            adapter = listAdapter
        }

        binding.myShopBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}