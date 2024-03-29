package com.blazebooks.data.models

data class Chapter(
    val number: Int = 0,
    val title: String = " ",
    val page: Int = 0,
    val image: String? = " ",
    var readed: Boolean = false,
    val textOrConnection: Any = "unknown"
)