package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.databinding.ActivityMainBinding
import ac.kr.tukorea.capstone_android.fragment.*
import ac.kr.tukorea.capstone_android.util.App
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? 		{
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if(App.prefs.getString("refresh_token","") == ""){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        super.onCreate(savedInstanceState)

        //getSupportActionBar()?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Main())

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener {

                when(it.itemId){
                    R.id.main -> replaceFragment(Main())
                    R.id.chat -> replaceFragment(Chat())
                    R.id.my_menu -> replaceFragment(MyMenu())

                    else -> {

                    }
                }
                true
            }

            floatingActionBtn.setOnClickListener {
                val intent = Intent(this.root.context,SalePostActivity::class.java)
                startActivity(intent)
            }


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