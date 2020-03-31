package com.blazebooks.model

import android.media.Image

data class Book(
    val title: String,
    val image: Image?,
    val author: String,
    val chapters: ArrayList<Chapter>,
    val premium: Boolean,
    val genre : String
) {
}