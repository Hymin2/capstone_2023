package ac.kr.tukorea.capstone_android.Interface

import ac.kr.tukorea.capstone_android.data.ProductDetailsResponseBody
import ac.kr.tukorea.capstone_android.data.ProductListResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("api/v1/product")
    fun getProductList(@Header("Authorization") token : String,
                       @Query("name") name : String?,
                       @Query("page") page : Int?,
                       @Query("filter") filter : String?,
                       @Query("category") category : Long) : Call<ProductListResponseBody>

    @GET("api/v1/product/{id}")
    fun getProductDetails(@Header("Authorization") token: String,
                          @Path("id") productId : Long) : Call<ProductDetailsResponseBody>
}