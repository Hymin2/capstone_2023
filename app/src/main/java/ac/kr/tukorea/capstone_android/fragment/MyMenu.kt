package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.activity.FeedListActivity
import ac.kr.tukorea.capstone_android.activity.OthersProfileActivity
import ac.kr.tukorea.capstone_android.activity.SaleDetailActivity
import ac.kr.tukorea.capstone_android.databinding.FragmentMyMenuBinding
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MyMenu : Fragment() {

    lateinit var binding : FragmentMyMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myShopTestButton.setOnClickListener{
            val intent = Intent(context,OthersProfileActivity::class.java)
            startActivity(intent)
        }
        binding.myShopTestButton2.setOnClickListener{
            val intent = Intent(context, FeedListActivity::class.java)
            startActivity(intent)
        }

        binding.myShopTestButton3.setOnClickListener{
            val intent = Intent(context, SaleDetailActivity::class.java)
            startActivity(intent)
        }
    }

}