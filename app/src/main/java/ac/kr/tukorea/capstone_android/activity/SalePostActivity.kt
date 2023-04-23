package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.DialogAdapter
import ac.kr.tukorea.capstone_android.adapter.SearchResultAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SalePostActivity : AppCompatActivity(),DialogAdapter.OnItemClickListener {

    private lateinit var salePostToolbar: Toolbar
    private lateinit var search_button: ImageButton
    private lateinit var dialog: BottomSheetDialog
    private lateinit var dialogAdapter: DialogAdapter
    private lateinit var dialogRecyclerView: RecyclerView

    private lateinit var productName : TextView
    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_post)

        salePostToolbar = findViewById(R.id.salePost_toolBar)

        setSupportActionBar(salePostToolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다. false로 해야 custom한 툴바의 이름이 화면에 보이게 됩니다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        salePostToolbar.title = "판매글 작성"

        productName = findViewById(R.id.salePost_productName)

        search_button = findViewById(R.id.search_dialog_button)
        for (i in 1..20) {
            list.add("item $i")
        }
        search_button.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        dialog = BottomSheetDialog(this, R.style.BottomDialogTheme)
        dialog.setContentView(dialogView)
        dialogRecyclerView = dialogView.findViewById(R.id.dialog_recyclerView)
        dialogAdapter = DialogAdapter(list, this)
        dialogRecyclerView.adapter = dialogAdapter

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
                Toast.makeText(applicationContext, "이미지 업로드", Toast.LENGTH_LONG).show()
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

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"Item $position clicked",Toast.LENGTH_SHORT)
        val clickedItem : String = list[position]
        productName.text = clickedItem
        dialogAdapter.notifyItemChanged(position)
        dialog.dismiss()
    }
}