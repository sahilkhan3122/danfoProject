package com.example.showfadriverletest.component

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast


class PopupDialog() {
    //PopupWindow display method
    @SuppressLint("ClickableViewAccessibility")
    fun showPopupWindow(view: View, messagee: String) {


        //Create a View object yourself through inflater
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View =
            inflater.inflate(com.example.showfadriverletest.R.layout.my_dialog_class, null)

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        //Initialize the elements of our window, install the handler
        val ok = popupView.findViewById<TextView>(com.example.showfadriverletest.R.id.Ok)
        val message = popupView.findViewById<TextView>(com.example.showfadriverletest.R.id.message)
        message.text = messagee
        ok.setOnClickListener { //As an example, display the message
            popupWindow.dismiss()
        }


        //Handler for clicking on the inactive zone of the window
      /*  popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }*/
    }
}