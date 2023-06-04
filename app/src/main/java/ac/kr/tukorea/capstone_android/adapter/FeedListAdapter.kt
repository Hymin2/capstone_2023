package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.data.FeedList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import java.text.DecimalFormat

class FeedListAdapter(private var items: ArrayList<PostInfo>, val context : Context) : RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder>() {

    class FeedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfileImage: ImageView = itemView.findViewById(R.id.feedList_userProfileImage)
        val userNickname: TextView = itemView.findViewById(R.id.feedList_userNickName)
        val images: ImageView = itemView.findViewById(R.id.feedList_productImage)
        val title: TextView = itemView.findViewById(R.id.feedList_title)
        val price : TextView = itemView.findViewById(R.id.feedList_productPrice)
        val content: TextView = itemView.findViewById(R.id.feedList_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list_item, parent, false)
        return FeedListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        val item = items[position]
        holder.userNickname.text = item.nickname

        if(item.userImage != null) {
            val glideUrl = GlideUrl(
                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + item.userImage
            )

            Glide.with(context).load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.userProfileImage)
        }
        holder.title.text = item.postTitle
        holder.content.text = item.postContent
        holder.price.text = toLongFormat(item.price)
    }

    private fun toLongFormat(price: Int): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
