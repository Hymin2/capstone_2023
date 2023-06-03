package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.DialogCategoryAdapter
import ac.kr.tukorea.capstone_android.adapter.DialogModelAdapter
import ac.kr.tukorea.capstone_android.adapter.MultiImageAdapter
import ac.kr.tukorea.capstone_android.databinding.ActivitySalePostBinding
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_sale_post.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.text.DecimalFormat

class SalePostActivity : AppCompatActivity(), DialogCategoryAdapter.OnItemClickListener, DialogModelAdapter.OnItemClickListener {

    lateinit var binding: ActivitySalePostBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogCategoryAdapter: DialogCategoryAdapter
    private lateinit var dialogModelAdapter: DialogModelAdapter
    private lateinit var dialogRecyclerView: RecyclerView

    private val categoryList = ArrayList<String>()
    private val modelList = ArrayList<String>()

    private val imageList = ArrayList<Uri?>()

    val imageAdapter = MultiImageAdapter(imageList, this)

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")  // 실제 API 엔드포인트 URL로 대체해야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // API 서비스 인스턴스 생성
        apiService = retrofit.create(ApiService::class.java)

        setSupportActionBar(binding.salePostToolBar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.salePostToolBar.title = "판매글 작성"

        categoryList.add("스마트폰")
        categoryList.add("태블릿")
        categoryList.add("노트북")

        modelList.add("1")
        modelList.add("2")
        modelList.add("3")
        modelList.add("4")
        modelList.add("5")
        modelList.add("6")

        binding.searchCategoryDialogButton.setOnClickListener {
            showBottomSheet1()
        }

        binding.searchProductDialogButton.setOnClickListener {
            showBottomSheet2()
        }

        binding.salePostPrice.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->

            val price: String = binding.salePostPrice.text.toString()

            if (hasFocus) {
                if (price.isNotEmpty()) {
                    binding.salePostPrice.setText(price.replace(",", "").replace("원", ""))
                }
            } else {
                if (price.isNotEmpty()) {
                    val formatPrice: String = toLongFormat(price.toLong())
                    binding.salePostPrice.setText(formatPrice)
                }
            }
        }

        imageAdapter.setItemClickListener(object : MultiImageAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                val builder = AlertDialog.Builder(this@SalePostActivity)
                builder.setTitle("이미지 삭제")
                    .setMessage("이미지를 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { dialog, which ->
                        imageList.removeAt(position)
                        imageAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("취소", null)
                builder.show()
            }
        })

        binding.salePostImageRecyclerView.adapter = imageAdapter
        binding.salePostImageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.salePostImageRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.HORIZONTAL
            )
        )
        binding.salePostImageRecyclerView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_toolbar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_btn_image -> {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(galleryIntent, 1)
                return true
            }
            R.id.action_btn_write -> {
                val price = binding.salePostPrice.text.toString()
                val priceToInt = price.replace("[^\\d]".toRegex(), "").toIntOrNull()
                val title = binding.salePostTitle.text.toString()
                val category = binding.salePostCategoryName.text.toString()
                val model = binding.salePostProductName.text.toString()
                val content = binding.salePostContent.text.toString()


                val imageParts = ArrayList<MultipartBody.Part>()
                for (imageUri in imageList) {
                    imageUri?.let { uri ->
                        val file = File(uri.path)
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val imagePart =
                            MultipartBody.Part.createFormData("images", file.name, requestBody)
                        imageParts.add(imagePart)
                    }
                }

                Log.e("post_title","$title")
                Log.e("post_category","$category")
                Log.e("post_model","$model")
                Log.e("post_price","$priceToInt")
                Log.e("post_content","$content")
                //Log.e("post_image","${imageParts[0]}")

                if (priceToInt != null && title != null && !(category.equals("")) &&
                    !(model.equals("")) && content != null && imageParts[0] != null) {

                    val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val categoryBody =
                        category.toRequestBody("text/plain".toMediaTypeOrNull())
                    val modelBody = model.toRequestBody("text/plain".toMediaTypeOrNull())
                    val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
                    val contentBody =
                        content.toRequestBody("text/plain".toMediaTypeOrNull())

                    // Retrofit을 사용하여 업로드 작업 수행
                    val call = apiService.uploadImage(imageParts, titleBody, categoryBody, modelBody,priceBody,contentBody)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            // 업로드 성공 처리
                            Toast.makeText(applicationContext, "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 업로드 실패 처리
                            Toast.makeText(applicationContext, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                        }
                    })

                    Toast.makeText(applicationContext, "글 작성 완료", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "모든 내용을 입력하세요", Toast.LENGTH_LONG).show()
                }
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomSheet1() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        dialog = BottomSheetDialog(this, R.style.BottomDialogTheme)
        dialog.setContentView(dialogView)
        dialogRecyclerView = dialogView.findViewById(R.id.dialog_recyclerView)
        dialogCategoryAdapter = DialogCategoryAdapter(categoryList, this)
        dialogRecyclerView.adapter = dialogCategoryAdapter
        // 리사이클러뷰 구분선
        val divderItemDecoration =
            DividerItemDecoration(dialogRecyclerView.context, LinearLayoutManager(this).orientation)
        dialogRecyclerView.addItemDecoration(divderItemDecoration)

        dialog.show()
    }

    private fun showBottomSheet2() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        dialog = BottomSheetDialog(this, R.style.BottomDialogTheme)
        dialog.setContentView(dialogView)
        dialogRecyclerView = dialogView.findViewById(R.id.dialog_recyclerView)
        dialogModelAdapter = DialogModelAdapter(modelList, this)
        dialogRecyclerView.adapter = dialogModelAdapter
        // 리사이클러뷰 구분선
        val divderItemDecoration =
            DividerItemDecoration(dialogRecyclerView.context, LinearLayoutManager(this).orientation)
        dialogRecyclerView.addItemDecoration(divderItemDecoration)

        dialog.show()
    }

    override fun onItemClick1(position: Int) {
        binding.salePostCategoryName.text = categoryList[position]
        dialog.dismiss()
    }

    override fun onItemClick2(position: Int) {
        binding.salePostProductName.text = modelList[position]
        dialog.dismiss()
    }

    private fun toLongFormat(price: Long): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 1) {
            imageList.clear()

            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if (count > 5) {
                    Toast.makeText(applicationContext, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageList.add(imageUri)
                }
            } else {
                data?.data?.let { uri ->
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        imageList.add(imageUri)
                    }
                }
            }
            imageAdapter.notifyDataSetChanged()
        }
    }
    interface ApiService {
        @Multipart
        @POST("upload") // 이거 수정좀
        fun uploadImage(
            @Part image: List<MultipartBody.Part>,
            @Part("title") title: RequestBody,
            @Part("category") category: RequestBody,
            @Part("model") model: RequestBody,
            @Part("content") content: RequestBody,
            @Part("price") price: RequestBody
        ): Call<ResponseBody>
    }
}
