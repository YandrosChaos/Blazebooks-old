package com.blazebooks.ui.main

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.ui.auth.LoginActivity
import com.blazebooks.util.DEFAULT_PROFILE_IMAGE
import com.blazebooks.util.DEFAULT_USERNAME_VALUE

class MainViewModel(
    private val repository: LoginRepository,
    private val prefs: PreferenceProvider
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    val urlBook: String? = prefs.getLastBook()
    var username: String = user?.displayName ?: DEFAULT_USERNAME_VALUE
    var usermail: String = user?.email.toString()

    /**
     *  Signs out from the current session and clean the SharedPreferences.
     *
     * @see LoginActivity
     *
     * @author Mounir
     * @author Victor Gonzalez
     */
    fun logout() {
        repository.logout()
        prefs.clearAll()
    }

    /**
     * Gets the user profile image. If is null, then use a default profile image.
     *
     * @author Víctor González
     */
    fun getStoredProfileImage() = (user?.photoUrl ?: DEFAULT_PROFILE_IMAGE).toString()

}