package ac.kr.tukorea.capstone_android.API

import ac.kr.tukorea.capstone_android.Interface.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitAPI {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val okHttpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getInstance() : Retrofit{
        return retrofit
    }

    val registerService: RegisterService by lazy {
        retrofit.create(RegisterService::class.java)
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

    val refreshService : RefreshService by lazy {
        retrofit.create(RefreshService::class.java)
    }

    val userService : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}
