package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ChatList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatList : ArrayList<ChatList>) :
    RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private lateinit var chatListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        chatListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_list_item,
            parent, false)
        return MyViewHolder(itemView, chatListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chatList[position]
        holder.chatProfileImageId.setImageResource(currentItem.profileImage)
        holder.chatOpponentName.text = currentItem.opponentName
        holder.chatRecentMessage.text = currentItem.recentMessage
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        val chatProfileImageId : ImageView = itemView.findViewById(R.id.chat_image)
        val chatOpponentName : TextView = itemView.findViewById(R.id.chat_other_nickname)
        val chatRecentMessage : TextView = itemView.findViewById(R.id.chat_recent_message)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}