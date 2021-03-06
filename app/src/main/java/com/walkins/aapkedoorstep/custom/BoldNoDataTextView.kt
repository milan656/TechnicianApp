package com.walkins.aapkedoorstep.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView


class BoldNoDataTextView : TextView {
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
        val font = Typeface.createFromAsset(getContext().assets, "fonts/Lato-SemiboldItalic.ttf")
        typeface = font
    }

}
