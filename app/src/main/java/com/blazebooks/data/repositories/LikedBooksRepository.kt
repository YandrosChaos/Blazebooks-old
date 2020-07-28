package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirebaseSource
import com.blazebooks.data.models.Book

/**
 * @author Victor Gonzalez
 */
class LikedBooksRepository(
    private val firebaseSource: FirebaseSource
) {

    fun isFavBook(book: Book) = firebaseSource.isFavBook(book)

    fun saveFavBook(book: Book) = firebaseSource.saveFavBook(book)

    fun deleteFavBook(book: Book) = firebaseSource.deleteFavBook(book)

}