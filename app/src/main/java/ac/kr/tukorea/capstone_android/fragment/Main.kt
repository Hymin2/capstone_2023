package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.MainActivity
import ac.kr.tukorea.capstone_android.activity.SearchResultActivity
import android.content.Context
import android.content.Intent
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import com.google.android.material.tabs.TabLayout

class Main : Fragment(), MainActivity.onBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val phone_tab = searchPhone()
        val tablet_tab = searchTablet()
        //val laptop_tab = searchLaptop()
        val tabs = view.findViewById<TabLayout>(R.id.searchDeviceTaps)
        val fragmentManager = childFragmentManager.beginTransaction()

        val searchView = view.findViewById<SearchView>(R.id.mainSearchView)

        fragmentManager.add(R.id.searchDeviceFrame, phone_tab).commit()

        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                val intent = Intent(context,SearchResultActivity::class.java)
                startActivity(intent)

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> replaceView(phone_tab)
                    1 -> replaceView(tablet_tab)
                    //2 -> replaceView(laptop_tab)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed() {

    }

    private fun replaceView(tab : Fragment){
        childFragmentManager.beginTransaction().replace(R.id.searchDeviceFrame, tab).commit()
    }
}