package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.data.FeedList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ac.kr.tukorea.capstone_android.R

class FeedListAdapter(private var feedList: ArrayList<FeedList>) :
    RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder>() {

    class FeedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedProfileImage: ImageView = itemView.findViewById(R.id.feedList_userProfileImage)
        val feedUserNickName: TextView = itemView.findViewById(R.id.feedList_userNickName)
        val feedProductImage: ImageView = itemView.findViewById(R.id.feedList_productImage)
        val feedProductModel: TextView = itemView.findViewById(R.id.feedList_productModel)
        val feedProductPrice: TextView = itemView.findViewById(R.id.feedList_productPrice)
        val feedProductMain: TextView = itemView.findViewById(R.id.feedList_productMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list_item, parent, false)
        return FeedListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        val feed = feedList[position]
        holder.feedProfileImage.setImageResource(feed.feedProfileImage)
        holder.feedUserNickName.text = feed.feedUserNickName
        holder.feedProductImage.setImageResource(feed.feedProductImage)
        holder.feedProductModel.text = feed.feedProductModel
        holder.feedProductPrice.text = "${feed.feedProductPrice} 원"
        holder.feedProductMain.text = feed.feedProductMain
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    fun updateList(list: ArrayList<FeedList>) {
        feedList = list
        notifyDataSetChanged()
    }
}
