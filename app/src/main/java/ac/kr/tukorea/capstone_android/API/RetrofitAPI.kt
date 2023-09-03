package ac.kr.tukorea.capstone_android.API

import ac.kr.tukorea.capstone_android.Interface.*
import ac.kr.tukorea.capstone_android.util.ServerInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
    private val BASE_URL = ServerInfo.SERVER_URL.url

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

    val postService : PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    val chatService : ChatService by lazy{
        retrofit.create(ChatService::class.java)
    }
}
