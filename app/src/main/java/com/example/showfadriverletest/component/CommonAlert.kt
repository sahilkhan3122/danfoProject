package com.example.showfadriverletest.component

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.example.showfadriverletest.R


var isShown = false

fun Activity.showSuccessAlert(message: String = "") {
    if (isShown){
        //do something
        return
    }
    isShown = true

    SnacyAlert.create(this)
        .setText(message)
//        .setTitle(BaseActivity.language?.getLanguage(getString(R.string.success)).toString())
        .setBackgroundColorRes(R.color.colorGreen)
        .setDuration(1500)
        .showIcon(true)
        .setTitleVisibility()
        .setIcon(R.drawable.baseline_check_circle_outline_24)
        .show()

    Handler(Looper.myLooper()!!).postDelayed(
        Runnable {
            isShown = false
        },
        1500
    )
}



fun Activity.showFailAlert(message: String = "") {
    if (isShown){
        //do something
        return
    }
    isShown = true

    SnacyAlert.create(this)
        .setText(message)
//        .setTitle(BaseActivity.language?.getLanguage(getString(R.string.success)).toString())
        .setBackgroundColorRes(R.color.red_dark)
        .setDuration(1500)
        .showIcon(true)
        .setTitleVisibility()
        .setIcon(R.drawable.ic_baseline_required_24)
        .show()

    Handler(Looper.myLooper()!!).postDelayed(
        Runnable {
            isShown = false
        },
        5000
    )
}









