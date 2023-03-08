package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityPostBinding
import ac.kr.tukorea.capstone_android.databinding.ActivityWriteBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.text.DecimalFormat

class PostActivity : AppCompatActivity() {

    lateinit var binding : ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = "게시글"

        val postTitle : TextView = findViewById(R.id.postTitle)
        val postImage : ImageView = findViewById(R.id.postImage)
        val postWriter : TextView = findViewById(R.id.postWriter)
        val postDate : TextView = findViewById(R.id.postDate)
        val postViews : TextView = findViewById(R.id.postViews)
        val postContent : TextView = findViewById(R.id.postContent)

        val bundle : Bundle? = intent.extras
        val title = bundle!!.getString("postTitle")
        val imageId = bundle.getInt("postimageId")
        val views = bundle.getInt("postViews")
        val writer = bundle.getString("postWriter")
        val date = bundle.getString("postDate")
        val content = bundle.getString("postContent")



        postTitle.text = title
        postImage.setImageResource(imageId)
        postWriter.text = writer
        postViews.text = views.toString()
        postDate.text = date
        postContent.text = content
    }
}