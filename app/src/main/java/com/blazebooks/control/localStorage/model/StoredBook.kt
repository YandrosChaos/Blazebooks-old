package com.blazebooks.control.localStorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blazebooks.model.Book
import com.google.gson.Gson

@Entity(tableName = "stored_books")
data class StoredBook(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "path")
    var path: String,
    @ColumnInfo(name = "lastPage")
    var lastPage: Int,
    @ColumnInfo(name = "json")
    var jsonData: String
) {

    /**
     * Stores a book in json format inside this object.
     *
     * @param book The book to store.
     *
     * @author Victor Gonzalez
     */
    fun storeToJsonData(book: Book) {
        this.jsonData = Gson().toJson(book)
    }

    /**
     * Gets the book stored in object format.
     *
     * @return Book
     *
     * @author Victor Gonzalez
     */
    fun transformFromJsonData(): Book {
        return Gson().fromJson(this.jsonData, Book::class.java)
    }

}