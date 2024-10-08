package com.example.showfadriverletest.util


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

fun View.visible() {
    visibility = VISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disabled() {
    isEnabled = false
}

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}

fun Context.getDeviceModel(): String {
    return Build.MODEL
}


fun Context.hideKeyboard() {
    this as Activity
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Context.hideKeyboard(view: View) {

    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun <T> Activity.startNewActivity(
    className: Class<T>,
    finish: Boolean = false,
    bundle: Bundle? = null,
) {
    val intent = Intent(this, className)
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
    if (finish) {
        finish()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}


fun BottomNavigationView.onItemSelected(function: (Int) -> Unit) {
    setOnItemSelectedListener { item ->
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).itemId == item.itemId) {
                function(i)
            }
        }
        return@setOnItemSelectedListener true
    }
}

fun BottomNavigationView.onItemReselected(function: (Int) -> Unit) {
    setOnItemReselectedListener { item ->
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).itemId == item.itemId) {
                function(i)
            }
        }
    }
}


fun Context.setGlideImage(
    imageUrl: String, imageView: ImageView,
    loaderProgress: ContentLoadingProgressBar?,
) {
    PrintLog.d("IMAGE_URL : $imageUrl")
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(applicationContext).load(imageUrl).skipMemoryCache(true)
            .apply(RequestOptions().error(R.drawable.cab_dummy_car))
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?, model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean,
                ): Boolean {
                    if (loaderProgress != null) loaderProgress.visibility = GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    if (loaderProgress != null) loaderProgress.visibility = GONE
                    return false
                }
            }).apply(RequestOptions.circleCropTransform()).into(imageView)
    }
}


fun Context.showMsgDialog(
    msg: String,
    positiveText: String? = "OK",
    listener: DialogInterface.OnClickListener? = null,
    negativeText: String? = "Cancel",
    negativeListener: DialogInterface.OnClickListener? = null,
    title: String? = "Location Disable",
    icon: Int? = null,
    isCancelable: Boolean = true,
) {

    val builder = AlertDialog.Builder(this, R.style.AlertDialogCustoms)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setCancelable(isCancelable)
    builder.setPositiveButton(positiveText) { dialog, which ->

        listener?.onClick(dialog, which)
    }
    if (negativeListener != null) {
        builder.setNegativeButton(negativeText) { dialog, which ->
            negativeListener.onClick(dialog, which)
        }
    }
    if (icon != null) {
        builder.setIcon(icon)
    }
    if (isCancelable) {
        builder.setOnDismissListener {
            // dialog?.dismiss()
        }
    }
    builder.create().show()


}

fun Context.getDrawable(id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}


@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s).isSuccess
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}



