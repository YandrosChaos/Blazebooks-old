package com.blazebooks.control.localStorage.dao

import androidx.room.*
import com.blazebooks.control.localStorage.model.StoredBook

@Dao
interface StoredBookDAO {

    @Insert
    fun insert(storedBook: StoredBook)

    @Delete
    fun delete(storedBook: StoredBook)

    @Update
    fun update(storedBook: StoredBook)

    @Query("SELECT * FROM stored_books")
    fun getAll(): MutableList<StoredBook>

    @Query("SELECT * FROM stored_books WHERE title=:title")
    fun get(title: String): StoredBook

    @Query("SELECT lastPage FROM stored_books WHERE title=:title")
    fun getPage(title: String): Int

    @Query("UPDATE stored_books SET lastPage=:page WHERE title=:title")
    fun updatePage(title: String, page: Int)

    @Query("SELECT COUNT(*) FROM stored_books WHERE stored_books.title=:title")
    fun exist(title: String): Int
}