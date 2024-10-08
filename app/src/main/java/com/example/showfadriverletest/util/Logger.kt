package com.example.showfadriverletest.util

import android.util.Log
import com.example.showfadriverletest.common.Constants
import java.util.*

object Logger {
    fun logD(message: String?) = Log.d(Constants.LOG_TAG, message!!)

    fun logD(message: String, flag: Int) = Log.d(Constants.LOG_TAG, "$flag-> $message")

    fun logI(message: String?) = Log.i(Constants.LOG_ERROR_TAG, Date().toString() + " " + message!!)

    fun logE(message: String?) = Log.e(Constants.LOG_ERROR_TAG, Date().toString() + " " + message!!)

    fun logE(e: Exception) = Log.e(Constants.LOG_ERROR_TAG, Date().toString() + " " + e.message, e)

    fun logE(message: String?, e: Exception) = Log.e(Constants.LOG_ERROR_TAG, "$message :-  ${e.message}", e)

}