package com.blazebooks.ui.reader

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.databinding.ActivityReaderBinding
import com.blazebooks.util.CURRENT_BOOK
import com.blazebooks.util.PATH_CODE
import com.blazebooks.util.toast
import kotlinx.android.synthetic.main.activity_book_style.*
import kotlinx.android.synthetic.main.activity_reader.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val ENCODING = "UTF-8"
private const val MIME_TYPE = "text/html"
private const val FIRST_PAGE = 1

/**
 * Muestra la vista de lectura del libro y lleva el flujo de la lectura.
 *
 * @author Mounir Zbayr
 * @author Víctor González
 */
class ReaderActivity : PreconfiguredActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ReaderViewModelFactory>()
    private lateinit var viewModel: ReaderViewModel
    private lateinit var binding: ActivityReaderBinding
    private var bookPath : String? = ""
    private val mediaPlayer: MediaPlayer= MediaPlayer()
    private lateinit var layoutFilter: ImageView
    private var lastPage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reader)
        layoutFilter = findViewById(R.id.readerFilterImageView)
        viewModel = ViewModelProvider(this, factory).get(ReaderViewModel::class.java)

        loadLightMode()
        clock()

        val titleBook = CURRENT_BOOK.title.toString()

        val bookFolder = "books/$titleBook"

        bookPath = "$bookFolder/$titleBook.epub"



        viewModel.filesPath = this.getExternalFilesDir(null)?.absolutePath

        viewModel.currentPage = intent.getIntExtra("CHAPTER",  viewModel.getLastPage(bookPath + "Page"))


        val book = readEPub(bookPath)

        //obtiene el número de la última página del epub
        lastPage = book.spine.spineReferences.size
        page(book, viewModel.currentPage, bookFolder)

        //Botón para ir a la página siguiente
        buttonNext.setOnClickListener {
            nextPag(book, bookFolder)
        }

        //Botón para ir a la página anterior
        buttonPrevious.setOnClickListener {
            previousPag(book, bookFolder)
        }

        //Botón para acceder a los ajustes del libro
        btn_readerSettings.setOnClickListener{


            val cssPath= this.getExternalFilesDir(null)?.absolutePath+"/"+bookFolder+"/Styles/style.css"

            val fm= supportFragmentManager
            val frag= SettingsDialogFragment(kodein, cssPath)
            frag.show( fm, "si")
        }

        //Botón que cambia la orientación de la activity
        ibtn_orientation.setOnClickListener{
            requestedOrientation = if(this.resources.configuration.orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        //Obtiene la canción a reproducir (Cuando esten las canciones se pondra el link como propiedad del libro y cada uno tendra la suya, de momento esta es de prueba)
        mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/blazebooks-5e827.appspot.com/o/Songs%2FTheWitcher.mp3?alt=media&token=c9567bff-3764-43ee-aa66-ab83cf366849")
        mediaPlayer.prepare()

        ibtn_music.setOnClickListener {

            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
            }else {
                mediaPlayer.start()
            }
        }

    }



    /**
     * Lee el epub y lo guarda en un objeto book
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    private fun readEPub(bookPath: String?): Book {
        val epubInputStream: InputStream =
            File(this.getExternalFilesDir(null)?.absolutePath + "/$bookPath").inputStream()
        val book: Book = EpubReader().readEpub(epubInputStream)
        epubInputStream.close()
        return book
    }


    /**
     * Al volver a la actividad se actualiza para mostrar los cambios
     *
     * @author Mounir Zbayr
     */
    override fun onRestart() {
        super.onRestart()
        recreate()
    }

    /**
     * Este método coge el contenido del libro y lo lee por capitulos dependiendo
     * del numero que reciba. Despues lo muestra en el WebView.
     *
     * @author Mounir Zbayr
     */
    private fun page(book: Book, numPage: Int, documents: String?) {
        updatePageTextView()
        val baseUrl = "file://${this.getExternalFilesDir(null)?.absolutePath}"
        webViewReader.loadDataWithBaseURL(
            baseUrl,
            viewModel.loadData(book, numPage, documents),
            MIME_TYPE,
            ENCODING,
            null
        )
    }

    /**
     * Muestra el numero de pagina actual
     */
    private fun updatePageTextView() {
        binding.tNumPages.text =
            String.format(
                resources.getString(R.string.pageNumber),
                viewModel.currentPage,
                lastPage
            )
    }

    /**
     * Método que aporta al botón buttonNext la función de avanzar a la página siguiente y
     * coloca el scroll en la posición inicial.
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    private fun nextPag(book: Book, bookFolder: String?) {
        if (viewModel.currentPage != lastPage) {
            viewModel.nextPage()
            page(book, viewModel.currentPage, bookFolder)
        }
    }

    /**
     * Método que aporta al botón buttonPrevious la función de retroceder a la pagina
     * anterior.
     *
     * @author Mounir Zbayr
     * @author Víctor González
     */
    private fun previousPag(book: Book, bookFolder: String?) {
        if (viewModel.currentPage != FIRST_PAGE) {
            viewModel.previousPage()
            page(book, viewModel.currentPage, bookFolder)
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
        if (viewModel.isLightModeOn()) {
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


    /**
     * Guarda la ultima página en el SharedPreferences cuando la activity está detenida
     *
     * @author Mounir Zbayr
     */
    override fun onStop() {
        super.onStop()
        viewModel.saveLastPagePref(bookPath+"Page")
        mediaPlayer.stop()
    }

}//class
