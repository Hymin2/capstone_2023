package ac.kr.tukorea.capstone_android.data

data class RegisterRequestBody(
    val username : String?,
    val password : String?,
    val email : String?,
    val phoneNumber : String?,
    val nickname : String?
)
