package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName
import java.sql.Date

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
    val productDetails : List<ProductDetails>,
    @SerializedName("usedProductPrices")
    val usedProductPrices : List<UsedProductPrice>
)

data class ProductDetails(
    @SerializedName("detailName")
    val detailName : String,
    @SerializedName("detailContent")
    val detailContent : String
)

data class UsedProductPrice(
    @SerializedName("time")
    val time : String,
    @SerializedName("price")
    val price : Int
)

