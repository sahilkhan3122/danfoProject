package com.example.showfadriverletest.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.showfadriverletest.R
import com.example.showfadriverletest.ui.WelcomeActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Config {
    var context: Context? = null

    constructor() {}
    constructor(context: Context?) {
        this.context = context
    }

    fun signOut() {
        context?.let { SessionSave.clearUserSession(it) }

        //Intent intent = new Intent(context, LoginActivity.class);
        val intent = Intent(context, WelcomeActivity::class.java)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.putExtra("EXIT", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)
        (context as Activity?)!!.finishAffinity()
        (context as Activity?)!!.overridePendingTransition(
           R.anim.enter_from_left,
            R.anim.enter_from_right
        )
    }

    fun isNumberInt(unformatted: String): Int {
        if (isValidate(unformatted)) {
            try {
                return unformatted.toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return Constants.INVALID_ID
    }

    fun getDate(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        return DateFormat.format(DATE_FORMAT_SHORT, cal).toString()
    }

    fun getDate(milliSeconds: String, dateFormat: String?): String {
        var df = SimpleDateFormat(DATE_TIME_SERVER_1)
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        try {
            val date = df.parse(milliSeconds)
            return formatter.format(date)
        } catch (e: ParseException) {
            df = SimpleDateFormat(DATE_TIME_SERVER_2)
            var date: Date? = null
            try {
                date = df.parse(milliSeconds)
                return formatter.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            //e.printStackTrace();
        }
        return milliSeconds
    }

    fun getDateTime(milliSeconds: String, dateFormat: String?): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return try {
            val currebtTime = (milliSeconds + "000").toLong()
            val date = Date(currebtTime)
            formatter.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        const val DATE_FORMAT_SHORT = "yyyy/MM/dd HH:mm"
        const val DATE_TIME_FORMAT_SHORT = "yyyy-MM-dd HH:mm"
        const val DATE_TIME_SERVER_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DATE_TIME_SERVER_2 = "yyyy/MM/dd HH/mm"
        const val DATE_FORMAT_COMPARE = "yyyy-MM-dd hh:mm:ss"
        const val DATE_FORMAT_COMPARE1 = "yyyy-MM-dd HH:mm:ss"
        const val DATE_FORMAT_ = "yyyy-MM-dd, EEE"
        fun isValidate(duration: String?): Boolean {
            return duration != null && !duration.isEmpty() && !duration.equals(
                "NULL",
                ignoreCase = true
            )
        }

        fun hideKeyboard(context: Context) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = (context as Activity).currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(context)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun logger(key: String?, veryLongString: String) {
            val maxLogSize = 1000
            for (i in 0..veryLongString.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = Math.min(end, veryLongString.length)
                Log.v(key, veryLongString.substring(start, end))
            }
        }
    }
}
