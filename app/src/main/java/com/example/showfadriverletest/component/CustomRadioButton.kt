package com.example.showfadriverletest.component

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.example.showfadriverletest.R

class CustomRadioButton : AppCompatRadioButton {
    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setTypeFace(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setTypeFace(context, attrs)
    }

    private fun setTypeFace(context: Context, attrs: AttributeSet?) {
        if (mTypefaces == null) {
            mTypefaces = HashMap()
        }

        // prevent exception in Android Studio / ADT interface builder
        if (this.isInEditMode) {
            return
        }
        val array = context.obtainStyledAttributes(attrs,R.styleable.CustomRadioButton)
        val typefaceAssetPath = array.getString(R.styleable.CustomRadioButton_typeFaceRb)
        if (typefaceAssetPath != null) {
            val typeface: Typeface?
            if (mTypefaces!!.containsKey(typefaceAssetPath)) {
                typeface = mTypefaces!![typefaceAssetPath]
            } else {
                val assets = context.assets
                typeface = Typeface.createFromAsset(assets, typefaceAssetPath)
                mTypefaces!![typefaceAssetPath] = typeface
            }
            setTypeface(typeface)
        } else {
            val face =
                Typeface.createFromAsset(getContext().assets, "fonts/josefinsans_regular.ttf")
            this.typeface = face
        }
        array.recycle()
    }

    companion object {
        private var mTypefaces: MutableMap<String, Typeface?>? = null
    }
}
