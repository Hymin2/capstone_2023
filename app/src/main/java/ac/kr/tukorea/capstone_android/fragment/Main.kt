package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.MainActivity
import android.content.Context
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import com.google.android.material.tabs.TabLayout

class Main : Fragment(), MainActivity.onBackPressedListener {

    lateinit var main_searchview : SearchView

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

        fragmentManager.add(R.id.searchDeviceFrame, phone_tab).commit()

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
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
        if (!main_searchview.isIconified) {
            main_searchview.isIconified = true
        } else {
            onBackPressed()
        }
    }

    private fun replaceView(tab : Fragment){
        childFragmentManager.beginTransaction().replace(R.id.searchDeviceFrame, tab).commit()
    }
}