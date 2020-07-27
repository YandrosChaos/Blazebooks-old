package com.blazebooks.ui.showbook

import android.util.Log
import androidx.lifecycle.ViewModel
import com.blazebooks.data.models.Book
import com.blazebooks.data.db.entities.StoredBook
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LikedBooksRepository
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.util.CURRENT_BOOK
import com.blazebooks.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.domain.Resources
import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.service.MediatypeService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


/**
 * @author Victor Gonzalez
 * @author Mounir Zbayr
 */
class ShowBookViewModel(
    private val preferences: PreferenceProvider,
    private val storedBooksRepository: StoredBooksRepository,
    private val likedBooksRepository: LikedBooksRepository
) : ViewModel() {

    var liked = false
    var exist = false

    /**
     * Queries to DDBB if the book is stored into FavBooks database and set
     * the Liked default value.
     *
     * @see liked
     */
    suspend fun isFavBook() = withContext(Dispatchers.IO) {
        likedBooksRepository.isLiked(CURRENT_BOOK.title.toString())
    }


    /**
     * Saves a book into the user favBooks collection.
     */
    suspend fun insertLikedBook(likedBook: Book) = withContext(Dispatchers.IO) {
        likedBooksRepository.save(likedBook)
    }


    /**
     * Deletes a book from user favBook collection.
     */
    suspend fun deleteLikedBook(title: String) = withContext(Dispatchers.IO) {
        likedBooksRepository.delete(title)
    }

    /**
     * Saves the last readed book into sharedPreferences.
     */
    fun saveIntoSharedPreferences(url: String) = preferences.setLastBook(url)


    /**
     * Almacena la información del libro dentro de la base de datos local.
     *
     * @param titleBook El título del libro.
     * @param path La ruta en la que se guardó el libro.
     */
    fun storeBookIntoLocalDatabase(titleBook: String, path: String) {
        //preparar objeto para almacenar localmente
        val storedBook =
            StoredBook(titleBook, path, 0, "")
        storedBook.storeToJsonData(CURRENT_BOOK)
        //guardar en la base de datos
        Coroutines.main {
            storedBooksRepository.saveStoredBook(storedBook)
            exist = true
        }
    }

    fun bookExist() {
        Coroutines.main {
            exist = storedBooksRepository.exist(CURRENT_BOOK.title.toString())
        }
    }


    /**
     * Método que obtiene las imagenes y el style.css del archivo epub y los almacena en la memoria interna para acceder a ellos al leer
     *
     * @author Mounir Zbayr
     */
    fun saveBookResources(FileObj: File, directory: String) {
        try {
            val epubis: InputStream = FileInputStream(FileObj)
            val book = EpubReader().readEpub(epubis)
            val res: Resources = book.resources
            val col: Collection<Resource> = res.all
            col.forEach {
                if (it.mediaType === MediatypeService.JPG
                    || it.mediaType === MediatypeService.PNG
                    || it.mediaType === MediatypeService.GIF
                ) {
                    val image = File(
                        directory, "Images/"
                                + it.href.replace("Images/", "")
                    )
                    image.parentFile?.mkdirs()
                    image.createNewFile()
                    val fos1 = FileOutputStream(image)
                    fos1.write(it.data)
                    fos1.close()
                } else if (it.mediaType === MediatypeService.CSS) {
                    val style = File(
                        directory, "Styles/"
                                + it.href.replace("Styles/", "")
                    )
                    style.parentFile?.mkdirs()
                    style.createNewFile()
                    val fos = FileOutputStream(style)
                    fos.write(it.data)
                    fos.close()
                }
            }
        } catch (e: java.lang.Exception) {
            Log.v("error", e.message.toString())
        }
    }

}