package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ChatRoom
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
import com.bumptech.glide.request.target.Target
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(private val chatList : ArrayList<ChatRoom>, val context : Context) :
    RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {

    private lateinit var chatListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        chatListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_room_list_item,
            parent, false)
        return MyViewHolder(itemView, chatListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chatList[position]

        holder.chatOpponentName.text = currentItem.opponentNickname
        holder.chatRecentMessage.text = currentItem.recentMessage

        if(currentItem.recentMessageTime != null) {
            val dateTimeSplit = currentItem.recentMessageTime.split(" ")

            val date = dateTimeSplit[0] + " " + dateTimeSplit[1] + " " + dateTimeSplit[2]

            val timeFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            timeFormatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            if(timeFormatter.format(Calendar.getInstance().time) == date)
                holder.chatRecentTime.text = dateTimeSplit[4] + " " + dateTimeSplit[5]
            else holder.chatRecentTime.text = date
        }

        if(currentItem.opponentUserImage != null){
            val glideUrl = GlideUrl(
                ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + currentItem.opponentUserImage
            )

            Glide.with(context).load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.chatProfileImage)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val chatProfileImage : ImageView = itemView.findViewById(R.id.chat_room_image)
        val chatOpponentName : TextView = itemView.findViewById(R.id.chat_room_other_nickname)
        val chatRecentMessage : TextView = itemView.findViewById(R.id.chat_room_recent_message)
        val chatRecentTime : TextView = itemView.findViewById(R.id.chat_room_recent_time)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}