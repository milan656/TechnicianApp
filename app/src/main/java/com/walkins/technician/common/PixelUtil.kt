package com.walkins.technician.common

import android.content.Context
import android.util.DisplayMetrics


class PixelUtil {

    private fun PixelUtil() {}

    companion object {

        public fun dpToPx(context: Context, dp: Int): Int {
            return Math.round(dp * getPixelScaleFactor(context))
        }

        public fun pxToDp(context: Context, px: Int): Int {
            return Math.round(px / getPixelScaleFactor(context))
        }

        private fun getPixelScaleFactor(context: Context): Float {
            val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
            return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
        }
    }
}