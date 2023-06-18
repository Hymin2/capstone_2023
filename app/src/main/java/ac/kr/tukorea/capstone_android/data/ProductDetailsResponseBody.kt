package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : MessageProductDetails
)

data class MessageProductDetails(
    @SerializedName("id")
    val id : Long,
    @SerializedName("productDetails")
    val productDetails : List<ProductDetails>
)

data class ProductDetails(
    @SerializedName("detailName")
    val detailName : String,
    @SerializedName("detailContent")
    val detailContent : String
)
