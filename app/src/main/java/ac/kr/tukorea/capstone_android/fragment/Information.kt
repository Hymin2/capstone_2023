package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.FragmentInformationBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class Information : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val info_tab = PhoneInfoFragment()
        val marketPrice_tab = PhoneMarketPriceFragment()
        val compare_tab = PhoneCompareFragment()
        val tabs = view.findViewById<TabLayout>(R.id.phone_taps)
        val fragmentManager = childFragmentManager.beginTransaction()

        fragmentManager.add(R.id.phone_frame, info_tab).commit()

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> replaceView(info_tab)
                    1 -> replaceView(marketPrice_tab)
                    2 -> replaceView(compare_tab)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun replaceView(tab : Fragment){
        childFragmentManager.beginTransaction().replace(R.id.phone_frame, tab).commit()
    }
}