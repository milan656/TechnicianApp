package com.walkins.technician.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by jkadvantage Team on 5/08/19.
 */


class BoldEditTexView : EditText {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    internal fun init(context: Context) {
        val font = Typeface.createFromAsset(getContext().assets, "fonts/Lato-Bold.ttf")
        typeface = font
    }

}