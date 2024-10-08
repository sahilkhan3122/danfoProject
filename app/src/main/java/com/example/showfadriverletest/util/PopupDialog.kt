package com.example.showfadriverletest.util

import android.app.Dialog
import android.content.Context
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.showfadriverletest.R

object PopupDialog {

    fun showDialogOfLocationPolicy(context: Context, message: String) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_common_error)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.dialog_ok_textview) as TextView
        tv_Message = dialog.findViewById<View>(R.id.dialog_message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView
        iv_close.setOnClickListener { dialog.dismiss() }
        iv_close.visibility = View.GONE
        tv_Ok.setOnClickListener {
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        tv_Title.visibility = View.GONE
        dialog.show()
    }

    fun collectData(context: Context, message: String, onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_common_error)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.dialog_ok_textview) as TextView
        tv_Message = dialog.findViewById<View>(R.id.dialog_message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView
        iv_close.setOnClickListener { dialog.dismiss() }
        iv_close.visibility = View.GONE
        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        tv_Title.visibility = View.GONE
        dialog.show()
    }



    fun notificationClear(context: Context, message: String,onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_common_error)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.dialog_ok_textview) as TextView
        tv_Message = dialog.findViewById<View>(R.id.dialog_message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView
        iv_close.setOnClickListener { dialog.dismiss() }
        iv_close.visibility = View.GONE
        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        tv_Title.visibility = View.GONE
        dialog.show()
    }




    fun sessionExpire(context: Context, message: String,onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val cancle: TextView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        cancle.setOnClickListener { dialog.dismiss() }
        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        dialog.show()
    }




    fun logout(context: Context, message: String, onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val cancle: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        /*tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView*/
        cancle.setOnClickListener { dialog.dismiss() }

        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        dialog.show()
    }

    fun socketDailog(context: Context, message: String) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val cancle: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.tvTitle) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        tv_Title.visibility = View.VISIBLE
        cancle.visibility = View.GONE
        tv_Ok.setOnClickListener {
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        dialog.show()
    }
    fun socketAleartDailog(context: Context, message: String) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val cancle: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.tvTitle) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        /*tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView*/
        tv_Title.visibility = View.VISIBLE
        tv_Title.setText(context.getString(R.string.alert_message))
        cancle.visibility = View.GONE
        tv_Ok.setOnClickListener {
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        dialog.show()
    }
    

    fun editVehicleDetailPopup(context: Context, message: String, onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val cancle: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        /*tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView*/
        cancle.visibility = View.GONE
        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text =
            message
        dialog.show()
    }


    fun initPopup(context: Context, message: String, onOkClick: () -> Unit) {
        val tv_Ok: TextView
        val tv_Message: TextView
        val tv_Title: TextView
        val cancle: TextView
        val ll_Ok: LinearLayout
        val iv_close: ImageView
        val dialog: Dialog = Dialog(context, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.window!!.attributes = lp
        tv_Ok = dialog.findViewById<View>(R.id.ok) as TextView
        tv_Message = dialog.findViewById<View>(R.id.message) as TextView
        tv_Title = dialog.findViewById<View>(R.id.tvTitle) as TextView
        cancle = dialog.findViewById<View>(R.id.cancel) as TextView
        /*tv_Title = dialog.findViewById<View>(R.id.dialog_title) as TextView
        ll_Ok = dialog.findViewById<View>(R.id.dialog_ok_layout) as LinearLayout
        iv_close = dialog.findViewById<View>(R.id.dialog_close) as ImageView*/
        tv_Title.visibility = View.VISIBLE
        cancle.visibility = View.GONE
        tv_Ok.setOnClickListener {
            onOkClick()
            dialog.dismiss()
        }
        tv_Message.textSize = 17.0f
        tv_Message.text = message
        dialog.show()
    }
}