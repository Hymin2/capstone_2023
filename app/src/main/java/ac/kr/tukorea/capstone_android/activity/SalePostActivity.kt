package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.DialogAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SalePostActivity : AppCompatActivity() {

    private lateinit var search_button : ImageButton
    private lateinit var dialog : BottomSheetDialog
    private lateinit var dialogAdapter: DialogAdapter
    private lateinit var dialogRecyclerView: RecyclerView
    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_post)

        search_button = findViewById(R.id.search_dialog_button)
        for (i in 1..10){
            list.add("item $i")
        }
        search_button.setOnClickListener{
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        dialog = BottomSheetDialog(this,R.style.BottomDialogTheme)
        dialog.setContentView(dialogView)
        dialogRecyclerView = dialogView.findViewById(R.id.dialog_recyclerView)
        dialogAdapter = DialogAdapter(list)
        dialogRecyclerView.adapter = dialogAdapter

        // 리사이클러뷰 구분선
        val divderItemDecoration = DividerItemDecoration(dialogRecyclerView.context, LinearLayoutManager(this).orientation)
        dialogRecyclerView.addItemDecoration(divderItemDecoration)

        dialog.show()
    }

}