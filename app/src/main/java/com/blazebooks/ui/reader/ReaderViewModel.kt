package com.blazebooks.ui.reader

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider
import nl.siegmann.epublib.domain.Book

private const val DEFAULT_STYLE =
    "<style>img{display: inline;height: auto;max-width: 100%;}</style>"

/**
 * @author Victor Gonzalez
 */
class ReaderViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    var currentPage = 1 //representa el número de la página actual
    var filesPath: String? = ""

    /**
     * Check if the light mode status: on/off.
     *
     * @author Víctor González
     */
    fun isLightModeOn() = preferenceProvider.getLightMode()

    fun nextPage() = currentPage++

    fun previousPage() = currentPage--

    fun loadData(book: Book, numPage: Int, documents: String?): String {
        val previousPage = numPage - 1
        var data = "$DEFAULT_STYLE${String(book.contents[previousPage].data)}"

        data = data.replace("../Images/", "$filesPath/$documents/Images/")
        data = data.replace("../Styles/", "$filesPath/$documents/Styles/")
        return data

    }
}