package com.blazebooks.control.localStorage.dao

import androidx.room.*
import com.blazebooks.control.localStorage.model.FavBook

/**
 * CRUD and Query operations about fav_books database.
 *
 * @see FavBook
 *
 * @author Victor Gonzalez
 */
@Dao
interface FavBookDAO {
    @Insert
    fun insert(favBook: FavBook)

    @Delete
    fun delete(favBook: FavBook)

    @Update
    fun update(favBook: FavBook)

    @Query("SELECT * FROM fav_books")
    fun getAll(): MutableList<FavBook>

    @Query("SELECT * FROM fav_books WHERE title=:title")
    fun get(title: String): FavBook

    /**
     * If a title exists into database, it returns 1. If not exists, it returns 0.
     */
    @Query("SELECT COUNT(*) FROM fav_books WHERE fav_books.title=:title")
    fun exist(title: String): Int

    @Query("SELECT title FROM fav_books")
    fun getAllTitles(): MutableList<String>
}