package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.Follow
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl

class FollowAdapter(private val followList: ArrayList<Follow>, val context : Context) : RecyclerView.Adapter<FollowAdapter.MyViewHolder>() {
    private lateinit var mListener : FollowAdapter.onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: FollowAdapter.onItemClickListener) {
        mListener = listener
    }

    fun getItem(position: Int): Follow {
        return followList[position]
    }

    class MyViewHolder(
        itemView: View,
        listener: FollowAdapter.onItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val followProfileImage = itemView.findViewById<ImageView>(R.id.follow_profileImage)
        val followUserName = itemView.findViewById<TextView>(R.id.follow_userName)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.follow_list_item,
            parent, false
        )
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: FollowAdapter.MyViewHolder, position: Int) {
        val item = followList[position]

        if(item.image.isNotEmpty()){
            val glideUrl = GlideUrl(
                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + item.image
            )

            Glide.with(context).load(glideUrl)
                .override(150)
                .into(holder.followProfileImage)
        }
        holder.followUserName.text = item.nickname
    }

    override fun getItemCount(): Int {
        return followList.size
    }
}
