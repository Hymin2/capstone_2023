package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.ChatActivity
import ac.kr.tukorea.capstone_android.adapter.ChatListAdapter
import ac.kr.tukorea.capstone_android.data.ChatRoom
import ac.kr.tukorea.capstone_android.data.ChatRoomResponseBody
import ac.kr.tukorea.capstone_android.databinding.FragmentChatBinding
import ac.kr.tukorea.capstone_android.room.database.MyDataBase
import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.stream.Collectors


class Chat : Fragment() {
    lateinit var binding : FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            chatListRadioGroup.setOnCheckedChangeListener { p0, rb ->
                when (rb) {
                    R.id.chatList_radio_buy -> {
                        chatListRadioBuy.setTypeface(null, Typeface.BOLD)
                        chatListRadioSell.setTypeface(null, Typeface.NORMAL)
                        insertBuyChatRoom()
                        getBuyChatRoom()
                    }

                    R.id.chatList_radio_sell -> {
                        chatListRadioBuy.setTypeface(null, Typeface.NORMAL)
                        chatListRadioSell.setTypeface(null, Typeface.BOLD)
                        insertSellChatRoom()
                        getSellChatRoom()
                    }
                }
            }

            chatListRadioGroup.clearCheck()
            chatListRadioGroup.check(R.id.chatList_radio_buy)

        }
    }

    private fun insertBuyChatRoom(){
        val chatService = RetrofitAPI.chatService
        val db = MyDataBase.getInstance(binding.root.context)

        chatService.getBuyChatRoom(App.prefs.getString("access_token", ""), App.prefs.getString("username", "")).enqueue(object: Callback<ChatRoomResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ChatRoomResponseBody>,
                response: Response<ChatRoomResponseBody>,
            ) {
                if(response.isSuccessful){
                    var chatList = response.body()!!.message
                    var chatRoomEntities = chatList.stream().map { i -> ChatRoomEntity(i.roomId, i.postId, i.opponentUsername, i.opponentNickname, i.opponentUserImage, "buy", i.recentMessage, i.recentMessageTime) }
                        .collect(Collectors.toList())

                    CoroutineScope(Dispatchers.IO).launch{
                        db!!.chatRoomDao().insertAll(chatRoomEntities)
                    }
                }
            }

            override fun onFailure(call: Call<ChatRoomResponseBody>, t: Throwable) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getBuyChatRoom(){
        val db = MyDataBase.getInstance(binding.root.context)

        CoroutineScope(Dispatchers.IO).launch {
            var rooms = db!!.chatRoomDao().getRoomsByMyUserType("buy")

            withContext(Dispatchers.Main) {
                setChatRoomRecyclerView(rooms)
            }
        }
    }

    private fun insertSellChatRoom(){
        val chatService = RetrofitAPI.chatService
        val db = MyDataBase.getInstance(binding.root.context)

        chatService.getSellChatRoom(App.prefs.getString("access_token", ""), App.prefs.getString("username", "")).enqueue(object: Callback<ChatRoomResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ChatRoomResponseBody>,
                response: Response<ChatRoomResponseBody>,
            ) {
                if(response.isSuccessful){
                    var chatList = response.body()!!.message

                    var chatRoomEntities = chatList.stream().map { i -> ChatRoomEntity(i.roomId, i.postId, i.opponentUsername, i.opponentNickname, i.opponentUserImage, "sell", i.recentMessage, i.recentMessageTime) }
                        .collect(Collectors.toList())

                    CoroutineScope(Dispatchers.IO).launch{
                        db!!.chatRoomDao().insertAll(chatRoomEntities)
                    }
                }
            }
            override fun onFailure(call: Call<ChatRoomResponseBody>, t: Throwable) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getSellChatRoom(){
        val db = MyDataBase.getInstance(binding.root.context)

        CoroutineScope(Dispatchers.IO).launch {
            val rooms = db!!.chatRoomDao().getRoomsByMyUserType("sell")

            withContext(Dispatchers.Main) {
                setChatRoomRecyclerView(rooms)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setChatRoomRecyclerView(rooms : List<ChatRoomEntity>){
        var chatList = rooms.stream()
            .map { i -> ChatRoom(i.roomId, i.postId, i.opponentUsername, i.opponentNickname, i.opponentUserImage, i.recentMessage, i.recentTime) }
            .collect(Collectors.toList()) as ArrayList<ChatRoom>

        var adapter = ChatListAdapter(chatList, binding.root.context)

        binding.chatListRecyclerView.adapter = adapter
        binding.chatListRecyclerView.adapter!!.notifyDataSetChanged()

        adapter.setOnItemClickListener(object : ChatListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ChatActivity::class.java)

                intent.putExtra("postId", chatList[position].postId)
                intent.putExtra("username", chatList[position].opponentUsername)
                intent.putExtra("nickname", chatList[position].opponentNickname)
                intent.putExtra("userImage", chatList[position].opponentUserImage)
                intent.putExtra("userType", "Buyer")

                startActivity(intent)
            }

        })
    }
}