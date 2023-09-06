package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.ChatActivity
import ac.kr.tukorea.capstone_android.adapter.ChatListAdapter
import ac.kr.tukorea.capstone_android.data.ChatRoomsResponseBody
import ac.kr.tukorea.capstone_android.databinding.FragmentChatBinding
import ac.kr.tukorea.capstone_android.room.database.MyDataBase
import ac.kr.tukorea.capstone_android.room.entity.ChatRoomEntity
import ac.kr.tukorea.capstone_android.util.App
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.stream.Collectors
import kotlin.streams.toList


class Chat : Fragment() {
    lateinit var binding : FragmentChatBinding
    var receiverRegistered = false
    var receiver : BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        if(binding.chatListRadioBuy.isChecked) {
            getBuyChatRoom()
        } else {
            getSellChatRoom()
        }

        startRegisterReceiver();
    }

    override fun onPause() {
        super.onPause()
        pauseRegisterReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishRegisterReceiver()
    }

    override fun onStart() {
        super.onStart()
        startRegisterReceiver()
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
                    }

                    R.id.chatList_radio_sell -> {
                        chatListRadioBuy.setTypeface(null, Typeface.NORMAL)
                        chatListRadioSell.setTypeface(null, Typeface.BOLD)
                        insertSellChatRoom()
                    }
                }
            }

            chatListRadioGroup.clearCheck()
            chatListRadioGroup.check(R.id.chatList_radio_buy)

        }
    }

    private fun startRegisterReceiver(){
        if(!receiverRegistered){
            if(receiver == null){
                receiver = object: BroadcastReceiver(){
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onReceive(p0: Context?, p1: Intent?) {
                        if(binding.chatListRadioBuy.isChecked) {
                            getBuyChatRoom()
                        } else {
                            getSellChatRoom()
                        }
                    }
                }
            }
            val filter = IntentFilter("com.package.notification")
            requireActivity().registerReceiver(receiver, filter)

            receiverRegistered = true
        }
    }

    private fun finishRegisterReceiver() {
        if (receiverRegistered) {
            requireActivity().unregisterReceiver(receiver)
            receiver = null
            receiverRegistered = false
        }
    }

    private fun pauseRegisterReceiver() {
        if (receiverRegistered) {
            receiverRegistered = false
        }
    }


    private fun insertBuyChatRoom(){
        val chatService = RetrofitAPI.chatService
        val db = MyDataBase.getInstance(binding.root.context)

        chatService.getBuyChatRoom(App.prefs.getString("access_token", ""), App.prefs.getString("username", "")).enqueue(object: Callback<ChatRoomsResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ChatRoomsResponseBody>,
                response: Response<ChatRoomsResponseBody>,
            ) {
                if(response.isSuccessful){
                    var chatList = response.body()!!.message
                    CoroutineScope(Dispatchers.IO).launch{
                        var chatRoomEntitiesId : List<Long> = db!!.chatRoomDao().getRoomsByMyUserType("buy").stream().map(ChatRoomEntity::roomId).collect(Collectors.toList())

                        var map = chatList.stream().collect(Collectors.groupingBy { i -> i.roomId in chatRoomEntitiesId })

                        if(map[true] != null) {
                            map[true]!!.stream().filter { i -> i.roomId in chatRoomEntitiesId }
                                .forEach { i ->
                                    db!!.chatRoomDao()
                                        .updateRoomOpponentUserByRoomId(i.opponentNickname,
                                            i.opponentUserImage,
                                            i.roomId)
                                }
                        }

                        if(map[false] != null) {
                            db!!.chatRoomDao().insertAll(map[false]!!
                                .stream()
                                .map { i -> ChatRoomEntity(i.roomId, i.postId, i.opponentUsername, i.opponentNickname, i.opponentUserImage, "buy", null, null) }
                                .toList())
                        }

                        withContext(Dispatchers.Main){
                            getBuyChatRoom()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ChatRoomsResponseBody>, t: Throwable) {

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

        chatService.getSellChatRoom(App.prefs.getString("access_token", ""), App.prefs.getString("username", "")).enqueue(object: Callback<ChatRoomsResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ChatRoomsResponseBody>,
                response: Response<ChatRoomsResponseBody>,
            ) {
                if(response.isSuccessful){
                    var chatList = response.body()!!.message

                    CoroutineScope(Dispatchers.IO).launch{
                        var chatRoomEntitiesId : List<Long> = db!!.chatRoomDao().getRoomsByMyUserType("sell").stream().map(ChatRoomEntity::roomId).collect(Collectors.toList())

                        var map = chatList.stream().collect(Collectors.groupingBy { i -> i.roomId in chatRoomEntitiesId })

                        if(map[true] != null) {
                            map[true]!!.stream().filter { i -> i.roomId in chatRoomEntitiesId }
                                .forEach { i ->
                                    db!!.chatRoomDao()
                                        .updateRoomOpponentUserByRoomId(i.opponentNickname,
                                            i.opponentUserImage,
                                            i.roomId)
                                }
                        }

                        if(map[false] != null) {
                            db!!.chatRoomDao().insertAll(map[false]!!
                                .stream()
                                .map { i -> ChatRoomEntity(i.roomId, i.postId, i.opponentUsername, i.opponentNickname, i.opponentUserImage, "sell", null, null) }
                                .toList())
                        }

                        withContext(Dispatchers.Main){
                            getSellChatRoom()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ChatRoomsResponseBody>, t: Throwable) {

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
        var adapter = ChatListAdapter(rooms as java.util.ArrayList<ChatRoomEntity>, binding.root.context)

        binding.chatListRecyclerView.adapter = adapter
        binding.chatListRecyclerView.adapter!!.notifyDataSetChanged()

        adapter.setOnItemClickListener(object : ChatListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ChatActivity::class.java)

                intent.putExtra("postId", rooms[position].postId)
                intent.putExtra("username", rooms[position].opponentUsername)
                intent.putExtra("nickname", rooms[position].opponentNickname)
                intent.putExtra("userImage", rooms[position].opponentUserImage)

                if(rooms[position].myUserType == "buy") intent.putExtra("userType", "Buyer")
                else intent.putExtra("userType", "Seller")

                startActivity(intent)
            }

        })
    }
}