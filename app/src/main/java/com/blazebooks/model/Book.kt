package com.blazebooks.model

import com.google.gson.Gson

data class Book(
    var title: String? = " ",
    var image: String? = " ",
    var author: String? = " ",
    var synopsis: String? = " ",
    var chapters: ArrayList<Chapter> = arrayListOf(
        Chapter(1, "Title of the Chapter", null, true, "Unknown"),
        Chapter(2, "Blah blah blah", null, true, "Unknown"),
        Chapter(3, "Suck or die", null, false, "Unknown"),
        Chapter(4, "Sssssspañah", null, false, "Unknown"),
        Chapter(5, "Coronachapter", null, false, "Unknown")
    ),
    var premium: Boolean = false,
    var genre: String? = " ",
    var readed: Boolean = false,
    var textOrConnection: Any = " "

) {


    /**
     * Return one specific chapter from the book
     *
     * @param position
     * @return Chapter
     */
    fun getChapter(position: Int): Chapter {
        return chapters[position]
    }
}