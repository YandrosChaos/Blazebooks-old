package com.blazebooks.ui.reader

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.databinding.ActivityReaderBinding
import com.blazebooks.util.CURRENT_BOOK
import com.blazebooks.util.LAST_BOOK_SELECTED_KEY
import com.blazebooks.util.toast
import kotlinx.android.synthetic.main.activity_reader.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    private val fragments:ArrayList<Fragment> = arrayListOf();
    private lateinit var viewPager2: ViewPager2
    private lateinit var absolutePath: String

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reader)
        layoutFilter = findViewById(R.id.readerFilterImageView)
        viewPager2 = findViewById(R.id.viewpager)
        viewModel = ViewModelProvider(this, factory).get(ReaderViewModel::class.java)
        absolutePath= this.getExternalFilesDir(null)?.absolutePath!!

        loadLightMode()
        clock()

        bookPath = preferences.getString(LAST_BOOK_SELECTED_KEY, null)
        viewModel.filesPath = absolutePath

        val chapter = intent.getIntExtra("CHAPTER",0)
        if(chapter!=0){
            viewModel.currentPage = chapter
        }else {
            viewModel.currentPage = viewModel.getLastPage(bookPath + "Page")
        }

        val book = readEPub(bookPath)

        val splitParts = bookPath?.split("/")
        val bookFolder = bookPath?.replace("/"+splitParts?.get(splitParts.size-1).toString(), "")

        //obtiene el número de la última página del epub
        lastPage = book.spine.spineReferences.size

        loadData(book, lastPage, viewModel.currentPage, bookFolder)

        //Botón para acceder a los ajustes del libro
        btn_readerSettings.setOnClickListener{
            val cssPath= "$absolutePath/$bookFolder/Styles/style.css"
            val fm= supportFragmentManager
            val frag= SettingsDialogFragment(kodein, cssPath)
            frag.show(fm, "si")
        }

        //Botón que cambia la orientación de la activity
        ibtn_orientation.setOnClickListener{
            requestedOrientation = if(this.resources.configuration.orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        //Obtiene la canción a reproducir (Cuando esten las canciones se pondra el link como propiedad del libro y cada uno tendra la suya, de momento esta es de prueba)
        try {
            mediaPlayer.setDataSource(CURRENT_BOOK.music)
        } catch (e: UninitializedPropertyAccessException) {
            mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/blazebooks-5e827.appspot.com/o/Songs%2FTheWitcher.mp3?alt=media&token=c9567bff-3764-43ee-aa66-ab83cf366849")
        }

        try{
            mediaPlayer.prepare()
            ibtn_music.setOnClickListener {
                if (mediaPlayer.isPlaying) mediaPlayer.pause()
                else mediaPlayer.start()
            }
        } catch (e: IOException){ }

    }

    /**
     * Lee el epub y lo guarda en un objeto book
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    private fun readEPub(bookPath: String?): Book {
        val epubInputStream: InputStream = File("$absolutePath/$bookPath").inputStream()
        val book: Book = EpubReader().readEpub(epubInputStream)
        epubInputStream.close()
        return book;
    }

    /**
     * Al volver a la actividad se actualiza para mostrar los cambios
     *
     * @author Mounir Zbayr
     */
    override fun onRestart() {
        super.onRestart()
        finish()
        overridePendingTransition(0,0)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    fun refresh() {
        finish()
        overridePendingTransition(0,0)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    private fun loadData(book: Book, pages: Int, currentPage: Int, documents: String?) {

        for (i in 1..pages){
            val datas= viewModel.loadData(book, i, documents);
            fragments.add(BookPageFragment.newInstance("file://${absolutePath}", datas, i+1))
        }
        initViewPager2WithFragments(fragments, currentPage)

    }

    private fun initViewPager2WithFragments(data: ArrayList<Fragment>, currentPage: Int)
    {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, data, this, binding)
        viewPager2.adapter = adapter
        viewPager2.currentItem = currentPage

    }

    /**
     * Método que añade al TextView tTime la hora en tiempo real
     *
     * @author Mounir Zbayr
     */
    private fun clock() {
        val thread = Thread {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000)
                    runOnUiThread { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) tTime.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) }
                }
            } catch (e: InterruptedException) {}
        }
        thread.start()
    }

    /**
     * Muestra el número de pagina actual
     */
    fun updatePageTextView(position: Int) {
        binding.tNumPages.text =
            String.format(
                resources.getString(R.string.pageNumber),
                position,
                lastPage
            )
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
        viewModel.currentPage= viewPager2.currentItem
        viewModel.saveLastPagePref(bookPath + "Page")
        mediaPlayer.stop()
    }

}//class
