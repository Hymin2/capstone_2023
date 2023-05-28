package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.adapter.MyProfileTabAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Like : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        recyclerView = view.findViewById(R.id.like_recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 그리드 리사이클러뷰 설정
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = gridLayoutManager

        // 그리드 리사이클러뷰 어댑터 설정
        val adapter = MyProfileTabAdapter()
        recyclerView.adapter = adapter

        // 아이템 데이터 설정
        val imageResources = listOf(
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.testimage1,
            R.drawable.testimage2,
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.testimage2,
            R.drawable.iphone14pro,
        )
        adapter.setImageResources(imageResources)
    }
}