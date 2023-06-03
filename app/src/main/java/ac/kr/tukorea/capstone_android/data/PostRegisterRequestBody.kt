package ac.kr.tukorea.capstone_android.data

data class PostRegisterRequestBody (
    val productId : Long,
    val username : String,
    val postTitle : String,
    val postContent : String,
    val price : Int
    )