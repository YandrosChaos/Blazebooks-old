package com.blazebooks.ui.showbook

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
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
 * @author Mounir Zbayr+
 * @author Víctor González
 */
class ReaderActivity : PreconfiguredActivity() {


    private var num = 0 //representa el número de página actual

    //colores RGBA del filtro
    private val alphaColor: Float = 0.5f
    private val redColor: Float = 0.61f
    private val greenColor: Float = 0.53f
    private val blueColor: Float = 0.05f

    //colores RGB del filtro de compatibilidad
    private val redColorCompat: Int = 136
    private val greenColorCompat: Int = 101
    private val blueColorCompat: Int = 78

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        loadLightMode()
        clock()

        val path = intent.getStringExtra("path")

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
     * @author Mounir
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
     * Método que aporta al botón buttonNext la función de avanzar a la página siguiente.
     *
     * @author Mounir
     */
    private fun next(pages: ArrayList<String>) {
        num++
        numPages.text = String.format(resources.getString(R.string.pageNumber), num, pages.size)
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
                            val time =
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                            tTime.text = time
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                readerActivityCL.setBackgroundColor(
                    Color.argb(
                        this.alphaColor,
                        this.redColor,
                        this.greenColor,
                        this.blueColor
                    )
                )
            } else {
                //VERSION.SDK_INT < O
                readerActivityCL.setBackgroundColor(
                    Color.rgb(
                        redColorCompat, greenColorCompat, blueColorCompat
                    )
                )
            }

        }
    }


}//class
