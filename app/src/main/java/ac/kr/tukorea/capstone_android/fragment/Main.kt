package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.DetailActivity
import ac.kr.tukorea.capstone_android.activity.MainActivity
import ac.kr.tukorea.capstone_android.activity.SearchResultActivity
import ac.kr.tukorea.capstone_android.adapter.ProductAdapter
import ac.kr.tukorea.capstone_android.databinding.FragmentMainBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import android.content.Intent
import android.util.Log
import android.widget.CheckBox
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class Main : Fragment() {
    lateinit var binding:FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val phone_tab = searchPhone()
        val tablet_tab = searchTablet()
        //val laptop_tab = searchLaptop()
        val fragmentManager = childFragmentManager.beginTransaction()
        val retrofitProduct = RetrofitProduct()
        var filter = ""
        var name = ""


        retrofitProduct.getProductList(null, null, 1L, binding, this)

        binding.apply {
            searchDeviceTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            filteringBoxPhone.visibility = View.VISIBLE
                            textTopItem.text = "인기 스마트폰 TOP 10"
                        }
                        1 -> {
                            filteringBoxPhone.visibility = View.GONE
                            textTopItem.text = "인기 태블릿 TOP 10"
                        }

                        2 -> {
                            filteringBoxPhone.visibility = View.GONE
                            textTopItem.text = "인기 노트북 TOP 10"
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            mainSearchView.isSubmitButtonEnabled = true
            mainSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    name = p0!!
                    retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)
                    mainSearchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    name = p0!!
                    retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)

                    return true
                }
            })

            /*
                 phone 필터링
             */

            val filterArrayList = arrayListOf(boxCompanySamsung, boxCompanyApple, boxCompanyEtc, boxPrice20,
                boxPrice40, boxPrice60, boxPrice80, boxPrice100, boxPrice120, boxPrice140, boxPrice160, boxPrice180, boxPrice200,
            boxSize5, boxSize6, boxSize7, boxSize8, boxProcessorSD8Gen2, boxProcessorSD8plusGen1, boxProcessorSD8Gen1, boxProcessorSD800, boxProcessorSD700, boxProcessorSD600,
            boxProcessorA16, boxProcessorA15, boxProcessorA14, boxProcessorA13, boxProcessorDimensity, boxProcessorExynos, boxProcessorHelio, boxProcessorMediaTek,
            boxRam2GB, boxRam3GB, boxRam4GB, boxRam6GB, boxRam8GB, boxRam12GB, boxRam16GB, boxMemory32GB, boxMemory64GB, boxMemory128GB, boxMemory256GB, boxMemory512GB, boxMemory1TB)


            for (cb in filterArrayList){
                cb.setOnClickListener {
                    if(cb.isChecked){
                        filter += (1001 + filterArrayList.indexOf(cb)).toString()
                        retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)
                    }else{
                        filter = filter.replace((1001 + filterArrayList.indexOf(cb)).toString(), "")
                        retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)
                    }
                }
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun replaceView(tab : Fragment){
        childFragmentManager.beginTransaction().replace(R.id.searchDeviceFrame, tab).commit()
    }
}
