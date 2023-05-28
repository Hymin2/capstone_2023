package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityProfileEditBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val changePhotoButton: Button = binding.profileEditBtnChangePhoto
        val profileImage: ImageView = binding.profileEditProfileImage

        changePhotoButton.setOnClickListener {
            openGallery()
        }

        val saveButton: Button = binding.profileEditBtnSave
        saveButton.setOnClickListener {
            uploadProfileImage()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.profileEditProfileImage.setImageURI(uri)
            }
        }
    }

    private fun uploadProfileImage() {
        val imageFile = File(selectedImageUri.path)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("profile_image", imageFile.name, requestFile)

        val userName = binding.profileEditEdtUserName.text.toString()
        val userNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userName)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // 서버의 기본 URL 설정
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.uploadProfileImage(imagePart, userNameBody)

        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                // 성공적으로 업로드된 경우의 처리
                if (response.isSuccessful) {
                    // 업로드 성공

                    // 이전 액티비티로 돌아가기
                    setResult(RESULT_OK)
                    finish()
                } else {
                    // 업로드 실패
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                // 업로드 실패한 경우의 처리
            }
        })
    }

    companion object {
        private const val BASE_URL = "http://121.143.64.12:8080/"
    }
}

interface ApiService {
    // 이미지와 userName 업로드 API
    @Multipart
    @POST("api/v1/user/{username}")
    fun uploadProfileImage(
        @Part image: MultipartBody.Part,
        @Part("userName") userName: RequestBody
    ): Call<UploadResponse>
}

data class UploadResponse(val success: Boolean, val message: String)
