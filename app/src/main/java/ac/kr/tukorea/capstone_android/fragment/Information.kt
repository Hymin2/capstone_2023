package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.FragmentInformationBinding
import android.os.Bundle
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
        val binding = FragmentInformationBinding.inflate(inflater, container, false)
        val info_tab = PhoneInfoFragment()
        val marketPrice_tab = PhoneMarketPriceFragment()
        val compare_tab = PhoneCompareFragment()
        val tabs = binding.phoneTaps
        val fragmentManager = childFragmentManager.beginTransaction()

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> fragmentManager
                        .replace(R.id.phone_frame, info_tab).commit()
                    1 -> fragmentManager
                        .replace(R.id.phone_frame, marketPrice_tab).commit()
                    2 -> fragmentManager
                        .replace(R.id.phone_frame, compare_tab).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return inflater.inflate(R.layout.fragment_information, container, false)
    }
}