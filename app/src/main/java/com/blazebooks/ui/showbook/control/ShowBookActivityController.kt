package com.blazebooks.ui.showbook.control

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.blazebooks.util.Constants
import com.blazebooks.data.localStorage.LocalStorageSingleton
import com.blazebooks.model.Book
import com.blazebooks.model.StoredBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
 */
class ShowBookActivityController(val context: Context) {

    var liked = false

    private val localDB = LocalStorageSingleton.getDatabase(context)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

    /**
     * Queries to DDBB if the book is stored into FavBooks database and set
     * the Liked default value.
     *
     * @see liked
     *
     * @author Victor Gonzalez
     */
    fun isFavBook() {
        db.collection("FavBooks")
            .document(firebaseUserID)
            .collection("likedBooks")
            .document(Constants.CURRENT_BOOK.title.toString().replace(" ", ""))
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    liked = doc.exists()
                }
            }
    }

    /**
     * Saves a book into the user favBooks collection.
     *
     * @author Victor Gonzalez
     */
    fun insertLikedBook(likedBook: Book) {
        liked = true
        db.collection("FavBooks")
            .document(firebaseUserID)
            .collection("likedBooks")
            .document(likedBook.title.toString().replace(" ", ""))
            .set(likedBook)
    }

    /**
     * Deletes a book from user favBook collection.
     *
     * @author Victor Gonzalez
     */
    fun deleteLikedBook(title: String) {
        liked = false
        db.collection("FavBooks")
            .document(firebaseUserID)
            .collection("likedBooks")
            .document(title.replace(" ", ""))
            .delete()
    }

    /**
     * Saves the last readed book into sharedPreferences.
     *
     * @author Victor Gonzalez
     */
    fun saveIntoSharedPreferences(url: String) {
        val editor: SharedPreferences.Editor =
            PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(Constants.LAST_BOOK_SELECTED_KEY, url)
        editor.apply()
    }

    /**
     * Almacena la información del libro dentro de la base de datos local.
     *
     * @param titleBook El título del libro.
     * @param path La ruta en la que se guardó el libro.
     *
     * @author Victor Gonzalez
     */
    fun storeBookIntoLocalDatabase(titleBook: String, path: String) {
        //preparar objeto para almacenar localmente
        val storedBook = StoredBook(titleBook, path, 0, "")
        storedBook.storeToJsonData(Constants.CURRENT_BOOK)
        //guardar en la base de datos
        localDB.storedBookDAO().insert(storedBook)
    }

    fun bookExist(): Boolean {
        return localDB.storedBookDAO().exist(Constants.CURRENT_BOOK.title.toString()) == 1
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
                    image.parentFile.mkdirs()
                    image.createNewFile()
                    val fos1 = FileOutputStream(image)
                    fos1.write(it.data)
                    fos1.close()
                } else if (it.mediaType === MediatypeService.CSS) {
                    val style = File(
                        directory, "Styles/"
                                + it.href.replace("Styles/", "")
                    )
                    style.parentFile.mkdirs()
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