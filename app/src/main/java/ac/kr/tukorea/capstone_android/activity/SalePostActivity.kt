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
import retrofit2.http.Multipart
import java.text.DecimalFormat


class SalePostActivity : AppCompatActivity(),DialogCategoryAdapter.OnItemClickListener {

    lateinit var binding : ActivitySalePostBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogCategoryAdapter: DialogCategoryAdapter
    private lateinit var dialogRecyclerView: RecyclerView

    private val list = ArrayList<String>()

    private val imageList = ArrayList<Uri?>()

    val imageAdapter = MultiImageAdapter(imageList,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.salePostToolBar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.salePostToolBar.title = "판매글 작성"

        list.add("스마트폰")
        list.add("태블릿")
        list.add("노트북")

        binding.searchDialogButton.setOnClickListener {
            showBottomSheet()
        }

        // 선택한 이미지 삭제
        // 이미지 선택 시 대화상자 출력
        // 대화상자 확인 선택 시 이미지 삭제
/*        binding.salePostImageView1.setOnClickListener {
            if (binding.salePostImageView1.drawable != null) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("이미지 삭제")
                    .setMessage("이미지를 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            // 확인 누를 시 이미지 삭제
                            binding.salePostImageView1.setImageResource(0)
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            // 취소버튼 누를 시
                            // 대화상자 꺼짐
                        })
                builder.show()
            }
        }*/

        binding.salePostPrice.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->

            val price : String = binding.salePostPrice.text.toString()

            if (hasFocus) {
                if(price.isNotEmpty()) {
                    binding.salePostPrice.setText(price.replace(",","").replace("원", ""))
                }
            } else {
                if(price.isNotEmpty()) {
                    val formatPrice : String = toLongFormat(price.toLong())
                    binding.salePostPrice.setText(formatPrice)
                }
            }
        }
        imageAdapter.setItemClickListener(object : MultiImageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                val builder = AlertDialog.Builder(this@SalePostActivity)
                builder.setTitle("이미지 삭제")
                    .setMessage("이미지를 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            // 확인 누를 시 이미지 삭제
                            imageList[position] = null
                            val filteredList = imageList.filterNotNull()
                            imageList.clear()
                            imageList.addAll(filteredList)

                            binding.salePostImageRecyclerView.adapter?.notifyDataSetChanged()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { imageDialog, id ->
                            // 취소버튼 누를 시
                            // 대화상자 꺼짐
                        })
                builder.show()
            }
        })
        val layoutmanager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
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
        // 리사이클러뷰 구분선
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
                // 뒤로가기 버튼 눌렀을 때
                finish()
                return true
            }
            R.id.action_btn_image -> {
                // 이미지 버튼 눌렀을 때
                // Toast.makeText(applicationContext, "이미지 업로드", Toast.LENGTH_LONG).show()
                /* pickImage()
                 return super.onOptionsItemSelected(item)*/

                var intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT

                startActivityForResult(intent, 200)
                return super.onOptionsItemSelected(item)
            }
            R.id.action_btn_write -> {
                // 확인 버튼 눌렀을 때
                Toast.makeText(applicationContext, "글 작성 완료", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,1)
        //startActivityForResult(intent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 200) {
            imageList.clear()

            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count>5) {
                    Toast.makeText(applicationContext, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                    return
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageList.add(imageUri)
                }
            } else { // 한 장만 선택
                data?.data?.let { uri ->
                    val imageUri : Uri? = data?.data
                    if(imageUri != null) {
                        imageList.add(imageUri)
                    }
                }
            }
            imageAdapter.notifyDataSetChanged()
        }
    }
/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if(requestCode == 1) {
                val uri = data?.data
                binding.salePostImageView1.setImageURI(uri)
            }
        }
    }*/

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"Item $position clicked",Toast.LENGTH_SHORT)
        val clickedItem : String = list[position]
        binding.salePostProductName.text = clickedItem

        dialogCategoryAdapter.notifyItemChanged(position)
        dialog.dismiss()
    }

    private fun toLongFormat(num : Long): String {
        val df = DecimalFormat("###,###")

        return df.format(num) + "원"
    }
}