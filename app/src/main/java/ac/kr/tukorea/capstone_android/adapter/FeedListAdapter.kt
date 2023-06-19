package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import java.text.DecimalFormat

class FeedListAdapter(private var items: ArrayList<PostInfo>, val context : Context) : RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder>() {
    private lateinit var mListener : FeedListAdapter.onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)

        fun onUserProfileImageClick(position: Int)
        fun onUserNicknameClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class FeedListViewHolder(itemView: View, listener: FeedListAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val userProfileImage: ImageView = itemView.findViewById(R.id.feedList_userProfileImage)
        val userNickname: TextView = itemView.findViewById(R.id.feedList_userNickName)
        val title: TextView = itemView.findViewById(R.id.feedList_title)
        val price : TextView = itemView.findViewById(R.id.feedList_productPrice)
        val content: TextView = itemView.findViewById(R.id.feedList_content)

        val onSaleTextView: TextView = itemView.findViewById(R.id.onSale_textView)
        val soldOutTranslucent: ImageView = itemView.findViewById(R.id.soldOut_translucent)
        val soldOutTextView: TextView = itemView.findViewById(R.id.soldOut_textView)

        val productImageViewPager: ViewPager2 = itemView.findViewById(R.id.saleProductList_productImage_viewPager)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            userProfileImage.setOnClickListener {
                listener.onUserProfileImageClick(adapterPosition)
            }

            userNickname.setOnClickListener {
                listener.onUserNicknameClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list_item, parent, false)
        return FeedListViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        val item = items[position]
        holder.userNickname.text = item.nickname

        // ViewPager2 어댑터 설정
        val imageAdapter = ImagePagerAdapter(context, item.postImages)
        holder.productImageViewPager.adapter = imageAdapter

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

/*        if (item.onSale) { // 판매 중인 상태
            holder.onSaleTextView.visibility = View.VISIBLE
            holder.soldOutTranslucent.visibility = View.INVISIBLE
            holder.soldOutTextView.visibility = View.INVISIBLE
        } else { // 판매 완료인 상태
            holder.onSaleTextView.visibility = View.INVISIBLE
            holder.soldOutTranslucent.visibility = View.VISIBLE
            holder.soldOutTextView.visibility = View.VISIBLE
        }*/
    }

    private fun toLongFormat(price: Int): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
