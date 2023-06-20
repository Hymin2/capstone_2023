package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.databinding.ActivityProfileEditBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitUser
import ac.kr.tukorea.capstone_android.util.App
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var selectedImageUri: Uri
    private val retrofitUser = RetrofitUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bitmap = intent.getParcelableExtra<Bitmap>("image")
        binding.profileEditProfileImage.setImageBitmap(bitmap)

        binding.profileEditBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.profileEditBtnChangePhoto.setOnClickListener {
            openGallery()
        }

        binding.profileEditBtnNickname.setOnClickListener {
            updateNickname()
        }
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun verifyStoragePermissions(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    lateinit var imageFile : File
    lateinit var requestFile :RequestBody
    lateinit var imagePart : MultipartBody.Part

    private fun openGallery() {
        verifyStoragePermissions(this)

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        resultLauncher.launch(intent)
    }

    private fun uploadImage(){
        retrofitUser.uploadProfileImage(App.prefs.getString("username", ""), imagePart, binding)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                selectedImageUri = uri

                imageFile = File(absolutelyPath(selectedImageUri, this))
                requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
                imagePart = MultipartBody.Part.createFormData("multipartFile", imageFile.name, requestFile)

                uploadImage()

                binding.profileEditProfileImage.setImageURI(uri)
            }
        }
    }

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private fun updateNickname(){
        var nickname = binding.profileEditEdtNickName.text.toString()

        if(nickname.isEmpty())
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT)
        else retrofitUser.updateNickname(App.prefs.getString("username", ""), nickname, binding)
    }
}

