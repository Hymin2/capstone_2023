package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityViewSalePostBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

class ViewSalePostActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewSalePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSalePostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.viewSalePostToolBar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 눌렀을 때
                finish()
                return true
            } else -> return super.onOptionsItemSelected(item)
        }
    }
}