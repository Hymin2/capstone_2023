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
import ac.kr.tukorea.capstone_android.databinding.FragmentMainBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import android.content.Intent
import android.widget.SearchView
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

        fragmentManager.add(R.id.searchDeviceFrame, phone_tab).commit()

        retrofitProduct.getProductList(null, null, 1L, binding)

        binding.lookmoreBtn1.setOnClickListener{
            val intent = Intent(context,DetailActivity::class.java)
            startActivity(intent)
        }
        binding.apply {
            searchDeviceTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> replaceView(phone_tab)
                        1 -> replaceView(tablet_tab)
                        //2 -> replaceView(laptop_tab)
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            mainSearchView.isSubmitButtonEnabled = true
            mainSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    retrofitProduct.getProductList(p0!!, null, 1L, binding)

                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    retrofitProduct.getProductList(p0!!, null, 1L, binding)

                    return true
                }
            })


        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun replaceView(tab : Fragment){
        childFragmentManager.beginTransaction().replace(R.id.searchDeviceFrame, tab).commit()
    }
}