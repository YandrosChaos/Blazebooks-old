package com.blazebooks.model

data class FavBook(
    var book_id: String = "",
    var user_id: String = ""
) {

    override fun toString(): String {
        return "$user_id${book_id.filter { char -> char != ' ' }}"
    }

    fun isEmpty(): Boolean {
        return book_id.isEmpty() && user_id.isEmpty()
    }
}