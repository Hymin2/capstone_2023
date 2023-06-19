package ac.kr.tukorea.capstone_android.data

import java.sql.Time

data class Chatting(
    var opponentId : String,
    var myId : String,
    var time : Time,
    var oppoentProfileImage : Int,
    var message : String,
)
