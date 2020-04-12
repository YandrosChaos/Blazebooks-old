package com.blazebooks.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.Utils.Companion.hideKeyboard
import com.blazebooks.adapter.SearchAdapter
import com.blazebooks.model.Book
import com.blazebooks.model.Chapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var bookList: MutableList<Book>
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mSearchView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mRecyclerView = findViewById(R.id.recyclerView_search)
        mProgressBar = findViewById(R.id.progress_circular)
        mSearchView = findViewById(R.id.new_searchView)

        //set the title in the toolbar
        activitySearchToolbarTv.text = intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)

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

    }

    /**
     * Filters the list by book's title and update the adapter list.
     *
     * @param filterItem
     */
    private fun filterList(filterItem: String) {
        val tempList: MutableList<Book> = ArrayList()

        for (item in bookList) {
            if (item.title?.toLowerCase(Locale.getDefault())
                    ?.contains(filterItem.toLowerCase(Locale.getDefault()))!!
            ) {
                tempList.add(item)
            }
        }
        mAdapter.updateList(tempList)
    }

    /**
     * If search view is occult, finish the activity. Else, closes the search view, updates
     * the view and hides the keyboard.
     *
     * @param view
     */
    fun previousActivity(view: View) {
        if (!mSearchView.isVisible) {
            finish()
        } else {
            mSearchView.visibility = View.GONE
            activitySearchToolbarTv.visibility = View.VISIBLE
            mSearchView.visibility = View.GONE

            hideKeyboard()

            activitySearchToolbarTv.visibility = View.VISIBLE
            DrawableCompat.setTint(
                activitySearchToolBarBtnReturn.background,
                resources.getColor(R.color.white)
            )//return button
            DrawableCompat.setTint(
                activitySearchToolBarBtnFilter.background,
                resources.getColor(R.color.white)
            )//filter button
            DrawableCompat.setTint(
                activitySearchToolbarBtnSearch.background,
                resources.getColor(R.color.white)
            )//search button
            filterList("")
        }

    }

    /**
     * <p>When the button is clicked, this method makes visible or gone
     * the search view. In addition, makes visible or invisible the
     * title text in toolbar.</p>
     * <p>Changes the color of the buttons if the search view is gone or not too.</p>
     *
     * @param view
     */
    fun showSearchBarItem(view: View) {
        mSearchView.hint = "Looking for..."

        if (!mSearchView.isVisible) {
            //shows the bar and occult the title
            mSearchView.visibility = View.VISIBLE
            activitySearchToolbarTv.visibility = View.GONE

            DrawableCompat.setTint(
                activitySearchToolBarBtnReturn.background,
                resources.getColor(R.color.colorPrimaryDark)
            )//return button
            DrawableCompat.setTint(
                activitySearchToolBarBtnFilter.background,
                resources.getColor(R.color.colorPrimaryDark)
            )//filter button
            DrawableCompat.setTint(
                activitySearchToolbarBtnSearch.background,
                resources.getColor(R.color.colorPrimaryDark)
            )//search button

        } else {
            //occult the bar and shows the title
            mSearchView.visibility = View.GONE
            activitySearchToolbarTv.visibility = View.VISIBLE
            DrawableCompat.setTint(
                activitySearchToolBarBtnReturn.background,
                resources.getColor(R.color.white)
            )//return button
            DrawableCompat.setTint(
                activitySearchToolBarBtnFilter.background,
                resources.getColor(R.color.white)
            )//filter button
            DrawableCompat.setTint(
                activitySearchToolbarBtnSearch.background,
                resources.getColor(R.color.white)
            )//search button
        }
    }

    /**
     * Load data, layout manager and adapter. When is loading, shows a progress bar.
     *
     * @see data
     */
    private fun getItemList() {
        //show progress bar
        mProgressBar.visibility = View.VISIBLE

        //load the data
        data()

        //load adapter and manager
        mAdapter = SearchAdapter(bookList, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        //occult progress bar
        mProgressBar.visibility = View.GONE
    }

    /**
     * Charge the data into current activity
     */
    private fun data() {



        val chaptersList = arrayListOf(
            Chapter(1, "Title of the Chapter", null, true, "Unknown"),
            Chapter(2, "Funciona?", null, true, "Unknown"),
            Chapter(3, "Suck or die", null, false, "Unknown"),
            Chapter(4, "Sssssspañah", null, false, "Unknown"),
            Chapter(5, "Coronachapter", null, false, "Unknown")
        )

        val db = FirebaseFirestore.getInstance() //Con esto accedemos a la base de datos de Firebase

        db.collection("Books") //Accede a la coleccion Books y devuelve todos los documentos
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val book= document.toObject(Book::class.java) //convierte el documento de firebase a la clase Book
                    val chapterList = ArrayList<Chapter>()
                    db.collection("Chapters") //accede a la lista Chapters (Esto cambiará cuando vea como relacionar colecciones)
                        .whereEqualTo("titulo", book.title) //Filtra por titulo (Tengo un atributo titulo en cada capitulo con el nombre del libro para que funcione de momento)
                        .get()
                        .addOnSuccessListener { chapters ->
                            for (chapter in chapters) {
                                chapterList.add(chapter.toObject(Chapter::class.java)) //se añaden lso capitulos de la bbdd a la lista de capitulos
                            }
                        }
                    book.chapters= chapterList //añade los capitulos al libro
                    bookList.add(book) //añade el libro a la lista
                }//for
                mAdapter.updateList(bookList) //actualiza la lista
            }


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

}
