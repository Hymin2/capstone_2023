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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Chat.newInstance] factory method to
 * create an instance of this fragment.
 */
class Chat : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter : ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatArrayList : ArrayList<ChatList>

    lateinit var chatProfileImageId : Array<Int>
    lateinit var chatOpponentName : Array<String>
    lateinit var chatRecentMessage : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Chat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Chat().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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