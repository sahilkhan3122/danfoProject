package com.example.showfadriverletest.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.showfadriverletest.R

class CustomEditTextView : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {}
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
        val array = context.obtainStyledAttributes(attrs,  R.styleable.CustomEditTextView)
        if (array != null) {
            val typefaceAssetPath = array.getString(R.styleable.CustomEditTextView_customTypeFaceEt)
            if (typefaceAssetPath != null) {
                var typeface: Typeface? = null
                if (mTypefaces!!.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces!![typefaceAssetPath]
                } else {
                    val assets = context.assets
                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath)
                    mTypefaces!![typefaceAssetPath] = typeface
                }
                setTypeface(typeface)
            } else {
                val face = Typeface.createFromAsset(
                    getContext().assets,
                    context.resources.getString(R.string.font_regular)
                )
                this.setTypeface(face)
            }
            prefixTag = array.getString(
                R.styleable.CustomEditTextView_prefixEt
            )
            array.recycle()
        }
    }

    var prefixTag: String? = null
    var mOriginalLeftPadding = -1f
    fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            if (prefixTag != null) {
                if (prefixTag != null && !prefixTag!!.isEmpty()) {
                    val widths = FloatArray(prefixTag!!.length)
                    paint.getTextWidths(prefixTag, widths)
                    var textWidth = 0f
                    for (w in widths) {
                        textWidth += w
                    }
                    mOriginalLeftPadding = compoundPaddingLeft.toFloat()
                    setPadding(
                        (textWidth + mOriginalLeftPadding).toInt(),
                        paddingRight, paddingTop,
                        paddingBottom
                    )
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (prefixTag != null) {
            if (prefixTag != null) {
                canvas.drawText(
                    prefixTag!!, mOriginalLeftPadding,
                    getLineBounds(0, null).toFloat(), paint
                )
            }
        }
    }

    companion object {
        private var mTypefaces: MutableMap<String, Typeface?>? = null
    }
}
