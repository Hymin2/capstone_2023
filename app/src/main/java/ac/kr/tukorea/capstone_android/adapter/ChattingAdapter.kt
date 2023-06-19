package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.Chatting
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ChattingAdapter(private val context: Context, private val chattingList : ArrayList<Chatting>) :
    RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_OPPONENT = 0
    private val MESSAGE_TYPE_MINE = 1

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val opponentProfileImage : CircleImageView = view.findViewById(R.id.chatting_opponent_profileImage)
        val message : TextView = view.findViewById(R.id.message_textView)
        val time : TextView = view.findViewById(R.id.messageTime_textView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingAdapter.ViewHolder {
        if (viewType == MESSAGE_TYPE_MINE){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_chat,parent,false)
            return ViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_opponent_chat,parent,false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChattingAdapter.ViewHolder, position: Int) {
        val chatting = chattingList[position]
        holder.message.text = chatting.message
        holder.time.text = chatting.time.toString()
    }

    override fun getItemCount(): Int {
        return chattingList.size
    }

/*
  userId 받아와야 함
    override fun getItemViewType(position: Int): Int {
        if (chattingList[position].opponentId == ) {    // 메시지 보낸 사람의 아이디를 구분
            return MESSAGE_TYPE_MINE
        } else {
            return MESSAGE_TYPE_OPPONENT
        }
    }
*/
}