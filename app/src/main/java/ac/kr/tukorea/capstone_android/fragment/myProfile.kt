package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.FollowerActivity
import ac.kr.tukorea.capstone_android.activity.FollowingActivity
import ac.kr.tukorea.capstone_android.activity.ProfileEditActivity
import ac.kr.tukorea.capstone_android.databinding.FragmentMyMenuBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMyProfileBinding
import android.content.Intent

class myProfile : Fragment() {

    private lateinit var binding : FragmentMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.followerLinear.setOnClickListener{
            val intent = Intent(context,FollowerActivity::class.java)
            startActivity(intent)
        }

        binding.followingLinaer.setOnClickListener{
            val intent = Intent(context,FollowingActivity::class.java)
            startActivity(intent)
        }

        binding.editProfile.setOnClickListener{
            val intent = Intent(context,ProfileEditActivity::class.java)
            startActivity(intent)
        }
    }

}