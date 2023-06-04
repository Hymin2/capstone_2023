package ac.kr.tukorea.capstone_android.util

enum class ServerInfo(val url: String) {
    SERVER_URL("http://121.173.202.44:8080/"),
    PRODUCT_IMAGE_URI("api/v1/product/img?name="),
    USER_IMAGE_URI("api/v1/user/img?name="),
    POST_IMAGE_URI("api/v1/post/img?name=")
}