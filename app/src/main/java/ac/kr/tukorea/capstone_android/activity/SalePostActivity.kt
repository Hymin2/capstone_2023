package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.API.RetrofitAPI.productService
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.DialogCategoryAdapter
import ac.kr.tukorea.capstone_android.adapter.DialogModelAdapter
import ac.kr.tukorea.capstone_android.adapter.MultiImageAdapter
import ac.kr.tukorea.capstone_android.data.ProductList
import ac.kr.tukorea.capstone_android.data.ProductListResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivitySalePostBinding
import ac.kr.tukorea.capstone_android.util.App
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.DecimalFormat
import java.util.stream.Collectors

class SalePostActivity : AppCompatActivity(), DialogCategoryAdapter.OnItemClickListener, DialogModelAdapter.OnItemClickListener {

    lateinit var binding: ActivitySalePostBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogCategoryAdapter: DialogCategoryAdapter
    private lateinit var dialogModelAdapter: DialogModelAdapter
    private lateinit var dialogRecyclerView: RecyclerView

    private val categoryList = ArrayList<String>()
    private var modelList = ArrayList<String>()
    private var productIdList = ArrayList<Long>()
    private var avgPrice = ArrayList<Int>()
    private val imageList = ArrayList<Uri?>()

    val imageAdapter = MultiImageAdapter(imageList, this)
    private val postService = RetrofitAPI.postService
    var productId : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalePostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        categoryList.add("스마트폰")
        categoryList.add("태블릿")
        categoryList.add("노트북")

        verifyStoragePermissions(this)

        binding.searchCategoryDialogButton.setOnClickListener {
            showBottomSheet1()
        }

        // 인텐트에서 데이터 받아오기
        val code = intent.getIntExtra("code",0)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val price = intent.getStringExtra("price")
        val productName = intent.getStringExtra("productName")
        val postImagesList = intent.getParcelableArrayListExtra<Uri>("postImages")

        if (code == 1) {
            binding.apply {
                salePostTitle.setText(title)
                salePostContent.setText(content)
                salePostPrice.setText(price)
                salePostProductName.setText(productName)

                if (postImagesList != null) {
                    for (uri in postImagesList) {
                        Log.e("이미지", "$uri")
                        imageList.add(uri)
                    }
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.searchProductDialogButton.setOnClickListener {
            if(categoryList.contains(binding.salePostCategoryName.text)) {
                showBottomSheet2()
            } else{
                Toast.makeText(this, "카테고리를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
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

        binding.salePostBtnBack.setOnClickListener{
            onBackPressed()
        }

        binding.salePostBtnImage.setOnClickListener{
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(galleryIntent, 1)
        }

        binding.salePostBtnWrite.setOnClickListener{
            val price = binding.salePostPrice.text.toString()
            val priceToInt = price.replace("[^\\d]".toRegex(), "").toIntOrNull()
            val title = binding.salePostTitle.text.toString()
            val category = binding.salePostCategoryName.text.toString()
            val model = binding.salePostProductName.text.toString()
            val content = binding.salePostContent.text.toString()

            val imageParts = ArrayList<MultipartBody.Part>()
            for (imageUri in imageList) {
                imageUri?.let { uri ->
                    val file = File(absolutelyPath(uri, this))
                    val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                    val imagePart = MultipartBody.Part.createFormData("multipartFiles", file.name, requestBody)
                    imageParts.add(imagePart)
                }
            }

            if (priceToInt != null && title != null && !(category.equals("")) &&
                !(model.equals("")) && content != null && imageParts.isNotEmpty()) {

                val username = App.prefs.getString("username", " ")

                Log.d("판매글 등록", productId.toString())
                // Retrofit을 사용하여 업로드 작업 수행
                postService.registerPost(App.prefs.getString("access_token", ""), productId, username.toRequestBody("text/plain".toMediaTypeOrNull()), title.toRequestBody("text/plain".toMediaTypeOrNull()), content.toRequestBody("text/plain".toMediaTypeOrNull()), priceToInt, imageParts).enqueue(object : Callback<ac.kr.tukorea.capstone_android.data.ResponseBody>{
                    override fun onResponse(
                        call: Call<ac.kr.tukorea.capstone_android.data.ResponseBody>,
                        response: Response<ac.kr.tukorea.capstone_android.data.ResponseBody>,
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@SalePostActivity, "판매글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else{
                            Toast.makeText(this@SalePostActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ac.kr.tukorea.capstone_android.data.ResponseBody>,
                        t: Throwable,
                    ) {
                        Log.d("판매글 등록", t.toString())
                        Toast.makeText(this@SalePostActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                })
            } else{
                Toast.makeText(this@SalePostActivity, "모든 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

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

        var category : Long = 0
        when(binding.salePostCategoryName.text){
            "스마트폰" -> category = 1
            "태블릿" -> category = 2
            "노트북" -> category = 3
        }
        productService.getProductList(App.prefs.getString("access_token", ""), null, null, category).enqueue(object: Callback<ProductListResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ProductListResponseBody>,
                response: Response<ProductListResponseBody>,
            ) {
                if(response.isSuccessful){
                    modelList = response.body()!!.message.productList.stream().map(ProductList::productName).collect(Collectors.toList()) as ArrayList<String>
                    productIdList = response.body()!!.message.productList.stream().map(ProductList::id).collect(Collectors.toList()) as ArrayList<Long>
                    avgPrice = response.body()!!.message.productList.stream().map(ProductList::averagePrice).collect(Collectors.toList()) as ArrayList<Int>
                } else{

                }
            }

            override fun onFailure(call: Call<ProductListResponseBody>, t: Throwable) {
                Toast.makeText(this@SalePostActivity, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

        })
        dialog.dismiss()
    }

    override fun onItemClick2(position: Int) {
        binding.salePostProductName.text = modelList[position]
        productId = productIdList[position]
        binding.salePostPrice.hint = "평균 시세: " + toLongFormat(avgPrice[position] + 0L)

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

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
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
}
