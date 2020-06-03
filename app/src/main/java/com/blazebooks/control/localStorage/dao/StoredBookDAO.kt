package com.blazebooks.control.localStorage.dao

import androidx.room.*
import com.blazebooks.model.StoredBook

/**
 * CRUD and Query operations about stored_book database.
 *
 * @see StoredBook
 *
 * @author Victor Gonzalez
 */
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

    /**
     * Updates the last page read.
     */
    @Query("UPDATE stored_books SET lastPage=:page WHERE title=:title")
    fun updatePage(title: String, page: Int)

    /**
     * Returns 0 if the row does not exist or 1 if it exists.
     */
    @Query("SELECT COUNT(*) FROM stored_books WHERE stored_books.title=:title")
    fun exist(title: String): Int
}