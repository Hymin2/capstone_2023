package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

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