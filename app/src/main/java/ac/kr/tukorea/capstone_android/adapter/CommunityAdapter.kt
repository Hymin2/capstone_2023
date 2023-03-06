package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.Post
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityAdapter(private val postList : ArrayList<Post>) :
        RecyclerView.Adapter<CommunityAdapter.MyViewHolder>() {

    private lateinit var postListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        postListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.community_list_item,
            parent, false)
        return MyViewHolder(itemView,postListener)
    }

    override fun onBindViewHolder(holder: CommunityAdapter.MyViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.postTitle.text = currentItem.postTitle
        holder.postWriter.text = currentItem.postWriter
        holder.postDate.text = currentItem.postDate
        holder.postViews.text = currentItem.postViews.toString()
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        val postTitle : TextView = itemView.findViewById(R.id.communityTitle)
        val postWriter : TextView = itemView.findViewById(R.id.communityWriter)
        val postDate : TextView = itemView.findViewById(R.id.communityDate)
        val postViews : TextView = itemView.findViewById(R.id.communityViews)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }
}