package ac.kr.tukorea.capstone_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ac.kr.tukorea.capstone_android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class MyProfileTabAdapter : RecyclerView.Adapter<MyProfileTabAdapter.ViewHolder>() {
    private var imageResources: List<Int> = emptyList()

    // 이미지 리소스 설정
    fun setImageResources(resources: List<Int>) {
        imageResources = resources
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myshop_list_item, parent, false)

        // 아이템의 너비를 계산하여 정사각형으로 설정
        val displayMetrics = parent.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = screenWidth / 3
        val layoutParams = view.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        view.layoutParams = layoutParams

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageResource = imageResources[position]
        holder.imageView.setImageResource(imageResource)
    }

    override fun getItemCount(): Int {
        return imageResources.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.myProfile_recyclerImage)
    }
}


