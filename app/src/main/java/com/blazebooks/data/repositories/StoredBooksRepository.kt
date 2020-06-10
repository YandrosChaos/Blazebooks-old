package com.blazebooks.data.repositories

import com.blazebooks.data.db.AppDatabase
import com.blazebooks.data.db.entities.StoredBook

class StoredBooksRepository(
    private val db: AppDatabase
) {
    suspend fun saveStoredBook(storedBook: StoredBook) = db.getStoredBookDAO().upsert(storedBook)
    suspend fun deleteStoredBook(storedBook: StoredBook) = db.getStoredBookDAO().delete(storedBook)
    suspend fun getAllStoredBooks() = db.getStoredBookDAO().getAll()

    suspend fun updateLastPage(title: String, page: Int) =
        db.getStoredBookDAO().updatePage(title, page)

    suspend fun exist(title: String): Boolean {
        return db.getStoredBookDAO().exist(title) == 1
    }
}