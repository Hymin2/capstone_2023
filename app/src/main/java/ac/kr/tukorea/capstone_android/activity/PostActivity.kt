package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.text.DecimalFormat

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

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