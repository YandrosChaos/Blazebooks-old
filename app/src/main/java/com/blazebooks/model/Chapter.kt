package com.blazebooks.model

import android.graphics.drawable.Drawable

data class Chapter(
    val number: Int,
    val title: String,
    val image: Drawable?,
    var readed : Boolean = false,
    val textOrConnection: Any
) {
}