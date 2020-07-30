package com.blazebooks.ui.showbook

import android.util.Log
import androidx.lifecycle.ViewModel
import com.blazebooks.data.models.Book
import com.blazebooks.data.db.entities.StoredBook
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.CatalogRepository
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
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    var liked = false
    var exist = false

    /**
     * Queries to DDBB if the book is stored into FavBooks database and set
     * the Liked default value.
     *
     * @see liked
     */
    suspend fun isFavBook(book: Book) = withContext(Dispatchers.IO) {
        catalogRepository.isFavBook(book)
    }


    /**
     * Saves a book into the user favBooks collection.
     */
    suspend fun insertLikedBook(book: Book) = withContext(Dispatchers.IO) {
        catalogRepository.saveFavBook(book)
    }


    /**
     * Deletes a book from user favBook collection.
     */
    suspend fun deleteLikedBook(book: Book) = withContext(Dispatchers.IO) {
        catalogRepository.deleteFavBook(book)
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
        Coroutines.main {
            storedBooksRepository.saveStoredBook(createStoredBookItem(titleBook, path))
            exist = true
        }
    }

    /**
     * Devuelve un objeto StoredBook listo para ser guardado en la base de
     * datos local, con toda la información del libro guardada en formato json.
     */
    private fun createStoredBookItem(title: String, path: String): StoredBook {
        return StoredBook(title, path).apply {
            storeToJsonData(CURRENT_BOOK)
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