package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagePagerAdapter(private val context: Context, private val imageList: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.viewpager_image, parent, false)) {
        val productImage = itemView.findViewById<ImageView>(R.id.viewPager_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder((parent))

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(ServerInfo.SERVER_URL.url + ServerInfo.POST_IMAGE_URI.url + imageList[position])
            .fitCenter()
            .into(holder.productImage)
    }
}

