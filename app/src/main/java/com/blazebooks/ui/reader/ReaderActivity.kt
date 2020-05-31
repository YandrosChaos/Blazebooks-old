package com.blazebooks.ui.reader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.PreconfiguredActivity
import kotlinx.android.synthetic.main.activity_reader.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.domain.MediaType
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import nl.siegmann.epublib.service.MediatypeService
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Muestra la vista de lectura del libro y lleva el flujo de la lectura.
 *
 * @author Mounir Zbayr
 * @author Víctor González
 */
class ReaderActivity : PreconfiguredActivity() {

    private lateinit var layoutFilter: ImageView
    private var num = 1 //representa el número de página actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        layoutFilter = findViewById(R.id.readerFilterImageView)

        loadLightMode()
        clock()

        val path = intent.getStringExtra(Constants.PATH_CODE)
        val documents = intent.getStringExtra("documents")

        //Lee el epub y lo guarda en un objeto book
        val epubInputStream: InputStream =
            File("/storage/emulated/0/Android/data/com.blazebooks/files/$path").inputStream()
        val book: Book = EpubReader().readEpub(epubInputStream)

        //obtiene informacion del epub
        val spine = book.spine
        val spineList = spine.spineReferences
        val count = spineList.size

        page(book, num, documents)

        numPages.text = String.format(resources.getString(R.string.pageNumber), num, count)


        buttonNext.setOnClickListener { next(book, count, documents) } //Botón para ir a la página siguiente
        buttonPrevious.setOnClickListener { previous(book, count, documents) } //Botón para ir a la página anterior

    }


    /**
     * Este método coge el contenido del libro y lo lee por capitulos dependiendo del numero que reciba. Despues lo muestra en el WebView
     *
     * @author Mounir Zbayr
     */
    private fun page(book: Book, numPage : Int, documents: String){


        val baseUrl="file://storage/emulated/0/Android/data/com.blazebooks/files/"
        var data = "<style>img{display: inline;height: auto;max-width: 100%;}</style>"+String(book.contents[numPage-1].data)

        data= data.replace("../Images/", "/storage/emulated/0/Android/data/com.blazebooks/files/$documents/ImagesBZB/")
        data= data.replace("../Styles/", "/storage/emulated/0/Android/data/com.blazebooks/files/$documents/")

        webViewReader.loadDataWithBaseURL(baseUrl,
            data, "text/html", "UTF-8", null)

        Log.d("html", data)
    }

    /**
     * Método que aporta al botón buttonNext la función de avanzar a la página siguiente y
     * coloca el scroll en la posición inicial.
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    private fun next(book: Book, pages : Int, documents: String) {
        if (num != pages) {
            num++
            page(book, num, documents)
            numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages)
        }
    }

    /**
     * Método que aporta al botón buttonPrevious la función de retroceder a la pagina anterior
     *
     * @author Mounir
     * @author Víctor González
     */
    private fun previous(book: Book, pages : Int, documents: String) {
        if (num != 1) {
            num--
            page(book, num, documents)
            numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages)
        }
    }

    /**
     * Método que añade al textview tTime la hora en tiempo real
     *
     * @author Mounir Zbayr
     */
    private fun clock() {

        val thread = Thread(Runnable {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000)
                    runOnUiThread {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tTime.text =
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                    }
                }
            } catch (e: InterruptedException) {
            }
        })
        thread.start()
    }



    /**
     * If ReadMode preference is switch on, then sets a dark background for the view.
     *
     * @author Victor Gonzalez
     */
    private fun loadLightMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getBoolean(Constants.READ_MODE_KEY, false)) {
            layoutFilter.visibility = View.VISIBLE
        }
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
        finish()
    }

}//class
