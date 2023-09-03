package ac.kr.tukorea.capstone_android.data

data class ChatCreateRequestBody(
    var salePostId : Long,
    var sellerUsername : String,
    var buyerUsername : String
)
