package com.blazebooks.data.db

import androidx.room.*
import com.blazebooks.data.db.entities.StoredBook

/**
 * CRUD and Query operations about stored_book database.
 *
 * @see StoredBook
 *
 * @author Victor Gonzalez
 */
@Dao
interface StoredBookDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(storedBook: StoredBook)

    @Delete
    suspend fun delete(storedBook: StoredBook)

    @Query("SELECT * FROM stored_books")
    suspend fun getAll(): MutableList<StoredBook>

    @Query("SELECT * FROM stored_books WHERE title=:title")
    fun get(title: String): StoredBook

    @Query("SELECT lastPage FROM stored_books WHERE title=:title")
    fun getPage(title: String): Int

    /**
     * Updates the last page read.
     */
    @Query("UPDATE stored_books SET lastPage=:page WHERE title=:title")
    suspend fun updatePage(title: String, page: Int)

    /**
     * Returns 0 if the row does not exist or 1 if it exists.
     */
    @Query("SELECT COUNT(*) FROM stored_books WHERE stored_books.title=:title")
    suspend fun exist(title: String): Int
}