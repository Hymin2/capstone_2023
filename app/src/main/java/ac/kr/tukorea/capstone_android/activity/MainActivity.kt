package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityMainBinding
import ac.kr.tukorea.capstone_android.fragment.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getSupportActionBar()?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Main())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.main -> replaceFragment(Main())
                R.id.chat -> replaceFragment(Chat())
                R.id.my_menu -> replaceFragment(MyMenu())

                else -> {

                }
            }
            true
        }
    }

    interface onBackPressedListener {
        fun onBackPressed()
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is onBackPressedListener){
                (fragment as onBackPressedListener).onBackPressed()
                return
            }
        }
    }
}