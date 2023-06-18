package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target


class MyProfileTabAdapter(val items : List<PostInfo>, val context : Context) : RecyclerView.Adapter<MyProfileTabAdapter.ViewHolder>() {
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
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

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val glideUrl = GlideUrl(
            ServerInfo.SERVER_URL.url + ServerInfo.POST_IMAGE_URI.url + item.postImages[0]
        )

        Glide.with(context).load(glideUrl)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.myProfile_recyclerImage)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}


