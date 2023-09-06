package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.FragmentMainBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
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

        retrofitProduct.getProductList(null, null, 1L, binding, this@Main)
        retrofitProduct.getTopProductList(1L, binding, this@Main)

        binding.apply {
            searchDeviceTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            filteringBoxTablet.visibility = View.GONE
                            filteringBoxLaptop.visibility = View.GONE
                            filteringBoxPhone.visibility = View.VISIBLE
                            textTopItem.text = "인기 스마트폰 TOP 10"
                            textProductList.text = "스마트폰 리스트"

                            retrofitProduct.getProductList(null, null, 1L, binding, this@Main)
                            retrofitProduct.getTopProductList(1L, binding, this@Main)
                        }
                        1 -> {
                            filteringBoxPhone.visibility = View.GONE
                            filteringBoxLaptop.visibility = View.GONE
                            filteringBoxTablet.visibility = View.VISIBLE
                            textTopItem.text = "인기 태블릿 TOP 10"
                            textProductList.text = "태블릿 리스트"

                            retrofitProduct.getProductList(null, null, 2L, binding, this@Main)
                            retrofitProduct.getTopProductList(2L, binding, this@Main)
                        }

                        2 -> {
                            filteringBoxTablet.visibility = View.GONE
                            filteringBoxPhone.visibility = View.GONE
                            filteringBoxLaptop.visibility = View.VISIBLE

                            textTopItem.text = "인기 노트북 TOP 10"
                            textProductList.text = "노트북 리스트"

                            retrofitProduct.getProductList(null, null, 3L, binding, this@Main)
                            retrofitProduct.getTopProductList(3L, binding, this@Main)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            mainSearchView.isSubmitButtonEnabled = true
            mainSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                val selectedTab : Long = 1

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    name = p0!!
                    retrofitProduct.getProductList(name, filter, (searchDeviceTabs.selectedTabPosition + selectedTab), binding, this@Main)
                    mainSearchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    name = p0!!
                    retrofitProduct.getProductList(name, filter, (searchDeviceTabs.selectedTabPosition + selectedTab), binding, this@Main)

                    return true
                }
            })

            /*
                 phone 필터링
             */

            val filterArrayListPhone = arrayListOf(boxCompanySamsung, boxCompanyApple, boxCompanyEtc, boxPrice20,
                boxPrice40, boxPrice60, boxPrice80, boxPrice100, boxPrice120, boxPrice140, boxPrice160, boxPrice180, boxPrice200,
            boxSize5, boxSize6, boxSize7, boxSize8, boxProcessorSD8Gen2, boxProcessorSD8plusGen1, boxProcessorSD8Gen1, boxProcessorSD800, boxProcessorSD700, boxProcessorSD600,
            boxProcessorA16, boxProcessorA15, boxProcessorA14, boxProcessorA13, boxProcessorDimensity, boxProcessorExynos, boxProcessorHelio, boxProcessorMediaTek,
            boxRam2GB, boxRam3GB, boxRam4GB, boxRam6GB, boxRam8GB, boxRam12GB, boxRam16GB, boxMemory32GB, boxMemory64GB, boxMemory128GB, boxMemory256GB, boxMemory512GB, boxMemory1TB)

            for (cb in filterArrayListPhone){
                cb.setOnClickListener {
                    if(cb.isChecked){
                        filter += "P" + (1001 + filterArrayListPhone.indexOf(cb)).toString()
                        retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)
                    }else{
                        filter = filter.replace(("P" + (1001 + filterArrayListPhone.indexOf(cb))).toString(), "")
                        retrofitProduct.getProductList(name, filter, 1L, binding, this@Main)
                    }
                }
            }

            val filterArrayListTablet = arrayListOf(boxCompanySamsungTablet, boxCompanyAppleTablet, boxCompanyMicrosoftTablet, boxCompanyLenovoTablet, boxCompanyEtcTablet,
                boxPrice20Tablet, boxPrice40Tablet, boxPrice60Tablet, boxPrice80Tablet, boxPrice100Tablet, boxPrice120Tablet, boxPrice140Tablet, boxPrice160Tablet, boxPrice180Tablet, boxPrice200Tablet,
                boxSize7Tablet, boxSize8Tablet, boxSize9Tablet, boxSize10Tablet, boxSize11Tablet, boxSize12Tablet, boxSize13Tablet, boxSize14Tablet, boxSize15Tablet, boxSize16Tablet, boxSize17Tablet, boxSize18Tablet, boxSize19Tablet,
                boxProcessorM2Tablet, boxProcessorM1Tablet, boxProcessorA15Tablet, boxProcessorA14Tablet, boxProcessorA13Tablet, boxProcessorSD8Gen1Tablet, boxProcessorSD800Tablet, boxProcessorSD700Tablet, boxProcessorSD600, boxProcessorExynos9Tablet, boxProcessorExynos7Tablet,
                boxRam2GBTablet, boxRam3GBTablet, boxRam4GBTablet, boxRam6GBTablet, boxRam8GBTablet, boxRam12GBTablet, boxRam16GBTablet,
                boxMemory32GBTablet, boxMemory64GBTablet, boxMemory128GBTablet, boxMemory256GBTablet, boxMemory512GBTablet, boxMemory1TBTablet)

            for (cb in filterArrayListTablet){
                cb.setOnClickListener {
                    if(cb.isChecked){
                        filter += "T" + (2001 + filterArrayListTablet.indexOf(cb)).toString()
                        retrofitProduct.getProductList(name, filter, 2L, binding, this@Main)
                    }else{
                        filter = filter.replace(("T" + (2001 + filterArrayListTablet.indexOf(cb))).toString(), "")
                        retrofitProduct.getProductList(name, filter, 2L, binding, this@Main)
                    }
                }
            }

            val filterArrayListLaptop = arrayListOf(boxLaptopCompanySamsung, boxLaptopCompanyApple, boxLaptopCompanyLG, boxLaptopCompanyLenovo, boxLaptopCompanyMsi, boxLaptopCompanyAsus, boxLaptopCompanyEtc,
                boxLaptopPrice50, boxLaptopPrice100, boxLaptopPrice150, boxLaptopPrice200, boxLaptopPrice250, boxLaptopPrice300, boxLaptopPrice300Exceed,
                boxLaptopSize12, boxLaptopSize14, boxLaptopSize16, boxLaptopSize18,
                boxLaptopProcessorM2, boxLaptopProcessorM1, boxLaptopProcessorI9, boxLaptopProcessorI7, boxLaptopProcessorI5, boxLaptopProcessorI3, boxLaptopProcessorRyzen9, boxLaptopProcessorRyzen7, boxLaptopProcessorRyzen5, boxLaptopProcessorRyzen3, boxLaptopProcessorEtc,
                boxLaptopRam4GB, boxLaptopRam8GB, boxLaptopRam16GB, boxLaptopRam32GB, boxLaptopRam64GB,
                boxLaptopGraphicM2, boxLaptopGraphicM1, boxLaptopGraphic4090, boxLaptopGraphic4080, boxLaptopGraphic4070, boxLaptopGraphic4060, boxLaptopGraphic4050, boxLaptopGraphic3080, boxLaptopGraphic3070, boxLaptopGraphic3060, boxLaptopGraphic3050,
                boxLaptopGraphic2080, boxLaptopGraphic2070, boxLaptopGraphic2060, boxLaptopGraphic2050)

            for (cb in filterArrayListLaptop){
                cb.setOnClickListener {
                    if(cb.isChecked){
                        filter += "L" + (3001 + filterArrayListLaptop.indexOf(cb)).toString()
                        retrofitProduct.getProductList(name, filter, 3L, binding, this@Main)
                    }else{
                        filter = filter.replace(("L" + (3001 + filterArrayListLaptop.indexOf(cb))).toString(), "")
                        retrofitProduct.getProductList(name, filter, 3L, binding, this@Main)
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
