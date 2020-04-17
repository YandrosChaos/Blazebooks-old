package com.blazebooks.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.Utils.Companion.hideKeyboard
import com.blazebooks.adapter.SearchAdapter
import com.blazebooks.model.Book
import com.blazebooks.model.Chapter
import com.blazebooks.ui.PreconfiguredActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_search.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Search book view.
 *
 * @see PreconfiguredActivity
 * @author  Victor Gonzalez
 */
class SearchActivity : PreconfiguredActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var bookList: MutableList<Book>
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mSearchView: EditText

    /**
     * Sets toolbar title. Gets a list of items and add the Text Change Listener, filter the list and
     * updates the adapter.
     *
     * @see getItemList
     * @see filterList
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mRecyclerView = findViewById(R.id.recyclerView_search)
        mProgressBar = findViewById(R.id.progress_circular)
        mSearchView = findViewById(R.id.new_searchView)

        //set the title in the toolbar and show the progress bar
        activitySearchToolbarTv.text = intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)
        mProgressBar.visibility = View.VISIBLE

        //load the data
        getItemList()

        //Search Event. After text change, filter the list and updates the adapter
        mSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filterList(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        //occult progress bar
        mProgressBar.visibility = View.GONE

    }

    /**
     * Filters the list by book's title and updates it in the adapter.
     * If the book's title contains the character, adds the book to a
     * temp list and passes the list to the adapter.
     *
     * @param filterItem
     * @see SearchAdapter.updateList
     * @author Victor Gonzalez
     */
    private fun filterList(filterItem: String) {
        val tempList: MutableList<Book> = ArrayList()
        bookList.forEach {
            if (it.title?.toLowerCase(Locale.getDefault())
                    ?.contains(filterItem.toLowerCase(Locale.getDefault()))!!
            ) {
                tempList.add(it)
            }
        }
        mAdapter.updateList(tempList)
    }

    /**
     * If search view is occult, finishes the activity. Else, closes the search view, updates
     * the view and hides the keyboard.
     *
     * @see switchToolbarMode
     * @see hideKeyboard
     * @param view
     *
     * @author Victor Gonzalez
     */
    fun previousActivity(view: View) {
        if (!mSearchView.isVisible) {
            finish()
        } else {
            hideKeyboard()
            switchToolbarMode(true)
        }

    }

    /**
     * When the button is clicked, switches search view.
     *
     * @see switchToolbarMode
     * @param view
     * @author  Victor Gonzalez
     */
    fun showSearchBarItem(view: View) {
        mSearchView.hint = getString(R.string.looking_for)
        switchToolbarMode(mSearchView.isVisible)
    }

    /**
     * Loads data, layout manager and adapter. While is loading, shows a progress bar.
     *
     * @see data
     * @author Victor Gonzalez
     */
    private fun getItemList() {
        //load data
        data()

        //configure and load adapter and manager
        mAdapter = SearchAdapter(bookList, this)
        mRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        mRecyclerView.adapter = mAdapter
    }

    /**
     * Load data from database to adapter.
     *
     * @see SearchAdapter.updateList
     *
     * @author Victor Gonzalez
     * @author Mounir
     */
    private fun data() {

        val db = FirebaseFirestore.getInstance() //Con esto accedemos a la base de datos de Firebase
        db.collection("Books") //Accede a la coleccion Books y devuelve todos los documentos
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val book =
                        document.toObject(Book::class.java) //convierte el documento de firebase a la clase Book
                    val chapterList = ArrayList<Chapter>()
                    db.collection("Chapters") //accede a la lista Chapters (Esto cambiará cuando vea como relacionar colecciones)
                        .whereEqualTo(
                            "titulo",
                            book.title
                        ) //Filtra por titulo (Tengo un atributo titulo en cada capitulo con el nombre del libro para que funcione de momento)
                        .get()
                        .addOnSuccessListener { chapters ->
                            for (chapter in chapters) {
                                chapterList.add(chapter.toObject(Chapter::class.java)) //se añaden lso capitulos de la bbdd a la lista de capitulos
                            }
                        }
                    book.chapters = chapterList //añade los capitulos al libro
                    bookList.add(book) //añade el libro a la lista
                }//for
                mAdapter.updateList(bookList) //actualiza la lista
            }

        val chaptersList = arrayListOf(
            Chapter(1, "Title of the Chapter", null, true, "Unknown"),
            Chapter(2, "Funciona?", null, true, "Unknown"),
            Chapter(3, "Suck or die", null, false, "Unknown"),
            Chapter(4, "Sssssspañah", null, false, "Unknown"),
            Chapter(5, "Coronachapter", null, false, "Unknown")
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
                false,
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
                true,
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
                false,
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
                false,
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
                false,
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
                false,
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
                true,
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
                false,
                "unknown"
            )
        )
    }

    /**
     * Switches the search toolbar between normal and search mode.
     *
     * @param searchVisible
     * @author Victor González
     */
    private fun switchToolbarMode(searchVisible: Boolean) {
        if (!searchVisible) {
            //shows the bar and occult the title
            mSearchView.visibility = View.VISIBLE
            activitySearchToolbarTv.visibility = View.GONE

            //set toolbar search colors
            DrawableCompat.setTint(
                activitySearchToolBarBtnReturn.background,
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
            )//return button
            DrawableCompat.setTint(
                activitySearchToolBarBtnFilter.background,
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
            )//filter button
            DrawableCompat.setTint(
                activitySearchToolbarBtnSearch.background,
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
            )//search button
        } else {
            //occult the bar and shows the title
            mSearchView.visibility = View.GONE
            activitySearchToolbarTv.visibility = View.VISIBLE
            //set colors
            DrawableCompat.setTint(
                activitySearchToolBarBtnReturn.background,
                ContextCompat.getColor(this, R.color.white)
            )//return button
            DrawableCompat.setTint(
                activitySearchToolBarBtnFilter.background,
                ContextCompat.getColor(this, R.color.white)
            )//filter button
            DrawableCompat.setTint(
                activitySearchToolbarBtnSearch.background,
                ContextCompat.getColor(this, R.color.white)
            )//search button
        }

    }

}
