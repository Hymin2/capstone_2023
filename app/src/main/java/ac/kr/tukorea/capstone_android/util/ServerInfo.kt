package ac.kr.tukorea.capstone_android.util

enum class ServerInfo(val url: String) {
    SERVER_URL("http://220.85.217.72:8080/"),
    STOMP_URL("ws://220.85.217.72:8080/stomp/chat"),
    PRODUCT_IMAGE_URI("api/v1/product/img?name="),
    USER_IMAGE_URI("api/v1/user/img?name="),
    POST_IMAGE_URI("api/v1/post/img?name=")
}