package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SaleDetailViewPagerAdapter (var Image: ArrayList<Int>) :
    RecyclerView.Adapter<SaleDetailViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.viewpager_image, parent, false)) {
        val productImage = itemView.findViewById<ImageView>(R.id.viewPager_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = Image.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.productImage.setImageResource(Image[position])
    }
}