package com.blazebooks.ui.search

import androidx.lifecycle.ViewModel
import com.blazebooks.data.db.entities.StoredBook
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.data.models.Book
import com.blazebooks.data.repositories.CatalogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Victor Gonzalez
 */
class SearchActivityViewModel(
    private val localBooksRepo: StoredBooksRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    val dataList = mutableListOf<Book>()

    /**
     * Gets all books from Firebase and stores it into dataList.
     *
     * @see dataList
     * @author Mounir
     * @author Victor Gonzalez
     */
    suspend fun getAllBooks() = withContext(Dispatchers.IO) {
        catalogRepository.getAllBooks()
    }

    /**
     * Gets all books stored locally and add it to dataList.
     *
     * @see dataList
     * @author Victor Gonzalez
     */
    suspend fun getStoredBooks() = withContext(Dispatchers.IO) {
        localBooksRepo.getAllStoredBooks().let {
            it.forEach { storedBook ->
                addStoredBookToDataList(storedBook)
            }
        }
    }

    /**
     * Gets all fav books from Firebase and stores it into dataList.
     *
     * @see dataList
     *
     * @author Victor Gonzalez
     */
    suspend fun getFavBooks() = withContext(Dispatchers.IO) {
        catalogRepository.getAllFavBooks()
    }

    suspend fun getNewBooks() = withContext(Dispatchers.IO) {
        catalogRepository.getNewBooks()
    }



    private fun addStoredBookToDataList(book: StoredBook) =
        dataList.add(book.transformFromJsonData())

}