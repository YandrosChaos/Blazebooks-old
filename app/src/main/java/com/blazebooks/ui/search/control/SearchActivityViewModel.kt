package com.blazebooks.ui.search.control

import androidx.lifecycle.ViewModel
import com.blazebooks.data.db.entities.StoredBook
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.data.models.Book
import com.blazebooks.data.repositories.CatalogRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Victor Gonzalez
 */
class SearchActivityViewModel(
    private val localBooksRepo: StoredBooksRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
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

    private fun addStoredBookToDataList(book: StoredBook) =
        dataList.add(book.transformFromJsonData())

}