package com.blazebooks.ui.showbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.blazebooks.R
import com.blazebooks.data.db.AppDatabase
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository
import com.blazebooks.ui.becomepremium.BecomePremiumActivity
import com.blazebooks.ui.reader.ReaderActivity
import com.blazebooks.util.*
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.item_show_book.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


/**
 * @author Victor Gonzalez
 */
class ShowBookActivity : PreconfiguredActivity(), KodeinAware {

    override val kodein by kodein()
    private val storedBooksRepository by instance<StoredBooksRepository>()
    private val premiumRepository by instance<PremiumRepository>()
    private val firebaseRepository by instance<LoginRepository>()

    private val adapter by lazy {
        ShowBookViewPagerAdapter(
            this
        )
    }
    private lateinit var controller: ShowBookActivityController

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

        //instanciar el controlador de la vista
        controller = ShowBookActivityController(
            this,
            storedBooksRepository,
            premiumRepository,
            firebaseRepository
        )

        //comprueba si el libro está en la lista de favs del user o no, y si está ya descargado o no
        controller.isFavBook()
        controller.bookExist()

        Handler().postDelayed({
            setLikeUI()
            setDownloadUI()
        }, 500)

    }

    /**
     * If the current book is liked, then removes the like. Else, set liked to true the book.
     * Later refresh the drawable state.
     *
     * @see AppDatabase
     *
     * @author Victor Gonzalez
     */
    fun addFav(view: View) {
        when (controller.liked) {
            true -> {
                //remove from favs
                showBookBtnFav.speed = -1f
                showBookBtnFav.playAnimation()
                controller.deleteLikedBook(CURRENT_BOOK.title.toString())
            }
            false -> {
                //add to favs
                showBookBtnFav.speed = 1f
                showBookBtnFav.playAnimation()
                controller.insertLikedBook(CURRENT_BOOK)
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
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun download(view: View) {

        when {
            !premium && CURRENT_BOOK.premium -> {
                //si el user no es premium pero el libro sí
                startActivity(Intent(this, BecomePremiumActivity::class.java))
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
            }

            !controller.exist -> {
                //si no está en la base de datos local
                showBookBtnDownload.speed = 1f
                showBookBtnDownload.playAnimation()
                view.refreshDrawableState()

                showBookBtnRead.isEnabled = false
                showBookBtnRead.isEnabled = false
                showBookBtnRead.isClickable = false

                val titleBook = CURRENT_BOOK.title.toString() //nombre del libro
                val documents =
                    "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
                val documentsFolder = File(this.filesDir, documents)
                val filesPath = this.getExternalFilesDir(null)?.absolutePath

                toast(getString(R.string.dwnloading))

                documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

                val mStorageRef =
                    FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
                //Con esto se obtiene la url del libro dependiendo de su nombre
                mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                    downloadFile(
                        it.toString(),
                        "$titleBook.epub",
                        "$filesPath/$documents"
                    )


                }.addOnFailureListener {
                    toast(getString(R.string.dwnload_error))
                    documentsFolder.delete() //Borra la carpeta creada al dar error
                }

                controller.storeBookIntoLocalDatabase(
                    titleBook,
                    documents
                )//almacena la info dentro de la base de datos local

                showBookBtnRead.isEnabled = true
            }
            else -> {
                view.snackbar(getString(R.string.already_dwnload))
            }
        }
    }//download

    /**
     * Recibe la URL y la ruta de destino y descarga el archivo usando PRDownloader
     *
     * @author Mounir Zbayr
     */
    private fun downloadFile(url: String, fileName: String, dirPath: String) {

        PRDownloader.download(url, dirPath, fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    controller.saveBookResources(
                        File("$dirPath/$fileName"),
                        dirPath
                    )

                    showBookBtnRead.isEnabled = true
                    showBookBtnRead.isClickable = true

                    toast(getString(R.string.dwnload_cmplete))
                }

                override fun onError(error: com.downloader.Error?) {
                    toast(getString(R.string.dwnload_error))
                }
            })

    }

    /**
     * Método de pulsado del boton Read, el cual lleva al libro elegido.
     * Si el usuario no es premium mostrará BecomePremiumActivity. Guarda el
     * libro en sharedPreferences para poder abrirlo directamente desde el main.
     *
     * @see BecomePremiumActivity
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun read(view: View) {

        if (!premium && CURRENT_BOOK.premium) {
            startActivity(Intent(this, BecomePremiumActivity::class.java))
            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
        } else {
            if (controller.exist) {
                val titleBook = showBookTvTitle.text.toString()
                val documents = "books/$titleBook"
                val i = Intent(this, ReaderActivity::class.java)
                val bookUrl = "$documents/$titleBook.epub"

                controller.saveIntoSharedPreferences(bookUrl)
                i.putExtra(PATH_CODE, bookUrl)
                i.putExtra("documents", documents)

                startActivity(i)
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
                finish()
            } else {
                view.snackbar(getString(R.string.not_dwnload_yet))
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
     * Sets the GUI depending user liked books.
     *
     * @author Victor Gonzalez
     */
    private fun setLikeUI() {
        if (controller.liked) {
            showBookBtnFav.progress = 1f
            showBookBtnFav.refreshDrawableState()
        }
    }

    /**
     * Sets the GUI depending user stored books.
     *
     * @author Victor Gonzalez
     */
    private fun setDownloadUI() {
        if (controller.exist) {
            showBookBtnDownload.progress = 1f
            showBookBtnDownload.refreshDrawableState()
        }
    }

}//class
