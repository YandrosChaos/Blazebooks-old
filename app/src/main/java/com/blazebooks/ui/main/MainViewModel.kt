package com.blazebooks.ui.main

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.ui.auth.LoginActivity
import com.blazebooks.ui.reader.ReaderActivity
import com.blazebooks.ui.settings.SettingsActivity
import com.blazebooks.util.PATH_CODE
import com.blazebooks.util.snackbar
import com.blazebooks.util.startLoginActivity

class MainViewModel(
    private val repository: LoginRepository,
    private val prefs: PreferenceProvider
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    var username: String? = user?.displayName.toString()
    var usermail: String? = user?.email.toString()
    var userimage: String? = user?.photoUrl.toString()
    var mainViewModelListener: MainViewModelListener? = null


    /**
     *  Signs out from the current session and clean the SharedPreferences.
     *
     * @see LoginActivity
     *
     * @author Mounir
     * @author Victor Gonzalez
     */
    fun logout(view: View) {
        repository.logout()
        prefs.clearAll()
        view.context.startLoginActivity()
    }

    fun onSettingsActivity(view: View) {
        Intent(view.context, SettingsActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun loadProfileData() {
        userimage = prefs.userImage()
        //return string if profile photo exist. If not, return null. Both return
        //default image.
        mainViewModelListener?.onLoadImage(
            (if (!userimage.isNullOrEmpty()) {
                userimage
            } else if (user?.photoUrl != null) {
                user?.photoUrl
            } else {
                null
            }).toString(),
            R.drawable.ic_reading_big
        )
    }

    /**
     * If exist a last book stored into shared preferences, then shows it. Else,
     * shows a snackbar.
     *
     * @author Victor Gonzalez
     */
    fun lastBook(view: View) {
        val urlBook: String? = prefs.getLastBook()
        if (!urlBook.isNullOrEmpty()) {
            Intent(view.context, ReaderActivity::class.java).also {
                it.putExtra(PATH_CODE, urlBook)
                view.context.startActivity(it)
            }
        }else{
            view.snackbar("Last book cannot be found.")
        }
    }

}