package com.blazebooks.data.models

import java.util.*
import kotlin.collections.ArrayList

data class Book(
    var title: String? = " ",
    var image: String? = " ",
    var music: String? = " ",
    var author: String? = " ",
    var synopsis: String? = " ",
    var chapters: ArrayList<Chapter> = arrayListOf(),
    var premium: Boolean = false,
    var genre: String? = " ",
    var readed: Boolean = false,
    var creationDate: Date = Date(),
    var textOrConnection: Any = " "
)