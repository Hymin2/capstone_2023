package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.Follow
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FollowAdapter(private val followList: ArrayList<Follow>, private val followListener: onItemClickListener
) : RecyclerView.Adapter<FollowAdapter.MyViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
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
        return MyViewHolder(itemView, followListener)
    }

    override fun onBindViewHolder(holder: FollowAdapter.MyViewHolder, position: Int) {
        val item = followList[position]

        holder.followProfileImage.setImageResource(item.userProfileImage)
        holder.followUserName.text = item.userName
    }

    override fun getItemCount(): Int {
        return followList.size
    }
}