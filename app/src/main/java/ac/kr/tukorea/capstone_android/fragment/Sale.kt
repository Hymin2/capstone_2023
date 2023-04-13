package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.adapter.SaleAdapter
import ac.kr.tukorea.capstone_android.data.Products
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.SaleActivity
import ac.kr.tukorea.capstone_android.activity.WriteActivity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList

class Home : Fragment() {
    private lateinit var adapter : SaleAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var productsArrayList : ArrayList<Products>
    private lateinit var writeFAB : FloatingActionButton

    lateinit var imageId : Array<Int>
    lateinit var title : Array<String>
    lateinit var price : Array<Int>
    lateinit var content : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.sale_recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = SaleAdapter(productsArrayList)
        recyclerView.adapter = adapter
        writeFAB = view.findViewById(R.id.write_fab)

        writeFAB.setOnClickListener{
            val intent = Intent(getActivity(), WriteActivity::class.java)
            startActivity(intent)
        }

        getUserData()
    }


    private fun dataInitialize() {

        productsArrayList = arrayListOf<Products>()

        imageId = arrayOf(
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
        )

        title = arrayOf(
            "갤럭시 S23 판매합니다",
            "아이폰 14프로 팜",
        )

        price = arrayOf(
            1000000,
            1100000,
        )

        content = arrayOf(
            "512기가 버전이며\n" +
                    "S23 입니다\n" +
                    "그린색상 입니다\n" +
                    "선물용으로도 매우 좋을 듯 싶습니다\n" +
                    "비싼케이스와 필름도 같이 드릴게요",
            "급하게 팔아요2",
        )
    }

    private fun getUserData() {
        for (i in imageId.indices) {
            val products = Products(imageId[i], title[i], price[i])
            productsArrayList.add(products)
        }

        var adapter = SaleAdapter(productsArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : SaleAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(context,SaleActivity::class.java)
                intent.putExtra("title",productsArrayList[position].saleTitle)
                intent.putExtra("imageId",productsArrayList[position].saleImage)
                intent.putExtra("price",productsArrayList[position].salePrice)
                intent.putExtra("content",content[position])

                startActivity(intent)
            }
        })
    }
}