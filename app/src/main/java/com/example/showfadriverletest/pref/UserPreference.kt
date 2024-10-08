package com.example.showfadriverletest.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import javax.inject.Inject

open class UserPreference @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val context: Context,
) {



 fun saveFirstName(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getFirstName(context: Context?): String {
        return if (context != null) {
            val preference = sharedPreferences
            preference.getString("code", "").toString()
        } else {
            ""
        }
    }
}