package com.blazebooks.data.models

data class Book(
    var title: String? = " ",
    var image: String? = " ",
    var author: String? = " ",
    var synopsis: String? = " ",
    var chapters: ArrayList<Chapter> = arrayListOf(),
    var premium: Boolean = false,
    var genre: String? = " ",
    var readed: Boolean = false,
    var textOrConnection: Any = " "
)