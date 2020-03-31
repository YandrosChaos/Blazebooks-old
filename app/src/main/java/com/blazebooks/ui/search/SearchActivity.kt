package com.blazebooks.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.model.Book
import com.blazebooks.model.Chapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.app_bar_search.*

class SearchActivity : AppCompatActivity() {
    private lateinit var bookList: ArrayList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //load title code from previous activity
        activitySearchToolbarTv.text = intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)

        data()

        activitySearchRv.layoutManager = LinearLayoutManager(this)
        activitySearchRv.adapter = SearchAdapter(bookList, this)
    }

    private fun data() {

        val chaptersList = arrayListOf(
            Chapter("Chapter I", null, "Unknown"),
            Chapter("Chapter II", null, "Unknown"),
            Chapter("Chapter III", null, "Unknown"),
            Chapter("Chapter IV", null, "Unknown"),
            Chapter("Chapter V", null, "Unknown")
        )
        bookList = arrayListOf(
            Book("Libro Primero", null, "Anonimo", chaptersList, true, "Terror"),
            Book("Libro Segundo", null, "Bob", chaptersList, false, "Adventure"),
            Book("Libro Tercero", null, "M. Rajoy", chaptersList, true, "Sci-Fy"),
            Book("Libro Cuarto", null, "Juanjo", chaptersList, true, "Terror"),
            Book("Libro Quinto", null, "Bob", chaptersList, false, "Fantasy"),
            Book("Libro Sexto", null, "Anonimo", chaptersList, false, "Terror"),
            Book("Libro Septimo", null, "Bob", chaptersList, false, "Verse"),
            Book("Libro Octavo", null, "Anonimo", chaptersList, true, "Interactive")
        )
    }

    fun previousActivity(view: View) {
        finish()
    }
}