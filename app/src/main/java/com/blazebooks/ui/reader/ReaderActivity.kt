package com.blazebooks.ui.reader

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.ui.PreconfiguredActivity
import kotlinx.android.synthetic.main.activity_reader.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
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


    private var num = 0 //representa el número de página actual

    //colores RGB del filtro de pantalla -> modo lectura
    private val redColor: Int = 112
    private val greenColor: Int = 66
    private val blueColor: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        loadLightMode()
        clock()

        val path = intent.getStringExtra(Constants.PATH_CODE)

        val pages = getText(path)

        numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages.size)

        //Convierte el html sacado del epub al texto visible en el lector
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textReader.text = Html.fromHtml(pages[num], Html.FROM_HTML_MODE_COMPACT)
        }
        textReader.movementMethod = ScrollingMovementMethod()//Añade el scroll de las páginas

        buttonNext.setOnClickListener { next(pages) } //Botón para ir a la página siguiente
        buttonPrevious.setOnClickListener { previous(pages) } //Botón para ir a la página anterior


    }

    /**
     * Método que obtiene el texto del epub correspondiente al libro seleccionado
     *
     * @author Mounir Zbayr
     */
    private fun getText(filepath: String?): ArrayList<String> {

        val epubInputStream: InputStream =
            File("/storage/emulated/0/Android/data/com.blazebooks/files/$filepath").inputStream()
        val book: Book = EpubReader().readEpub(epubInputStream)
        val spine = book.spine
        val spineList = spine.spineReferences
        val count = spineList.size
        Log.i("epublib", "title: " + book.title);

        var i = 0
        val arrayList = ArrayList<String>()
        while (count > i) {
            arrayList.add(spine.getResource(i).reader.readText())
            i++
        }

        return arrayList

    }

    /**
     * Método que aporta al botón buttonNext la función de avanzar a la página siguiente y
     * coloca el scroll en la posición inicial.
     *
     * @author Mounir
     * @author Victor Gonzalez
     */
    private fun next(pages: ArrayList<String>) {
        num++
        numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages.size)
        textReader.scrollTo(0,0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textReader.text = Html.fromHtml(pages[num], Html.FROM_HTML_MODE_COMPACT)
        }
    }

    /**
     * Método que aporta al botón buttonPrevious la función de retroceder a la pagina anterior
     *
     * @author Mounir
     * @author Víctor González
     */
    private fun previous(pages: ArrayList<String>) {
        if (num != 0) {
            num--
            numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages.size)
            textReader.scrollTo(0,0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textReader.text = Html.fromHtml(pages[num], Html.FROM_HTML_MODE_COMPACT)
            }
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
            readerActivityCL.setBackgroundColor(
                Color.rgb(
                    redColor,
                    greenColor,
                    blueColor
                )
            )
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
