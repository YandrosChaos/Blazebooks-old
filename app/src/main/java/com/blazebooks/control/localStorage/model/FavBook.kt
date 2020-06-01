package com.blazebooks.control.localStorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_books")
data class FavBook(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    var title: String = ""
) {
}