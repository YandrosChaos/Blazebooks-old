package com.blazebooks.view.search.control

import android.content.Context
import com.blazebooks.R
import com.blazebooks.model.Book
import java.util.*

/**
 * @author Victor Gonzalez
 */
class SearchFilterController(val context: Context) {
    val filterList: MutableList<Pair<String, String>> = mutableListOf()

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
     * @param filterCharacter Strings which the filter searches in book's titles.
     * @param filterList Checkbox which used for filter books by language, author, genre...
     *
     * @author Victor Gonzalez
     */
    fun filterList(
        filterCharacter: String,
        booksToFilter: MutableList<Book>
    ): MutableList<Book> {

        //filter by title
        var tempListWithFilters = mutableListOf<Book>()
        tempListWithFilters = filterByTitle(booksToFilter, filterCharacter)

        if (!filterList.isNullOrEmpty()) {
            filterList.forEach { filterItem ->
                when (filterItem.first) {

                    context.getString(R.string.genres) -> {
                        //filter by genre
                        tempListWithFilters = filterByGenre(tempListWithFilters, filterItem.second)
                    }

                    context.getString(R.string.premium) -> {
                        tempListWithFilters =
                            filterByPremium(tempListWithFilters, filterItem.second)
                    }

                    context.getString(R.string.authors) -> {
                        //filter by author
                        tempListWithFilters = filterByAuthor(tempListWithFilters, filterItem.second)
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

    fun updateFilters(newFilterList: MutableList<Pair<String, String>>){
        filterList.clear()
        filterList.addAll(newFilterList)
    }

    fun clearFilters(){
        filterList.clear()
    }

    private fun filterByTitle(
        booksToFilter: MutableList<Book>,
        filterCharacter: String
    ): MutableList<Book> {
        return booksToFilter.filter { book ->
            book.title!!.toLowerCase(Locale.ROOT)
                .contains(filterCharacter.toLowerCase(Locale.ROOT))
        } as MutableList<Book>
    }

    private fun filterByGenre(booksToFilter: MutableList<Book>, genre: String): MutableList<Book> {
        return booksToFilter.filter { book ->
            book.genre!!.toLowerCase(Locale.ROOT).contains(genre.toLowerCase(Locale.ROOT))
        } as MutableList<Book>
    }

    private fun filterByPremium(
        booksToFilter: MutableList<Book>,
        premium: String
    ): MutableList<Book> {
        return if (premium.toLowerCase(Locale.ROOT) == "premium") {
            //filter premium
            booksToFilter.filter { book ->
                book.premium
            } as MutableList<Book>
        } else {
            //filter not premium
            booksToFilter.filterNot { book ->
                book.premium
            } as MutableList<Book>
        }
    }

    private fun filterByAuthor(
        booksToFilter: MutableList<Book>,
        author: String
    ): MutableList<Book> {
        return booksToFilter.filter { book ->
            book.author!!.toLowerCase(Locale.ROOT).contains(author.toLowerCase(Locale.ROOT))
        } as MutableList<Book>
    }
}
