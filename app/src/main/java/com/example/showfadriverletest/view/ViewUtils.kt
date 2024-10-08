package com.example.showfadriverletest.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.showfadriverletest.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun View.showSnackBar(message: String) {

    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(context.getString(R.string.dismiss)) {}
    snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.black))
    snackBar.show()
}

fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getDateFormat(time: Date?): String? {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    return sdf.format(time)
}

fun hideSoftKeyboard(activity: Activity, view: View? = null) {
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        // TODO: handle exception
        e.printStackTrace()
    }
}


fun <T> Activity.startNewActivityWithClear(
    className: Class<T>,
    finish: Boolean = false,
    bundle: Bundle? = null
) {
    val intent = Intent(this, className)

    bundle?.let {
        intent.putExtras(it)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
}


fun Context.dialogShow(
    title: String? = null,
    msg: String = "",
    positiveText: String? = null,
    negativeText: String? = null,
    positiveClick: (() -> Unit)? = null,
    negativeClick: (() -> Unit)? = null,
    dialogView: View? = null,
): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(this)

    builder.setView(dialogView)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(
            positiveText
        ) { _, _ -> positiveClick?.invoke() }
        .setNegativeButton(negativeText)
        { _, _ -> negativeClick?.invoke() }.background = ColorDrawable(Color.TRANSPARENT)

    return builder
}

