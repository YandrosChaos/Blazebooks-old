package com.blazebooks.view.showbook

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.control.dataAccessObjects.FavBookDao
import com.blazebooks.control.localStorage.LocalStorageSingleton
import com.blazebooks.model.FavBook
import com.blazebooks.model.StoredBook
import com.blazebooks.view.PreconfiguredActivity
import com.blazebooks.view.becomepremium.BecomePremiumActivity
import com.blazebooks.view.reader.ReaderActivity
import com.blazebooks.view.showbook.control.ShowBookViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.item_show_book.*
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.domain.Resources
import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.service.MediatypeService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import kotlin.concurrent.schedule


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
    private val currentFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var liked = false
    private val favBook =
        FavBook(Constants.CURRENT_BOOK.title.toString())

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

        if (FavBookDao().exist(
                FavBook(
                    Constants.CURRENT_BOOK.title.toString(),
                    currentFirebaseUser!!.uid
                )
            )
        ) {
            showBookBtnFav.progress = 1f
            showBookBtnFav.refreshDrawableState()
            liked = true
        }
        Toast.makeText(this, liked.toString(), Toast.LENGTH_LONG).show()


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
                FavBookDao().delete(
                    FavBook(
                        Constants.CURRENT_BOOK.title.toString(),
                        currentFirebaseUser!!.uid
                    )
                )
                false
            }
            false -> {
                //add to favs
                showBookBtnFav.speed = 1f
                showBookBtnFav.playAnimation()
                FavBookDao().insert(
                    FavBook(
                        Constants.CURRENT_BOOK.title.toString(),
                        currentFirebaseUser!!.uid
                    )
                )
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
     * @see storeBookIntoLocalDatabase
     *
     * @author MounirZbayr
     * @author Victor Gonzalez
     */
    fun download(view: View) {

        when {
            Constants.CURRENT_USER.premium != Constants.CURRENT_BOOK.premium -> {
                //si no es premium
                startActivity(Intent(this, BecomePremiumActivity::class.java))
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
            }

            LocalStorageSingleton.getDatabase(this).storedBookDAO()
                .exist(Constants.CURRENT_BOOK.title.toString()) != 1 -> {
                //si no está en la base de datos local

                val titleBook = Constants.CURRENT_BOOK.title.toString() //nombre del libro
                val documents ="books/$titleBook" //La carpeta creada irá dentro de la carpeta books
                val documentsFolder = File(this.filesDir, documents)
                val filesPath= this.getExternalFilesDir(null)?.absolutePath

                Toast.makeText(this, getString(R.string.dwnloading), Toast.LENGTH_SHORT).show()

                documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

                val mStorageRef =
                    FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
                //Con esto se obtiene la url del libro dependiendo de su nombre
                mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                    downloadFile(this, titleBook, documents, it.toString())
                    Toast.makeText(this, titleBook, Toast.LENGTH_SHORT).show()

                    //Retrasa la ejecucion del método para dar tiempo a la descarga
                    Timer("SettingUp", false).schedule(5000) {
                        saveBookResources(File("$filesPath/$documents/$titleBook.epub"),"$filesPath/$documents")
                    }

                    Toast.makeText(
                        this,
                        getString(R.string.dwnload_cmplete),
                        Toast.LENGTH_SHORT
                    ).show()


                }.addOnFailureListener {
                    Toast.makeText(this, getString(R.string.dwnload_error), Toast.LENGTH_SHORT)
                        .show()
                    documentsFolder.delete() //Borra la carpeta creada al dar error
                }
                storeBookIntoLocalDatabase(
                    titleBook,
                    documents
                )//almacena la info dentro de la base de datos local
            }//if
            else -> {
                //si no es ninguno de los anteriores casos
                Toast.makeText(
                    this,
                    getString(R.string.already_dwnload),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }//download

    /**
     * Almacena la información del libro dentro de la base de datos local.
     *
     * @param titleBook El título del libro.
     * @param path La ruta en la que se guardó el libro.
     *
     * @author Victor Gonzalez
     */
    private fun storeBookIntoLocalDatabase(titleBook: String, path: String) {
        //preparar objeto para almacenar localmente
        val storedBook = StoredBook(titleBook, path, 0, "")
        storedBook.storeToJsonData(Constants.CURRENT_BOOK)
        //guardar en la base de datos
        LocalStorageSingleton.getDatabase(this).storedBookDAO().insert(storedBook)
    }



    /**
     * Método que obtiene las imagenes y el style.css del archivo epub y los almacena en la memoria interna para acceder a ellos al leer
     *
     * @author Mounir Zbayr
     */
    private fun saveBookResources(FileObj: File, directory: String) {
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
