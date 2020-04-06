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

    /**
     * Load try data
     */
    private fun data() {

        val chaptersList = arrayListOf(
            Chapter(1, "Title of the Chapter", null, "Unknown"),
            Chapter(2, "Blah blah blah", null, "Unknown"),
            Chapter(3, "Suck or die", null, "Unknown"),
            Chapter(4, "Sssssspañah", null, "Unknown"),
            Chapter(5, "Coronachapter", null, "Unknown")
        )
        bookList = arrayListOf(
            Book(
                "Libro Primero",
                null,
                "Anonimo",
                getString(R.string.synopsis_example),
                chaptersList,
                true,
                "Terror",
                "unknown"
            ),
            Book(
                "Libro Segundo",
                null,
                "Bob",
                getString(R.string.synopsis_example),
                chaptersList,
                false,
                "Adventure",
                "unknown"
            ),
            Book(
                "Libro Tercero",
                null,
                "M. Rajoy",
                getString(R.string.synopsis_example),
                chaptersList,
                true,
                "Sci-Fy",
                "unknown"
            ),
            Book(
                "Libro Cuarto",
                null,
                "Juanjo",
                getString(R.string.synopsis_example),
                chaptersList,
                true,
                "Terror",
                "unknown"
            ),
            Book(
                "Libro Quinto",
                null,
                "Bob",
                getString(R.string.synopsis_example),
                chaptersList,
                false,
                "Fantasy",
                "unknown"
            ),
            Book(
                "Libro Sexto",
                null,
                "Anonimo",
                getString(R.string.synopsis_example),
                chaptersList,
                false,
                "Terror",
                "unknown"
            ),
            Book(
                "Libro Septimo",
                null,
                "Bob",
                getString(R.string.synopsis_example),
                chaptersList,
                false,
                "Verse",
                "unknown"
            ),
            Book(
                "Libro Octavo",
                null,
                "Anonimo",
                getString(R.string.synopsis_example),
                chaptersList,
                true,
                "Interactive",
                "unknown"
            )
        )
    }

    /**
     * Finish the activity
     *
     * @param view
     */
    fun previousActivity(view: View) {
        finish()
    }
}