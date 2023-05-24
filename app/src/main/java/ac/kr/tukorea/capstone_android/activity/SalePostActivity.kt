package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.DialogCategoryAdapter
import ac.kr.tukorea.capstone_android.adapter.MultiImageAdapter
import ac.kr.tukorea.capstone_android.databinding.ActivitySalePostBinding
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File
import java.text.DecimalFormat

class SalePostActivity : AppCompatActivity(), DialogCategoryAdapter.OnItemClickListener {

    lateinit var binding: ActivitySalePostBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogCategoryAdapter: DialogCategoryAdapter
    private lateinit var dialogRecyclerView: RecyclerView

    private val list = ArrayList<String>()

    private val imageList = ArrayList<File?>()
    val imageAdapter = MultiImageAdapter(imageList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.salePostToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.salePostToolBar.title = "판매글 작성"

        list.add("스마트폰")
        list.add("태블릿")
        list.add("노트북")

        binding.searchDialogButton.setOnClickListener {
            showBottomSheet()
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
                val file = imageList[position]
                val imagePath = file?.path
                Toast.makeText(this@SalePostActivity, "이미지 경로: $imagePath", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this@SalePostActivity)
                builder.setTitle("이미지 삭제")
                    .setMessage("이미지를 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            imageAdapter.clearImages()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            // 취소버튼 누를 시
                            // 대화상자 꺼짐
                        })
                builder.show()
            }
        })

        val layoutmanager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.salePostImageRecyclerView.apply {
            layoutManager = layoutmanager
            adapter = imageAdapter
        }
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        dialog = BottomSheetDialog(this, R.style.BottomDialogTheme)
        dialog.setContentView(dialogView)
        dialogRecyclerView = dialogView.findViewById(R.id.dialog_recyclerView)
        dialogCategoryAdapter = DialogCategoryAdapter(list, this)
        dialogRecyclerView.adapter = dialogCategoryAdapter
        val divderItemDecoration =
            DividerItemDecoration(dialogRecyclerView.context, LinearLayoutManager(this).orientation)
        dialogRecyclerView.addItemDecoration(divderItemDecoration)

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_btn_image -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(intent, 200)
                return super.onOptionsItemSelected(item)
            }
            R.id.action_btn_write -> {
                Toast.makeText(applicationContext, "글 작성 완료", Toast.LENGTH_LONG).show()
                uploadImages()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem: String = list[position]
        binding.salePostProductName.text = clickedItem

        dialog.dismiss()
    }

    private fun toLongFormat(price: Long): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    private fun uploadImages() {
        val images = imageAdapter.getImages()

        // 이미지 업로드 로직 추가
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    val file = File(getRealPathFromURI(imageUri))
                    imageAdapter.addImage(file)
                }
            } else if (data.data != null) {
                val imageUri: Uri = data.data!!
                val file = File(getRealPathFromURI(imageUri))
                imageAdapter.addImage(file)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor.close()
        return path
    }
}
