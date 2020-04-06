package com.blazebooks.model

import android.media.Image

data class Book(
    val title: String,
    val image: Image?,
    val author: String,
    val synopsis : String,
    val chapters: ArrayList<Chapter>,
    val premium: Boolean,
    val genre : String,
    val textOrConnection: Any
) {

    /**
     * Return one specific chapter from the book
     *
     * @param position
     * @return Chapter
     */
    fun getChapter(position : Int) : Chapter {
        return chapters[position]
    }
}