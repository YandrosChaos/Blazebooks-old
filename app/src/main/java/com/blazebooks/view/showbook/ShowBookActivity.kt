package com.blazebooks.view.showbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.control.localStorage.LocalStorageSingleton
import com.blazebooks.view.PreconfiguredActivity
import com.blazebooks.view.becomepremium.BecomePremiumActivity
import com.blazebooks.view.reader.ReaderActivity
import com.blazebooks.view.showbook.control.ShowBookActivityController
import com.blazebooks.view.showbook.control.ShowBookViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.item_show_book.*
import java.io.File
import java.util.*
import kotlin.concurrent.schedule


/**
 * @see PreconfiguredActivity
 * @see ChapterFragment
 * @see SynopsisFragment
 *
 * @author Victor Gonzalez
 */
class ShowBookActivity : PreconfiguredActivity() {
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
        controller = ShowBookActivityController(this)

        //comprueba si el libro está en la lista de favs del user o no
        controller.isFavBook()

        Handler().postDelayed({
            //if current book is liked, set the drawable and boolean
            setLikeUI()
        }, 500)

    }

    /**
     * If the current book is liked, then removes the like. Else, set liked to true the book.
     * Later refresh the drawable state.
     *
     * @see LocalStorageSingleton
     *
     * @author Victor Gonzalez
     */
    fun addFav(view: View) {
        when (controller.liked) {
            true -> {
                //remove from favs
                showBookBtnFav.speed = -1f
                showBookBtnFav.playAnimation()
                controller.deleteLikedBook(Constants.CURRENT_BOOK.title.toString())
            }
            false -> {
                //add to favs
                showBookBtnFav.speed = 1f
                showBookBtnFav.playAnimation()
                controller.insertLikedBook(Constants.CURRENT_BOOK)
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

            !controller.bookExist() -> {
                //si no está en la base de datos local

                val titleBook = Constants.CURRENT_BOOK.title.toString() //nombre del libro
                val documents =
                    "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
                val documentsFolder = File(this.filesDir, documents)
                val filesPath = this.getExternalFilesDir(null)?.absolutePath

                Toast.makeText(this, getString(R.string.dwnloading), Toast.LENGTH_SHORT).show()

                documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

                val mStorageRef =
                    FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
                //Con esto se obtiene la url del libro dependiendo de su nombre
                mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                    controller.downloadFile(this, titleBook, documents, it.toString())
                    Toast.makeText(this, titleBook, Toast.LENGTH_SHORT).show()

                    //Retrasa la ejecucion del método para dar tiempo a la descarga
                    Timer("SettingUp", false).schedule(5000) {
                        controller.saveBookResources(
                            File("$filesPath/$documents/$titleBook.epub"),
                            "$filesPath/$documents"
                        )
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
                controller.storeBookIntoLocalDatabase(
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
        if (Constants.CURRENT_USER.premium != Constants.CURRENT_BOOK.premium) {
            startActivity(Intent(this, BecomePremiumActivity::class.java))
            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
        } else {
            if (controller.bookExist()) {
                val titleBook = showBookTvTitle.text.toString()
                val documents = "books/$titleBook"
                val i = Intent(this, ReaderActivity::class.java)
                val bookUrl = "$documents/$titleBook.epub"

                controller.saveIntoSharedPreferences(bookUrl)
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
     * Sets the GUI depending user liked books.
     *
     * @author Victor Gonzalez
     */
    private fun setLikeUI() {
        if (controller.liked) {
            showBookBtnFav.speed = 1f
            showBookBtnFav.playAnimation()
            showBookBtnFav.refreshDrawableState()
        }
    }

}//class
