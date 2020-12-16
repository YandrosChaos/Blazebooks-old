package com.blazebooks.ui.reader

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.CatalogRepository
import io.reactivex.functions.Consumer
import nl.siegmann.epublib.domain.Book

private const val DEFAULT_STYLE =
    "<style>img{display: inline;height: auto;max-width: 100%;}</style>"

/**
 * @author Victor Gonzalez
 */
class ReaderViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    var currentPage: Int = 0 //representa el número de la página actual
    var filesPath: String? = ""

    /**
     * Check if the light mode status: on/off.
     */
    fun isLightModeOn() = preferenceProvider.getLightMode()

    /**
     * Cambia datos en el HTML para que el libro reconozca las imagenes y estilos descargados
     *
     * @author Mounir Zbayr
     */
    fun loadData(book: Book, numPage: Int, documents: String?): String {
        val previousPage = numPage - 1
        var data = "$DEFAULT_STYLE${String(book.contents[previousPage].data)}"

        data = data.replace("../Images/", "$filesPath/$documents/Images/")
        data = data.replace("../Styles/", "$filesPath/$documents/Styles/")
        return data

    }

    /**
     * Compurba si la aplicacion esta conectada a internet
     *
     * @author Mounir Zbayr
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    fun saveLastPagePref(key: String) = preferenceProvider.setLastPage(key, currentPage)

    fun getLastPage(key: String) = preferenceProvider.getLastPage(key)

}