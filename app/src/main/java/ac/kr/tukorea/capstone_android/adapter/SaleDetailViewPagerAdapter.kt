package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

class SaleDetailViewPagerAdapter (var images: List<String>, val context : Context) :
    RecyclerView.Adapter<SaleDetailViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.viewpager_image, parent, false)) {
        val productImage = itemView.findViewById<ImageView>(R.id.viewPager_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        Glide.with(context).load(ServerInfo.SERVER_URL.url + ServerInfo.POST_IMAGE_URI.url + images[position])
            .fitCenter()
            .into(holder.productImage)
    }
}