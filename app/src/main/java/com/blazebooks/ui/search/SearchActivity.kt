package com.blazebooks.ui.search

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blazebooks.R
import com.blazebooks.data.models.Book
import com.blazebooks.data.models.CustomGridRecyclerView
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivitySearchBinding
import com.blazebooks.ui.customdialogs.filter.FilterDialog
import com.blazebooks.ui.customdialogs.filter.FilterDialogListener
import com.blazebooks.ui.search.control.SearchAdapter
import com.blazebooks.ui.search.control.SearchFilter
import com.blazebooks.util.Coroutines
import com.blazebooks.util.TOOLBAR_TITLE_CODE
import com.blazebooks.util.hideKeyboard
import com.blazebooks.util.snackbar
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.app_bar_search.*
import kotlinx.android.synthetic.main.app_bar_search.view.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

private const val DELAYED_GETDATA_TIME: Long = 1000

/**
 * Search book activity.
 *
 * @see PreconfiguredActivity
 * @see FilterDialogListener
 * @see FilterDialog
 *
 * @author  Victor Gonzalez
 */
class SearchActivity : PreconfiguredActivity(), FilterDialogListener, KodeinAware {

    override val kodein by kodein()
    private val factory by instance<SearchActivityViewModelFactory>()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchActivityViewModel
    private lateinit var mAdapter: SearchAdapter

    private var bookList: MutableList<Book> = mutableListOf()
    private lateinit var mSearchView: EditText
    private lateinit var searchFilter: SearchFilter

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProvider(this, factory).get(SearchActivityViewModel::class.java)

        mSearchView = binding.searchToolbar.searchViewEditText
        searchFilter = SearchFilter(this)

        intent.getStringExtra(TOOLBAR_TITLE_CODE)?.apply {
            setToolbarTitle(this)
            data(this)
        }

        configureLayout()
        configureAdapter()

        Handler().postDelayed({
            mAdapter.updateList(bookList)
            mAdapter.notifyDataSetChanged()
            binding.searchALoadingSKV.visibility = View.GONE
            runRecyclerViewAnimation()
        }, DELAYED_GETDATA_TIME)

        //Search Event. After text change, filter the list and updates the adapter
        mSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mAdapter.updateList(searchFilter.filterList(p0.toString(), bookList))
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
    fun previousActivity(view: View) =
        if (!mSearchView.isVisible) {
            onBackPressed()
        } else {
            hideKeyboard()
            switchToolbarMode(true)
        }


    /**
     * When the button is clicked, switches search view.
     *
     * @see switchToolbarMode
     * @author  Victor Gonzalez
     */
    fun showSearchBarItem(view: View) = switchToolbarMode(mSearchView.isVisible)


    /**
     * Shows the filter menu.
     *
     * @author Victor Gonzalez
     */
    fun createFilterDialog(view: View) {
        binding.searchActivityFilterFragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
            .replace(
                R.id.searchActivityFilterFragment,
                FilterDialog(
                    searchFilter.filterList
                )
            ).commit()
    }

    /**
     * Returns the filters from the FilterDialog.
     *
     * @see FilterDialog
     *
     * @author Victor Gonzalez
     */
    override fun onReturnFilters(dialog: FilterDialog) {
        searchFilter.updateFilters(dialog.filterReturnList)
        onCloseDialog(dialog)
    }

    /**
     * Clean the list of filters.
     *
     * @see FilterDialog
     * @author Victor Gonzalez
     */
    override fun onClearFilters(dialog: FilterDialog) = searchFilter.clearFilters()


    /**
     * Closes the filter dialog and updates the view.
     *
     * @author Victor Gonzalez
     */
    override fun onCloseDialog(dialog: FilterDialog) {
        mAdapter.updateList(searchFilter.filterList("", bookList))
        dialog.dismiss()
        binding.searchActivityFilterFragment.visibility = View.GONE
        runRecyclerViewAnimation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

    private fun setToolbarTitle(title: String) {
        activitySearchToolbarTv.text = title
    }

    private fun configureLayout() =
        binding.recyclerViewSearch.apply {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.gridlayout_animation_from_bottom)
        }


    private fun configureAdapter() {
        mAdapter = SearchAdapter(bookList, this)
        binding.recyclerViewSearch.adapter = mAdapter
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
        binding.recyclerViewSearch.scheduleLayoutAnimation()
    }

    /**
     * Switches the search toolbar between normal and search mode.
     *
     * @param searchVisible
     * @author Victor González
     */
    private fun switchToolbarMode(searchVisible: Boolean) =
        if (!searchVisible) {
            showSearchEditText()
            setSearchViewColors(R.color.colorPrimaryDark)
        } else {
            hideSearchEditText()
            setSearchViewColors(R.color.white)
        }

    private fun showSearchEditText() {
        mSearchView.visibility = View.VISIBLE
        activitySearchToolbarTv.visibility = View.GONE
    }

    private fun hideSearchEditText() {
        mSearchView.visibility = View.GONE
        activitySearchToolbarTv.visibility = View.VISIBLE
    }

    private fun setSearchViewColors(color: Int) {
        DrawableCompat.setTint(
            activitySearchToolBarBtnReturn.background,
            ContextCompat.getColor(this, color)
        )
        DrawableCompat.setTint(
            activitySearchToolBarBtnFilter.background,
            ContextCompat.getColor(this, color)
        )
        DrawableCompat.setTint(
            activitySearchToolbarBtnSearch.background,
            ContextCompat.getColor(this, color)
        )
    }

    /**
     * Load data from database.
     *
     * @see SearchAdapter.updateList
     *
     * @author Victor Gonzalez
     * @author Mounir Zbayr
     */
    private fun data(downloadType: String) {
        when (downloadType) {
            getString(R.string.fav_books) -> getFavBooks()
            getString(R.string.my_books) -> getLocalBooks()
            else -> getAllBooks()
        }

        bookList = viewModel.dataList
    }

    private fun getLocalBooks() = Coroutines.main { viewModel.getStoredBooks() }

    private fun getFavBooks() = lifecycleScope.launch {
        try {
            viewModel.getFavBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.dataList.addAll(it)
                }, {
                    binding.root.snackbar(it.message.toString())
                })
        } catch (e: ApiException) {
        }
    }

    private fun getAllBooks() = lifecycleScope.launch {
        try {
            viewModel.getAllBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.dataList.addAll(it)
                }, {
                    binding.root.snackbar(it.message.toString())
                })
        } catch (e: ApiException) {
        }
    }


}
