package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.ChatActivity
import ac.kr.tukorea.capstone_android.activity.SaleActivity
import ac.kr.tukorea.capstone_android.adapter.ChatAdapter
import ac.kr.tukorea.capstone_android.data.ChatList
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Chat : Fragment() {
    private lateinit var adapter : ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatArrayList : ArrayList<ChatList>

    lateinit var chatProfileImageId : Array<Int>
    lateinit var chatOpponentName : Array<String>
    lateinit var chatRecentMessage : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.chat_recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, 1))


        adapter = ChatAdapter(chatArrayList)
        recyclerView.adapter = adapter

        getUserData()
    }

    private fun dataInitialize() {

        chatArrayList = arrayListOf<ChatList>()

        chatProfileImageId = arrayOf(
            R.drawable.profile_image,
            R.drawable.profile_image,
            R.drawable.profile_image,
            )

        chatOpponentName = arrayOf(
            "한국공학대학교",
            "TU KOREA",
            "한국공대"
            )

        chatRecentMessage = arrayOf(
            "네 알겠습니다",
            "안녕하세요~",
            "판매글 보고 문의 드립니다."
            )
    }

    private fun getUserData() {
        for (i in chatProfileImageId.indices) {
            val chat = ChatList(
                chatProfileImageId[i],
                chatOpponentName[i],
                chatRecentMessage[i]
            )
            chatArrayList.add(chat)
        }
        var adapter = ChatAdapter(chatArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ChatAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("chatOpponentName",chatArrayList[position].opponentName)
                startActivity(intent)
            }
        })
    }
}