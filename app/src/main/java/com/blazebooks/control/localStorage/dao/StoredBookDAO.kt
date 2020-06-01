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

    @Query("SELECT * FROM stored_books WHERE title=:title")
    fun exist(title: String): Boolean
}