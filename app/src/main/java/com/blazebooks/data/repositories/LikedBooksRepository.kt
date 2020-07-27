package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirestoreLikedBooks
import com.blazebooks.data.models.Book

/**
 * Llama a las funciones dentro de FirestoreLikedBooks.
 *
 * @see FirestoreLikedBooks
 * @author Victor Gonzalez
 */
class LikedBooksRepository(
    private val likedBooksRepository: FirestoreLikedBooks
) {

    fun isLiked(title: String) = likedBooksRepository.isLiked(title)

    fun save(book : Book) = likedBooksRepository.save(book)

    fun delete(title: String) = likedBooksRepository.delete(title)

}