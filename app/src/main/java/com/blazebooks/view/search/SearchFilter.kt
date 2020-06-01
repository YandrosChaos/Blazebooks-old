package com.blazebooks.view.search

import android.content.Context
import com.blazebooks.R
import com.blazebooks.model.Book
import com.blazebooks.model.CustomGridRecyclerView
import com.blazebooks.view.search.control.SearchAdapter
import java.util.*

class SearchFilter(val context: Context) {


    /**
     * <p>Filters the list by book's title and updates it in the adapter.
     * If the book's title contains the character, adds the book to a
     * temp list and passes the list to the adapter.</p>
     *
     * <p>Uses the filterList to filtering the list. Each checkbox clicked is one
     * layer added to the filter.</p>
     *
     * <p> When list is filtered, calls to the adapter, updates the list and run the
     * animation.</p>
     *
     * @param filterItem Strings which the filter searches in book's titles.
     * @param filterList Checkbox which used for filter books by language, author, genre...
     *
     * @see SearchAdapter
     * @see runRecyclerViewAnimation
     * @see CustomGridRecyclerView
     *
     * @author Victor Gonzalez
     */
    fun filterList(
        filterCharacter: String,
        filterList: MutableList<Pair<String, String>>?,
        BooksToFilter: MutableList<Book>
    ): MutableList<Book> {

        //filter by title
        var tempListWithFilters = mutableListOf<Book>()
        tempListWithFilters = BooksToFilter.filter { book ->
            book.title!!.toLowerCase(Locale.ROOT)
                .contains(filterCharacter.toLowerCase(Locale.ROOT))
        } as MutableList<Book>

        if (!filterList.isNullOrEmpty()) {
            filterList.forEach { filterItem ->
                when (filterItem.first) {

                    context.getString(R.string.genres) -> {
                        //filter by genre and title
                        tempListWithFilters = tempListWithFilters.filter { book ->
                            book.genre!!.contains(filterItem.second)
                        } as MutableList<Book>
                    }

                    context.getString(R.string.premium) -> {
                        tempListWithFilters =
                            if (filterItem.second
                                    .toLowerCase(Locale.ROOT) == "premium"
                            ) {
                                //filter premium
                                tempListWithFilters.filter { book ->
                                    book.premium
                                } as MutableList<Book>
                            } else {
                                //filter not premium
                                tempListWithFilters.filterNot { book ->
                                    book.premium
                                } as MutableList<Book>
                            }
                    }

                    context.getString(R.string.authors) -> {
                        //filter by author
                        tempListWithFilters = tempListWithFilters.filter { book ->
                            book.author!!.contains(filterItem.second)
                        } as MutableList<Book>
                    }

                    context.getString(R.string.language) -> {
                        //filter by language
                        //TODO -> FILTER BY LANGUAGE
                    }

                }
            }
        }
        return tempListWithFilters
    }
}
