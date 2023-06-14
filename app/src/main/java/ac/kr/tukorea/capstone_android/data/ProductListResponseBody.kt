package ac.kr.tukorea.capstone_android.data

import com.google.gson.annotations.SerializedName

data class ProductListResponseBody(
    @SerializedName("result")
    val result : String,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message : MessageProductList
)

data class MessageProductList(
    @SerializedName("category_id")
    val categoryId : Long,
    @SerializedName("category_name")
    val categoryName : String,
    @SerializedName("productList")
    val productList : List<ProductList>
)

data class ProductList(
    @SerializedName("id")
    val id : Long,
    @SerializedName("productName")
    val productName : String,
    @SerializedName("modelName")
    val modelName : String,
    @SerializedName("companyName")
    val companyName : String,
    @SerializedName("images")
    val images : List<String>,
    @SerializedName("averagePrice")
    val averagePrice : Int,
    @SerializedName("transactionNum")
    val transactionNum : Int
)