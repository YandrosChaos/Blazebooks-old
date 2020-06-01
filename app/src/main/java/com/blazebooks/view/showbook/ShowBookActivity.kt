package com.blazebooks.view.showbook

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.control.localStorage.LocalStorageSingleton
import com.blazebooks.control.localStorage.model.FavBook
import com.blazebooks.view.PreconfiguredActivity
import com.blazebooks.view.becomepremium.BecomePremiumActivity
import com.blazebooks.view.reader.ReaderActivity
import com.blazebooks.view.showbook.control.ShowBookViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.item_show_book.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.MediaType
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.service.MediatypeService
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


/**
 * @see PreconfiguredActivity
 * @see ChapterFragment
 * @see SynopsisFragment
 * @author Victor Gonzalez
 */
class ShowBookActivity : PreconfiguredActivity() {
    private val adapter by lazy {
        ShowBookViewPagerAdapter(
            this
        )
    }
    private var liked = false
    private val favBook = FavBook(Constants.CURRENT_BOOK.title.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        activityShowBookViewPager.adapter = adapter

        //crear las diferentes pestañas
        val tabLayoutMediator =
            TabLayoutMediator(activityShowBookTabLayout, activityShowBookViewPager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = getString(R.string.synopsis)
                        }
                        1 -> {
                            tab.text = getString(R.string.chapters)
                        }
                    }
                })
        tabLayoutMediator.attach()

        //if current book is liked, set the drawable and boolean
        if (LocalStorageSingleton.getDatabase(applicationContext).favBookDAO()
                .exist(favBook.title) != 0
        ) {
            showBookBtnFav.progress = 1f
            liked = true
        }


    }

    /**
     * If the current book is liked, then removes the like. Else, set liked to true the book.
     * Later refresh the drawable state.
     *
     * @see liked
     * @see LocalStorageSingleton
     * @see FavBook
     * @see com.blazebooks.control.localStorage.dao.FavBookDAO
     *
     * @author Victor Gonzalez
     */
    fun addFav(view: View) {
        liked = when (liked) {
            true -> {
                //remove from favs
                showBookBtnFav.speed = -1f
                showBookBtnFav.playAnimation()
                LocalStorageSingleton.getDatabase(applicationContext).favBookDAO().delete(favBook)
                false
            }
            false -> {
                //add to favs
                showBookBtnFav.speed = 1f
                showBookBtnFav.playAnimation()
                LocalStorageSingleton.getDatabase(applicationContext).favBookDAO().insert(favBook)
                true
            }
        }
        view.refreshDrawableState()
    }

    /**
     * Método que al pulsar el botón de descarga obtiene el archivo del firebase storage y lo guarda
     * en una carpeta con el nombre del libro, creando ésta en la carpeta del proyecto.
     *
     * Si el usuario no es premium, mostrará BecomePremiumActivity.
     *
     * @see BecomePremiumActivity
     * @see downloadFile
     *
     * @author MounirZbayr
     * @author Victor Gonzalez
     */
    fun download(view: View) {


        if (Constants.CURRENT_USER.premium != Constants.CURRENT_BOOK.premium) {
            startActivity(Intent(this, BecomePremiumActivity::class.java))
            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
        } else {
            val titleBook = showBookTvTitle.text.toString() //nombre del libro
            val documents = "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
            val documentsFolder = File(this.filesDir, documents)

            //si la carpeta existe solo mostrará el mensaje, si no la creará y descargará el libro
            if (documentsFolder.exists()) Toast.makeText(
                this,
                getString(R.string.already_dwnload),
                Toast.LENGTH_SHORT
            ).show()
            else {
                Toast.makeText(this, getString(R.string.dwnloading), Toast.LENGTH_SHORT).show()

                documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

                val mStorageRef =
                    FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
                //Con esto se obtiene la url del libro dependiendo de su nombre
                mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                    downloadFile(this, titleBook, documents, it.toString())

                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        //Lee el epub y lo guarda en un objeto book
                        val epubInputStream: InputStream =
                            File("/storage/emulated/0/Android/data/com.blazebooks/files/$documents/$titleBook.epub").inputStream()
                        val book: Book = EpubReader().readEpub(epubInputStream)
                        saveBookResources(book, documents)
                        Toast.makeText(
                            this,
                            getString(R.string.dwnload_cmplete),
                            Toast.LENGTH_SHORT
                        ).show()
                    }, 3000)

                }.addOnFailureListener {
                    Toast.makeText(this, getString(R.string.dwnload_error), Toast.LENGTH_SHORT)
                        .show()
                    documentsFolder.delete() //Borra la carpeta creada al dar error
                }
            }//if

        }//if

    }//download

    /**
     * Método que transforma los datos obtenidos en imagenes y los almacena en una carpeta creada en el directorio del libro actual
     *
     * @author Mounir Zbayr
     */
    private fun createDirectoryAndSaveFile(
        imageToSave: Bitmap,
        fileName: String,
        path: String
    ) {
        val direct =
            File("/storage/emulated/0/Android/data/com.blazebooks/files/$path/ImagesBZB/")
        if (!direct.exists()) {
            val wallpaperDirectory =
                File("/storage/emulated/0/Android/data/com.blazebooks/files/$path/ImagesBZB/")
            wallpaperDirectory.mkdirs()
        }
        val file =
            File("/storage/emulated/0/Android/data/com.blazebooks/files/$path/ImagesBZB/$fileName")
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 20, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Método que obtiene las imagenes y el style.css del archivo epub y los almacena en la memoria interna para acceder a ellos al leer
     *
     * @author Mounir Zbayr
     */
    private fun saveBookResources(book: Book, path: String) {
        val bitmapTypes: Array<MediaType> =
            arrayOf(MediatypeService.PNG, MediatypeService.GIF, MediatypeService.JPG)
        val bitmapResources: List<Resource> =
            book.resources.getResourcesByMediaTypes(bitmapTypes)


        for (r in bitmapResources) {
            val bm = BitmapFactory.decodeByteArray(r.data, 0, r.data.size)
            createDirectoryAndSaveFile(bm, r.id.toString(), path)
        }

        val file = File("/storage/emulated/0/Android/data/com.blazebooks/files/$path/", "style.css")

        book.resources.getById("style.css").inputStream.copyTo(FileOutputStream(file))
    }

    /**
     * Recibe la URL y la ruta de destino y descarga el archivo usando DownloadManager
     *
     * @author Mounir Zbayr
     */
    private fun downloadFile(
        context: Context,
        fileName: String,
        destinationDirectory: String?,
        uri: String?
    ) {

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val link = Uri.parse(uri)
        val request = DownloadManager.Request(link)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            context,
            destinationDirectory,
            "$fileName.epub"
        )
        downloadManager.enqueue(request)
    }

    /**
     * Método de pulsado del boton Read, el cual lleva al libro elegido.
     * Si el usuario no es premium mostrará BecomePremiumActivity. Guarda el
     * libro en sharedPreferences para poder abrirlo directamente desde el main.
     *
     * @see BecomePremiumActivity
     * @see saveIntoSharedPreferences
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun read(view: View) {
        if (Constants.CURRENT_USER.premium != Constants.CURRENT_BOOK.premium) {
            startActivity(Intent(this, BecomePremiumActivity::class.java))
            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
        } else {
            val titleBook = showBookTvTitle.text.toString()
            val documents = "books/$titleBook"
            val documentsFolder = File(this.filesDir, documents)



            if (documentsFolder.exists()) {

                val i = Intent(this, ReaderActivity::class.java)
                val bookUrl = "$documents/$titleBook.epub"

                saveIntoSharedPreferences(bookUrl)
                i.putExtra(Constants.PATH_CODE, bookUrl)
                i.putExtra("documents", documents)

                startActivity(i)
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.not_dwnload_yet), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

    /**
     * Saves the last readed book into sharedPreferences.
     *
     * @author Victor Gonzalez
     */
    private fun saveIntoSharedPreferences(url: String) {
        val editor: SharedPreferences.Editor =
            PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putString(Constants.LAST_BOOK_SELECTED_KEY, url)
        editor.apply()
    }

}//class
