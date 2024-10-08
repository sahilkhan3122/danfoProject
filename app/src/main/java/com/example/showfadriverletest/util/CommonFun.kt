package com.example.showfadriverletest.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.showfadriverletest.R

object CommonFun {

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getColor(color: Int, context: Context) = ContextCompat.getColor(context, color)


     fun showSetting(activity: Activity, s: String): Boolean {
        val showRationale =
            activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!showRationale) {
            val deniedDialog = androidx.appcompat.app.AlertDialog.Builder(activity)

            deniedDialog.setTitle(R.string.permission_denied)
            deniedDialog.setMessage(s)
            deniedDialog.setPositiveButton(
                R.string.settings
            ) { _, _ ->
                showPermissionDeniedDialog(activity)
            }
            deniedDialog.setCancelable(false)
            deniedDialog.show()
        }
        return false
    }

     fun showPermissionDeniedDialog(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", activity.packageName, null
        )
        intent.data = uri
        activity.startActivity(intent)
    }


    fun checkIsConnectionReset(code: Int): Boolean {
        return code == 502
    }

     fun hideStatusBar(activity: Activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
    }

     fun statusBar(activity: Activity) {
        val window: Window = activity.window
        window.statusBarColor = ContextCompat.getColor(
            activity,
            com.example.showfadriverletest.R.color.theme_color
        )
    }
}