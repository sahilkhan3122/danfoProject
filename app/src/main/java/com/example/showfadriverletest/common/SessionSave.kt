package com.example.showfadriverletest.common

import android.app.Activity
import android.content.Context

object SessionSave {
    //Store data's into sharedPreference
    fun saveUserSession(key: String?, value: String?, context: Context) {
        val editor =
            context.getSharedPreferences(Common.PREFRENCE_USER, Activity.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Get Data's from SharedPreferences
    fun getUserSession(key: String?, context: Context): String? {
        val prefs = context.getSharedPreferences(Common.PREFRENCE_USER, Activity.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    /*fun getInitData(context: Context): InitData? {
        val strBookingDetails = getUserSession(Common.SESSION_INIT_INFO, context)
        return if (strBookingDetails != null && !strBookingDetails.trim { it <= ' ' }
                .equals("", ignoreCase = true)) {
            Gson().fromJson(strBookingDetails, InitData::class.java)
        } else null
    }*/

 /*   fun getEstimatedFare(context: Context): CarData? {
        val vehicalList = getUserSession(Common.SESSION_ESTIMATED_FARE, context)
        return if (vehicalList != null && !vehicalList.trim { it <= ' ' }
                .equals("", ignoreCase = true)) {
            Gson().fromJson(vehicalList, CarData::class.java)
        } else null
    }*/

    fun clearUserSession(context: Context) {
        val editor =
            context.getSharedPreferences(Common.PREFRENCE_USER, Activity.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }
}
