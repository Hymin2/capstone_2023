package ac.kr.tukorea.capstone_android.util

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context){
    private val PREFS_FILENAME = "prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String):String{
        return prefs.getString(key,defValue).toString()
    }

    fun setString(key: String, value: String){
        prefs.edit().putString(key, value).apply()
    }
}