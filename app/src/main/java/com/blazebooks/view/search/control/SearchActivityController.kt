package com.blazebooks.view.search.control

import android.content.Context
import com.blazebooks.R
import com.blazebooks.control.localStorage.LocalStorageSingleton
import com.blazebooks.model.Book
import com.blazebooks.model.Chapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Victor Gonzalez
 */
class SearchActivityController(
    val context: Context,
    private val downloadType: String
) {

    private val dataList = mutableListOf<Book>()

    /**
     * Load data from database.
     *
     * @see SearchAdapter.updateList
     *
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    fun data(): MutableList<Book> {
        when (downloadType) {
            context.getString(R.string.fav_books) -> getFavBooks()
            context.getString(R.string.my_books) -> getStoredBooks()
            else -> getAllBooks()
        }
        return dataList
    }

    private fun getAllBooks() {
        val db =
            FirebaseFirestore.getInstance() //Con esto accedemos a la base de datos de Firebase
        db.collection("Books") //Accede a la colección Books y devuelve todos los documentos
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val book =
                        document.toObject(Book::class.java) //convierte el documento de firebase a la clase Book
                    val chapterList = ArrayList<Chapter>()
                    db.collection("Books").document(document.id).collection("Chapters")
                        .get()
                        .addOnSuccessListener { chapters ->
                            for (chapter in chapters) {
                                chapterList.add(chapter.toObject(Chapter::class.java)) //se añaden los capitulos de la bbdd a la lista de capitulos
                            }
                        }
                    book.chapters = chapterList //añade los capitulos al libro
                    dataList.add(book) //añade el libro a la lista
                }//for
            }
    }

    private fun getStoredBooks() {
        val storedBooks =
            LocalStorageSingleton.getDatabase(context).storedBookDAO().getAll()
        if (!storedBooks.isNullOrEmpty()) {
            storedBooks.forEach {
                dataList.add(it.transformFromJsonData())
            }
        }
    }

    private fun getFavBooks() {
         val db: FirebaseFirestore = FirebaseFirestore.getInstance()
         val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("FavBooks")
            .document(firebaseUserID)
            .collection("likedBooks")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        dataList.add(document.toObject(Book::class.java))
                    }
                }


            }
    }

}