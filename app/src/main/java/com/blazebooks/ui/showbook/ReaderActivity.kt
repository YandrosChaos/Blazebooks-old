package com.blazebooks.ui.showbook

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.blazebooks.R
import com.blazebooks.ui.PreconfiguredActivity
import kotlinx.android.synthetic.main.activity_reader.*
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
import java.io.InputStream

class ReaderActivity : PreconfiguredActivity() {

    var num= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val i= intent

        val path= i.getStringExtra("path")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textReader.text = Html.fromHtml(getText(path)[num], Html.FROM_HTML_MODE_COMPACT)
        }
        textReader.movementMethod = ScrollingMovementMethod()
        buttonNext.setOnClickListener { next(path)}
        buttonPrevious.setOnClickListener { previous(path)}

    }

    private fun getText(filepath: String?): ArrayList<String> {


        val epubInputStream: InputStream = File("/storage/emulated/0/Android/data/com.blazebooks/files/$filepath").inputStream()
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

    private fun next(filepath: String?){
        num++
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textReader.text = Html.fromHtml(getText(filepath)[num], Html.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun previous(filepath: String?){
        num--
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textReader.text = Html.fromHtml(getText(filepath)[num], Html.FROM_HTML_MODE_COMPACT)
        }
    }


}//class
