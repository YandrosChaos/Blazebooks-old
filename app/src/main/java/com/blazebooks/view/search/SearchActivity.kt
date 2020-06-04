package com.blazebooks.view.search

import android.os.Bundle
import android.os.Handler
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
import com.blazebooks.model.Book
import com.blazebooks.model.CustomGridRecyclerView
import com.blazebooks.view.PreconfiguredActivity
import com.blazebooks.view.dialogs.FilterDialog
import com.blazebooks.view.search.control.SearchActivityController
import com.blazebooks.view.search.control.SearchAdapter
import com.blazebooks.view.search.control.SearchFilterController
import kotlinx.android.synthetic.main.app_bar_search.*


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
    private val waitTime: Long = 500L
    private lateinit var mRecyclerView: CustomGridRecyclerView
    private var bookList: MutableList<Book> = mutableListOf()
    private lateinit var searchActivityController: SearchActivityController
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mSearchView: EditText
    private lateinit var mFilterDialogFrameLayout: FrameLayout
    private lateinit var searchFilterController: SearchFilterController

    /**
     * Sets toolbar title. Gets a list of items and add the Text Change Listener, filter the list and
     * updates the adapter. Also runs the custom recycler view animation by first time.
     *
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

        //controllers
        searchFilterController = SearchFilterController(this)
        searchActivityController =
            SearchActivityController(this, intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE))

        //set the title in the toolbar and show the progress bar
        activitySearchToolbarTv.text = intent.getStringExtra(Constants.TOOLBAR_TITLE_CODE)

        //configure and load adapter and manager
        mAdapter = SearchAdapter(bookList, this)
        mRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        mRecyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.gridlayout_animation_from_bottom)
        mRecyclerView.adapter = mAdapter

        //load data
        bookList = searchActivityController.data()

        //wait to get data from data base
        Handler().postDelayed({
            mAdapter.updateList(bookList)
            mAdapter.notifyDataSetChanged()
            //run the custom recyclerView animation.
            runRecyclerViewAnimation()
        }, waitTime)


        //Search Event. After text change, filter the list and updates the adapter
        mSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mAdapter.updateList(searchFilterController.filterList(p0.toString(), bookList))
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
            .replace(
                R.id.searchActivityFilterFragment,
                FilterDialog(searchFilterController.filterList)
            )
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
        searchFilterController.updateFilters(dialog.filterReturnList)
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
        searchFilterController.clearFilters()
    }

    /**
     * Closes the filter dialog and updates the view.
     *
     * @see FilterDialog.FilterDialogListener
     * @author Victor Gonzalez
     */
    override fun onCloseDialog(dialog: FilterDialog) {
        mAdapter.updateList(searchFilterController.filterList("", bookList))
        dialog.dismiss()
        mFilterDialogFrameLayout.visibility = View.GONE
        runRecyclerViewAnimation()
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
