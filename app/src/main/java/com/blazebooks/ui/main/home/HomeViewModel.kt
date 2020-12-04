package com.blazebooks.ui.main.home

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.ui.search.SearchActivity
import com.blazebooks.util.TOOLBAR_TITLE_CODE
import com.blazebooks.util.snackbar

class HomeViewModel : ViewModel() {

    fun onMyBooks(view: View) {
        Intent(view.context, SearchActivity::class.java).also {
            it.putExtra(TOOLBAR_TITLE_CODE, view.context.getString(R.string.my_books))
            view.context.startActivity(it)
        }
    }

    fun onNewBooks(view: View) {
        Intent(view.context, SearchActivity::class.java).also {
            it.putExtra(TOOLBAR_TITLE_CODE, view.context.getString(R.string.new_books))
            view.context.startActivity(it)
        }
    }

    fun notImplemented(view: View){
        view.snackbar("Not implemented yet :c")
    }

}