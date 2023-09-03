package ac.kr.tukorea.capstone_android.util

enum class ServerInfo(val url: String) {
    SERVER_URL("http://10.0.2.2:8080/"),
    STOMP_URL("ws://10.0.2.2:8080/stomp/chat"),
    PRODUCT_IMAGE_URI("api/v1/product/img?name="),
    USER_IMAGE_URI("api/v1/user/img?name="),
    POST_IMAGE_URI("api/v1/post/img?name=")
}