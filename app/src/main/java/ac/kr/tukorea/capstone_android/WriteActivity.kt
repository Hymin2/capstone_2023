package ac.kr.tukorea.capstone_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        supportActionBar?.title = "판매글 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}