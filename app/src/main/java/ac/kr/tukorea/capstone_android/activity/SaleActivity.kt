package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityPostBinding
import ac.kr.tukorea.capstone_android.databinding.ActivitySaleBinding
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat
import kotlin.math.log

class SaleActivity : AppCompatActivity() {

    lateinit var binding : ActivitySaleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = "판매글"

        val saleTitle : TextView = findViewById(R.id.saleTitle)
        val saleImage : ImageView = findViewById(R.id.saleImage)
        val salePrice : TextView = findViewById(R.id.salePrice)
        val saleContent : TextView = findViewById(R.id.saleContent)

        val bundle : Bundle? = intent.extras
        val title = bundle!!.getString("title")
        val imageId = bundle.getInt("imageId")
        val price = bundle.getInt("price")
        val content = bundle.getString("content")

        // 화폐 단위 표기
        val t_dec_up = DecimalFormat("#,###")
        var str_change_money_up = t_dec_up.format(price)
        println("화폐 표시 : "+str_change_money_up)

        saleTitle.text = title
        saleImage.setImageResource(imageId)
        salePrice.text = str_change_money_up.toString() + "원"
        saleContent.text = content
    }
}