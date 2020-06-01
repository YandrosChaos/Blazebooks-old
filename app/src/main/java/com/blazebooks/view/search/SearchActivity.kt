package com.blazebooks.view.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.Utils.Companion.hideKeyboard
import com.blazebooks.control.localStorage.LocalStorageSingleton
import com.blazebooks.control.localStorage.model.FavBook
import com.blazebooks.model.Book
import com.blazebooks.model.Chapter
import com.blazebooks.model.CustomGridRecyclerView
import com.blazebooks.view.PreconfiguredActivity
import com.blazebooks.view.dialogs.FilterDialog
import com.blazebooks.view.search.control.SearchAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_search.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Search book activity.
 *
 * @see PreconfiguredActivity
 * @see FilterDialog.FilterDialogListener
 * @see FilterDialog
 *
 * @author  Victor Gonzalez
 */
class SearchActivity : PreconfiguredActivity(), FilterDialog.FilterDialogListener {
    private lateinit var mRecyclerView: CustomGridRecyclerView
    private var bookList: MutableList<Book> = mutableListOf()
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mSearchView: EditText
    private lateinit var mFilterDialogFrameLayout: FrameLayout
    private var filterList: MutableList<Pair<String, String>> = mutableListOf()
    private lateinit var searchFilter: SearchFilter

    /**
     * Sets toolbar title. Gets a list of items and add the Text Change Listener, filter the list and
     * updates the adapter. Also runs the custom recycler view animation by first time.
     *
     * @see getItemList
     * @see filterList
     * @see CustomGridRecyclerView
     *
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mRecyclerView = findViewById(R.id.recyclerView_search)
        mSearchView = findViewById(R.id.searchViewEditText)
        mFilterDialogFrameLayout = findViewById(R.id.searchActivityFilterFragment)
        searchFilter = SearchFilter(this)

        //set the title in the toolbar and show the progress bar
        activitySearchToolbarTv.text = intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)

        //load the data
        getItemList()

        //run the custom recyclerView animation.
        runRecyclerViewAnimation()

        //Search Event. After text change, filter the list and updates the adapter
        mSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mAdapter.updateList(searchFilter.filterList(p0.toString(), filterList, bookList))
                runRecyclerViewAnimation()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    /**
     * If search view is occult, finishes the activity. Else, closes the search view, updates
     * the view and hides the keyboard.
     *
     * @see switchToolbarMode
     * @see hideKeyboard
     * @see onBackPressed
     * @param view
     *
     * @author Victor Gonzalez
     */
    fun previousActivity(view: View) {
        if (!mSearchView.isVisible) {
            onBackPressed()
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
     * Shows the filter menu.
     *
     * @author Victor Gonzalez
     */
    fun createFilterDialog(view: View) {
        mFilterDialogFrameLayout.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
            .replace(R.id.searchActivityFilterFragment, FilterDialog(filterList))
            .commit()
    }

    /**
     * Returns the filters from the FilterDialog.
     *
     * @see FilterDialog
     * @see FilterDialog.FilterDialogListener
     *
     * @author Victor Gonzalez
     */
    override fun onReturnFilters(dialog: FilterDialog) {
        filterList.clear()
        filterList = dialog.filterReturnList
        onCloseDialog(dialog)
    }

    /**
     * Clean the list of filters.
     *
     * @see FilterDialog
     * @see FilterDialog.FilterDialogListener
     * @author Victor Gonzalez
     */
    override fun onClearFilters(dialog: FilterDialog) {
        filterList.clear()
    }

    /**
     * Closes the filter dialog and updates the view.
     *
     * @see FilterDialog.FilterDialogListener
     * @author Victor Gonzalez
     */
    override fun onCloseDialog(dialog: FilterDialog) {
        mAdapter.updateList(searchFilter.filterList("",filterList,bookList))
        dialog.dismiss()
        mFilterDialogFrameLayout.visibility = View.GONE
        runRecyclerViewAnimation()
    }


    /**
     * Loads data, layout manager and adapter.
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
        mRecyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.gridlayout_animation_from_bottom)
        mRecyclerView.adapter = mAdapter
    }

    /**
     * Runs the custom recyclerView animation.
     *
     * @see CustomGridRecyclerView
     * @see SearchAdapter
     *
     * @author Victor Gonzalez
     */
    private fun runRecyclerViewAnimation() {
        mAdapter.notifyDataSetChanged()
        mRecyclerView.scheduleLayoutAnimation()
    }

    /**
     * Load data from database to adapter.
     *
     * @see SearchAdapter.updateList
     *
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    private fun data() {
        when (intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)) {
            resources.getString(R.string.fav_books) -> {

                //consulta la base de datos local
                val favBooksList =
                    LocalStorageSingleton.getDatabase(applicationContext)
                        .favBookDAO()
                        .getAllTitles()

                if (favBooksList.isNotEmpty()) {
                    val db =
                        FirebaseFirestore.getInstance() //Con esto accedemos a la base de datos de Firebase
                    db.collection("Books").whereIn(
                        "title",
                        favBooksList
                    )//Accede a la colección Books y devuelve los documentos que están en favoritos
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val book =
                                    document.toObject(Book::class.java) //convierte el documento de firebase a la clase Book
                                val chapterList = ArrayList<Chapter>()
                                db.collection("Books").document(document.id).collection("Chapters")
                                    .get()
                                    .addOnSuccessListener { chapters ->
                                        for (chapter in chapters) {
                                            chapterList.add(chapter.toObject(Chapter::class.java)) //se añaden los capitulos de la bbdd a la lista de capitulos
                                        }
                                    }
                                book.chapters = chapterList //añade los capitulos al libro
                                bookList.add(book) //añade el libro a la lista
                            }//for
                            mAdapter.updateList(bookList) //actualiza la lista
                        }
                }

            }

            else -> {

                val db =
                    FirebaseFirestore.getInstance() //Con esto accedemos a la base de datos de Firebase
                db.collection("Books") //Accede a la colección Books y devuelve todos los documentos
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val book =
                                document.toObject(Book::class.java) //convierte el documento de firebase a la clase Book
                            val chapterList = ArrayList<Chapter>()
                            db.collection("Books").document(document.id).collection("Chapters")
                                .get()
                                .addOnSuccessListener { chapters ->
                                    for (chapter in chapters) {
                                        chapterList.add(chapter.toObject(Chapter::class.java)) //se añaden los capitulos de la bbdd a la lista de capitulos
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
                    )
                )
            }
        }


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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

}
