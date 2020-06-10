package com.blazebooks.ui.main.catalog

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.ui.search.SearchActivity
import com.blazebooks.util.TOOLBAR_TITLE_CODE

class CatalogViewModel : ViewModel() {

    /**
     * Starts a new activity and pass a String by param
     * depending of the view.id
     *
     * @param view
     * @author Victor Gonzalez
     */
    fun onSearch(view: View) {
        Intent(view.context, SearchActivity::class.java).also {
            it.putExtra(
                TOOLBAR_TITLE_CODE,
                when (view.id) {
                    R.id.fragmentBooksBook ->
                        view.resources.getString(R.string.fav_books)
                    R.id.fragmentBooksIb ->
                        view.resources.getString(R.string.interactive_books)
                    R.id.fragmentBooksAll ->
                        view.resources.getString(R.string.all_books)
                    else -> "SEARCH"
                }
            )
            view.context.startActivity(it)
        }
    }

}