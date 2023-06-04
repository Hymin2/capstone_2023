package ac.kr.tukorea.capstone_android.data

data class FeedList(
    var feedProfileImage : Int,
    var feedUserNickName : String,
    var feedProductImage : List<Int>,
    var feedProductModel : String,
    var feedProductPrice : Int,
    var feedProductMain : String,
    var onSale : Boolean
)
