package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ChatMessage
import ac.kr.tukorea.capstone_android.util.ChatMessageViewType
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
import kotlin.collections.ArrayList

class ChatMessageAdapter(private val items : ArrayList<ChatMessage>,
                         private val opponentNickname : String,
                         private val opponentImage : String?,
                         val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class RightViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        val message : TextView = itemView.findViewById(R.id.message_right)
        val time : TextView = itemView.findViewById(R.id.message_right_time)
    }

    inner class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message : TextView = itemView.findViewById(R.id.message_left)
        val time : TextView = itemView.findViewById(R.id.message_left_time)
    }

    inner class FirstLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message : TextView = itemView.findViewById(R.id.message_first_left)
        val time : TextView = itemView.findViewById(R.id.message_first_left_time)
        val nickname : TextView = itemView.findViewById(R.id.message_first_left_profile_nickname)
        val image : ImageView = itemView.findViewById(R.id.message_first_left_profile_image)
    }

    inner class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message : TextView = itemView.findViewById(R.id.message_center)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            ChatMessageViewType.RIGHT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.chat_right_item,
                    parent, false)
                return RightViewHolder(itemView)
            }
            ChatMessageViewType.LEFT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.chat_left_item,
                    parent, false)
                return LeftViewHolder(itemView)
            }
            ChatMessageViewType.FIRST_LEFT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.chat_first_left_item,
                    parent, false)
                return FirstLeftViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.chat_center_item,
                    parent, false)
                return CenterViewHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val parser = SimpleDateFormat("a hh:mm:ss", Locale.KOREA)
        val timeFormatter = SimpleDateFormat("a hh:mm", Locale.KOREA)

        if(holder is RightViewHolder){
            holder.message.text = item.message

            holder.time.text = timeFormatter.format(parser.parse(item.time))

            if(isTimeVisibility(position)) holder.time.visibility = View.VISIBLE
            else holder.time.visibility = View.INVISIBLE

        } else if(holder is LeftViewHolder){
            holder.message.text = item.message

            holder.time.text = timeFormatter.format(parser.parse(item.time))

            if(isTimeVisibility(position)) holder.time.visibility = View.VISIBLE
            else holder.time.visibility = View.INVISIBLE

        } else if(holder is FirstLeftViewHolder){
            holder.message.text = item.message

            holder.time.text = timeFormatter.format(parser.parse(item.time))
            holder.nickname.text = opponentNickname

            if(isTimeVisibility(position)) holder.time.visibility = View.VISIBLE
            else holder.time.visibility = View.INVISIBLE

            if(opponentImage != null) {
                val glideUrl = GlideUrl(
                    ServerInfo.SERVER_URL.url + ServerInfo.USER_IMAGE_URI.url + opponentImage
                )

                Glide.with(context).load(glideUrl)
                    .override(Target.SIZE_ORIGINAL)
                    .into(holder.image)
            }

        } else if(holder is CenterViewHolder){
            holder.message.text = item.message
        }
    }

    private fun isTimeVisibility(position: Int) : Boolean{
        if(position == items.size - 1) return true

        val item = items[position]
        val nextItem = items[position + 1]

        var currentTime = item.time
        var nextTime = nextItem.time

        if(item.time != null && nextItem.time != null) {
            val parser = SimpleDateFormat("a hh:mm:ss", Locale.KOREA)
            val timeFormatter = SimpleDateFormat("a hh:mm", Locale.KOREA)

            currentTime = timeFormatter.format(parser.parse(item.time))
            nextTime = timeFormatter.format(parser.parse(nextItem.time))
        }

        if (items.size == 2) return true
        else if(nextTime == currentTime && items[position + 1].viewType == item.viewType) return false
        else if(nextTime == currentTime && items[position + 1].viewType == ChatMessageViewType.LEFT && item.viewType == ChatMessageViewType.FIRST_LEFT) return false
        else return true
    }

    override fun getItemCount(): Int {
        return items.size
    }

}