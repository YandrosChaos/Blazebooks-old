package com.blazebooks

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast


class WebInterface (c: Context?){
    var mContext: Context? = c

    @JavascriptInterface
    fun playSound(toast: String?) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun pauseSound(toast: String?) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}