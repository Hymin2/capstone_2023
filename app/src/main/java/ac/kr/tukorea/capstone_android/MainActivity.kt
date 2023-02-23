package ac.kr.tukorea.capstone_android

import ac.kr.tukorea.capstone_android.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_menu -> replaceFragment(Home())
                R.id.community -> replaceFragment(Community())
                R.id.information -> replaceFragment(Information())
                R.id.chat -> replaceFragment(Chat())
                R.id.my_menu -> replaceFragment(MyMenu())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }
}