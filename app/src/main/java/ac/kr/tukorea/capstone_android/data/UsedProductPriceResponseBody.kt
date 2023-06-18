package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class UsedProductPriceResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : UsedProductPriceMessage
)

data class UsedProductPriceMessage(
    @SerializedName("productId")
    val productId : Long,
    @SerializedName("usedProductPrices")
    val usedProductPrices : List<UsedProductPrice>
)

data class UsedProductPrice(
    @SerializedName("time")
    val time : String,
    @SerializedName("price")
    val price : Int
)